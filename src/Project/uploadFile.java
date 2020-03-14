package Project;

//import java.util.*;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;



public class uploadFile extends mainFile
{
	public static String fileLocation;
	public static String fileName;
	public static ArrayList<String> array_of_file_sha = new ArrayList<>();;
	public static StringBuilder Insertquery = new StringBuilder("Insert into Hashtable (rollHash,sha256) values ");
	public static connectionDatabase con=null;
	public static void upload(int UserId) throws Exception
	{

		createChunk d = new createChunk();
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
//		System.out.println("Enter number of files want to upload");
//		int n = sc.nextInt();
		insertIntoUserFile(userId);
		int n = 1;
		for(int i =0; i<n; i++)
		{
			System.out.println("Enter File Name:\t");
			fileName = sc.next();
			fileLocation = "file/"+fileName;
			d.createChunks();
			Insertquery.deleteCharAt(Insertquery.length()-1);
//			Insertquery.append(" ON DUPLICATE KEY UPDATE hash = values (hash),hash256 = values (hash256)");
			System.out.println(Insertquery);
			insertQueryToDb();
			System.out.println("--------------------------FILE "+i+"--------------------------------------------");		
		}
		System.out.println("Thank You: Files Uploaded");
	}
	public static void insertIntoUserFile(int userid) throws Exception
	{
		con=new connectionDatabase();
		PreparedStatement pstmt=con.getConnect().prepareStatement("insert into userFile (userId,fileName) values(?,?)");
		pstmt.setInt(1,userid);
		pstmt.setString(2,fileName);
		pstmt.executeUpdate();
	}
	public static String[] retriveShaId(int fileId) throws Exception
	{
		//PreparedStatement pstmt=con.getConnect().prepareStatement("select ")
		String[] arr=null;
		return arr;
	}
	public static void insertIntoFileTable(int UserId ) throws Exception
	{
		int fileId=0;
		con=new connectionDatabase();
		PreparedStatement pstmt=con.getConnect().prepareStatement("select userfileId from userFile where userId=?");
		pstmt.setInt(1,UserId);
		ResultSet rs=pstmt.executeQuery();
		while(rs.next())
		{
			fileId=rs.getInt(1);
		}
		String[] sha256Id=retriveShaId(fileId);
	}
	public static void insertQueryToDb() throws Exception
	{
		con = new connectionDatabase();
		Statement stmt = null;
		System.out.println(Insertquery);
		stmt = con.getConnect().createStatement();
		stmt.execute(Insertquery.toString());
		con.closeConnection(con, stmt);
	}
}
