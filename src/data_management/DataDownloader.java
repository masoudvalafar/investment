package data_management;

import java.text.ParseException;

import data_management.exceptions.SymbolNotFoundException;

public interface DataDownloader {

	/**
	 * Provide the daily historical data from begning to today
	 * 
	 * @param symbol
	 * @return
	 * @throws ParseException
	 * @throws SymbolNotFoundException 
	 */
	String getHistoricalData(String symbol) throws ParseException, SymbolNotFoundException;

}
