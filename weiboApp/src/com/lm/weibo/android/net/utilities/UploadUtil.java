package com.lm.weibo.android.net.utilities;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import android.util.Log;

import com.lm.weibo.android.net.AppException;
import com.lm.weibo.android.net.AppException.EnumException;
import com.lm.weibo.android.net.entities.FileEntity;

public class UploadUtil {
	public static void upload(OutputStream out, String filePath, byte[] barry, int totalSent)
			throws AppException {
		BufferedOutputStream outStream = null;
		int bytesRead;
        int bytesAvailable;
        int bufferSize;
        byte[] buffer;
        FileInputStream fis = null;
        int maxBufferSize = 1 * 1024;
		try {
			outStream = new BufferedOutputStream(out);
			fis = new FileInputStream(new File(filePath));
			bytesAvailable = fis.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];
			bytesRead = fis.read(buffer, 0, bufferSize);
            long transferred = 0;
            final Thread thread = Thread.currentThread();
            while (bytesRead > 0) {
                if (thread.isInterrupted()) {
                    (new File(filePath)).delete();
                    throw new InterruptedIOException();
                }
                outStream.write(buffer, 0, bufferSize);
                bytesAvailable = fis.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fis.read(buffer, 0, bufferSize);
                transferred += bytesRead;
                if (transferred % 50 == 0)
                	outStream.flush();
            }
            outStream.write(buffer);
            outStream.write(barry);
            totalSent += barry.length;
            outStream.write(barry);
            totalSent += barry.length;
            out.flush();
            
			/*
			while (fis.read(buffer, 0, 1024) != -1) {
				outStream.write(buffer, 0, buffer.length);
			}
			fis.close();
			outStream.write("\r\n".getBytes());
			byte[] end_data = ("--" + BOUNDARY + "--\r\n").getBytes();
			outStream.write(end_data);
			outStream.flush();
			*/
		} catch (FileNotFoundException e) {
			throw new AppException(EnumException.FileException, e.getMessage());
		} catch (IOException e) {
			throw new AppException(EnumException.IOException, e.getMessage());
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void upload(OutputStream out, String postContent, ArrayList<FileEntity> entities) throws AppException {
		String BOUNDARY = getBoundry();
		String PREFIX = "--", LINEND = "\r\n";
		String CHARSET = "UTF-8";
		DataOutputStream outStream = new DataOutputStream(out);
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\"" + "data" + "\"" + LINEND);
			sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
			sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
			sb.append(LINEND);
			sb.append(postContent);
			sb.append(LINEND);
			outStream.write(sb.toString().getBytes());
			int i = 0;
			for (FileEntity entity : entities) {
				StringBuilder sb1 = new StringBuilder();
				sb1.append(PREFIX);
				sb1.append(BOUNDARY);
				sb1.append(LINEND);
				sb1.append("Content-Disposition: form-data; name=\"file" + (i++) + "\"; filename=\"" + entity.getFileName() + "\"" + LINEND);
				sb1.append("Content-Type: " + entity.getFileType() + LINEND);
				sb1.append(LINEND);
				outStream.write(sb1.toString().getBytes());
				
				InputStream is = new FileInputStream(entity.getFilePath());
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}
				is.close();
				outStream.write(LINEND.getBytes());
			}
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
			outStream.write(end_data);
			outStream.flush();
		} catch (FileNotFoundException e) {
			throw new AppException(EnumException.FileException, e.getMessage());
		} catch (IOException e) {
			throw new AppException(EnumException.IOException, e.getMessage());
		}
	}
	
	public static String getBoundry() {
        StringBuffer _sb = new StringBuffer();
        for (int t = 1; t < 12; t++) {
            long time = System.currentTimeMillis() + t;
            if (time % 3 == 0) {
                _sb.append((char) time % 9);
            } else if (time % 3 == 1) {
                _sb.append((char) (65 + time % 26));
            } else {
                _sb.append((char) (97 + time % 26));
            }
        }
        return _sb.toString();
    }
	
	public static String getBoundaryMessage(String boundary, Map params, String fileField, String fileName, String fileType) {
        StringBuffer res = new StringBuffer("--").append(boundary).append("\r\n");

        Iterator keys = params.keySet().iterator();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            String value = (String) params.get(key);
            res.append("Content-Disposition: form-data; name=\"")
                    .append(key).append("\"\r\n").append("\r\n")
                    .append(value).append("\r\n").append("--")
                    .append(boundary).append("\r\n");
        }
        res.append("Content-Disposition: form-data; name=\"").append(fileField)
                .append("\"; filename=\"").append(fileName)
                .append("\"\r\n").append("Content-Type: ")
                .append(fileType).append("\r\n\r\n");

        return res.toString();
    }
}
