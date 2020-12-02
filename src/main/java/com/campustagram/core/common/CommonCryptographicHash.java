package com.campustagram.core.common;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.tomcat.util.codec.binary.Base64;

import com.campustagram.core.controller.log.ILogger;

@ManagedBean(name = "commonCryptographicHash")
@ViewScoped
public class CommonCryptographicHash {
	private static ILogger logger = new ILogger();
	private static final String ACTIVE_CLASS_NAME = "CommonFunctions";

	/**
	 * String encryption with md5.
	 * 
	 * @author Salih Emre Kuru
	 * @param str
	 * @return string encrypted with md5. <br>
	 *         Returns null if an error occurs.
	 */
	public static String stringToHash(String str) {
		try {
			return md5Hashing(str);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	// ====================================================================================================================
	// PASSWORD FORMATTERS START:
	// ====================================================================================================================
	/**
	 * String encryption with Sha1 then md5 and then AES. Prefix and suffix are
	 * added to the password.
	 * 
	 * @author Salih Emre Kuru
	 * @param password
	 * @return encrypted string
	 */
	public static String encryptChar(char[] password) {
		return CommonCryptographicHash.encryptAES(CommonCryptographicHash
				.md5Hashing(CommonCryptographicHash.sha1(CommonCryptographicHash.addPrefixSuffixToPassword(password))));
	}

	public static String encryptStringMD5SHA1(char[] password) {
		return CommonCryptographicHash
				.md5Hashing(CommonCryptographicHash.sha1(CommonCryptographicHash.addPrefixSuffixToPassword(password)));
	}

	/**
	 * String encryption with AES.
	 * 
	 * @param value
	 * @return string encrypted with AES
	 */
	public static String encryptAES(String value) {
		return encryptAES(String.valueOf(CommonConstants.KEY), String.valueOf(CommonConstants.INITVECTOR), value);
	}

	/**
	 * Helper method for string encryption with AES.
	 * 
	 * @param key
	 * @param initVector
	 * @param value
	 * @return string encrypted with AES
	 */
	private static String encryptAES(String key, String initVector, String value) {
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

			byte[] encrypted = cipher.doFinal(value.getBytes());
			// encrypted string
			return Base64.encodeBase64String(encrypted);
		} catch (Exception e) {
			logger.writeError(ACTIVE_CLASS_NAME, "encryptAES", CommonFunctions.getExceptionAsString(e),
					CommonConstants.ERROR);
		}
		return null;
	}

	/**
	 * String decryption with AES.
	 * 
	 * @param value
	 * @return string decrypted with AES
	 */
	public static String decryptAES(String value) {
		return decryptAES(String.valueOf(CommonConstants.KEY), String.valueOf(CommonConstants.INITVECTOR), value);
	}

	/**
	 * Helper method for string decrypted with AES.
	 * 
	 * @author Salih Emre Kuru
	 * @param key
	 * @param initVector
	 * @param encrypted
	 * @return string decrypted with AES
	 */
	private static String decryptAES(String key, String initVector, String encrypted) {
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

			byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

			return new String(original);
		} catch (Exception e) {
			logger.writeError(ACTIVE_CLASS_NAME, "decryptAES", CommonFunctions.getExceptionAsString(e),
					CommonConstants.ERROR);
		}
		return null;
	}

	/**
	 * Increase Password length with prefix and suffix.
	 * 
	 * @param password
	 * @return increased password
	 */
	private static char[] addPrefixSuffixToPassword(char[] password) {
		char[] result = new char[720];
		int index = 0;
		for (int p = 0; p < CommonConstants.getAppPrefix().length; p++) {
			result[index++] = CommonConstants.getAppPrefix()[p];
		}
		for (int p = 0; p < password.length; p++) {
			result[index++] = password[p];
		}
		for (int p = 0; p < CommonConstants.getAppSuffix().length; p++) {
			result[index++] = CommonConstants.getAppSuffix()[p];
		}
		return result;
	}

	/**
	 * Checks the Password length.
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isThePasswordLongEnough(char[] str) {
		return (str.length > CommonConstants.MIN_PASSWORD_LENGTH);
	}

	/**
	 * String encryption with md5.
	 * 
	 * @param str
	 * @return string encrypted with md5
	 */
	private static String md5Hashing(String str) {
		if (null != str) {
			MessageDigest md;
			try {
				md = MessageDigest.getInstance("MD5");
				md.update(str.getBytes());

				byte[] byteData = md.digest();

				// convert the byte to hex format method 1
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < byteData.length; i++) {
					sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
				}

				// convert the byte to hex format method 2
				StringBuffer hexString = new StringBuffer();
				for (int i = 0; i < byteData.length; i++) {
					String hex = Integer.toHexString(0xff & byteData[i]);
					if (hex.length() == 1)
						hexString.append('0');
					hexString.append(hex);
				}
				return hexString.toString();
			} catch (NoSuchAlgorithmException e) {
				logger.writeError(ACTIVE_CLASS_NAME, "MD5Hashing", CommonFunctions.getExceptionAsString(e),
						CommonConstants.ERROR);
			}
		}
		return null;
	}

	/**
	 * Hashing with SHA1
	 *
	 * @param 
	 * @return String encrypted with SHA1
	 */
	private static String sha1(char[] input) {
		try {
			MessageDigest mDigest = MessageDigest.getInstance("SHA1");
			byte[] result = mDigest.digest(String.valueOf(input).getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < result.length; i++) {
				sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			logger.writeError(ACTIVE_CLASS_NAME, "sha1", CommonFunctions.getExceptionAsString(e),
					CommonConstants.ERROR);
			return null;
		}
	}
	// ====================================================================================================================
	// PASSWORD FORMATTERS END:
	// ====================================================================================================================

}