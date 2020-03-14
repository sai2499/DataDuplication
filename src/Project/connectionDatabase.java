package Project;
import java.io.*;
import java.sql.*;
import java.util.*;
public class connectionDatabase
{
	//DATABASE CONNECTION MODULE
	//static final String JDBC_DRIVER = "org.h2.Driver";
	//static final String DB_URL = "jdbc:h2:~/FinalProject";
	   //  Database credentials
	//static final String USER = "sa";
	//static final String PASS = "";
	//================EXCEPTION HANDLING NOT DONE
	Connection con;
	Statement stmt;
	public Connection getConnect() throws Exception
	{
		FileInputStream fin=new FileInputStream("dbCred.properties");
		Properties pro=new Properties();
		pro.load(fin);
		String JDBC_DRIVER=pro.getProperty("driver");
		String DB_URL=pro.getProperty("url");
		String USER=pro.getProperty("user");
		String PASS=pro.getProperty("pass");
		Class.forName(JDBC_DRIVER);
		Connection con= DriverManager.getConnection(DB_URL,USER,PASS);
		return	con;
	}
	public void closeConnection(connectionDatabase c , Statement stmt) throws Exception
	{
		stmt.close();
		c.getConnect().close();
	}
	public void runQuery(String query) throws Exception
	{
		stmt = null;
		if(con == null)
		{
			stmt = getConnect().createStatement();
			stmt.execute(query);
			closeConnection(this, stmt);
			return;
		}
		stmt.execute(query);
		closeConnection(this, stmt);
	}
}
