package Project;
import java.io.*;
import java.sql.*;
import java.util.*;
public class connectionDatabase
{
	public Connection con=null;
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
		con= DriverManager.getConnection(DB_URL,USER,PASS);
		return	con;
	}
}
