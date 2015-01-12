import event_management.EventManager;
import general.Company;

public class RunInvestment {

	public static void main(String[] args) {
		
		// Initializing and downloading data for the companies
		/*
		DataDownloader dataDownloader = new YahooDataDownloader();
		DataParser dataParser = new DataParser();
		DatabaseConnector dbConnector = DatabaseConnector.getInstance();

		dbConnector.initializeDB();
		String response = null;
		try {
			for (Company company : Company.values()) {
				
				System.out.println("working on symbol: " + company.getSymbol());
				
				// get last update
				Date lastAvailableDataPointDate = dbConnector.getLastUpdate(company.getSymbol());
				if (lastAvailableDataPointDate == null) {
					lastAvailableDataPointDate = new SimpleDateFormat("yyyy-MM-dd").parse("1900-01-01");
				}
				System.out.println("got last update: " + lastAvailableDataPointDate);
				
				// get data
				try {
					response = dataDownloader.getHistoricalData(company.getSymbol(), lastAvailableDataPointDate);
					System.out.println("downloaded the data.");
				} catch (SymbolNotFoundException e) {
					System.out.println(company.getSymbol() + ": " + e);
					continue;
				}

				if (response == null) {
					System.out.println("no data to work on!");
					continue;
				}
				// insert to db
				List<HistoricalData> data = null;
				try {
					data = dataParser.parseData(company.getSymbol(), response, DocumentFormats.CSV);
					System.out.println("completed parsing the data. data length: " + data.size());
				} catch (IOException e) {
					System.out.println("error in parsing data!");
					System.out.println(e);
				}
				
				dbConnector.insertHistoricalData(company.getSymbol(), data);
				System.out.println("completed inserting the data.");
			}
		} catch (ParseException e) {
			System.out.println("error in parsing...");
			System.out.println(e);
		}
		*/

		/*
		 *  Event handling
		 */
		
		// create an event filter, pass it to eventmanager and it finds all the similar 
		
		EventManager eventManager = EventManager.getInstance();
		eventManager.findEvents(Company.apple.getSymbol());
		//eventManager.monitorEvent(Company.apple.getSymbol(), 20, "2014-9-16");
		
	}

}
