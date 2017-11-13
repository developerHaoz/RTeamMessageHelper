package com.hans.alpha.utils.io;

import com.hans.alpha.android.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Serialize Utils
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2012-5-14
 */
public class SerializeUtil {

    public static final String TAG = "MotherShip.SerializeUtil";
    private SerializeUtil() {
        throw new AssertionError();
    }

    /**
     * Deserialization object from file.
     * 
     * @param filePath file path
     * @return de-serialized object
     * @throws RuntimeException if an error occurs
     */
    public static Object deserialization(String filePath) {
        Log.d(TAG,"[deserialization] filePath:%s",filePath);
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(filePath));
            Object o = in.readObject();
            in.close();
            return o;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("ClassNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            IOUtil.close(in);
        }
    }

    /**
     * Serialize object to file.
     * 
     * @param filePath file path
     * @param obj object
     * @throws RuntimeException if an error occurs
     */
    public static void serialization(String filePath, Object obj) {
        Log.d(TAG,"[serialization] filePath:%s",filePath);
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(filePath));
            out.writeObject(obj);
            out.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            IOUtil.close(out);
        }
    }

}
