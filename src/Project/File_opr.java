package Project;

import java.io.*;
import java.util.LinkedHashSet;
import java.util.Set;

public class File_opr
{
	public static void createFile(int hash_val,String word, String hash256val) throws IOException
	{
		String filename = "chunks/"+hash256val;
		FileWriter fw = new FileWriter(filename);
		fw.write(word);
		fw.close();
	}
	public static void createOriginal(String[] sha256,String fileName) throws Exception
	{
		for(int i=0;i<sha256.length;i++)
		{

		}
	}

}
