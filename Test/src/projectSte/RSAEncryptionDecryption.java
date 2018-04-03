/**
 * 
 */
package projectSte;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * @author Minato
 *
 */
public class RSAEncryptionDecryption {

	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, IOException {
		String minato = "Hello Minato!";

		byte[] data = minato.getBytes();

		byte[] dataEncrypt = rsaEncrypt(data);

		String str = new String(dataEncrypt, StandardCharsets.UTF_8);
		
		System.out.println(str.length());
		
		System.out.println(new String(dataEncrypt, StandardCharsets.UTF_8));

		byte[] dataDecryption = rsaDecryption(dataEncrypt);

		System.out.println(new String(dataDecryption, StandardCharsets.UTF_8));
	}

	private static PublicKey readKeyFromFile(String keyFileName) throws IOException {
		InputStream in = new FileInputStream(keyFileName);
		ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(in));

		try {
			BigInteger m = (BigInteger) oin.readObject();
			BigInteger e = (BigInteger) oin.readObject();

			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			PublicKey pubKey = fact.generatePublic(keySpec);

			return pubKey;

		} catch (Exception e) {
			throw new RuntimeException("Spurious serialisation error", e);
		} finally {
			oin.close();
		}
	}

	private static PrivateKey readPrivateKeyFromFile(String keyFileName) throws IOException {
		InputStream in = new FileInputStream(keyFileName);
		ObjectInputStream oInputStream = new ObjectInputStream(new BufferedInputStream(in));

		try {
			BigInteger m = (BigInteger) oInputStream.readObject();
			BigInteger e = (BigInteger) oInputStream.readObject();

			RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			PrivateKey prKey = fact.generatePrivate(keySpec);

			return prKey;

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			oInputStream.close();
		}
	}

	private static byte[] rsaEncrypt(byte[] data) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

		PublicKey pubKey = readKeyFromFile("C:\\Users\\Minato\\Documents\\JS\\Certificate\\RSA\\ByJava\\public.key");
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		byte[] cipherData = cipher.doFinal(data);

		return cipherData;
	}

	private static byte[] rsaDecryption(byte[] data) throws IOException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

		PrivateKey prKey = readPrivateKeyFromFile(
				"C:\\Users\\Minato\\Documents\\JS\\Certificate\\RSA\\ByJava\\private.key");
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, prKey);
		byte[] cipherData = cipher.doFinal(data);

		return cipherData;
	}
}
