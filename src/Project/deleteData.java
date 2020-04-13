package Project;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class deleteData
{
    public static connectionDatabase con=null;
    public static Scanner sc=null;
    public static RetrieveIDs rid=null;
    public static Map<String,Integer> mapCount = new HashMap<>();

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
        PreparedStatement pstmt = con.getConnect().prepareStatement("select * from shaTable");
        ResultSet rs = pstmt.executeQuery();
        int count = 0;
        while (rs.next()) {
            mapCount.put(rs.getString(1), rs.getInt(2));
        }
        for (int i = 0; i < sha256.length; i++) {
            if (mapCount.containsKey(sha256[i])) {
                count = mapCount.get(sha256[i]);
                if (count <= 1) {
                    deleteShaTable.setString(1, sha256[i]);
                    deleteShaTable.addBatch();
                } else {
                    count--;
                    updateCount.setInt(1, count);
                    updateCount.setString(2, sha256[i]);
                    updateCount.addBatch();
                }
            } else {
                System.out.print("nothing found");
            }
        }
        deleteHashTable.setInt(1,fileId);
        deleteHashTable.addBatch();
        int[] deleteShaTablexe=deleteShaTable.executeBatch();
        int[] updatecountexe=updateCount.executeBatch();
        int[] deleteFiledetailsTablexe=deleteFiledetailsTable.executeBatch();
        int[] deleteUserFileTablexe=deleteUserFileTable.executeBatch();
        int[] deleteHashTablexe=deleteHashTable.executeBatch();
    }
    public static void deleteUser(int userId) throws Exception
    {

    }
}
