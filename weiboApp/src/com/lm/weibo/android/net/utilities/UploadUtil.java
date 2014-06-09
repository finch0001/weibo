package com.lm.weibo.android.net.utilities;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import com.lm.weibo.android.net.AppException;
import com.lm.weibo.android.net.AppException.EnumException;
import com.lm.weibo.android.net.entities.FileEntity;

public class UploadUtil {
	public static void upload(OutputStream out, String filePath)
			throws AppException {
		String BOUNDARY = "7d4a6d158c9";
		DataOutputStream outStream = new DataOutputStream(out);
		try {
			outStream.writeBytes("--" + BOUNDARY + "\r\n");
			outStream
					.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\"; filename=\""
							+ filePath.substring(filePath.lastIndexOf("/") + 1)
							+ "\"" + "\r\n");
			outStream.writeBytes("\r\n");
			byte[] buffer = new byte[1024];
			FileInputStream fis = new FileInputStream(filePath);
			while (fis.read(buffer, 0, 1024) != -1) {
				outStream.write(buffer, 0, buffer.length);
			}
			fis.close();
			outStream.write("\r\n".getBytes());
			byte[] end_data = ("--" + BOUNDARY + "--\r\n").getBytes();
			outStream.write(end_data);
			outStream.flush();
		} catch (FileNotFoundException e) {
			throw new AppException(EnumException.FileException, e.getMessage());
		} catch (IOException e) {
			throw new AppException(EnumException.IOException, e.getMessage());
		}
	}

	public static void upload(OutputStream out, String postContent, ArrayList<FileEntity> entities) throws AppException {
		String BOUNDARY = "7d4a6d158c9";
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
}
