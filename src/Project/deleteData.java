package Project;

import java.util.Scanner;

public class deleteData
{
    public static connectionDatabase con=null;
    public static Scanner sc=null;
    public static RetrieveIDs rid=null;
    
    public static void deleteFile(int UserId) throws Exception
    {
        System.out.println("---------------------------Download File---------------------------");
        System.out.println("Files you have: ");
        String fileName=rid.showFiles(UserId);
        int fileId=rid.retrieveFileId(UserId,fileName);
        Integer[] shaId=rid.retrieveShaId(fileId);
        String[] sha256=rid.retrieveSha(shaId);
    }
    public static void deleteUser(int userId) throws Exception
    {

    }
}
