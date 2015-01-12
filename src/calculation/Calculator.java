package calculation;

import java.util.List;

import data_management.EventMonitoringResult;


public interface Calculator {

	List<Double> calculateGain(EventMonitoringResult symbolData, EventMonitoringResult snpData);

}
