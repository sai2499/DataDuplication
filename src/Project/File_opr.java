package Project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class File_opr
{
	public static void createFile(int hash_val,String word, String hash256val) throws IOException
	{
		String filename = "chunks/"+hash256val;
		FileWriter fw = new FileWriter(filename);
		fw.write(word);
		fw.close();
	}
	public static void createOriginal(String[] sha256)
	{
		for(int i=0;i<sha256.length;i++)
		{
			String filename="chucks/"+sha256[i];
			File file=new File(filename);
			//BufferedReader bfr
		}

	}

}
