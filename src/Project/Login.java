package Project;

import java.sql.*;

public class Login {
	
	public boolean checkLog(String user,String pass) throws Exception
	{		
		connectionDatabase c = new connectionDatabase();
		PreparedStatement pstmtCheck=c.getConnect().prepareStatement("select * from userTable where userName=? and passwords=?");
		pstmtCheck.setString(1,user);
		pstmtCheck.setString(2,pass);
		ResultSet rs = pstmtCheck.executeQuery();
		if (rs.next())
		{
			return true;
		}
		else
		{

        	return false;
        }
	}
}
