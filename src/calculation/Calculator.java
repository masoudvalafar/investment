package calculation;

import java.util.List;

import data_management.HistoricalData;

public class Calculator {

	private List<HistoricalData> snpResults;
	private List<HistoricalData> symbolResults;
	private int symbolTotalDays;

	public Calculator(List<HistoricalData> snpResults, List<HistoricalData> symbolResults) {
		this.snpResults = snpResults;
		this.symbolResults = symbolResults;
	}

	public void syncResults() {
		this.symbolTotalDays = symbolResults.size() < snpResults.size() ? symbolResults.size() : snpResults.size();
		symbolResults = symbolResults.subList(0, symbolTotalDays);
		snpResults = snpResults.subList(0, symbolTotalDays);

	}

	public int getSymbolTotalDays() {
		return symbolTotalDays;
	}

	public double getSymbolGain(int day) {
		return (symbolResults.get(day).getClose() - symbolResults.get(day + 1).getClose())
				/ symbolResults.get(day + 1).getClose() * 100;
	}

	public double getSnpGain(int day) {
		return (snpResults.get(day).getClose() - snpResults.get(day + 1).getClose())
				/ snpResults.get(day + 1).getClose() * 100;
	}

}
