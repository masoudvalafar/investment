package data_management;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVRecord;

public class HistoricalData {
	
	String symbol;
	String date;
	double open;
	double high;
	double low;
	double close;
	Long volume;
	double adjClose;
	
	public HistoricalData(ResultSet resultSet) throws SQLException {
		this.symbol = resultSet.getString("symbol");
		this.date = resultSet.getString("date");
		this.close = resultSet.getDouble("close");
	}
	
	public HistoricalData(CSVRecord csvRecord, String symbol) {
		this.symbol = symbol;
		this.date = csvRecord.get(0);
		this.open = Double.valueOf(csvRecord.get(1));
		this.high = Double.valueOf(csvRecord.get(2));
		this.low = Double.valueOf(csvRecord.get(3));
		this.close = Double.valueOf(csvRecord.get(4));
		this.volume = Long.valueOf(csvRecord.get(5));
		this.adjClose = Double.valueOf(csvRecord.get(6));
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public Long getVolume() {
		return volume;
	}

	public void setVolume(Long volume) {
		this.volume = volume;
	}

	public double getAdjClose() {
		return adjClose;
	}

	public void setAdjClose(double adjClose) {
		this.adjClose = adjClose;
	}
}