package com.sky.web.tools;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class RestUtils {
	public static String generateHmacSHA256Signature(String data, String key) throws GeneralSecurityException {
	
		SecretKeySpec secret_key = new javax.crypto.spec.SecretKeySpec(key.getBytes(), "HmacSHA256");
		StringBuilder verificationResult = new StringBuilder();
		try {
			Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
			sha256_HMAC.init(secret_key); 
			byte[] mac_data = sha256_HMAC.doFinal(data.getBytes());
			for (final byte element : mac_data) {
				verificationResult.append(Integer.toString((element & 0xff) + 0x100, 16).substring(1));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return verificationResult.toString();
	}
}
