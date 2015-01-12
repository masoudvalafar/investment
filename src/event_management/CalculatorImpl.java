package event_management;

import java.util.ArrayList;
import java.util.List;

import calculation.Calculator;
import data_management.EventMonitoringResult;
import data_management.HistoricalData;

public class CalculatorImpl implements Calculator {

	/*
	 * This method needs to be tested Gain concept needs to be defined -
	 * currently it is:
	 * 
	 * (symbol today price - symbol last day price ) - (snp today price - snp
	 * yesterday price)
	 */
	@Override
	public List<Double> calculateGain(EventMonitoringResult symbolData, EventMonitoringResult snpData) {
		List<Double> gains = new ArrayList<Double>();

		// first before then the target date and then after
		List<HistoricalData> beforeTargetSymbol = symbolData.getBeforeTargetList();
		List<HistoricalData> beforeTargetSnp = snpData.getBeforeTargetList();

		List<HistoricalData> afterTargetSymbol = symbolData.getAfterTargetList();
		List<HistoricalData> afterTargetSnp = snpData.getAfterTargetList();

		if (beforeTargetSymbol.size() != beforeTargetSnp.size() || afterTargetSymbol.size() != afterTargetSnp.size()) {
			return null;
		}

		// gains before the event
		for (int i = 1; i < beforeTargetSymbol.size(); i++) {
			gains.add((beforeTargetSymbol.get(i).getClose() - beforeTargetSymbol.get(i - 1).getClose())
					- (beforeTargetSnp.get(i).getClose() - beforeTargetSnp.get(i - 1).getClose()));
		}

		// calculating gain around target date
		if (symbolData.getTargetDateResult() != null && snpData.getTargetDateResult() != null) {
			// gain on event date
			gains.add((symbolData.getTargetDateResult().getClose() - beforeTargetSymbol.get(
					beforeTargetSymbol.size() - 1).getClose())
					- (snpData.getTargetDateResult().getClose() - beforeTargetSnp.get(beforeTargetSnp.size() - 1)
							.getClose()));
			// gain for the day immediately after event
			gains.add((afterTargetSymbol.get(0).getClose() - symbolData.getTargetDateResult().getClose())
					- (afterTargetSnp.get(0).getClose() - snpData.getTargetDateResult().getClose()));
		} else {
			// gain for the day immediately after event
			gains.add((afterTargetSymbol.get(0).getClose() - beforeTargetSymbol.get(beforeTargetSymbol.size() - 1)
					.getClose())
					- (afterTargetSnp.get(0).getClose() - beforeTargetSnp.get(beforeTargetSnp.size() - 1).getClose()));
		}

		// gains after the event
		for (int i = 1; i < beforeTargetSymbol.size(); i++) {
			gains.add((beforeTargetSymbol.get(i).getClose() - beforeTargetSymbol.get(i - 1).getClose())
					- (beforeTargetSnp.get(i).getClose() - beforeTargetSnp.get(i - 1).getClose()));
		}

		return gains;
	}

}
