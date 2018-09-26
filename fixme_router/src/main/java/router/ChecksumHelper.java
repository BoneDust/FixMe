package router;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

public class ChecksumHelper
{
    public static String generateChecksum(String message)
    {
        String result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] md5Hash = digest.digest(message.getBytes("UTF-8"));
            result = DatatypeConverter.printHexBinary(md5Hash);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return result;
    }

    public static boolean isValidChecksum(String message)
    {
        String realMessage = message.split("Checksum=")[0];
        String checksum = message.split("Checksum=")[1];
        checksum = checksum.substring(0, checksum.length() - 1);
        if (generateChecksum(realMessage).equals(checksum))
            return (true);
        else
            return (false);
    }
}
