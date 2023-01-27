package database_management;
import java.sql.SQLException;

public class test {
	public static void main(String args[]) throws SQLException {
		db_creator.createAll();
		
		System.out.println("\n Affichage db vide \n");
		db_users_manager.selectALLusers();
		
		System.out.println("\n Ajout de 3 personnes \n");
		db_users_manager.updateUserTable("12.167.134.50", "P1");
		db_users_manager.updateUserTable("12.167.134.51", "P2");
		db_users_manager.updateUserTable("12.167.134.52", "P3");
		db_users_manager.selectALLusers();
		
		System.out.println("\n Modification d'un pseudo \n");
		db_users_manager.updateUserTable("12.167.134.51", "New P2");
		db_users_manager.selectALLusers();
		
		System.out.println("\n Selection d'un utilisateur \n");
		String user1 = db_users_manager.getUserPseudo("12.167.134.50");
		System.out.println("P1 pseudo : " + user1);
		
		System.out.println("\n Suppresion de P2 \n");
		db_users_manager.removeUser("12.167.134.51");
		db_users_manager.selectALLusers();
	}
}
