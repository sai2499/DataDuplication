package Project;

import java.io.*;

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
		String str;
		String str1=" ";
		for(int i=0;i<sha256.length;i++)
		{
			String chuckName="chunks/"+sha256[i];
			 File file=new File(chuckName);
			 BufferedReader br=new BufferedReader(new FileReader(file));
			 while((str=br.readLine())!=null)
			 {
			 	str1=str1+str;
			 }
		}
		String finalName="download/"+fileName;
		FileWriter fw=new FileWriter(finalName);
		fw.write(str1);
		fw.close();
	}

}
