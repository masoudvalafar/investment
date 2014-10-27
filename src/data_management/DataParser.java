package data_management;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class DataParser {
	public List<HistoricalData> parseData(String symbol, String data, DocumentFormats format) throws IOException {
		List<HistoricalData> historicalDataList = new ArrayList<HistoricalData>();
		
		if (DocumentFormats.CSV == format) {
			for (CSVRecord csvRecord : CSVParser.parse(data, CSVFormat.RFC4180)) {
				try{
					historicalDataList.add(new HistoricalData(csvRecord, symbol));
				} catch (NumberFormatException e) {
					continue;
				}
			}
			
		}
		return historicalDataList;
	}

}
