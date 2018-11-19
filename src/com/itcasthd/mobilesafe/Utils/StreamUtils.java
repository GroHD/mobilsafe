package com.itcasthd.mobilesafe.Utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class StreamUtils {

	/**
	 * 将InputStream 流转换为String
	 * 
	 * @param in
	 *            传入的字符流
	 * @return 字符串,返回null 代表异常
	 */
	public static String Stream2String(InputStream in) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		try {
			while ((len = in.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
			// 返回读取的数据
			return bos.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			try {
				bos.close();
				in.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
