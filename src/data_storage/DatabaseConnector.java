package data_storage;

import general.Company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import data_management.HistoricalData;

public class DatabaseConnector {

	static DatabaseConnector instance = null;
	static Connection connection;

	private DatabaseConnector() {
		connection = DatabaseConnection.getConnection();
	}

	public static DatabaseConnector getInstance() {
		if (instance == null) {
			instance = new DatabaseConnector();
		}
		return instance;
	}

	public void initializeDB() {
		Statement statement;
		try {
			statement = connection.createStatement();
			// statement.executeUpdate("drop table if exists symbols;");
			// statement.executeUpdate("drop table if exists symbols_historical_price;");

			ResultSet resultSet = statement
					.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='symbols'");

			// initializing the symbols db
			if (!resultSet.next()) {
				statement.executeUpdate("create table symbols (symbol, name, last_data_date, first_data_date);");
				PreparedStatement prep = connection.prepareStatement("insert into symbols values (?, ?, ?, ?);");
				for (Company company : Company.values()) {
					prep.setString(1, company.getSymbol());
					prep.setString(2, company.getName());
					prep.setString(3, null);
					prep.setString(4, null);
					prep.addBatch();
				}
				prep.executeBatch();
			}

			resultSet = statement
					.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='symbols_historical_price'");

			if (!resultSet.next()) {
				statement
						.executeUpdate("create table symbols_historical_price (symbol, date, open, high, low, close, volume, adjClose);");
			}
		} catch (SQLException e) {
			System.out.println("db issues!");
			System.out.println(e);
		}

	}

	public Date getLastUpdate(String symbol) throws ParseException {
		return getDate(symbol, true);
	}

	public Date getFirstUpdate(String symbol) throws ParseException {
		return getDate(symbol, false);
	}

	private Date getDate(String symbol, boolean lastDay) throws ParseException {
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT last_data_date, first_data_date FROM symbols where symbol = '"
					+ symbol + "'");
			String result = resultSet.getString(lastDay ? 1 : 2);
			return result == null ? null : new SimpleDateFormat("yyyy-MM-dd").parse(result);
		} catch (SQLException e) {
			System.out.println("db issues!");
			System.out.println(e);
			return null;
		}
	}

	public boolean insertHistoricalData(String symbol, List<HistoricalData> dataList) throws ParseException {
		if (dataList == null || dataList.isEmpty()) {
			return false;
		}

		Date lastDate = new SimpleDateFormat("yyyy-MM-dd").parse(dataList.get(0).getDate());
		Date firstDate = new SimpleDateFormat("yyyy-MM-dd").parse(dataList.get(0).getDate());

		try {
			PreparedStatement prep;
			prep = connection.prepareStatement("insert into symbols_historical_price values (?, ?, ?, ?, ?, ?, ?, ?);");
			for (HistoricalData data : dataList) {
				// System.out.println(data.getDate());
				Date historicalDataDate = new SimpleDateFormat("yyyy-MM-dd").parse(data.getDate());
				// System.out.println(historicalDataDate);
				if (historicalDataDate.after(lastDate)) {
					lastDate = historicalDataDate;
				}
				if (firstDate.after(historicalDataDate)) {
					firstDate = historicalDataDate;
				}

				prep.setString(1, symbol);
				prep.setString(2, data.getDate());
				prep.setDouble(3, data.getOpen());
				prep.setDouble(4, data.getHigh());
				prep.setDouble(5, data.getLow());
				prep.setDouble(6, data.getClose());
				prep.setLong(7, data.getVolume());
				prep.setDouble(8, data.getAdjClose());
				prep.addBatch();
			}
			prep.executeBatch();

		} catch (SQLException e) {
			System.out.println("error!!!");
			System.out.println(e);
			return false;
		}

		// update symbol table
		Date symbolTableLastUpdate = getLastUpdate(symbol);
		if (symbolTableLastUpdate == null || lastDate.after(symbolTableLastUpdate)) {

			String lastUpdatePreparedStatement = "UPDATE symbols SET last_data_date = ? WHERE symbol = ?;";
			try {
				PreparedStatement prep = connection.prepareStatement(lastUpdatePreparedStatement);
				prep.setString(1, getDateInFormat(lastDate));
				prep.setString(2, symbol);
				prep.executeUpdate();
			} catch (SQLException e) {
				System.out.println("error in updating db!");
				System.out.println(e);
			}
		}

		Date symbolTableFirstUpdate = getLastUpdate(symbol);
		if (symbolTableFirstUpdate == null || firstDate.before(symbolTableFirstUpdate)) {

			String firstUpdatePreparedStatement = "UPDATE symbols SET first_data_date = ? WHERE symbol = ?;";
			try {
				PreparedStatement prep = connection.prepareStatement(firstUpdatePreparedStatement);
				prep.setString(1, getDateInFormat(firstDate));
				prep.setString(2, symbol);
				prep.executeUpdate();
			} catch (SQLException e) {
				System.out.println("error in updating db!");
				System.out.println(e);
			}
		}

		return true;
	}

	private String getDateInFormat(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);
	}

	public static void main(String args[]) throws ParseException {
		DatabaseConnector d = new DatabaseConnector();
		Date from = new SimpleDateFormat("yyyy-MM-dd").parse("2010-11-01");
		System.out.println(from);
		System.out.println(d.getDateInFormat(from));
	}

	public List<HistoricalData> getHistoricalPricing(String symbol) {

		List<HistoricalData> results = new ArrayList<HistoricalData>();

		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			resultSet = statement
					.executeQuery("SELECT symbol, date, close FROM symbols_historical_price where symbol = '" + symbol
							+ "'");
			while (resultSet.next()) {
				results.add(new HistoricalData(resultSet));
			}
		} catch (SQLException e) {
			System.out.println("db issues!");
			System.out.println(e);
		}

		return results;
	}
}
