package Project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class UpdateFile
{
    public static RetrieveIDs rid=null;
    public static connectionDatabase con=null;
    public static uploadFile up=null;
    public static Scanner sc=null;
    public static void update(int userId) throws Exception
    {
        int versionCount=0;
        rid=new RetrieveIDs();
        sc=new Scanner(System.in);
        con=new connectionDatabase();
        System.out.println("Enter the name of the file: ");
        String fileName=sc.nextLine();
        int fileId=rid.retrieveFileId(userId,fileName);
        int versionNo=rid.retrieveVersionNo(fileId);
        String newFileLocation="File/"+fileName;
        if(checkFileName(userId,fileName))
        {
            LocalDateTime myDateObj = LocalDateTime.now();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy|HH:mm:ss");
            String formattedDate = myDateObj.format(myFormatObj);
            String[] arr=fileName.split("\\.");
            String newFileName=arr[0]+"_"+formattedDate+"."+arr[1];
            versionCount=versionNo+1;
            PreparedStatement insertPstmt=con.getConnect().prepareStatement("insert into userFile (userId,fileName,versionNo,versionOf) values (?,?,?,?)");
            insertPstmt.setInt(1,userId);
            insertPstmt.setString(2,newFileName);
            insertPstmt.setInt(3,versionCount);
            insertPstmt.setInt(4,fileId);
            insertPstmt.executeUpdate();
            int versionId=rid.retrieveVersionId(fileId,versionCount);
            createChunk d = new createChunk();
            d.createChunks(versionId,newFileLocation);
            System.out.println(versionId);
            up.insertVersionFileTable(versionId);
        }
        else
        {
            System.out.println("No file found");
        }
    }
    public static boolean checkFileName(int userId,String fileName) throws Exception
    {
        con=new connectionDatabase();
        PreparedStatement pstmt=con.getConnect().prepareStatement("select * from userFile where userId=? and fileName=?");
        pstmt.setInt(1,userId);
        pstmt.setString(2,fileName);
        ResultSet rs=pstmt.executeQuery();
        if(rs.next())
        {
            return true;
        }
        else
        {
            return false;
        }
    }



}
