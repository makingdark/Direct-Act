package DBUtil;

import java.sql.*;

public class SQLCon {
private static Connection conn = null;
	
	public static Connection getConnection() {
		String url = "jdbc:mysql://localhost:8889/rekenen";
		String username = "root";
		String passwd = "root";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			conn = DriverManager.getConnection(url, username, passwd);
			} 
		catch (Exception e) {
			System.out.print("niet gelukt");
			e.printStackTrace();
		}
		return conn;
	}
	public static void closeConnection(Connection conn) {
		try {
			conn.close();
		} catch (Exception e) {
			e.getMessage();
		}
	}
}
