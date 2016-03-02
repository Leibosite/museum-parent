package com.qingruan.museum.framework.util;

/**
 * Created by tommy on 2014/11/26.
 */

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 于对java数据进行序列化和反序列化处理
 * 
 * @author:
 * @update: 2012-4-12 上午8:34:47
 */

public class SerializeUtil {

	public final static String CHARSET_ISO88591 = "iso-8859-1";

	/**
	 * @Title: serialize
	 * @Description: java对象序列化 <br>
	 *               eg:<br>
	 *               Map<String,String> map = new HashMap<String,String>();<br>
	 *               map.put("test","序列化");<br>
	 *               String serializedMapStr=SerializeUtil.serialize(map);
	 * @param original
	 *            要进行序列化的java对象
	 * @return String 序列化的后的值
	 * @throws java.io.IOException
	 * 
	 * 
	 */
	public static String serialize(Object original) throws IOException {
		if (null == original)
			return null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(original);
		byte[] str = baos.toByteArray();
		oos.close();
		baos.close();
		return new String(str, CHARSET_ISO88591);
	}

	/**
	 * @Title: deserialize
	 * @Description: 序列化的String对象的反序列化<br>
	 *               需要自己进行强制转换得到最终类型<br>
	 *               eg:<br>
	 *               Map newmap =
	 *               (Map)SerializeUtil.deserialize(serializedMapStr);
	 * @param serializedstr
	 *            经序列化处理过的信息
	 * @return Object 反序列化后生成的Object。<br>
	 * @throws java.io.IOException
	 * @throws java.io.UnsupportedEncodingException
	 * @throws ClassNotFoundException
	 * 
	 * 
	 */
	public static Object deserialize(String serializedstr)
			throws UnsupportedEncodingException, IOException,
			ClassNotFoundException {
		if (null == serializedstr)
			return null;
		BufferedInputStream bis = new BufferedInputStream(
				new ByteArrayInputStream(
						serializedstr.getBytes(CHARSET_ISO88591)));
		ObjectInputStream ois = new ObjectInputStream(bis);
		Object obj = ois.readObject();
		ois.close();
		bis.close();
		return obj;
	}

	public static byte[] objectToByteArray(Object original) throws IOException {
		if (null == original)
			return null;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		try (ObjectOutputStream oout = new ObjectOutputStream(bout);) {
			oout.writeObject(original);
		}
		return bout.toByteArray();
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> byteArrayToObject(byte[] bytearry)
			throws IOException, ClassNotFoundException {
		if (bytearry.length == 0)
			return null;
		return (Map<String, Object>) new ObjectInputStream(
				new ByteArrayInputStream(bytearry)).readObject();
	}
}
