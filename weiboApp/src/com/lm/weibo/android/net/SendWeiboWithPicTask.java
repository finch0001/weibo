package com.lm.weibo.android.net;

import java.io.File;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.lm.weibo.android.utils.ImageUtility;
import com.lm.weibo.android.utils.WeiboException;
import com.lm.weibo.android.views.WriteWeiboActivity;

public class SendWeiboWithPicTask extends AsyncTask<Void, Long, Void> {
    WeiboException e;
    long size;
    
    Context context;
    Handler handler;
    String token;
    String picPath;
    String content;
    
	public SendWeiboWithPicTask(Context context, Handler handler, String token, String picPath, String content) {
		this.context = context;
		this.handler = handler;
		this.token = token;
		this.picPath = picPath;
		this.content = content;
	}

	@Override
	protected Void doInBackground(Void... params) {
		boolean result = false;
		
		try {
			if (!TextUtils.isEmpty(picPath)) {
				String uploadPicPath = ImageUtility.compressPic(context, picPath);
                size = new File(uploadPicPath).length();
                result = sendPic(uploadPicPath);
			}
		} catch (WeiboException e) {
			Message msg = new Message();
			msg.what = WriteWeiboActivity.SEND_FAILED;
			msg.obj = e.getMessage();
			handler.sendMessage(msg);
			cancel(true);
		}
		
		if (!result) {
			cancel(true);
		} {
			handler.sendEmptyMessage(WriteWeiboActivity.SEND_SUCCESS);
		}
		
		return null;
	}
	
	private boolean sendPic(String uploadPicPath) throws WeiboException {
		return new SendWeiboWithPic(context).setPic(uploadPicPath).sendNewMsgWithPic(context, content);
	}

}
