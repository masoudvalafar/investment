package event_management;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

import data_management.DataManager;
import data_management.EventMonitoringResult;
import data_management.HistoricalData;
import data_storage.DatabaseConnector;

public class DataManagerImpl implements DataManager {

	DatabaseConnector dbConnector = DatabaseConnector.getInstance();

	@Override
	public EventMonitoringResult getData(String symbol, int monitoringPeriod, String date) throws ParseException {
		EventMonitoringResult result = new EventMonitoringResult();

		Date targetDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);

		List<HistoricalData> symbolData = dbConnector.getHistoricalPricing(symbol);
		Date dataDate = new SimpleDateFormat("yyyy-MM-dd").parse(symbolData.get(symbolData.size() - 1).getDate());
		Date previousDate = null;
		for (int i = symbolData.size() - 2; i >= 0; i--) {
			previousDate = dataDate;
			dataDate = new SimpleDateFormat("yyyy-MM-dd").parse(symbolData.get(i).getDate());

			// the list are all sorted by last day, so we have to reverse them
			if (targetDate.compareTo(dataDate) == 0) {
				System.out.println("target date found!");
				result.setTargetDateResult(symbolData.get(i));
				if (i < symbolData.size() - 1 - monitoringPeriod) {
					result.setBeforeTargetList(Lists.reverse(symbolData.subList(i + 1, i + monitoringPeriod + 1)));
				}
				if (i > monitoringPeriod) {
					result.setAfterTargetList(Lists.reverse(symbolData.subList(i - monitoringPeriod - 1, i - 1)));
				}

				break;
			} else if (targetDate.compareTo(previousDate) > 0 && targetDate.compareTo(dataDate) < 0) {
				System.out.println("target date not found!");
				if (i < symbolData.size() - 1 - monitoringPeriod) {
					result.setBeforeTargetList(Lists.reverse(symbolData.subList(i + 1, i + monitoringPeriod + 1)));
				}
				if (i > monitoringPeriod) {
					result.setAfterTargetList(Lists.reverse(symbolData.subList(i - monitoringPeriod - 1, i - 1)));
				}

				break;
			}

		}

		return result;
	}

	@Override
	public List<HistoricalData> getData(String symbol) {
		return Lists.reverse(dbConnector.getHistoricalPricing(symbol));
	}

}
