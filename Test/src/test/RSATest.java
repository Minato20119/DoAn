/**
 * 
 */
package test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.cert.Certificate;
import java.util.Base64;

import javax.crypto.Cipher;

/**
 * @author Minato
 *
 */
public class RSATest {

	// Generating key pairs
	public static KeyPair generateKeyPair() throws Exception {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048, new SecureRandom());
		KeyPair pair = generator.generateKeyPair();

		return pair;
	}

	// Encryption
	public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
		Cipher encryptCipher = Cipher.getInstance("RSA");
		encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

		byte[] cipherText = encryptCipher.doFinal(plainText.getBytes("UTF-8"));

		return Base64.getEncoder().encodeToString(cipherText);
	}

	// Decryption
	public static String decrypt(String cipherText, PrivateKey privateKey) throws Exception {
		byte[] bytes = Base64.getDecoder().decode(cipherText);

		Cipher decriptCipher = Cipher.getInstance("RSA");
		decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

		return new String(decriptCipher.doFinal(bytes), "UTF-8");
	}

	// Sign
	public static String sign(String plainText, PrivateKey privateKey) throws Exception {
		Signature privateSignature = Signature.getInstance("SHA256withRSA");
		privateSignature.initSign(privateKey);
		privateSignature.update(plainText.getBytes("UTF-8"));

		byte[] signature = privateSignature.sign();

		return Base64.getEncoder().encodeToString(signature);
	}

	// Verify
	public static boolean verify(String plainText, String signature, PublicKey publicKey) throws Exception {
		Signature publicSignature = Signature.getInstance("SHA256withRSA");
		publicSignature.initVerify(publicKey);
		publicSignature.update(plainText.getBytes("UTF-8"));

		byte[] signatureBytes = Base64.getDecoder().decode(signature);

		return publicSignature.verify(signatureBytes);
	}

	// Get key pair form keystore
	public static KeyPair getKeyPairFromKeyStore() throws Exception {
		InputStream ins = new FileInputStream("C:\\Users\\Minato\\Pictures\\Image\\MyServer.jks");

		KeyStore keyStore = KeyStore.getInstance("JCEKS");
		keyStore.load(ins, "changeit".toCharArray()); // Keystore password
		KeyStore.PasswordProtection keyPassword = // Key password
				new KeyStore.PasswordProtection("changeit".toCharArray());

		KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry("MyServer",
				keyPassword);

		Certificate cert = keyStore.getCertificate("MyServer");
		PublicKey publicKey = cert.getPublicKey();
		System.out.println("Public key: " + publicKey);
		PrivateKey privateKey = privateKeyEntry.getPrivateKey();
		System.out.println("Private key: " + privateKey);
		return new KeyPair(publicKey, privateKey);
	}

	public static void main(String[] args) throws Exception {
		// // First generate a public/private key pair
		// KeyPair pair = generateKeyPair();
		//
		// String signature = sign("foobar", pair.getPrivate());
		//
		// // Let's check the signature
		// boolean isCorrect = verify("foobar", signature, pair.getPublic());
		// System.out.println("Signature correct: " + isCorrect);
		//
		// // Our secret message
		// String message = "the answer to life the universe and everything";
		//
		// // Encrypt the message
		// String cipherText = encrypt(message, pair.getPublic());
		//
		// System.out.println("Public key: " + pair.getPublic());
		// System.out.println("Private key: " + pair.getPrivate().toString());
		//
		// System.out.println("Cipher text: " + cipherText);
		//
		// // Now decrypt it
		// String decipheredMessage = decrypt(cipherText, pair.getPrivate());
		//
		// System.out.println(decipheredMessage);

		KeyPair pair = getKeyPairFromKeyStore();

		String signature = sign("Minato", pair.getPrivate());

		// Let's check the signature
		boolean isCorrect = verify("Minato", signature, pair.getPublic());
		System.out.println("Signature correct: " + isCorrect);

	}
}
