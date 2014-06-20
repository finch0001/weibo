package com.lm.weibo.android.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

public class ImageUtility {
	public static String compressPic(Context context, String picPath) {

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inSampleSize = 2;

		Bitmap bitmap = BitmapFactory.decodeFile(picPath, options);
		int exifRotation = ImageUtility.getFileExifRotation(picPath);
		if (exifRotation != 0) {
			// TODO write EXIF instead of rotating bitmap.
			Matrix mtx = new Matrix();
			mtx.postRotate(exifRotation);
			Bitmap adjustedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), mtx, true);
			if (adjustedBitmap != bitmap) {
				bitmap.recycle();
				bitmap = adjustedBitmap;
			}
		}
		FileOutputStream stream = null;
		String tmp = FileManager.getUploadPicTempFile(context);
		try {
			new File(tmp).getParentFile().mkdirs();
			new File(tmp).createNewFile();
			stream = new FileOutputStream(new File(tmp));
		} catch (IOException ignored) {

		}
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

		if (stream != null) {
			try {
				stream.close();
				bitmap.recycle();
			} catch (IOException ignored) {

			}
		}
		return tmp;
	}
	
	public static int getFileExifRotation(String filePath) {
        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
            		ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
                default:
                    return 0;
            }
        } catch (IOException e) {
            return 0;
        }
    }
}
