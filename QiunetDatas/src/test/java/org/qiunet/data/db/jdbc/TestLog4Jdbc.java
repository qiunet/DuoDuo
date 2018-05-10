package org.qiunet.data.db.jdbc;

import org.junit.Ignore;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author qiunet
 *         Created on 17/2/14 16:10.
 */
public class TestLog4Jdbc {
	@Test
	public void testLog4Jdbc(){
		String url = "jdbc:mysql://127.0.0.1:3306/test_0?useUnicode=true&useSSL=false";
		String driver= "com.mysql.jdbc.Driver";
		String ursename="root";
		String password="qiuyang";
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, ursename, password);
			Statement statement = connection.createStatement();
			statement.execute("SELECT * from login;");
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
