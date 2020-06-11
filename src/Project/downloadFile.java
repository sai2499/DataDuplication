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
        String fileNameTry=rid.showFilesName(file_Id);
        Integer[] shaId=rid.retrieveShaId(file_Id);
        String[] sha256=rid.retrieveSha(shaId);
        String[] changeName=fileNameTry.split("[_.]");
        int len=changeName.length-1;
        String fileName = changeName[0]+"(v"+rid.getVersionNo(file_Id)+")"+"."+changeName[len];
        File_opr.createOriginal(sha256,fileName);
    }
}
