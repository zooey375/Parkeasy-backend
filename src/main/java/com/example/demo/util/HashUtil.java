package com.example.demo.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class HashUtil {
	// 產生隨機鹽
	public static String generateSalt()  {
		SecureRandom secureRandom = new SecureRandom();
		byte[] salt = new byte[16];
		secureRandom.nextBytes(salt);
		// 將 byte[] 透過 Base64 編碼方便儲存
		return Base64.getEncoder().encodeToString(salt);
	}
	
	// 產生雜湊
	public static String hashPassword(String password, String salt)  {
		try {
			// 加密演算法: SHA-256
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			// 加鹽
			md.update(salt.getBytes());
			// 進行加密
			byte[] bytes = md.digest(password.getBytes());
			//System.out.println(Arrays.toString(bytes));
			// 將 byte[] 透過 Base64 編碼方便儲存
			return Base64.getEncoder().encodeToString(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// 產生雜湊
		public static String getHash(String password) {
			try {
				// 加密演算法: SHA-256
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				// 進行加密
				byte[] bytes = md.digest(password.getBytes());
				//System.out.println(Arrays.toString(bytes));
				// 將 byte[] 透過 Base64 編碼方便儲存
				return Base64.getEncoder().encodeToString(bytes);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
}