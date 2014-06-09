package com.lm.weibo.android.net;

import com.lm.weibo.android.net.utilities.UserInfo;

public class AppException extends Exception {
	private static final long serialVersionUID = 1L;

	public enum EnumException {
		ParseException, CancelException, IOException, NormallException, ClientProtocolException, ConnectionException, CloudException, FileException, JSONException
	}

	private EnumException mExceptionType;
	private String detailMessage;
	private UserInfo info;

	public AppException(EnumException type, String detailMessage) {
		super(detailMessage);
		this.mExceptionType = type;
		this.detailMessage = detailMessage;
	}

	public AppException(EnumException type, String reasonPhrase,
			UserInfo errorInfo) {
		this.mExceptionType = type;
		this.detailMessage = reasonPhrase;
		this.info = errorInfo;
	}

	public UserInfo getErrorInfo() {
		return info;
	}
}
