package com.lm.weibo.android.net.callback;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

import com.lm.weibo.android.net.AppException;
import com.lm.weibo.android.net.AppException.EnumException;
import com.lm.weibo.android.net.Request;
import com.lm.weibo.android.net.interfaces.ICallback;
import com.lm.weibo.android.net.interfaces.IProgressListener;
import com.lm.weibo.android.net.utilities.IOUtilities;
import com.lm.weibo.android.net.utilities.TextUtil;
import com.lm.weibo.android.net.utilities.UserInfo;

public abstract class AbstractCallback<T> implements ICallback<T> {
	public static final int IO_BUFFER_SIZE = 4 * 1024;
	public Class<T> returnClass;
	public Type returnType;
	public String path;

	protected boolean isCancelled;

	@Override
	public void checkIfCancelled() throws AppException {
		if (isCancelled) {
			throw new AppException(EnumException.CancelException,
					"request has been cancelled");
		}
	}

	public T handle(HttpResponse response, IProgressListener mProgressListener)
			throws AppException {
		// file, json, xml, image, string
		checkIfCancelled();
		try {
			HttpEntity entity = response.getEntity();

			int statusCode = response.getStatusLine().getStatusCode();
			switch (statusCode) {
			case HttpStatus.SC_OK:
			case HttpStatus.SC_CREATED:
				if (TextUtil.isValidate(path)) {
					FileOutputStream fos = new FileOutputStream(path);
					InputStream in = null;
					if (entity.getContentEncoding() != null) {
						String encoding = entity.getContentEncoding()
								.getValue();
						if (encoding != null
								&& "gzip".equalsIgnoreCase(encoding)) {
							in = new GZIPInputStream(entity.getContent());
						} else if (encoding != null
								&& "deflate".equalsIgnoreCase(encoding)) {
							in = new InflaterInputStream(entity.getContent());
						}
					} else {
						in = entity.getContent();
					}
					byte[] b = new byte[IO_BUFFER_SIZE];
					int read;
					long currentPos = 0;
					long length = entity.getContentLength();
					while ((read = in.read(b)) != -1) {
						checkIfCancelled();
						if (mProgressListener != null) {
							currentPos += read;
							mProgressListener.onProgressUpdate(
									(int) (currentPos / 1024),
									(int) (length / 1024));
						}
						fos.write(b, 0, read);
					}
					fos.flush();
					fos.close();
					in.close();
					return bindData(path);
				} else {
					return bindData(EntityUtils.toString(entity,
							Request.ENCODING));
				}
			default:
				UserInfo errorInfo = new UserInfo();
				if (entity != null) {
					errorInfo.putInfo(UserInfo.CLOUD_RESPONSE_BODY,
							EntityUtils.toString(entity));
				}
				errorInfo.putInfo(UserInfo.CLOUD_RESPONSE_STATUS_CODE, statusCode);
				throw new AppException(EnumException.CloudException, response.getStatusLine().getReasonPhrase(), errorInfo);
			}
		} catch (ParseException e) {
			throw new AppException(EnumException.ParseException, e.getMessage());
		} catch (IOException e) {
			throw new AppException(EnumException.IOException, e.getMessage());
		}
	}

	@Override
	public T handle(HttpURLConnection response,
			IProgressListener mProgressListener) throws AppException {
		checkIfCancelled();
		int statusCode = -1;
		InputStream in = null;
		try {
			statusCode = response.getResponseCode();
			long contentLength = response.getContentLength();
			switch (statusCode) {
			case HttpStatus.SC_OK:
			case HttpStatus.SC_CREATED:
				String encoding = response.getContentEncoding();
				if (encoding != null && "gzip".equalsIgnoreCase(encoding)) {
					in = new GZIPInputStream(response.getInputStream());
				} else if (encoding != null && "default".equalsIgnoreCase(encoding)) {
					in = new InflaterInputStream(response.getInputStream());
				} else {
					in = response.getInputStream();
				}
				if (TextUtil.isValidate(path)) {
					FileOutputStream fos = new FileOutputStream(path);
					byte[] b = new byte[IO_BUFFER_SIZE];
					int curPos = 0;
					int read;
					while ((read = in.read(b)) != -1) {
						checkIfCancelled();
						if (mProgressListener != null) {
							curPos += read;
							mProgressListener.onProgressUpdate(curPos / 1024, (int)contentLength / 1024);
						}
						fos.write(b, 0, read);
					}
					fos.flush();
					fos.close();
					in.close();
					return bindData(path);
				} else {
					return bindData(IOUtilities.readStreamToMemory(in));
				}
			default:
				UserInfo errorInfo = new UserInfo();
				errorInfo.putInfo(UserInfo.CLOUD_RESPONSE_STATUS_CODE, statusCode);
				errorInfo.putInfo(UserInfo.CLOUD_RESPONSE_BODY, IOUtilities.readStreamToMemory(response.getErrorStream()));
				throw new AppException(EnumException.CloudException, response.getResponseMessage(), errorInfo);
			}
		} catch (FileNotFoundException e) {
			throw new AppException(EnumException.FileException, e.getMessage());
		} catch (IOException e) {
			throw new AppException(EnumException.ConnectionException, e.getMessage());
		}
	}
	
	protected T bindData(String content) throws AppException {
		checkIfCancelled();
		return null;
	}
	
	public T onPreHandle(T object) {
		// 操作数据前做一些操作如插入数据库
		return object;
	}
	
	@Override
	public T onPreRequest() {
		// 请求数据前做一些操作如查询数据库是否有数据
		return null;
	}

	@Override
	public boolean onPrepareParams(OutputStream out) throws AppException {
		// TODO Auto-generated method stub
		return false;
	}

	public AbstractCallback<T> setPath(String path) {
		this.path = path;
		return this;
	}

	public AbstractCallback<T> setReturnClass(Class<T> returnClass) {
		this.returnClass = returnClass;
		return this;
	}

	public AbstractCallback<T> setReturnType(Type type) {
		this.returnType = type;
		return this;
	}

	public void cancel() {
		isCancelled = true;
	}
}
