package com.lm.weibo.android.net.utilities;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.lm.weibo.android.net.AppException;
import com.lm.weibo.android.net.AppException.EnumException;

public class IOUtilities {
	
	public static String readFromFile(String path) {
		ByteArrayOutputStream out = null;
		FileInputStream in = null;
		try {
			File f = new File(path);
			in = new FileInputStream(f);
			out = new ByteArrayOutputStream(1024);
			byte[] b = new byte[1000];
			int n;
			while ((n = in.read(b)) != -1) {
				out.write(b, 0, n);
			}
			out.flush();
			return new String(out.toByteArray());
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static String readStreamToMemory(InputStream in) throws AppException {
		String result = "";
		String inputLine = null;
		try {
			InputStreamReader reader = new InputStreamReader(in);
			BufferedReader buffer = new BufferedReader(reader);
			while ((inputLine = buffer.readLine()) != null) {
				result += inputLine + "\n";
			}
			reader.close();
			return result;
		} catch (IOException e) {
			throw new AppException(EnumException.IOException, e.getMessage());
		}
	}
}
