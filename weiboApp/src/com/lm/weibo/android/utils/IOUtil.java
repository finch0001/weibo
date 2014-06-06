package com.lm.weibo.android.utils;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class IOUtil {
	/**
	 * 将字符串内容写入指定的文件中
	 * 
	 * @param data 要写入的字符串
	 * @param path 要保存的路径
	 * @throws Exception
	 */
	public static void writeToFile(String data, String path) throws Exception {
		FileOutputStream fos = new FileOutputStream(path);
		InputStream in = new ByteArrayInputStream(data.getBytes());
		byte[] b = new byte[4 * 1024];
		int read;
		while ((read = in.read(b)) != -1) {
			fos.write(b, 0, read);
		}
		fos.flush();
		fos.close();
		in.close();
	}
}
