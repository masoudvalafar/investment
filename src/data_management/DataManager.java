package data_management;

import java.text.ParseException;
import java.util.List;

public interface DataManager {

	EventMonitoringResult getData(String symbol, int monitoringPeriod, String date) throws ParseException;

	List<HistoricalData> getData(String symbol);

}
