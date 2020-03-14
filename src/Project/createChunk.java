package Project;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class createChunk extends uploadFile
{

		
		public final int hconst = 69069; // good hash multiplier for MOD 2^32
		public int mult; // this will hold the p^n value
		int[] buffer; // circular buffer - reading from file stream
		int buffptr;
		StringBuilder string_w = new StringBuilder();
		InputStream is;
		
		
		public void createChunks() throws IOException 
		{
			int mask = 1 << 13;
			mult = 1;
			buffptr = 0;
			mask--; // 13 bit of '1's
			File f = new File(fileLocation);
			FileInputStream fs;
			try 
			{
				fs = new FileInputStream(f);
				BufferedInputStream bis = new BufferedInputStream(fs);	// BufferedInputStream is faster to read byte-by-byte from
				this.is = bis;
				long length = bis.available();
				System.out.println("**Size of file:**"+length);
				long curr = length;
				int counter = 0;
				//get the initial 1k hash window //
				Integer hash = inithash(1024-1,0);
				curr -= bis.available();
				System.out.println("CURRENT SIZE OF CURR : " + curr);
				while (curr < length) 
				{
					if ((hash & mask) == 0 || curr==length-1)
					{
						// window found - hash it, 
						// compare it to the other file chunk list
						byte[] wordArray = string_w.toString().getBytes(); 
						String hashIn256=sha256hash.getHash256(wordArray);

						array_of_file_sha.add(hashIn256);
						if(!map.containsKey(hash))
						{
							ArrayList<String> arrr = new ArrayList<String>(Arrays.asList(hashIn256));
							map.put(hash, arrr);
//							String ars = String.join(",", arrr);
							Insertquery.append("(" + hash + " , '" + hashIn256 + "' ),");
//							insertDb.insertInHashTable(hash, ars);
//							System.out.println(counter++ +"=> NO hash\tNO 256\t" +"  =>  "+hash + "\tsha256\t"+hashIn256);
							File_opr.createFile(hash,string_w.toString(),hashIn256);
//							System.out.println(arrr);
						}
						else if(!map.get(hash).contains(hashIn256))
						{
							map.get(hash).add(hashIn256);
							ArrayList<String> arrr = (ArrayList<String>) map.get(hash);
							String ars = String.join(",", arrr);
							Insertquery.append("(" + hash + "," + hashIn256 + "),");
//							insertDb.updateInHashTable(hash, ars);
//							System.out.println(counter++ +"=> YES hash\tNO 256\t" +"  =>  "+hash + "\tsha256\t"+hashIn256);
							File_opr.createFile(hash,string_w.toString(),hashIn256);
						}
						else
						{
							System.out.println(counter++ +"=> YES hash\tYES 256\t" +"  =>  "+hash + "\tsha256\t"+hashIn256);
						}

						
						if(!map4count.containsKey(hashIn256))
						{
							map4count.put(hashIn256, 1);
//							insertDb.insertInShaCount(hashIn256, 1);
						}else
						{
							int count = map4count.get(hashIn256);
							map4count.replace(hashIn256, count, count+1);
//							insertDb.updateInShaCount(hashIn256, count+1);

						}
						
						
						
						string_w.setLength(0);;
					}
					// next window's hash  //
					hash = nexthash(hash);
					curr++;
				}
				
				String string_of_file_sha = String.join(",",array_of_file_sha);
//				insertDb.insertInFileDetails(fileName, string_of_file_sha);
				array_of_file_sha.clear();
			
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	
		public int nexthash(int prevhash) throws IOException
		{
				int c = is.read(); // next byte from stream
				string_w.append((char) c);
				prevhash -= mult * buffer[buffptr]; // remove the last value
				prevhash *= hconst; // multiply the whole chain with prime
				prevhash += c; // add the new value
				buffer[buffptr] = c; // circular buffer, 1st pos == lastpos
				buffptr++;
				buffptr = buffptr % buffer.length;
				return prevhash;
		}
		public int inithash(int from, int to) throws IOException 
		{
			buffer = new int[from - to + 1]; // create circular buffer
			int hash = 0;
			string_w.setLength(0);;
			// calculate the hash sum of p^n * a[x]
			for (int i = 0; i <= from - to; i++)
			{
				int c = is.read();
				string_w.append((char) c);
				buffer[buffptr] = c; 
				buffptr++;
				buffptr = buffptr % buffer.length;
				hash *= hconst; // multiply the current hash with constant
				hash += c; // add byte to hash
				if(i>0) // calculate the large p^n value for later usage
				{
					mult *= hconst;
				}
			}
			return hash ;
		}
}