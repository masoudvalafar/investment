package event_management;

import general.Company;

import java.util.ArrayList;
import java.util.List;

import calculation.Calculator;
import data_management.HistoricalData;
import data_storage.DatabaseConnector;

public class EventManager {

	private static EventManager eventManager;
	List<HistoricalData> snpResultsOriginal = null;
	DatabaseConnector dbConnector = DatabaseConnector.getInstance();
	private int monitoringPeriod = 20;

	public static EventManager getInstance() {
		if (eventManager == null) {
			eventManager = new EventManager();
		}

		return eventManager;

	}

	public void findEvents() {

		List<List<Double>> positiveEventsObservation = new ArrayList<List<Double>>();
		List<List<Double>> negativeEventsObservation = new ArrayList<List<Double>>();
		ArrayList<Integer> companyNegativeEvents = new ArrayList<Integer>();
		ArrayList<Integer> companyPositiveEvents = new ArrayList<Integer>();

		for (Company company : Company.values()) {

			if (company == Company.snp) {
				continue;
			}

			companyNegativeEvents.clear();
			companyPositiveEvents.clear();

			/*
			 * getting/loading the data for symbol
			 */
			Calculator calculator = new Calculator(getSNPResults(), dbConnector.getHistoricalPricing(company
					.getSymbol()));
			calculator.syncResults();

			/*
			 * comparing symbol changes vs snp to find the events
			 */
			double symbolGain = -1;
			double snpGain = -1;

			for (int day = calculator.getSymbolTotalDays() - 2; day > 0; day--) {

				symbolGain = calculator.getSymbolGain(day);
				snpGain = calculator.getSnpGain(day);

				// event definition
				double relativeGain = symbolGain - snpGain;
				if (Math.abs(relativeGain) > 4) {
					if (relativeGain > 4) {
						companyPositiveEvents.add(day);
					} else {
						companyNegativeEvents.add(day);
					}

//					System.out.println(company.getSymbol() + " - snp gain: " + snpGain + " - symbol gain: "
//							+ symbolGain + " - " + relativeGain);
				}
			}

			/*
			 * getting the data from monitoringPeriod days before events until
			 * monitoringPeriod after
			 */

			for (int event : companyNegativeEvents) {
				if (event > this.monitoringPeriod && event < calculator.getSymbolTotalDays() - monitoringPeriod) {
					// monitoring event from 10 days earlier to 10 days after
					ArrayList<Double> monitoring = new ArrayList<Double>();
					for (int i = event + monitoringPeriod; i > event - (monitoringPeriod + 1); i--) {
						monitoring.add(calculator.getSymbolGain(i));
					}
					negativeEventsObservation.add(monitoring);
				}

			}

			for (int event : companyPositiveEvents) {
				if (event > this.monitoringPeriod && event < calculator.getSymbolTotalDays() - monitoringPeriod) {
					// monitoring event from 10 days earlier to 10 days after
					ArrayList<Double> monitoring = new ArrayList<Double>();
					for (int i = event + monitoringPeriod; i > event - (monitoringPeriod + 1); i--) {
						monitoring.add(calculator.getSymbolGain(i));
					}
					positiveEventsObservation.add(monitoring);
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
