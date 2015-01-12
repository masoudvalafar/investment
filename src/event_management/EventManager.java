package event_management;

import general.Company;

import java.text.ParseException;
import java.util.List;

import calculation.Calculator;
import data_management.DataManager;
import data_management.EventMonitoringResult;
import data_management.HistoricalData;
import data_storage.DatabaseConnector;

public class EventManager {

	private static EventManager eventManager;
	List<HistoricalData> snpResultsOriginal = null;
	DatabaseConnector dbConnector = DatabaseConnector.getInstance();
	private DataManager dataManager = new DataManagerImpl();
	Calculator calculator = new CalculatorImpl();

	public static EventManager getInstance() {
		if (eventManager == null) {
			eventManager = new EventManager();
		}

		return eventManager;

	}

	/*
	 * TODO: This method is not implemented completely
	 */
	public void findEvents(String symbol) {
		// getting data
		List<HistoricalData> symbolData =  dataManager.getData(symbol);
		List<HistoricalData> snpData = dataManager.getData(Company.snp.getSymbol());
		
		// syncing them
		int minLength = Math.min(symbolData.size(), snpData.size());
		symbolData = symbolData.subList(symbolData.size() - minLength, symbolData.size());
		snpData = snpData.subList(snpData.size() - minLength, snpData.size());
		System.out.println(symbolData.get(0).getDate());
		System.out.println(snpData.get(0).getDate());
		// calculating gain
	}

	
	public void monitorEvent(String symbol, int monitoringPeriod, String date) {
		EventMonitoringResult symbolData;
		EventMonitoringResult snpData;
		try {
			symbolData = dataManager.getData(symbol, monitoringPeriod, date);
			snpData = dataManager.getData(Company.snp.getSymbol(), monitoringPeriod, date);
			/*
			 * this is for testing getData and needs to move to a test file
			if (a.getTargetDateResult() != null) {
				System.out.println(a.getTargetDateResult().getDate());
			}
			System.out.println(a.getBeforeTargetList().get(0).getDate());
			System.out.println(a.getBeforeTargetList().get(19).getDate());
			System.out.println(a.getAfterTargetList().get(0).getDate());
			System.out.println(a.getAfterTargetList().get(19).getDate());
			*/
		} catch (ParseException e) {
			return;
		}
		
		List<Double> a = calculator.calculateGain(symbolData, snpData);
		System.out.println(a.size());
		for (Double d: a) {
			System.out.println(d);
		}
	}

}
