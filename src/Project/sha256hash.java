package Project;

import java.security.MessageDigest;

public class sha256hash
{
	public static String getHash256(byte[] inputBytes)
	{
		String hash256calc = new String();
		StringBuffer hexDigest = new StringBuffer();
		try
		{
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(inputBytes);
			byte[] digestedBytes = messageDigest.digest();
			for(int i=0; i<digestedBytes.length;i++)
			{
				hexDigest.append(Integer.toString((digestedBytes[i]&0xff)+0x100,16).substring(1));
			}
			hash256calc = hexDigest.toString();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return hash256calc;
	}

}