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
				
				// get last update
				Date startDate = dbConnector.getLastUpdate(company.getSymbol());
				if (startDate == null) {
					startDate = new SimpleDateFormat("MM/dd/yy").parse("1/1/1900");
				} 
				
				// get data
				try {
					response = dataDownloader.getHistoricalData(company.getSymbol(), startDate);
				} catch (SymbolNotFoundException e) {
					System.out.println(company.getSymbol() + ": " + e);
					continue;
				}

				// insert to db
				List<HistoricalData> data = null;
				try {
					data = dataParser.parseData(company.getSymbol(), response, DocumentFormats.CSV);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dbConnector.insertHistoricalData(company.getSymbol(), data);
				
			}
		} catch (ParseException e) {
		}


	}

}
