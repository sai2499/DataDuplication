package Project;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import com.mysql.cj.x.protobuf.MysqlxPrepare;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class downloadFile extends mainFile
{
    public static connectionDatabase con=null;
    public static Scanner sc=null;
    public static void download(int UserId) throws Exception
    {
        System.out.println("---------------------------Download File---------------------------");
        System.out.println("Files you have: ");
        String fileName=showFiles(UserId);
        int fileId=retrieveFileId(UserId,fileName);
        Integer[] shaId=retrieveShaId(fileId);
        String[] sha256=retrieveSha(shaId);
        File_opr.createOriginal(sha256);
    }
    public static String showFiles(int UserId) throws Exception
    {
        sc=new Scanner(System.in);
        con=new connectionDatabase();
        PreparedStatement pstmt=con.getConnect().prepareStatement("select fileName from userFile where userId=?");
        pstmt.setInt(1,UserId);
        ResultSet rs=pstmt.executeQuery();
        while(rs.next())
        {
            System.out.println(rs.getString(1));
        }
        System.out.println("Enter the name of the file: ");
        String fileName=sc.next();
        return fileName;
    }
    public static Integer[] retrieveShaId(int fileId) throws Exception
    {
        con=new connectionDatabase();
        ArrayList<Integer> arr=new ArrayList<Integer>();
        PreparedStatement pstmt=con.getConnect().prepareStatement("select shaId from fileDetails where userFileId=?");
        pstmt.setInt(1,fileId);
        ResultSet rs=pstmt.executeQuery();
        while(rs.next())
        {
            arr.add(rs.getInt(1));
        }
        Integer[] shaArr= new Integer[arr.size()];
        shaArr=arr.toArray(shaArr);
        return shaArr;
    }
    public static int retrieveFileId(int userId,String fileName) throws Exception
    {
        int userFileId=0;
        con=new connectionDatabase();
        PreparedStatement pstmt=con.getConnect().prepareStatement("select userFileId from userFile where userId=? and fileName=?");
        pstmt.setInt(1,userId);
        pstmt.setString(2,fileName);
        ResultSet rs=pstmt.executeQuery();
        while(rs.next())
        {
            userFileId=rs.getInt(1);
        }
        return userFileId;
    }
    public static String[] retrieveSha(Integer[] shaid) throws Exception
    {
        ArrayList<String> arrSha=new ArrayList<String>();
        con=new connectionDatabase();
        PreparedStatement pstmt=con.getConnect().prepareStatement("select sha256 from hashtable where shaid=?");
        for(int i=0;i<shaid.length;i++)
        {
            pstmt.setInt(1,shaid[i]);
            ResultSet rs=pstmt.executeQuery();
            while(rs.next())
            {
                arrSha.add(rs.getString(1));
            }
        }
        String[] sha256arr=new String[arrSha.size()];
        sha256arr=arrSha.toArray(sha256arr);
        return sha256arr;
    }
}
