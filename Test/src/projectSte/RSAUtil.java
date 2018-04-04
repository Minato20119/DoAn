/**
 * 
 */
package projectSte;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * @author Minato
 *
 */
public class RSAUtil {

	private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC1+2oCiFBLI+Zrtz7GsJYHFdh+QjYlb8IP5+cGhxInpv5SD89yTrLybfB8l31tP7EcVUCZYec0fIzLBV6/sDTviAxrZrjfpVqjb1xmWozMU4EHHcsJsY2ZwLXdRLTm79xNaQEovocm9k+hz9FmHJp9tzIgxxRvBDxc3c5O8W87RQIDAQAB";
	private static String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALX7agKIUEsj5mu3PsawlgcV2H5CNiVvwg/n5waHEiem/lIPz3JOsvJt8HyXfW0/sRxVQJlh5zR8jMsFXr+wNO+IDGtmuN+lWqNvXGZajMxTgQcdywmxjZnAtd1EtObv3E1pASi+hyb2T6HP0WYcmn23MiDHFG8EPFzdzk7xbztFAgMBAAECgYA8yKcWBXXcQzd5jxFWyDC0WSWgtnjjdDq7mVffJEp6GBsmScWeho7azsHeu7LXRMjhj9At6zeQnnFgdoC5Qh/LuYz39df26U0oW3gp5M5Smqz0Bz8nusPdgG1H42sE1vXqfPFWpRAG5lvtqhs7Ew29kkglQJhOMgn8Kn7o2y1yWQJBAOWUgT17WCaT1uAADkK3Mle5Qv9xXWGlrKDa8/BpMGZ/0Keytk2yYgMh7aYXt5RI6rHiaCChLzRnGt1tS0nhwpcCQQDK7KiFKaOkPvqtVxbBIFaiChFNWYV2evNdti+bacQIW77MKUkhQGi4TAo2lESS7ZN8/+vRchIOv8igd/2SeZiDAkEAhxTfCm7lyiUYvEosGeMy0tzl5+krGcZikW7jM2ShHt47xkBn9cVs2A0BiMXRtYVF2o6t1+0fGkVEUF77eqhEVwJBAKDJU93vWFJT9JxUXRs5XGuYPo1jWXS/hwXBzYb/DuzwhQWP0Pof5tngplcItC0XQb3RqjCSiexV2coMSUc+QbECQBUgUJvEgGikgASGQM/ttCtAwBllG0SJPGE8KOZMABM9pZsVyQAdN9jTPzFOkQbXDVnq8zgpoJb/BtFWWZIvf2w=";

	public static PublicKey getPublicKey(String base64PublicKey) {
		PublicKey publicKey = null;
		try {
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			publicKey = keyFactory.generatePublic(keySpec);
			// System.out.println("Public key: " + publicKey);
			return publicKey;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return publicKey;
	}

	public static PrivateKey getPrivateKey(String base64PrivateKey) {
		PrivateKey privateKey = null;
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
		KeyFactory keyFactory = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		try {
			privateKey = keyFactory.generatePrivate(keySpec);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}

		// System.out.println("Private key: " + privateKey);
		return privateKey;
	}

	public static byte[] encrypt(String data, String publicKey) throws BadPaddingException, IllegalBlockSizeException,
			InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
		return cipher.doFinal(data.getBytes());
	}

	public static String decrypt(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException,
			NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return new String(cipher.doFinal(data));
	}

	public static String decrypt(String data, String base64PrivateKey) throws IllegalBlockSizeException,
			InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		return decrypt(Base64.getDecoder().decode(data.getBytes()), getPrivateKey(base64PrivateKey));
	}

	// public static void main(String[] args)
	// throws IllegalBlockSizeException, InvalidKeyException,
	// NoSuchPaddingException, BadPaddingException {
	// try {
	// String encryptedString = Base64.getEncoder().encodeToString(encrypt("Hello
	// Minato!", publicKey));
	// System.out.println("Encrypt: " + encryptedString);
	//
	// String decryptedString = RSAUtil.decrypt(encryptedString, privateKey);
	// System.out.println("Decrypt: " + decryptedString);
	//
	// } catch (NoSuchAlgorithmException e) {
	// System.err.println(e.getMessage());
	// }
	//
	// }

	// set text to encrypt
	public static String getTextToEncrypt(String text) throws InvalidKeyException, BadPaddingException,
			IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException {
		String encryptedString = Base64.getEncoder().encodeToString(encrypt(text, publicKey));

		return encryptedString;
	}

	// get text to decrypt
	public static String getTextToDecrypt(String textEncrypted) throws InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		String decryptedString = RSAUtil.decrypt(textEncrypted, privateKey);

		return decryptedString;
	}
}
