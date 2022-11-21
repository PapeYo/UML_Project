package database_management;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class db_manager {
	
	private static String url;
	private static Connection connection;
	
	public static Connection connect() {
		url = "jdbc:sqlite:C:/Chat_App/chat_app_db.db";
		try {
			connection = DriverManager.getConnection(url);
			System.out.println("Connection is successful to the database " + url);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}
	
	public static void disconnect() {
		try {
            if (connection != null) {
                connection.close();
                System.out.println("Disconnection is successful.");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
	}
}
