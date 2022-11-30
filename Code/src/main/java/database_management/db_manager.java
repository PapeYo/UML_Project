package database_management;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class db_manager {
	
	private static String db_url = "jdbc:sqlite:Database_folder/chat_app_db.db";
	private static Connection connection;
	
	public static Connection connect() {
		try {
			connection = DriverManager.getConnection(db_url);
			System.out.println("Connection is successful to the database " + db_url);
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
