package com.itcasthd.mobilesafe.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

public static String MD5(String encode) {
	// 密码md5加密
	StringBuffer  sbstr = new StringBuffer();
	try {
		//增加密码复杂度
		encode=encode+"ZXJT@TYYX!+98~";
		//指定加密算法
		MessageDigest instance = MessageDigest.getInstance("MD5");
		//将加密的字符串转换成byte类型的数据,然后随机的打乱
		byte [] buffPass = instance.digest(encode.getBytes());
		//循环遍历buffPass,然后让其生成32位字符串,固定写法
		for(byte b :buffPass) {
			int i = b&0xff;
			//将i转换为16进制的字符
			String hexString = Integer.toHexString(i);
			if(hexString.length()<2) {
				hexString="0"+hexString;
			}
			sbstr.append(hexString);
		}
		
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return  sbstr.toString();
}

}
