package com.hans.alpha.utils.io;

import com.hans.alpha.constant.MotherShipConst;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


public class DefaultFileCopyProcessor implements FileUtil.FileCopyProcessor {

	/**
	 * copyFileToFileProcess
	 * @param from,maybe directory
	 * @param to,maybe directory
	 * @param isFile,maybe directory or file
	 * @return boolean,if true keep going copy,only active in directory so far
	 */
	public boolean copyFileToFileProcess(final String from, final String to, final boolean isFile){
		try{
			if(isFile){
				String fromFile=new File(from).getAbsolutePath();
				String toFile=new File(to).getAbsolutePath();
				if (fromFile.equals(toFile)) {
					toFile = toFile+"_copy";
				}
				FileUtil.createFile(toFile);
				InputStream inputStream = new FileInputStream(fromFile);
				OutputStream outputStream = new FileOutputStream(toFile);
				try{
					byte[] buffer = new byte[MotherShipConst.Capacity.BYTES_PER_KB];
					int length=-1;
					while((length=inputStream.read(buffer,0,buffer.length))!=-1){
						outputStream.write(buffer,0,length);
						outputStream.flush();
					}
				}finally{
					if(inputStream!=null){
						inputStream.close();
					}
					if(outputStream!=null){
						outputStream.close();
					}
				}
			}else{
				FileUtil.createDirectory(to);
			}
		}catch (Exception e) {
			throw new FileCopyException(e);
		}
		return true;
	}
}
