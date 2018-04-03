/**
 * 
 */
package projectSte;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

/**
 * @author Minato
 *
 */
public class RSAKeyPairGenerator {

	private PrivateKey privateKey;
	private PublicKey publicKey;

	public RSAKeyPairGenerator() throws NoSuchAlgorithmException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		keyGen.initialize(1024);

		KeyPair keyPair = keyGen.generateKeyPair();
		this.privateKey = keyPair.getPrivate();
		this.publicKey = keyPair.getPublic();
	}

	private void writeTofile(String path, byte[] key) throws IOException {
		File f = new File(path);
		f.getParentFile().mkdirs();

		FileOutputStream fos = new FileOutputStream(f);
		fos.write(key);
		fos.flush();
		fos.close();
	}

	private PublicKey getPublicKey() {
		return publicKey;
	}

	private PrivateKey getPrivateKey() {
		return privateKey;
	}

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		RSAKeyPairGenerator keyPairGenerator = new RSAKeyPairGenerator();

		keyPairGenerator.writeTofile("C:\\Users\\Minato\\Documents\\JS\\Certificate\\RSA\\ByJava\\public1.key",
				keyPairGenerator.getPublicKey().getEncoded());
		keyPairGenerator.writeTofile("C:\\Users\\Minato\\Documents\\JS\\Certificate\\RSA\\ByJava\\private1.key",
				keyPairGenerator.getPrivateKey().getEncoded());

		System.out.println("Public key: " + Base64.getEncoder().encodeToString(keyPairGenerator.getPublicKey().getEncoded()));
		System.out.println(
				"Private key: " + Base64.getEncoder().encodeToString(keyPairGenerator.getPrivateKey().getEncoded()));
	}
}
