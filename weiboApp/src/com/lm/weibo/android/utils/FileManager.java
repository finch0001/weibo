package com.lm.weibo.android.utils;

import java.io.File;

import android.content.Context;
import android.os.Environment;

public class FileManager {
	public static boolean isExternalStorageMounted() {
		boolean canRead = Environment.getExternalStorageDirectory().canRead();
		boolean onlyRead = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED_READ_ONLY);
		boolean unMounted = Environment.getExternalStorageState().equals(
				Environment.MEDIA_UNMOUNTED);

		return !(!canRead || onlyRead || unMounted);
	}

	public static String getUploadPicTempFile(Context context) {
		if (!isExternalStorageMounted()) {
			return "";
		} else {
			return getSdCardPath(context) + File.separator + "upload.jpg";
		}
	}

	private static String getSdCardPath(final Context context) {
		if (isExternalStorageMounted()) {
			File path = context.getExternalCacheDir();
			if (path != null) {
				return path.getAbsolutePath();
			}
		} else {
			return "";
		}
		return "";
	}
}
