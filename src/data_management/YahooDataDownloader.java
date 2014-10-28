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
		request.append("&a=" + (cal.get(Calendar.MONTH) - 1));
		request.append("&b=" + cal.get(Calendar.DAY_OF_MONTH));
		request.append("&c=" + cal.get(Calendar.YEAR));

		cal.setTime(today);
		request.append("&d=" + (cal.get(Calendar.MONTH) - 1));
		request.append("&e=" + cal.get(Calendar.DAY_OF_MONTH));
		request.append("&f=" + cal.get(Calendar.YEAR));
		request.append("&g=d"); // daily

		return request.toString();
	}

	@Override
	public String getHistoricalData(String symbol) throws ParseException, SymbolNotFoundException {
		Date startDate = new SimpleDateFormat("yyyy-mm-dd").parse("1900/1/1");
		return getHistoricalData(symbol, startDate);
	}

	@Override
	public String getHistoricalData(String symbol, Date startDate) throws SymbolNotFoundException {
		Date today = new Date();
		return getHistoricalData(symbol, startDate, today);
	}

	@Override
	public String getHistoricalData(String symbol, Date startDate, Date endDate) throws SymbolNotFoundException {
		String queryText = BuildHistoricalDataRequest(symbol, startDate, endDate);
		String url = String.format("%s%s", BASE_URL, queryText);

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
		} catch (IllegalStateException | IOException e) {
			System.out.println(e);
		}

		return response;
	}
}
