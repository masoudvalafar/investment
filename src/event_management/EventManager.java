package event_management;

import general.Company;

import java.util.ArrayList;
import java.util.List;

import data_management.HistoricalData;
import data_storage.DatabaseConnector;

public class EventManager {

	private static EventManager eventManager;
	List<HistoricalData> snpResultsOriginal = null;
	DatabaseConnector dbConnector = DatabaseConnector.getInstance();

	public static EventManager getInstance() {
		if (eventManager == null) {
			eventManager = new EventManager();
		}

		return eventManager;

	}

	public void findEvents() {

		List<List<Double>> positiveEventsDates = new ArrayList<List<Double>>();
		List<List<Double>> negativeEventsDates = new ArrayList<List<Double>>();
		ArrayList<Integer> companyNegativeEvents = new ArrayList<Integer>();
		ArrayList<Integer> companyPositiveEvents = new ArrayList<Integer>();
		
		for (Company company : Company.values()) {
			
			if (company == Company.snp) {
				continue;
			}
			
			companyNegativeEvents.clear();
			companyPositiveEvents.clear();
			
			/*
			 * getting the data for symbol
			 */
			List<HistoricalData> snpResults = getSNPResults();
			List<HistoricalData> symbolResults = dbConnector.getHistoricalPricing(company.getSymbol());

			int symbolTotalDays = symbolResults.size() < snpResults.size() ? symbolResults.size() : snpResults.size();
			symbolResults = symbolResults.subList(0, symbolTotalDays);
			snpResults = snpResults.subList(0, symbolTotalDays);

			/*
			 * comparing symbol changes vs snp to find the events
			 */
			double symbolTodayValue = -1;
			double snpTodayValue = -1;
			double symbolPreviousValue = -1;
			double snpPreviousValue = -1;
			double symbolGain = -1;
			double snpGain = -1;
			
			for (int day = symbolTotalDays - 1; day > 0; day--) {
				symbolTodayValue = symbolResults.get(day).getClose();
				snpTodayValue = snpResults.get(day).getClose();

				if (symbolPreviousValue != -1) {
					symbolGain = (symbolTodayValue - symbolPreviousValue) / symbolPreviousValue * 100;
					snpGain = (snpTodayValue - snpPreviousValue) / snpPreviousValue * 100;

					// event definition
					double relativeGain = symbolGain - snpGain;
					if (Math.abs(relativeGain) > 4) {
						if (relativeGain > 4) {
							companyPositiveEvents.add(day);
						} else {
							companyNegativeEvents.add(day);
						}
						
						System.out.println(company.getSymbol() + " - " + symbolResults.get(day).getDate()
								+ " - snp gain: " + snpGain + " - symbol gain: " + symbolGain + " - " + relativeGain);
					}
				}

				symbolPreviousValue = symbolTodayValue;
				snpPreviousValue = snpTodayValue;
			}
			
			/*
			 * getting the data from 20 days before events
			 */
			
			for (int event: companyNegativeEvents) {
				if (event > 10 && event < symbolTotalDays - 10) {
					for (int i = event + 10; i > event - 10; i--) {
						System.out.print(symbolResults.get(i).getClose() - symbolResults.get(i + 1).getClose());
					}
					System.out.println();
				}
					
			}
			break;
		}
	}

	private List<HistoricalData> getSNPResults() {
		if (snpResultsOriginal == null) {
			snpResultsOriginal = dbConnector.getHistoricalPricing(Company.snp.getSymbol());
		}

		return new ArrayList<HistoricalData>(snpResultsOriginal);
	}

}
