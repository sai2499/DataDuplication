package Project;

import java.sql.ResultSet;
import java.sql.Statement;

public class Login {
	
	public boolean checklog(String user,String pass) throws Exception
	{		
		connectionDatabase c = new connectionDatabase();
		Statement stmt = null;
		String query="SELECT * FROM login WHERE username = '"+ user + "' AND passwords = '" + pass + "'";
		System.out.println(query);
		try
		{
			stmt = c.getConnect().createStatement();
			ResultSet rs = stmt.executeQuery(query);
            if (rs.next())
            {
            	c.closeConnection(c, stmt);
            	return true;
            } 
            else 
            {
            	c.closeConnection(c, stmt);
            	return false;
            }
		}
		catch(Exception e)
		{ 
			//Handle errors for JDBC 
			e.printStackTrace();
		}
		c.closeConnection(c, stmt);
		return false;
	}
}
