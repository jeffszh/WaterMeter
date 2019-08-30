package cn.amware.node.red.net;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class NetUtils {

	public static String bytesToHexStr(byte[] bytes) {
		StringBuilder result = new StringBuilder();
		for (byte b : bytes) {
			result.append(String.format("%02x", b));
		}
		return result.toString();
	}

	public static String getTimeStamp() {
		return getTimeStamp(new Date());
	}

	public static String getTimeStamp(Date time) {
		return "" + time.getTime() / 1000;
	}

	public static String makeMd5(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] md5Bytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
			return bytesToHexStr(md5Bytes);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException(e);	// 不可能
		}
	}

}
