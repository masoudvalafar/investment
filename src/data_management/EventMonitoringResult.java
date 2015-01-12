package data_management;

import java.util.List;

public class EventMonitoringResult {

	private HistoricalData targetDateResult;
	private List<HistoricalData> beforeEventList = null;
	private List<HistoricalData> afterTargetList = null;

	public void setTargetDateResult(HistoricalData targetDateResult) {
		this.targetDateResult = targetDateResult;
	}
	
	public HistoricalData getTargetDateResult() {
		return targetDateResult;
	}

	public void setBeforeTargetList(List<HistoricalData> beforeEventList) {
		this.beforeEventList  = beforeEventList;
	}

	public void setAfterTargetList(List<HistoricalData> afterTargetList) {
		this.afterTargetList = afterTargetList;
	}

	public List<HistoricalData> getBeforeTargetList() {
		return beforeEventList;
	}

	public List<HistoricalData> getAfterTargetList() {
		return afterTargetList;
	}

}
