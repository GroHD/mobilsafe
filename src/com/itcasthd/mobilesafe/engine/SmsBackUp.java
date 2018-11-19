package com.itcasthd.mobilesafe.engine;

import java.io.File;
import java.io.FileOutputStream;

import org.xmlpull.v1.XmlSerializer;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Xml;

/**
 * 短信备份
 * 
 * @author Administrator
 *
 */
public class SmsBackUp {

	/**
	 * 备份短信
	 * 
	 * @param context
	 *            上下文
	 * @param uri
	 *            内容提供者
	 * @param savePath
	 *            要保存到什么位置
	 * @param callback
	 *            回调接口
	 */
	public static void backup(Context context, String savePath, CallBack callback) {
		try {
			int index = 1;
			Uri uri = Uri.parse("content://sms");
			// 要保存到那个目录下
			File file = new File(savePath);
			// 内容解析者
			/*
			 * address 发短信的人 date 是发送或接受短信事件 read 是否读取短信 1 是已读 0是未读 type 短信类型 1 是收短信 2是发短信
			 * body 短信内容
			 * 
			 */
			Cursor query = context.getContentResolver().query(uri,
					new String[] { "address", "date", "read", "type", "body" }, null, null, null);
			// pd.setMax(query.getCount());
			callback.setMax(query.getCount());
			FileOutputStream fos = new FileOutputStream(file);
			// 判断是否有数据
			StringBuilder sbstr = new StringBuilder();
			XmlSerializer xml = Xml.newSerializer();
			xml.setOutput(fos, "utf-8");
			// xml规范
			xml.startDocument("utf-8", true);
			//
			xml.startTag(null, "SMSS");
			while (query.moveToNext()) {
				xml.startTag(null, "SMS");
				xml.startTag(null, "address");
				xml.text(query.getString(0));
				xml.endTag(null, "address");
				xml.startTag(null, "date");
				xml.text(query.getString(1));
				xml.endTag(null, "date");
				xml.startTag(null, "read");
				xml.text(query.getString(2));
				xml.endTag(null, "read");
				xml.startTag(null, "type");
				xml.text(query.getString(3));
				xml.endTag(null, "type");
				xml.startTag(null, "body");
				xml.text(query.getString(4));
				xml.endTag(null, "body");
				xml.endTag(null, "SMS");
				// pd.setProgress(index++);
				callback.setProgress(index++);
			}
			xml.endTag(null, "SMSS");
			xml.endDocument();
			xml.flush();
			fos.close();

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

	}

	/*
	 * 回调： 1.定义一个接口 2.底部工艺接口中未实现的业务逻辑方法(短信总数设置，备份短信过程中短信百分比更新)
	 * 3.传递一个实现了此接口的类的对象(至备份短信的工具类中)，接口的实现类，一定实现了上诉两个为实现方法(就决定使用对话框，还是进度条)
	 * 4.获取传递进来的方法，在合适的地方(设置总数，设置百分比的地方)做方法的调用
	 * 
	 */

	public interface CallBack {
		// 短信总数设置为实现方法(由自己决定使用对话框.setMax(max)还是用 进度条.setMax(max))
		public void setMax(int max);

		// 短信备份过程中百分比
		public void setProgress(int max);
		//这里面可以写有什么需要的业务逻辑。

	}
}
