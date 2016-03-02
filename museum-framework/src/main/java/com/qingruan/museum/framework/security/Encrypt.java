package com.qingruan.museum.framework.security;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;


/**
 * 数据加密
 * @author 14cells
 *
 */
public class Encrypt{
	// 工作密钥
	private static final byte[] DES_KEY = { 21, 1, -110, 82, -32, -85, -128,
			-65 };

	/**
	 * 加密函数
	 * 
	 * @param data
	 *            要加密的数据
	 * @return 密文以String 输出
	 */
	public static String encryptBasedDes(String data) {
		String encryptedData = null;
		try {
			// 随机数源
			SecureRandom sr = new SecureRandom();
			DESKeySpec deskey = new DESKeySpec(DES_KEY);
			// 创建一个密钥工厂，然后用它把DESKeySpec转换成一个SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(deskey);
			// 加密对象
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key, sr);
			// 加密，并把字节数组编码成字符串

//			encryptedData = BASE64Encoder().encode(cipher
//					.doFinal(data.getBytes()));
			encryptedData = Base64.encodeBase64String(cipher.doFinal(data
					.getBytes()));
		} catch (Exception e) {
			// log.error("加密错误，错误信息：", e);
			throw new RuntimeException("加密错误，错误信息：", e);
		}
		return encryptedData;
	}
}
