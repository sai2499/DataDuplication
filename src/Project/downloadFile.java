package Project;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import com.mysql.cj.x.protobuf.MysqlxPrepare;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class downloadFile
{
    public static connectionDatabase con=null;
    public static Scanner sc=null;
    public static RetrieveIDs rid=null;
    public static void download(int UserId) throws Exception
    {
        rid=new RetrieveIDs();
        System.out.println("---------------------------Download File---------------------------");
        System.out.println("Files you have: ");
        int file_Id=rid.showFiles(UserId);
        String fileName=rid.showFilesName(file_Id);
        int fileId=rid.retrieveFileId(UserId,fileName);
        Integer[] shaId=rid.retrieveShaId(fileId);
        String[] sha256=rid.retrieveSha(shaId);
        File_opr.createOriginal(sha256,fileName);
    }
}
