package com.lm.weibo.android.net;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.lm.weibo.android.R;
import com.lm.weibo.android.net.utilities.UploadUtil;
import com.lm.weibo.android.utils.AccessTokenKeeper;
import com.lm.weibo.android.utils.AppLogger;
import com.lm.weibo.android.utils.ErrorCode;
import com.lm.weibo.android.utils.Util;
import com.lm.weibo.android.utils.WeiboException;

public class SendWeiboWithPic {
    private static final int UPLOAD_CONNECT_TIMEOUT = 15 * 1000;
    private static final int UPLOAD_READ_TIMEOUT = 5 * 60 * 1000;
	
	private String pic;
	private Context context;
	
	public SendWeiboWithPic(Context context) {
		this.context = context;
	}
	
	public SendWeiboWithPic setPic(String pic) {
		this.pic = pic;
		return this;
	}
	
	public boolean sendNewMsgWithPic(Context context, String content) throws WeiboException {
		String url = Urls.url_upload;
        Map<String, String> map = new HashMap<String, String>();
        String access_token = AccessTokenKeeper.readAccessToken(context).getToken();

		map.put("access_token", access_token);
        map.put("status", content);

        return executeUploadTask(url, map, pic, "pic");
	}
	
	public boolean executeUploadTask(String url, Map<String, String> param, String path, String imageParamName) throws WeiboException {
        return !Thread.currentThread().isInterrupted() && doUploadFile(url, param, path, imageParamName);
    }
	
	public boolean doUploadFile(String urlStr, Map<String, String> param, String path, String imageParamName) throws WeiboException {
        String BOUNDARYSTR = UploadUtil.getBoundry();
        File targetFile = new File(path);
        byte[] barry = null;
        int contentLength = 0;
        String sendStr = "";
        try {
            barry = ("--" + BOUNDARYSTR + "--\r\n").getBytes("UTF-8");
            sendStr = UploadUtil.getBoundaryMessage(BOUNDARYSTR, param, imageParamName, new File(path).getName(), "image/png");
            contentLength = sendStr.getBytes("UTF-8").length + (int) targetFile.length() + 2 * barry.length;
        } catch (UnsupportedEncodingException e) {

        }
        int totalSent = 0;
        String lenstr = Integer.toString(contentLength);

        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        FileInputStream fis = null;
        String errorStr = context.getString(R.string.timeout);
        try {
            URL url = null;
            url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(UPLOAD_CONNECT_TIMEOUT);
            urlConnection.setReadTimeout(UPLOAD_READ_TIMEOUT);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            urlConnection.setRequestProperty("Charset", "UTF-8");
            urlConnection.setRequestProperty("Content-type", "multipart/form-data;boundary=" + BOUNDARYSTR);
            urlConnection.setRequestProperty("Content-Length", lenstr);
            ((HttpURLConnection) urlConnection).setFixedLengthStreamingMode(contentLength);
            urlConnection.connect();

            out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write(sendStr.getBytes("UTF-8"));
            totalSent += sendStr.getBytes("UTF-8").length;

            fis = new FileInputStream(targetFile);

            int bytesRead;
            int bytesAvailable;
            int bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024;

            bytesAvailable = fis.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            bytesRead = fis.read(buffer, 0, bufferSize);
            long transferred = 0;
            final Thread thread = Thread.currentThread();
            while (bytesRead > 0) {
                if (thread.isInterrupted()) {
                    targetFile.delete();
                    throw new InterruptedIOException();
                }
                out.write(buffer, 0, bufferSize);
                bytesAvailable = fis.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fis.read(buffer, 0, bufferSize);
                transferred += bytesRead;
                if (transferred % 50 == 0)
                    out.flush();
            }

            out.write(barry);
            totalSent += barry.length;
            out.write(barry);
            totalSent += barry.length;
            out.flush();
            out.close();
            int status = urlConnection.getResponseCode();

            if (status != HttpURLConnection.HTTP_OK) {
                String error = handleError(urlConnection);
                throw new WeiboException(context, error);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new WeiboException(context, errorStr, e);
        } finally {
            Util.closeSilently(fis);
            Util.closeSilently(out);
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return true;
    }
	
	private String handleError(HttpURLConnection urlConnection) throws WeiboException {
        String result = readError(urlConnection);
        String err = null;
        int errCode = 0;
        try {
            AppLogger.e("error=" + result);
            JSONObject json = new JSONObject(result);
            err = json.optString("error_description", "");
            if (TextUtils.isEmpty(err))
                err = json.getString("error");
            errCode = json.getInt("error_code");
            WeiboException exception = new WeiboException(context);
            exception.setError_code(errCode);
            exception.setOriError(err);
            if (errCode == ErrorCode.EXPIRED_TOKEN) {
                Toast.makeText(context, context.getString(R.string.expired_token), Toast.LENGTH_LONG).show();
            }
            throw exception;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
	
	private String readError(HttpURLConnection urlConnection) throws WeiboException {
        InputStream is = null;
        BufferedReader buffer = null;
        String errorStr = context.getString(R.string.timeout);
        try {
            is = urlConnection.getErrorStream();
            if (is == null) {
                errorStr = context.getString(R.string.unknown_sina_network_error);
                throw new WeiboException(context, errorStr);
            }

            String content_encode = urlConnection.getContentEncoding();
            if (null != content_encode && !"".equals(content_encode) && content_encode.equals("gzip")) {
                is = new GZIPInputStream(is);
            }

            buffer = new BufferedReader(new InputStreamReader(is));
            StringBuilder strBuilder = new StringBuilder();
            String line;
            while ((line = buffer.readLine()) != null) {
                strBuilder.append(line);
            }
            AppLogger.d("error result=" + strBuilder.toString());
            return strBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new WeiboException(context, errorStr, e);
        } finally {
            Util.closeSilently(is);
            Util.closeSilently(buffer);
            urlConnection.disconnect();
        }
    }
}
