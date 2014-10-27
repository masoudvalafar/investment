package data_storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

	private static final String DB_NAME = "test.db";
	private static final String DRIVER = "sqlite:";
	private static Connection instance;
	
	private DatabaseConnection() {
		try {
			instance = DriverManager.getConnection("jdbc:" + DRIVER + ":" + DB_NAME);
		} catch (SQLException e) {
			System.out.println("unable to start the db!!!");
		}
	}

	public static Connection getConnection() {
		if (instance == null) {
			new DatabaseConnection();
		}
		
		return instance;
	}

}
