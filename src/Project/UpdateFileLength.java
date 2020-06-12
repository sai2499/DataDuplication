package Project;

import java.io.File;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateFileLength
{
    public static connectionDatabase con;

    public static void LengthOfOriginalFile(int fileId, String FileName) throws Exception
    {
        con=new connectionDatabase();
        String locationFile="File/"+FileName;
        File fin= new File(locationFile);
        long filelen=0;
        if(fin.exists())
        {
            filelen=fin.length()/1024;
        }
        PreparedStatement pstmt=con.getConnect().prepareStatement("update userFile set fileSize=? where userFileId=?");
        pstmt.setLong(1,filelen+1);
        pstmt.setInt(2,fileId);
        pstmt.executeUpdate();
    }
    public static void LengthOfChunkFile(String[] sha256list) throws Exception
    {
        con=new connectionDatabase();
        PreparedStatement pstmt=con.getConnect().prepareStatement("update hashTable set chunkSize=? where sha256=?");
        String fileLocation="";
        long filelen=0;
        for(int i=0;i<sha256list.length;i++)
        {
            fileLocation="chunks/"+sha256list[i];
            File fin=new File(fileLocation);
            if(fin.exists())
            {
                filelen=fin.length()/1024;
                pstmt.setLong(1,filelen+1);
                pstmt.setString(2,sha256list[i]);
                pstmt.addBatch();
            }
            else
            {
                System.out.println("File not found");
                break;
            }
        }
        int[] arr=pstmt.executeBatch();
    }
}
