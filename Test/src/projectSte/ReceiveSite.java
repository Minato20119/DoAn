/**
 * 
 */
package projectSte;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;

/**
 * @author Minato
 *
 */
public class ReceiveSite {

	private static String BINARY_TEXT = "";
	private static final int NUMBER_BLOK_CIPHER_INDICATOR = 6;
	private static final int NUMBER_BLOK_CIPHER = 4;
	private static final String PATH_FILE_IMAGE_STEGO = "C:\\Users\\Minato\\Pictures\\Image\\output.png";

	public static void main(String[] args) {

		BufferedImage image = inputImage(PATH_FILE_IMAGE_STEGO);
		getValueRGB(image);

		System.out.println("BINARY_TEXT: " + BINARY_TEXT);

		String textEncrypted = "";

		for (int i = 0; i <= BINARY_TEXT.length(); i += 8) {
			String word = "";

			if (i > 0) {
				word = BINARY_TEXT.substring(i - 8, i);
				int charCode = convertBinaryToDecimal(word);
				textEncrypted += new Character((char) charCode).toString();
			}
		}

		System.out.println();

		// System.out.println("Text Encrypted from image: " + textEncrypted);

		textEncrypted = textEncrypted.substring(0, textEncrypted.indexOf("="));

		System.out.println("Text encryped: " + textEncrypted);

		String textDecrypted = "";
		try {
			textDecrypted = RSAUtil.getTextToDecrypt(textEncrypted);
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException
				| NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println();
		System.out.println("Text decrypted: " + textDecrypted);
	}

	// Convert binary to decimal
	private static int convertBinaryToDecimal(String binary) {

		int decimal = Integer.parseInt(binary, 2);

		return decimal;
	}

	private static BufferedImage inputImage(String pathFileImage) {
		BufferedImage image = null;
		File file = null;
		try {

			file = new File(pathFileImage);
			image = ImageIO.read(file);

		} catch (IOException e) {
			System.out.println("Error input image: " + e);
		}
		return image;
	}

	private static void getValueRGB(BufferedImage image) {

		int height = image.getHeight();
		int width = image.getWidth();

		// System.out.println("Width: " + width);
		// System.out.println("Height: " + height);

		int rgb = 0;
		String textEncrypted = "";

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				rgb = image.getRGB(j, i);

				// if (BINARY_TEXT.length() == 1500) {
				// break;
				// }

				String word = "";
				
				if (BINARY_TEXT.length() >= 8) {
					word = BINARY_TEXT.substring(BINARY_TEXT.length() - 8, BINARY_TEXT.length());
					int charCode = convertBinaryToDecimal(word);
					textEncrypted += new Character((char) charCode).toString();
				}
				
				if (textEncrypted.contains("=")) {
					break;
				}

				getCipher(rgb);
			}

			// if (BINARY_TEXT.length() == 1500) {
			// break;
			// }

			if (textEncrypted.contains("=")) {
				break;
			}
		}
	}

	private static void getCipher(int rgb) {

		int red = (rgb >> 16) & 0xff;
		int green = (rgb >> 8) & 0xff;
		int blue = rgb & 0xff;

		// convert to binary
		String redBinary = convertDecimalToBinary(red);
		String greenBinary = convertDecimalToBinary(green);
		String blueBinary = convertDecimalToBinary(blue);

		// System.out.println("Red: " + redBinary);
		// System.out.println("Green: " + greenBinary);
		// System.out.println("Blue: " + blueBinary);

		// Check indicator with binary "00"
		if (greenBinary.substring(NUMBER_BLOK_CIPHER_INDICATOR, greenBinary.length()).equals("00")) {
			BINARY_TEXT = BINARY_TEXT + (redBinary.substring(NUMBER_BLOK_CIPHER, redBinary.length()));
			// System.out.println("Append red: " + redBinary.substring(NUMBER_BLOK_CIPHER,
			// redBinary.length()));
		}

		// Check indicator with binary "01"
		if (greenBinary.substring(NUMBER_BLOK_CIPHER_INDICATOR, greenBinary.length()).equals("01")) {
			BINARY_TEXT = BINARY_TEXT + (blueBinary.substring(NUMBER_BLOK_CIPHER, blueBinary.length()));
			// System.out.println("Append blue: " + blueBinary.substring(NUMBER_BLOK_CIPHER,
			// blueBinary.length()));
		}

		// Check indicator with binary "10"
		if (greenBinary.substring(NUMBER_BLOK_CIPHER_INDICATOR, greenBinary.length()).equals("10")) {

			BINARY_TEXT = BINARY_TEXT + (redBinary.substring(NUMBER_BLOK_CIPHER, redBinary.length()));
			BINARY_TEXT = BINARY_TEXT + (blueBinary.substring(NUMBER_BLOK_CIPHER, blueBinary.length()));
			// System.out.println("Append red and blue: " +
			// redBinary.substring(NUMBER_BLOK_CIPHER, redBinary.length())
			// + " " + blueBinary.substring(NUMBER_BLOK_CIPHER, blueBinary.length()));
		}

		// Check indicator with binary "11"
		if (greenBinary.substring(NUMBER_BLOK_CIPHER_INDICATOR, greenBinary.length()).equals("11")) {
			// System.out.println("No append.");
		}
	}

	// Get binary
	private static String convertDecimalToBinary(int value) {

		String binaryString = "0000000" + Integer.toBinaryString(value);
		binaryString = binaryString.substring(binaryString.length() - 8);

		return binaryString;
	}
}
