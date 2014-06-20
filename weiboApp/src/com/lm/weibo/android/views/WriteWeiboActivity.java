package com.lm.weibo.android.views;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.lm.weibo.android.R;
import com.lm.weibo.android.net.AppException;
import com.lm.weibo.android.net.Request;
import com.lm.weibo.android.net.Request.RequestMethod;
import com.lm.weibo.android.net.Request.RequestTool;
import com.lm.weibo.android.net.SendWeiboWithPicTask;
import com.lm.weibo.android.net.Urls;
import com.lm.weibo.android.net.callback.JsonCallback;
import com.lm.weibo.android.utils.AccessTokenKeeper;
import com.lm.weibo.android.utils.Util;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public class WriteWeiboActivity extends Activity {
	private Context context;

	private Button btn_send, btn_addimg, btn_addemotion;
	private ImageView thumbimage;
	private EditText edt_content;
	private Oauth2AccessToken mAccessToken;

	// 要上传的图片的路径
	private String picPath;
	// 从相机拍摄
	private static final int PHOTO_WITH_CAMERA = 1010;
	// 从SD卡中获取图片
	private static final int PHOTO_WITH_DATA = 1011;
	// 拍摄照片存储的文件夹路径
	private static final File PHOTO_DIR = new File(
			Environment.getExternalStorageDirectory() + "/DCIM/Camera");
	// 拍摄的照片文件
	private File capturefile;
	
	private ProgressDialog dialog;

	public static final int SEND_SUCCESS = 101;
	public static final int SEND_FAILED = 102;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SEND_SUCCESS:
				showToast("微博发送成功");
				dialog.dismiss();
				WriteWeiboActivity.this.finish();
				break;
			case SEND_FAILED:
				dialog.dismiss();
				showToast("微博发送失败\n" + msg.obj.toString());
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_writeweibo);
		context = WriteWeiboActivity.this;
		
		dialog = new ProgressDialog(context);
		dialog.setMessage("正在发送微博,请稍候...");

		mAccessToken = AccessTokenKeeper
				.readAccessToken(getApplicationContext());

		btn_send = (Button) findViewById(R.id.writeweibo_send);
		btn_addimg = (Button) findViewById(R.id.writeweibo_addimg);
		btn_addemotion = (Button) findViewById(R.id.writeweibo_addemotion);
		edt_content = (EditText) findViewById(R.id.writeweibo_edt);
		thumbimage = (ImageView) findViewById(R.id.writeweibo_img);

		btn_addimg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showAddImgDialog();
			}
		});

		btn_send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (canSend()) {
					dialog.show();
					if (withPic()) {
						sendWeiboMsgWithPic(picPath);
					} else {
						sendWeiboMsg();
					}
				}
			}
		});
	}
	
	private boolean withPic() {
		if (thumbimage.getVisibility() == View.VISIBLE) {
			return true;
		} else {
			return false;
		}
	}
	
	private void sendWeiboMsg() {
		Request request = new Request(Urls.url_update, RequestMethod.POST,
				RequestTool.HTTPURLCONNECTION);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Connection", "Keep-Alive");
		headers.put("Charset", "UTF-8");
		headers.put("Accept-Encoding", "gzip, deflate");
		request.headers = headers;
		request.postContent = "access_token=" + mAccessToken.getToken() + "&status=" + edt_content.getText().toString();
		request.setCallback(new JsonCallback<String>() {

			@Override
			public void onFailure(AppException result) {
				showToast(getString(R.string.send_failed));
				dialog.dismiss();
			}

			@Override
			public void onSuccess(String result) {
				showToast(getString(R.string.send_successfully));
				dialog.dismiss();
				WriteWeiboActivity.this.finish();
			}
		});
		request.execute();
	}
	
	private void sendWeiboMsgWithPic(String path) {
		SendWeiboWithPicTask task = new SendWeiboWithPicTask(context, handler, mAccessToken.getToken(), picPath, edt_content.getText().toString());
		task.execute();
	}
	
	/*
	private String sendStr = "";
	private int totalSent = 0;
	byte[] barry = null;
	private void sendWeiboMsgWithPic(final String picpath) {
		Request request = new Request(Urls.url_upload, RequestMethod.POST,
				RequestTool.HTTPURLCONNECTION);
		String BOUNDARYSTR = UploadUtil.getBoundry();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Connection", "Keep-Alive");
		headers.put("Charset", "UTF-8");
		headers.put("Content-type", "multipart/form-data;boundary=" + BOUNDARYSTR);
		
        int contentLength = 0;
		try {
			barry = ("--" + BOUNDARYSTR + "--\r\n").getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		File targetFile = new File(picpath);
		Map<String, String> map = new HashMap<String, String>();
        map.put("access_token", mAccessToken.getToken());
        map.put("status", edt_content.getText().toString());
		sendStr = UploadUtil.getBoundaryMessage(BOUNDARYSTR, map, "pic", new File(picpath).getName(), "image/png");
		try {
			contentLength = sendStr.getBytes("UTF-8").length + (int) targetFile.length() + 2 * barry.length;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String lenstr = Integer.toString(contentLength);
		headers.put("Content-Length", lenstr);
		headers.put("contentLength", String.valueOf(contentLength));
		request.headers = headers;
		request.postContent = sendStr;
		request.setCallback(new JsonCallback<String>() {

			@Override
			public boolean onPrepareParams(OutputStream out)
					throws AppException {
				try {
					totalSent += sendStr.getBytes("UTF-8").length;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				UploadUtil.upload(out, picpath, barry, totalSent);
				return super.onPrepareParams(out);
			}

			@Override
			public void onFailure(AppException result) {
				showToast(getString(R.string.send_failed));
				dialog.dismiss();
			}

			@Override
			public void onSuccess(String result) {
				showToast(getString(R.string.send_successfully));
				dialog.dismiss();
				WriteWeiboActivity.this.finish();
			}
		});
		request.execute();
	}
	*/

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		File file = null;
		Bitmap pic = null;
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case PHOTO_WITH_CAMERA:
				// 获取拍摄的文件
				picPath = capturefile.getAbsolutePath();
				file = new File(picPath);
				pic = decodeFile(file);
				thumbimage.setImageBitmap(pic);
				thumbimage.setVisibility(View.VISIBLE);
				break;
			case PHOTO_WITH_DATA:
				// 获取从图库选择的文件
				Uri uri = data.getData();
				String scheme = uri.getScheme();
				if (scheme.equalsIgnoreCase("file")) {
					picPath = uri.getPath();
					file = new File(picPath);
					pic = decodeFile(file);
					thumbimage.setImageBitmap(pic);
					thumbimage.setVisibility(View.VISIBLE);
				} else if (scheme.equalsIgnoreCase("content")) {
					Cursor cursor = getContentResolver().query(uri, null, null,
							null, null);
					cursor.moveToFirst();
					picPath = cursor.getString(1);
					file = new File(picPath);
					pic = decodeFile(file);
					thumbimage.setImageBitmap(pic);
					thumbimage.setVisibility(View.VISIBLE);
				}
				break;
			}
		}
	}

	// 显示插入图片选择对话框
	private void showAddImgDialog() {
		Context dialogContext = new ContextThemeWrapper(context,
				android.R.style.Theme_Holo);
		String[] choices = { "相机拍摄", "本地相册" };
		ListAdapter adapter = new ArrayAdapter<String>(dialogContext,
				android.R.layout.simple_list_item_1, choices);
		AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext);
		builder.setTitle("添加图片");
		builder.setSingleChoiceItems(adapter, -1,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switch (which) {
						case 0:
							String status = Environment.getExternalStorageState();
							// 判断是否有SD卡
							if (status.equals(Environment.MEDIA_MOUNTED)) {
								Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
								capturefile = new File(PHOTO_DIR, getPhotoFileName());
								try {
									capturefile.createNewFile();
									// 将拍摄的照片信息存到capturefile中
									i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(capturefile));
								} catch (IOException e) {
									e.printStackTrace();
								}
								// 用户点击了从照相机获取
								startActivityForResult(i, PHOTO_WITH_CAMERA);
							} else {
								showToast("没有SD卡");
							}
							break;
						case 1:
							// 从相册中去获取
							Intent intent = new Intent();
							// 开启Pictures画面Type设定为image
							intent.setType("image/*");
							// 使用Intent.ACTION_GET_CONTENT这个Action
							intent.setAction(Intent.ACTION_GET_CONTENT);
							// 取得相片后返回本画面
							startActivityForResult(intent, PHOTO_WITH_DATA);
							break;
						}
					}
				});
		builder.create().show();
	}

	
	/**
	 * 已废弃不用
	 * @param requesturl
	 * @param token
	 * @param status
	 *
	private void sendWeiBoByPost(String requesturl, String token, String status) {
		HttpPost httprequest = new HttpPost(requesturl);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("access_token", token));
		params.add(new BasicNameValuePair("status", status));
		try {
			HttpEntity httpentity = new UrlEncodedFormEntity(params, "UTF-8");
			httprequest.setEntity(httpentity);
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse httpresponse = httpclient.execute(httprequest);
			if (httpresponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String strresult = EntityUtils.toString(httpresponse
						.getEntity());
				handler.sendEmptyMessage(SEND_SUCCESS);
				Log.d("lm", "success");
			} else {
				handler.sendEmptyMessage(SEND_FAILED);
				Log.d("lm", "fail");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	
	private void showToast(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	// 通过相机回传图片的文件名
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".png";
	}

	// 压缩图片,避免内存不足报错
	private Bitmap decodeFile(File f) {
		Bitmap b = null;
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;

			FileInputStream fis = new FileInputStream(f);
			BitmapFactory.decodeStream(fis, null, o);
			fis.close();

			int scale = 1;
			if (o.outHeight > 100 || o.outWidth > 100) {
				scale = (int) Math.pow(
						2,
						(int) Math.round(Math.log(100 / (double) Math.max(
								o.outHeight, o.outWidth)) / Math.log(0.5)));
			}

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			fis = new FileInputStream(f);
			b = BitmapFactory.decodeStream(fis, null, o2);
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
	}
	
	private boolean canSend() {
        boolean haveContent = !TextUtils.isEmpty(edt_content.getText().toString());
        boolean haveToken = !TextUtils.isEmpty(mAccessToken.getToken());
        int sum = Util.length(edt_content.getText().toString());
        int num = 140 - sum;
        boolean contentNumBelow140 = (num >= 0);

        if (haveContent && haveToken && contentNumBelow140) {
            return true;
        } else {
            if (!haveContent && !haveToken) {
                Toast.makeText(this,
                        getString(R.string.content_cant_be_empty_and_dont_have_account),
                        Toast.LENGTH_SHORT).show();
            } else if (!haveContent) {
            	edt_content.setError(getString(R.string.content_cant_be_empty));
            } else if (!haveToken) {
                Toast.makeText(this, getString(R.string.dont_have_account), Toast.LENGTH_SHORT)
                        .show();
            }

            if (!contentNumBelow140) {
            	edt_content.setError(getString(R.string.content_words_number_too_many));
            }
        }
        return false;
    }
}
