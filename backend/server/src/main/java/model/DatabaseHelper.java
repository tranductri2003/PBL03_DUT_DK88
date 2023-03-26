package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.qos.logback.core.joran.sanity.Pair;

public class DatabaseHelper {
	
	private static DatabaseHelper instance;
	private Connection connection;
	private PreparedStatement statement;
	
	private DatabaseHelper() {
		String connectionUrl =
                "jdbc:sqlserver://LAPTOP-7BLM86RC\\SQLEXPRESS:1433;"
                        + "encrypt=true;"
                        + "trustServerCertificate=true;"
                        + "databaseName=DK88;"
                        + "characterEncoding=utf-8;";
		String user = "LoveLGK";
		String password = "secuoikhanh10namnua";
		try {
			connection = DriverManager.getConnection(connectionUrl, user, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		statement.close();
		connection.close();
	}

	public static DatabaseHelper getInstance() {
		if (instance == null) instance = new DatabaseHelper();
		return instance;
	}
	
	public void setQuery(String sql_query, HashMap<Integer, Object> params) {
		try {
			statement = connection.prepareStatement(sql_query);
			for (var me : params.entrySet()) {
				if (me.getValue() instanceof Integer)
					statement.setInt(me.getKey(), (int) me.getValue());
				else if (me.getValue() instanceof Boolean)
					statement.setBoolean(me.getKey(), (Boolean) me.getValue());
				else
					statement.setString(me.getKey(), (String) me.getValue());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ResultSet readData() {
		try {
			return statement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean updateData() {
		try {
		
			return statement.executeUpdate() > 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
}
