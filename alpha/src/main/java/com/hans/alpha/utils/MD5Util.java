package com.hans.alpha.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MS5
 */
public class MD5Util {

    private static final String TAG = "MotherShip.MD5Util";
    private static final int    STREAM_BUFFER_LENGTH = 1024;

    public static MessageDigest getDigest(final String algorithm) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance(algorithm);
    }

    public static byte[] md5(String txt) {
        return md5(txt.getBytes());
    }

    public static byte[] md5(byte[] bytes) {
        try {
            MessageDigest digest = getDigest("MD5");
            digest.update(bytes);
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] md5(InputStream is) throws NoSuchAlgorithmException, IOException {
        return updateDigest(getDigest("MD5"), is).digest();
    }

    public static MessageDigest updateDigest(final MessageDigest digest, final InputStream data) throws IOException {
        final byte[] buffer = new byte[STREAM_BUFFER_LENGTH];
        int read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);

        while (read > -1) {
            digest.update(buffer, 0, read);
            read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);
        }

        return digest;
    }

    public static String getFileMD5String(String filePath)
    {
        return byteArrayToHexString(getFileMD5(filePath));
    }

    public static byte[] getFileMD5(String filePath)
    {
        FileInputStream fileInputStream = null;

        try
        {
            fileInputStream = new FileInputStream(filePath);
            MessageDigest digester = MessageDigest.getInstance("MD5");
            byte[] bytes = new byte[8192];
            int byteCount;
            while ((byteCount = fileInputStream.read(bytes)) > 0)
            {
                digester.update(bytes, 0, byteCount);
            }

            return digester.digest();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (fileInputStream != null) fileInputStream.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static String byteArrayToHexString(byte[] arrayIn)
    {
        if(arrayIn==null)
        {
            return null;
        }

        StringBuilder builder=new StringBuilder(arrayIn.length*2);

        for(byte oneByte:arrayIn)
        {
            builder.append(String.format("%02X", oneByte));
        }

        return builder.toString();
    }
}
