import java.io.IOException;
import java.text.ParseException;

import data_management.DataDownloader;
import data_management.DataParser;
import data_management.DocumentFormats;
import data_management.YahooDataDownloader;
import data_management.exceptions.SymbolNotFoundException;

public class RunInvestment {

	public enum Company {
		snp("SNP", "S&P 500"),
		blizzard("ATVI", "Activision Blizzard"),
		adobe("ADBE", "Adobe Systems Incorporated"),
		akamai("AKAM", "Akamai Technologies, Inc"),
		amazon("AMZN", "Amazon.com, Inc."),
		apple("AAPL", "Apple Inc."),
		bidu("BIDU", "Baidu.com, Inc."),
		broadcom("BRCM", "Broadcom Corporation"),
		cisco("CSCO", "Cisco Systems, Inc."),
		ebay("EBAY", "eBay Inc."),
		expedia("EXPE", "Expedia, Inc."),
		facebook("FB", "Facebook, Inc."),
		google_a("GOOGL", "Google Inc. Class A"),
		google_c("GOOG", "Google Inc. Class C"),
		intel("INTC", "Intel Corporation"),
		microsoft("MSFT", "Microsoft Corporation"),
		netflix("NFLX", "Netflix"),
		nvidia("NVDA", "NVIDIA Corporation"),
		qualcom("QCOM", "QUALCOMM Incorporated"),
		tesla("TSLA", "Tesla Motors, Inc."),
		yahoo("YHOO", "Yahoo! Inc.");
		
		
		
		String symbol;
		String name;
		Company(String symbol, String name) {
			this.symbol = symbol;
			this.name = name;
		}
		
		String getSymbol(){
			return symbol;
		}
		
		String getName() {
			return name;
		}
	}
	

	public static void main(String[] args) {
		DataDownloader dataDownloader = new YahooDataDownloader();
		DataParser dataParser = new DataParser();

		String response = null;
		try {
			for (Company company : Company.values()) {
				try {
					response = dataDownloader.getHistoricalData(company.getSymbol());
				} catch (SymbolNotFoundException e) {
					System.out.println(company.getSymbol() + ": " + e);
					continue;
				}

				try {
					System.out.println(company.getSymbol() + ": " + dataParser.parseData(response, DocumentFormats.CSV).size());
				} catch (IOException ioe) {
				}
			}
		} catch (ParseException e) {
		}

	}

}
