package data_management;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class YahooDataDownloader implements DataDownloader {

	private static final Object BASE_URL = "http://ichart.finance.yahoo.com/table.csv?";

	private String BuildHistoricalDataRequest(String symbol, Date startDate, Date today) {
		// We're subtracting 1 from the month because yahoo
		// counts the months from 0 to 11 not from 1 to 12.

		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		StringBuilder request = new StringBuilder();
		request.append("s=" + symbol);
		request.append("&a=" + (cal.get(Calendar.MONTH)));
		request.append("&b=" + cal.get(Calendar.DAY_OF_MONTH));
		request.append("&c=" + cal.get(Calendar.YEAR));

		cal.setTime(today);
		request.append("&d=" + (cal.get(Calendar.MONTH)));
		request.append("&e=" + cal.get(Calendar.DAY_OF_MONTH));
		request.append("&f=" + cal.get(Calendar.YEAR));
		request.append("&g=d"); // daily

		return request.toString();
	}

	@Override
	public String getHistoricalData(String symbol) throws ParseException, SymbolNotFoundException {
		Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse("1900-01-01");
		return getHistoricalData(symbol, startDate);
	}

	@Override
	public String getHistoricalData(String symbol, Date startDate) throws SymbolNotFoundException {
		Date today = new Date();
		return getHistoricalData(symbol, startDate, today);
	}

	@Override
	public String getHistoricalData(String symbol, Date startDate, Date endDate) throws SymbolNotFoundException {
		if (endDate.before(startDate)) {
			return null;
		}
		
		String queryText = BuildHistoricalDataRequest(symbol, startDate, endDate);
		String url = String.format("%s%s", BASE_URL, queryText);

		System.out.println("Downloading data from " + startDate + " to " + endDate);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = httpclient.execute(httpGet);
		} catch (IOException e) {
			System.out.println(e);
		}

		if (httpResponse.getStatusLine().getStatusCode() == 404) {
			throw new SymbolNotFoundException();
		}

		String response = null;
		StringWriter writer = new StringWriter();
		try {
			InputStream is = httpResponse.getEntity().getContent();
			IOUtils.copy(is, writer, "UTF-8");
			response = writer.toString();
		} catch (IllegalStateException e) {
			System.out.println(e);
		} catch ( IOException e) {
			System.out.println(e);
		}

		return response;
	}
	
	public static void main(String[] args) throws ParseException, SymbolNotFoundException {
		YahooDataDownloader y = new YahooDataDownloader();
		
		Date from = new SimpleDateFormat("yyyy-MM-dd").parse("2010-11-01");
		Date to = new SimpleDateFormat("yyyy-MM-dd").parse("2010-12-05");
		System.out.println(y.getHistoricalData("YHOO", from, to));
	}
}
