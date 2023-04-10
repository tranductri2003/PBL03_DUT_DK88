package service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class HashService {

	private static final String SECRET_STR = "|UyYeuKhanh|KhanhManhMeDeThuong|secuoiKhanh10namnua|";

	private static byte[] getSHA(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			return md.digest(input.getBytes(StandardCharsets.UTF_8));
		} catch (NoSuchAlgorithmException ee) {
		}
		return null;
	}

	private static String toHexString(byte[] hash) {
		BigInteger number = new BigInteger(1, hash);
		StringBuilder hexString = new StringBuilder(number.toString(16));
		while (hexString.length() < 64)
			hexString.insert(0, '0');
		return hexString.toString();
	}

	public static String hash(String... args) {
		StringBuilder tmp = new StringBuilder(SECRET_STR);
		for (String tt : args) {
			tmp.append(tt);
			tmp.append(SECRET_STR);
		}
		return toHexString(getSHA(tmp.toString()));
	}
	
}
