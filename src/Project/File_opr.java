package Project;

import java.io.FileWriter;
import java.io.IOException;

public class File_opr {
	
	public static void createFile(int hash_val,String word, String hash256val) throws IOException
	{
		String filename = "chunks/"+hash256val;
		FileWriter fw = new FileWriter(filename);
		fw.write(word);
		fw.close();
	}
	
}
