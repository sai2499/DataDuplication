package Project;

import java.io.*;
import java.sql.*;
import java.util.*;

public class createChunk
{

		public connectionDatabase con = null;
		public final int hconst = 69069; // good hash multiplier for MOD 2^32
		public int mult; // this will hold the p^n value
		int[] buffer; // circular buffer - reading from file stream
		int buffptr;
		public Map<Integer, ArrayList<String>> map = new HashMap<Integer, ArrayList<String>>();
		public ArrayList<String> array_of_file_sha = new ArrayList<>();
		Map<String,Integer> map4Count = new HashMap<>();
		StringBuilder string_w = new StringBuilder();
		InputStream is;
		
		
		public void createChunks(int fileId,String fileLocation) throws Exception
		{
			int mask = 1 << 13;
			mult = 1;
			buffptr = 0;
			mask--; // 13 bit of '1's
			File f = new File(fileLocation);
			FileInputStream fs;
			con = new connectionDatabase();
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
				PreparedStatement insertQuery = con.getConnect().prepareStatement("Insert into Hashtable (userFileId,rollHash,sha256) values (?,?,?)");
				while (curr < length) 
				{
					if ((hash & mask) == 0 || curr==length-1)
					{
						// window found - hash it, 
						// compare it to the other file chunk list
						byte[] wordArray = string_w.toString().getBytes(); 
						String hashIn256 = sha256hash.getHash256(wordArray);

						array_of_file_sha.add(hashIn256);
						if(!map.containsKey(hash))
						{
							ArrayList<String> arrr = new ArrayList<String>(Arrays.asList(hashIn256));
							map.put(hash, arrr);
							insertQuery.setInt(1,fileId);
							insertQuery.setInt(2,hash);
							insertQuery.setString(3,hashIn256);
							insertQuery.addBatch();
							File_opr.createFile(hash,string_w.toString(),hashIn256);
						}
						else if(!map.get(hash).contains(hashIn256))
						{
							map.get(hash).add(hashIn256);
							ArrayList<String> arrr = (ArrayList<String>) map.get(hash);
							String ars = String.join(",", arrr);
							insertQuery.setInt(1,fileId);
							insertQuery.setInt(2,hash);
							insertQuery.setString(3,hashIn256);
							insertQuery.addBatch();
							File_opr.createFile(hash,string_w.toString(),hashIn256);
						}
						else
						{
							System.out.println(counter++ +"=> YES hash\tYES 256\t" +"  =>  "+hash + "\tsha256\t"+hashIn256);
						}

						string_w.setLength(0);;
					}

					hash = nexthash(hash);
					curr++;
				}
				
				String string_of_file_sha = String.join(",",array_of_file_sha);
				//INSERTING VALUE IN HASHTABLE
				int[] executeRecords = insertQuery.executeBatch();
				PreparedStatement InsertCount = con.getConnect().prepareStatement("Insert into shaTable values (? , ?)");
				PreparedStatement updateCount = con.getConnect().prepareStatement("update shaTable set shacount = ? where sha256Value = ?");
				getShaCount();
				//INSERTING COUNTS
				for(int i = 0 ; i < array_of_file_sha.size() ; i++)
				{
					boolean res = isAvailSha(array_of_file_sha.get(i));
					if(res)
					{
//						UPDATE COUNT
						int count = map4Count.get(array_of_file_sha.get(i));
						updateCount.setInt(1 , ++count);
						updateCount.setString(2 , array_of_file_sha.get(i));
						updateCount.addBatch();
					}
					else
					{
						InsertCount.setString(1,array_of_file_sha.get(i));
						InsertCount.setInt(2,1);
						InsertCount.addBatch();
					}
				}
				System.out.println(array_of_file_sha);
				int[] insertExec = InsertCount.executeBatch();
				int[] updateExec = updateCount.executeBatch();

				array_of_file_sha.clear();
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

		public void getShaCount() throws Exception
		{
			con = new connectionDatabase();
			PreparedStatement pstmt = con.getConnect().prepareStatement("select * from shaTable");
			ResultSet rs = pstmt.executeQuery();
			if(rs != null)
			{
				while(rs.next())
				{
					System.out.println(rs.getString(1));
					map4Count.put(rs.getString(1) , rs.getInt(2));
				}
			}
			else
			{
				System.out.println("SHA TABLE EMPTY");
			}
		}

		public boolean isAvailSha(String hashIn256)
		{
			if(map4Count.containsKey(hashIn256))
			{
				return true;
			}
			else
			{
				return false;
			}
		}

}