package data_management;

import java.text.ParseException;
import java.util.Date;

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
	String getHistoricalData(String symbol, Date startDate) throws SymbolNotFoundException;
	String getHistoricalData(String symbol, Date startDate, Date endDate) throws SymbolNotFoundException;

}
