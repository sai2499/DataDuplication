package Project;

import java.sql.*;
import java.util.*;

public class RetrieveIDs
{
    public static connectionDatabase con=null;
    public static Scanner sc=null;
    public static Map<String,Integer> countmap=new HashMap<>();
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
    public static Integer[] retrieveAllFileId(int userId) throws Exception
    {
        con=new connectionDatabase();
        ArrayList<Integer> allFileIdArr=new ArrayList<>();
        PreparedStatement pstmt=con.getConnect().prepareStatement("select userFileId from userFile where userId=?");
        pstmt.setInt(1,userId);
        ResultSet rs=pstmt.executeQuery();
        while(rs.next())
        {
            allFileIdArr.add(rs.getInt(1));
        }
        Integer[] allFileId=new Integer[allFileIdArr.size()];
        allFileId=allFileIdArr.toArray(allFileId);
        return allFileId;
    }
    public static String[] retrieveAllShaValue(int userId) throws Exception
    {
        con=new connectionDatabase();
        Integer[] AllFileId=retrieveAllFileId(userId);
        ArrayList<String> allShaValuesArr=new ArrayList<>();
        PreparedStatement pstmt=con.getConnect().prepareStatement("select sha256 from hashTable where userFileId=?");
        for(int i=0;i<AllFileId.length;i++)
        {
            pstmt.setInt(1,AllFileId[i]);
            ResultSet rs=pstmt.executeQuery();
            while(rs.next())
            {
                allShaValuesArr.add(rs.getString(1));
            }
        }
        String[] allShaValues=new String[allShaValuesArr.size()];
        allShaValues=allShaValuesArr.toArray(allShaValues);
        return allShaValues;
    }
}

