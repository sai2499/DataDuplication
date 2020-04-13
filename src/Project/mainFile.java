package Project;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class mainFile {
	
	public static Map<Integer, ArrayList<String>> map = new HashMap<Integer, ArrayList<String>>();
	public static Map<String, Integer> map4count = new HashMap<String,Integer>();
	public static String username;
	public static Scanner sc;
	public static int userId=0;
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		sc = new Scanner(System.in);
		System.out.print("ENTER USERNAME : ");
		username = sc.next();
		System.out.print("ENTER PASSWORD : ");
		String password = sc.next();
		
		Login login = new Login();
		if(login.checklog(username, password))
		{
			//USER MENU
			connectionDatabase c = new connectionDatabase();
			PreparedStatement pstmt=c.getConnect().prepareStatement("select userId from userTable where userName=?");
			pstmt.setString(1,username);
			ResultSet rst=pstmt.executeQuery();
			while(rst.next())
			{
				userId=rst.getInt(1);
			}
			userMenu(userId);
		}
		else
		{
			System.out.println("Invalid Credentials or User Not Exist: Do you want to register(0/1)");
			
			int ch=sc.nextInt();
			
			switch(ch){
			
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
				String query = "INSERT INTO userTable (userName,passwords,emailId) VALUES('"
								+ name + "' , '" + passWord +"' , '"
								+ email + "');";
				String query1="insert into login (userName,passwords) values ('"+ name + "' , '" + passWord +"');";
				connectionDatabase c = new connectionDatabase();
				c.runQuery(query);
				c.runQuery(query1);
				PreparedStatement pstmt=c.getConnect().prepareStatement("select userId from userTable where userName=?");
				pstmt.setString(1,name);
				ResultSet rst=pstmt.executeQuery();
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
		System.out.println("Welcome "+ username + "\nPlease Select a option\n\t1. Upload File\n\t2. Download File\n\t3. Delete File\n\t4. Delete User\n\t5. updateFile\n\t6. Exit");
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
			deleteData.deleteUser(userId);
			break;
		case 5:
			//uploadFile.update();
			break;
		default:
			System.out.println("Invalid Input");
			break;
		}
	}

}
