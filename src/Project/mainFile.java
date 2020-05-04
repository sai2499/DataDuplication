package Project;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class mainFile
{
	public static connectionDatabase con=null;

	public static Map<String, Integer> map4count = new HashMap<String,Integer>();
	public static String username;
	public static Scanner sc;
	public static int userId=0;
	public static void main(String[] args) throws Exception
	{
		sc = new Scanner(System.in);
		System.out.print("ENTER USERNAME : ");
		username = sc.next();
		System.out.print("ENTER PASSWORD : ");
		String password = sc.next();
		Login login = new Login();
		if(login.checkLog(username, password))
		{
			//USER MENU
			con= new connectionDatabase();
			PreparedStatement pstmtCheck=con.getConnect().prepareStatement("select userId from userTable where userName=?");
			pstmtCheck.setString(1,username);
			ResultSet rst=pstmtCheck.executeQuery();
			while(rst.next())
			{
				userId=rst.getInt(1);
			}
			userMenu(userId);
		}
		else
		{
			con=new connectionDatabase();
			System.out.println("Invalid Credentials or User Not Exist: Do you want to register(0/1)");
			int ch=sc.nextInt();
			switch(ch)
			{
			case 0:
				System.out.println("Thank You");
				break;
			case 1:
				System.out.println("Enter the name: ");
				String name=sc.next();
				System.out.println("Enter the pass: ");
				String passWord=sc.next();
				System.out.println("Enter the email: ");
				String email=sc.next();
				PreparedStatement pstmtUserTable=con.getConnect().prepareStatement("insert into userTable (userName,passwords,emailId) values (?,?,?)");
				pstmtUserTable.setString(1,name);
				pstmtUserTable.setString(2,passWord);
				pstmtUserTable.setString(3,email);
				int insertValue=pstmtUserTable.executeUpdate();
				PreparedStatement pstmtUserId=con.getConnect().prepareStatement("select userId from userTable where userName=?");
				pstmtUserId.setString(1,name);
				ResultSet rst=pstmtUserId.executeQuery();
				while(rst.next())
				{
					userId=rst.getInt(1);
				}
				userMenu(userId);
			
			default:
				System.out.println(":: INVALID OPTION ::");
				break;

			}
		}
		
	}
	
	public static void userMenu(int userId) throws Exception
	{
		System.out.println("Welcome "+ username + "\nPlease Select a option\n\t1. Upload File\n\t2. Download File\n\t3. Delete File\n\t4. Delete Version\n\t5. Delete User\n\t6. updateFile\n\t7. Exit");
		int ch=sc.nextInt();
		switch(ch)
		{
		case 1:
			uploadFile.insertIntoUserFile(userId);
			break;
		case 2:
			downloadFile.download(userId);
			break;
		case 3:
			deleteData.deleteFile(userId);
			break;
		case 4:
			deleteData.deleteVersion(userId);
			break;
		case 5:
			deleteData.deleteUser(userId);
			break;
		case 6:
			UpdateFile.update(userId);
			break;
		default:
			System.out.println("Invalid Input");
			break;
		}
	}
}
