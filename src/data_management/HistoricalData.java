package data_management;

import org.apache.commons.csv.CSVRecord;

public class HistoricalData {
	
	String date;
	double open;
	double high;
	double low;
	double close;
	Long volume;
	double adjClose;
	
	HistoricalData(CSVRecord csvRecor) {
		this.date = csvRecor.get(0);
		this.open = Double.valueOf(csvRecor.get(1));
		this.high = Double.valueOf(csvRecor.get(2));
		this.low = Double.valueOf(csvRecor.get(3));
		this.close = Double.valueOf(csvRecor.get(4));
		this.volume = Long.valueOf(csvRecor.get(5));
		this.adjClose = Double.valueOf(csvRecor.get(6));
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