import general.Company;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import data_management.DataDownloader;
import data_management.DataParser;
import data_management.DocumentFormats;
import data_management.HistoricalData;
import data_management.SymbolNotFoundException;
import data_management.YahooDataDownloader;
import data_storage.DatabaseConnector;

public class RunInvestment {

	public static void main(String[] args) {
		DataDownloader dataDownloader = new YahooDataDownloader();
		DataParser dataParser = new DataParser();
		DatabaseConnector dbConnector = DatabaseConnector.getInstance();
		
		// we should pass all companies!?
		dbConnector.initializeDB();

		String response = null;
		try {
			for (Company company : Company.values()) {
				
				System.out.println("working on symbol: " + company.getSymbol());
				
				// get last update
				Date lastAvailableDataPointDate = dbConnector.getLastUpdate(company.getSymbol());
				if (lastAvailableDataPointDate == null) {
					lastAvailableDataPointDate = new SimpleDateFormat("yyyy-mm-dd").parse("1900-1-1");
				}
				System.out.println("got last update!");
				
				// get data
				try {
					response = dataDownloader.getHistoricalData(company.getSymbol(), lastAvailableDataPointDate);
					System.out.println("downloaded the data.");
				} catch (SymbolNotFoundException e) {
					System.out.println(company.getSymbol() + ": " + e);
					continue;
				}

				// insert to db
				List<HistoricalData> data = null;
				try {
					data = dataParser.parseData(company.getSymbol(), response, DocumentFormats.CSV);
					System.out.println("completed parsing the data. data length: " + data.size());
				} catch (IOException e) {
					System.out.println("error in parsing data!");
					System.out.println(e);
				}
				
				dbConnector.insertHistoricalData(company.getSymbol(), data);
				System.out.println("completed inserting the data.");
			}
		} catch (ParseException e) {
		}


	}

}
