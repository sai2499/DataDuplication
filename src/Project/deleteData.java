package Project;
import java.sql.*;
import java.util.*;

public class deleteData
{
    public static connectionDatabase con=null;
    public static Scanner sc=null;
    public static RetrieveIDs rid=null;
    public static Map<String,Integer> mapCountFile = new HashMap<>();
    public static Map<String,Integer> mapCountUser= new HashMap<>();
    public static void deleteFile(int UserId) throws Exception
    {
        System.out.println("---------------------------Delete File---------------------------");
        System.out.println("Files you have: ");
        String fileName = rid.showFiles(UserId);
        int fileId = rid.retrieveFileId(UserId, fileName);
        Integer[] shaId = rid.retrieveShaId(fileId);
        String[] sha256 = rid.retrieveSha(shaId);
        con = new connectionDatabase();
        PreparedStatement deleteHashTable=con.getConnect().prepareStatement("delete from hashTable where userFileId=?");
        PreparedStatement deleteUserFileTable = con.getConnect().prepareStatement("delete from userfile where userFileId=? ");
        deleteUserFileTable.setInt(1, fileId);
        deleteUserFileTable.addBatch();
        PreparedStatement deleteShaTable = con.getConnect().prepareStatement("delete from shaTable where sha256Value=?");
        PreparedStatement updateCount = con.getConnect().prepareStatement("update shaTable set shacount=? where sha256Value=?");
        PreparedStatement deleteFiledetailsTable = con.getConnect().prepareStatement("delete from fileDetails where userFileId=?");
        deleteFiledetailsTable.setInt(1, fileId);
        deleteFiledetailsTable.addBatch();
        PreparedStatement pstmtShaCount = con.getConnect().prepareStatement("select * from shaTable");
        ResultSet rs = pstmtShaCount.executeQuery();
        int count = 0;
        while (rs.next()) {
            mapCountFile.put(rs.getString(1), rs.getInt(2));
        }
        for (int i = 0; i < sha256.length; i++)
        {
            if (mapCountFile.containsKey(sha256[i]))
            {
                count = mapCountFile.get(sha256[i]);
                if (count <= 1)
                {
                    deleteShaTable.setString(1, sha256[i]);
                    deleteShaTable.addBatch();
                }
                else
                {
                    count--;
                    updateCount.setInt(1, count);
                    updateCount.setString(2, sha256[i]);
                    updateCount.addBatch();
                }
            }
            else
            {
                System.out.print("nothing found");
            }
        }
        deleteHashTable.setInt(1,fileId);
        deleteHashTable.addBatch();
        int[] deleteShaTableExe=deleteShaTable.executeBatch();
        int[] updateCountExe=updateCount.executeBatch();
        int[] deleteFileDetailsTableExe=deleteFiledetailsTable.executeBatch();
        int[] deleteUserFileTableExe=deleteUserFileTable.executeBatch();
        int[] deleteHashTableExe=deleteHashTable.executeBatch();
    }
    public static void deleteUser(int userId) throws Exception
    {
        int count=0;
        con=new connectionDatabase();
        System.out.println("---------------------------Delete User---------------------------");
        rid=new RetrieveIDs();
        Integer[] allFileId=rid.retrieveAllFileId(userId);
        String[] allShaValue=rid.retrieveAllShaValue(userId);
        PreparedStatement deleteFromUserTable=con.getConnect().prepareStatement("delete from userTable where userId=?");
        PreparedStatement deleteFromUserFileTable=con.getConnect().prepareStatement("delete from userFile where userId=?");
        PreparedStatement deleteShaTable = con.getConnect().prepareStatement("delete from shaTable where sha256Value=?");
        PreparedStatement deleteFileDetailsTable=con.getConnect().prepareStatement("delete from fileDetails where userFileId=?");
        PreparedStatement deleteHashTable=con.getConnect().prepareStatement("delete from hashTable where userFileId=?");
        PreparedStatement updateCount = con.getConnect().prepareStatement("update shaTable set shacount=? where sha256Value=?");
        PreparedStatement retreiveShaValue=con.getConnect().prepareStatement("select * from shaTable");
        deleteFromUserTable.setInt(1,userId);
        deleteFromUserFileTable.setInt(1,userId);
        deleteFromUserTable.addBatch();
        deleteFromUserFileTable.addBatch();
        ResultSet rs=retreiveShaValue.executeQuery();
        while(rs.next())
        {
            mapCountUser.put(rs.getString(1),rs.getInt(2));
        }
        for(int i=0;i<allShaValue.length;i++)
        {
            if (mapCountFile.containsKey(allShaValue[i]))
            {
                count = mapCountFile.get(allShaValue[i]);
                if (count <= 1)
                {
                    deleteShaTable.setString(1, allShaValue[i]);
                    deleteShaTable.addBatch();
                }
                else
                {
                    count--;
                    updateCount.setInt(1, count);
                    updateCount.setString(2, allShaValue[i]);
                    updateCount.addBatch();
                }
            }
            else
            {
                System.out.print("nothing found");
            }
        }
        for(int i=0;i<allFileId.length;i++)
        {
            deleteHashTable.setInt(1,allFileId[i]);
            deleteFileDetailsTable.setInt(1,allFileId[i]);
            deleteHashTable.addBatch();
            deleteFileDetailsTable.addBatch();
        }
        int[] deleteShaTableExe=deleteShaTable.executeBatch();
        int[] updateCountExe=updateCount.executeBatch();
        int[] deleteFileDetailsTableExe=deleteFileDetailsTable.executeBatch();
        int[] deleteHashTableExe=deleteHashTable.executeBatch();
        int[] deleteFromUserFileTableExe=deleteFromUserFileTable.executeBatch();
        int[] deleteFromUserTableExe=deleteFromUserTable.executeBatch();
    }
}
