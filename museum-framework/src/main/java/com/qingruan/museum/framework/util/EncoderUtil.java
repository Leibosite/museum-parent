package com.qingruan.museum.framework.util;

public class EncoderUtil {

	public static String switchString(String str) {
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i < str.length() / 2 + 1; i++) {
			if (i * 2 - 1 < str.length()) {
				sb.append(str.charAt(i * 2 - 1));
			}
			if (i * 2 - 2 < str.length()) {
				sb.append(str.charAt(i * 2 - 2));
			}
		}
		return sb.toString();
	}

	public static String tranSimCode12To11(String SimCode) {
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i < SimCode.length() / 2 + 1; i++) {
			sb.append(SimCode.charAt(i * 2 - 1));
			sb.append(SimCode.charAt(i * 2 - 2));
		}
		return sb.substring(0, sb.length() - 1);
	}

	public static byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
				.byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
				.byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}

	/**
	 * 十六进制字符串转换为二进制数组
	 * @param hexStr
	 * @param byteLen
	 * @return
	 */
	public static byte[] HexString2Bytes(String hexStr, int byteLen) {
		int strLen = hexStr.length();
		StringBuffer sb = new StringBuffer(hexStr);
		if (byteLen > strLen / 2) {
			int setLen = byteLen * 2;
			for (int i = 0; i < setLen - strLen; i++) {
				sb.insert(0, "0");
			}
		}
		hexStr = sb.toString();
		byte[] ret = new byte[byteLen];
		byte[] tmp = hexStr.getBytes();
		for (int i = 0; i < byteLen; i++) {
			ret[i] = uniteBytes(tmp[(i * 2)], tmp[(i * 2 + 1)]);
		}
		return ret;
	}

	public static byte HexString2Byte(String hexStr) {
		StringBuffer sb = new StringBuffer(hexStr);
		hexStr = sb.toString();
		byte ret = 0;
		byte[] tmp = hexStr.getBytes();
		for (int i = 0; i < 1; i++) {
			ret = uniteBytes(tmp[(i * 2)], tmp[(i * 2 + 1)]);
		}
		return ret;
	}

	public static String toBCDEncode(String str) {
		str = str.replaceAll("\\*", "A");
		str = str.replaceAll("\\#", "B");
		str = str.replaceAll(" ", "F");
		return str;
	}

	public static String toBCDEncodeWithF(String str, int length) {
		StringBuilder sb = new StringBuilder();
		if (str.length() < length) {
			String fStr = "";
			for (int index = 0; index < length - str.length(); index++) {
				fStr = fStr + "F";
			}
			str = str + fStr;
		}
		for (int i = 1; i < str.length() / 2 + 1; i++) {
			sb.append(str.charAt(i * 2 - 1));
			sb.append(str.charAt(i * 2 - 2));
		}
		return sb.toString();
	}

	public static byte getValidate(byte[] bytes, int start, int end) {
		int check = 0;
		for (int i = start; i < end; i++) {
			check += getUnsignValue(bytes[i]);
		}
		byte kk = (byte) (check % 256);
		return kk;
	}

	public static byte swap(byte b) {
		b = (byte) (((b & 0xF0) >> 4) + ((b & 0xF) << 4));
		return b;
	}

	public static String getHexStr(byte temp) {
		
		return Integer.toHexString((temp & 0xFF )+256).substring(1).toUpperCase();
		
	}

	public static String getHexStr(byte[] temp) {
		StringBuffer sb = new StringBuffer();
		for (int index = 0; index < temp.length; index++) {
			sb.append(getHexStr(temp[index]));
		}
		return sb.toString();
	}

	public static String int2HexStr(int i, int formatLen) {
		String hexStr = Integer.toHexString(i);
		if (hexStr.length() < formatLen) {
			StringBuffer sb = new StringBuffer(hexStr);
			for (int j = hexStr.length(); j < formatLen; j++) {
				sb.insert(0, "0");
			}
			return sb.toString().toUpperCase();
		}

		StringBuffer sb = new StringBuffer("");
		sb.append(hexStr.substring(hexStr.length() - formatLen));
		return sb.toString().toUpperCase();
	}

	public static String int2HexStr(long i, int formatLen) {
		String hexStr = Long.toHexString(i);

		if (hexStr.length() < formatLen) {
			StringBuffer sb = new StringBuffer(hexStr);
			for (int j = hexStr.length(); j < formatLen; j++) {
				sb.insert(0, "0");
			}
			return sb.toString().toUpperCase();
		}

		StringBuffer sb = new StringBuffer("");
		sb.append(hexStr.substring(hexStr.length() - formatLen));
		return sb.toString().toUpperCase();
	}

	public static String long2HexStr(long i, int formatLen) {
		String hexStr = Long.toHexString(i);

		if (hexStr.length() < formatLen) {
			StringBuffer sb = new StringBuffer(hexStr);
			for (int j = hexStr.length(); j < formatLen; j++) {
				sb.insert(0, "0");
			}
			return sb.toString().toUpperCase();
		}

		StringBuffer sb = new StringBuffer("");
		sb.append(hexStr.substring(hexStr.length() - formatLen));
		return sb.toString().toUpperCase();
	}

	public static String long2BinaryStr(long i, int formatLen) {
		String binaryStr = Long.toBinaryString(i);
		if (binaryStr.length() < formatLen) {
			StringBuffer sb = new StringBuffer(binaryStr);
			for (int index = binaryStr.length(); index < formatLen; index++) {
				sb.insert(0, "0");
			}
			return sb.toString();
		}
		StringBuffer sb = new StringBuffer("");
		sb.append(binaryStr.substring(binaryStr.length() - formatLen));
		return sb.toString();
	}

	public static char[] bytesToChars(byte[] bytes) {
		String s = new String(bytes);
		char[] chars = s.toCharArray();
		return chars;
	}

	public static byte[] charsToBytes(char[] chars) {
		String s = new String(chars);
		byte[] bytes = s.getBytes();
		return bytes;
	}

	public static char byteToChar(byte bytes) {
		byte[] temp = { bytes };
		String s = new String(temp);
		char chars = s.toCharArray()[0];
		return chars;
	}

	public static byte charToByte(char chars) {
		char[] temp = { chars };
		String s = new String(temp);
		byte bytes = s.getBytes()[0];
		return bytes;
	}

	public static int getUnsignValue(byte byteStr) {
		int unsignedByte = byteStr >= 0 ? byteStr : 256 + byteStr;
		return unsignedByte;
	}

	public static int getUnsignValue(byte[] byteStr) {
		int count = 0;
		for (int i = 0; i < byteStr.length; i++) {
			count += getUnsignValue(byteStr[i])
					* new Double(Math.pow(2.0D, (byteStr.length - 1 - i) * 8))
							.intValue();
		}
		return count;
	}

	public static long getUnsignValueToLong(byte[] byteStr) {
		long count = 0L;
		for (int i = 0; i < byteStr.length; i++) {
			count += getUnsignValue(byteStr[i])
					* new Double(Math.pow(2.0D, (byteStr.length - 1 - i) * 8))
							.intValue();
		}
		return count;
	}

	public static String byte2binaryStr(byte bytes) {
		StringBuilder sb = new StringBuilder("00000000");
		for (int bit = 0; bit < 8; bit++) {
			if ((bytes >> bit & 0x1) > 0) {
				sb.setCharAt(7 - bit, '1');
			}
		}
		return sb.toString();
	}

	public static String byte2binaryStr(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(byte2binaryStr(bytes[i]));
		}
		return sb.toString();
	}

	public static int binaryToInt(String binaryStr) {
		return Integer.parseInt(binaryStr, 2);
	}

	public static byte binaryStrToByte(String binaryStr) {
		return (byte) Integer.parseInt(binaryStr, 2);
	}

	public static byte[] binaryStrToBytes(String binaryStr, int byteLen) {
		byte[] bytes = new byte[byteLen];
		int binaryStrLen = byteLen * 8;
		if (binaryStr.length() < binaryStrLen) {
			StringBuffer sb = new StringBuffer(binaryStr);
			for (int i = binaryStr.length(); i < binaryStrLen; i++) {
				sb.insert(0, "0");
			}
			binaryStr = sb.toString();
		} else {
			binaryStr = binaryStr.substring(binaryStr.length() - binaryStrLen);
		}
		for (int index = 0; index < byteLen; index++) {
			String byteBin = binaryStr.substring(index * 8, index * 8 + 8);
			bytes[index] = binaryStrToByte(byteBin);
		}
		return bytes;
	}

	public static short byteArrayToShort(byte[] b) {
		return byteArrayToShort(b, 0);
	}

	public static short byteArrayToShort(byte[] b, int offset) {
		short value = 0;
		for (int i = 0; i < 2; i++) {
			int shift = (1 - i) * 8;
			value = (short) (value + ((b[(i + offset)] & 0xFF) << shift));
		}
		return value;
	}

	public static int byteArrayToInt(byte[] b) {
		return byteArrayToInt(b, 0);
	}

	public static int byteArrayToInt(byte[] b, int offset) {
		int value = 0;
		for (int i = 0; i < 4; i++) {
			int shift = (3 - i) * 8;
			value += ((b[(i + offset)] & 0xFF) << shift);
		}
		return value;
	}
	
	//整形字符串，转换成ASCII码字符串
	public static String int2AsciiString(String intString){
		
		char[] intChar = intString.toCharArray();
		StringBuffer phoneAscii = new StringBuffer();
		for(char i : intChar){
			phoneAscii.append((int)i);
		}
		
		return phoneAscii.toString();
	}

	public static String hex2BinStr(String s, int len) {
		int i = Integer.parseInt(s, 16);
		String bin = Integer.toBinaryString(i);
		
		StringBuffer binStr = new StringBuffer();
		if(bin.length()<len){
			binStr.append(bin);
			for(int j=0;j<len-bin.length();j++){
				binStr.insert(0, "0");
			}
		}else{
			binStr.append(bin);
		}
		
		return binStr.toString();
	}
}
	
