package Project;
import java.sql.*;
import java.util.*;
public class uploadFile extends mainFile
{
	public static connectionDatabase con = null;
	public static String fileLocation;
	public static String fileName;
	public static ArrayList<String> array_of_file_sha = new ArrayList<>();
	public static Scanner sc=null;
	public static void insertIntoUserFile(int userId) throws Exception
	{
		System.out.println("Enter the name of the file: ");
		sc = new Scanner(System.in);
		fileName=sc.next();
		con=new connectionDatabase();
		PreparedStatement pstmt=con.getConnect().prepareStatement("insert into userFile (userId,fileName) values(?,?)");
		pstmt.setInt(1,userId);
		pstmt.setString(2,fileName);
		pstmt.executeUpdate();
		int fileId=retrieveFileId(fileName);
		upload(fileId,fileName);
		insertIntoFileTable(fileId,fileName);
	}
	public static void upload(int fileId,String fileName) throws Exception
	{
		createChunk d = new createChunk();
//		System.out.println("Enter number of files want to upload");
//		int n = sc.nextInt();
		int n = 1;
		for(int i =0; i<n; i++)
		{
			fileLocation = "file/"+fileName;
			d.createChunks(fileId);
			System.out.println("--------------------------FILE "+i+"--------------------------------------------");		
		}
		System.out.println("Thank You: Files Uploaded");
	}
	public static void insertIntoFileTable(int FileId,String fileName ) throws Exception
	{

		con = new connectionDatabase();
		int fileId=retrieveFileId(fileName);
		Integer[] sha256Id=retrieveShaId(fileId);
		PreparedStatement pstmt = con.getConnect().prepareStatement("insert into fileDetails values(?,?)");
		for(int i=0;i<sha256Id.length;i++)
		{
			pstmt.setInt(1,FileId);
			pstmt.setInt(2,sha256Id[i]);
			pstmt.addBatch();
		}

		int[] records=pstmt.executeBatch();
	}
	public static int retrieveFileId(String fileName) throws Exception
	{
		int fileId = 0;
		PreparedStatement pstmt = con.getConnect().prepareStatement("select userFileId from userFile where fileName=?");
		pstmt.setString(1,fileName);
		ResultSet rs=pstmt.executeQuery();
		while(rs.next())
		{
			fileId=rs.getInt(1);
		}
		return fileId;
	}
	public static Integer[] retrieveShaId(int fileId) throws Exception
	{
		ArrayList<Integer> arr=new ArrayList<Integer>();
		PreparedStatement pstmt = con.getConnect().prepareStatement("select shaId from hashTable where userFileId=? ");
		pstmt.setInt(1,fileId);
		ResultSet rs=pstmt.executeQuery();
		while(rs.next())
		{
			arr.add(rs.getInt(1));
		}
		Integer[] arrId = new Integer[arr.size()];
		arrId = arr.toArray(arrId);
		return arrId;
	}


}
