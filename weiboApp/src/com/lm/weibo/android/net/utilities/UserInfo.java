package com.lm.weibo.android.net.utilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class UserInfo implements Serializable {
	private static final long serialVersionID = 1L;
	public static final String USER_INFO = "user_info";
	public static final String CLOUD_RESPONSE_BODY = "cloud_response_body";
	public static final String CLOUD_RESPONSE_STATUS_CODE = "cloud_response_status_code";

	private HashMap<String, Object> mInfoMap;

	public UserInfo() {
		mInfoMap = new HashMap<String, Object>();
	}

	public UserInfo(HashMap<String, Object> map) {
		mInfoMap = map;
	}

	public void putInfo(String key, Object value) {
		mInfoMap.put(key, value);
	}

	public void removeInfo(String key) {
		mInfoMap.remove(key);
	}

	public ArrayList getUserInfoArrayList(String key) {
		return containsKey(key) ? (ArrayList) mInfoMap.get(key) : null;
	}

	private boolean containsKey(String key) {
		Iterator it = mInfoMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry entry = (Entry) it.next();
			if ((entry.getKey().toString()).equalsIgnoreCase(key)) {
				return true;
			}
		}
		return false;
	}

	public int getInt(String key) {
		return (Integer) mInfoMap.get(key);
	}
}
