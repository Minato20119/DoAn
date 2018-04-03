/**
 * 
 */
package projectSte;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author Minato
 *
 */
public class SentSite {

	private static final String PATH_FILE_IMAGE = "C:\\Users\\Minato\\Pictures\\ss.png";
	private static final String PATH_OUTPUT_NEW_IMAGE = "C:\\Users\\Minato\\Pictures\\Image\\output.png";
	private static final String PATH_FILE_TEXT = "C:\\Users\\Minato\\Pictures\\Image\\text.txt";
	private static final int LIMIT_BIT_TO_CHANGE = 7;
	private static final int NUMBER_BLOCK_CIPHER = 4;
	private static final int NUMBER_BLOCK_CIPHER_INDICATOR = 6;
	private static int increase = 0;
	private static int checkGreenToChange = 0;
	private static BufferedImage image = null;
	private static BufferedReader bufferedReader;

	public static void main(String[] args) {

		String text = inputTextFile();
		String textBinary = convertTextToBinary(text);
		image = inputImage(PATH_FILE_IMAGE);
		getValueRGB(textBinary);
		writeNewImage();
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

	// Read text file
	private static String inputTextFile() {

		FileReader fileReader = null;
		String readLine2 = "";

		try {
			fileReader = new FileReader(PATH_FILE_TEXT);
			bufferedReader = new BufferedReader(fileReader);

			String readLine;
			while ((readLine = bufferedReader.readLine()) != null) {
				readLine2 += readLine;
				System.out.println(readLine);
			}

		} catch (IOException e) {
			System.out.println("Error input text file: " + e);
		} finally {
			try {
				if (bufferedReader != null)
					bufferedReader.close();

				if (fileReader != null)
					fileReader.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}

		return readLine2;
	}

	// Convert text to binary
	private static String convertTextToBinary(String text) {

		byte[] bytes = text.getBytes();
		StringBuilder binary = new StringBuilder();

		for (byte b : bytes) {
			int value = b;

			for (int i = 0; i < 8; i++) {
				binary.append((value & 128) == 0 ? 0 : 1);
				value <<= 1;
			}
		}

		System.out.println("Length: " + binary.length() + ": " + binary);

		String str = binary.toString();

		for (int i = 0; i < str.length(); i++) {

			if ((i % 4 == 0) && (i > 0)) {
				System.out.print(" ");
			}

			System.out.print(str.charAt(i));
		}

		return binary.toString();
	}

	private static void getValueRGB(String cipherText) {

		int height = image.getHeight();
		int width = image.getWidth();

		// System.out.println("Width: " + width);
		// System.out.println("Height: " + height);

		int rgb = 0;

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				rgb = image.getRGB(j, i);

				//
				if (increase >= cipherText.length()) {
					System.out.println("i: " + i + " | j: " + j);
					break;
				}

				// Set new rgb
				int rgbNew = setNewRGB(rgb, cipherText);
				image.setRGB(j, i, rgbNew);
			}

			//
			if (increase >= cipherText.length()) {
				break;
			}
		}
	}

	private static int setNewRGB(int rgb, String cipherText) {

		System.out.println("============= Set New RGB =======================");

		int red = (rgb >> 16) & 0xff;
		int green = (rgb >> 8) & 0xff;
		int blue = rgb & 0xff;

		// convert to binary
		String redBinary = convertDecimalToBinary(red);
		String greenBinary = convertDecimalToBinary(green);
		String blueBinary = convertDecimalToBinary(blue);

		System.out.println("RedBinary  : " + redBinary);
		System.out.println("GreenBinary: " + greenBinary);
		System.out.println("BlueBinary : " + blueBinary);

		System.out.println("============= Red: add CipherToBit ==============");
		int redNew = addCipherToBit(redBinary, cipherText);
		System.out.println("============= changeRed =========================");
		int redChanged = changeRed(red, redNew);

		System.out.println("============= Blue: add CipherToBit =============");
		int blueNew = addCipherToBit(blueBinary, cipherText);
		System.out.println("============= changeBlue ========================");
		int blueChanged = changeBlue(blue, blueNew);

		System.out.println("============= changeGreen =======================");
		int greenChanged = changeGreen(greenBinary, red, redNew, blue, blueNew);

		// Reset check green to change
		checkGreenToChange = 0;

		System.out.println();

		rgb = (redChanged << 16) | (greenChanged << 8) | blueChanged;

		return rgb;
	}

	// Write new image
	private static void writeNewImage() {
		try {
			File file = new File(PATH_OUTPUT_NEW_IMAGE);
			ImageIO.write(image, "png", file);
		} catch (IOException e) {
			System.out.println("Error write image: " + e);
		}
	}

	// Add cipher to red or blue
	private static int addCipherToBit(String rgbBinary, String cipherText) {

		if (increase + NUMBER_BLOCK_CIPHER > cipherText.length()) {

			// No embed (270 - 255 = 15 > 7)
			return 270;
		}

		String getCipher = cipherText.substring(increase, increase + NUMBER_BLOCK_CIPHER);

		System.out.println("Cipher: " + getCipher);

		String rgbChanged = rgbBinary.substring(0, NUMBER_BLOCK_CIPHER) + getCipher;

		System.out.println("rgbChanged: " + rgbChanged);

		int rgbNew = convertBinaryToDecimal(rgbChanged);

		return rgbNew;
	}

	// Add cipher to indicator
	private static int changeGreen(String greenBinary, int red, int redNew, int blue, int blueNew) {

		// 00
		if (checkGreenToChange == 1) {
			System.out.println("GreenChanged: " + greenBinary.substring(0, NUMBER_BLOCK_CIPHER_INDICATOR) + "00");
			return convertBinaryToDecimal(greenBinary.substring(0, NUMBER_BLOCK_CIPHER_INDICATOR) + "00");
		}

		// 01
		if (checkGreenToChange == 2) {
			System.out.println("GreenChanged: " + greenBinary.substring(0, NUMBER_BLOCK_CIPHER_INDICATOR) + "01");
			return convertBinaryToDecimal(greenBinary.substring(0, NUMBER_BLOCK_CIPHER_INDICATOR) + "01");
		}

		// 10
		if (checkGreenToChange == 3) {
			System.out.println("GreenChanged: " + greenBinary.substring(0, NUMBER_BLOCK_CIPHER_INDICATOR) + "10");
			return convertBinaryToDecimal(greenBinary.substring(0, NUMBER_BLOCK_CIPHER_INDICATOR) + "10");
		}

		// 11
		System.out.println("GreenChanged: " + greenBinary.substring(0, NUMBER_BLOCK_CIPHER_INDICATOR) + "11");
		return convertBinaryToDecimal(greenBinary.substring(0, NUMBER_BLOCK_CIPHER_INDICATOR) + "11");
	}

	// Get binary
	private static String convertDecimalToBinary(int value) {

		String binaryString = "0000000" + Integer.toBinaryString(value);
		binaryString = binaryString.substring(binaryString.length() - 8);

		return binaryString;
	}

	// Convert binary to decimal
	private static int convertBinaryToDecimal(String binary) {

		int decimal = Integer.parseInt(binary, 2);

		return decimal;
	}

	// Check RGB to change
	private static int changeRed(int red, int redNew) {

		System.out.println("Red: " + red + " RedNew: " + redNew + " Result: " + Math.abs(red - redNew));

		if (Math.abs(red - redNew) > LIMIT_BIT_TO_CHANGE) {
			System.out.println("No Embed");
			return red;
		}

		increase = increase + NUMBER_BLOCK_CIPHER;

		checkGreenToChange++;

		System.out.println("Embed");
		System.out.println("increase: " + increase);

		return redNew;
	}

	private static int changeBlue(int blue, int blueNew) {

		System.out.println("Blue: " + blue + " BlueNew: " + blueNew + " Result: " + Math.abs(blue - blueNew));

		if (Math.abs(blue - blueNew) > LIMIT_BIT_TO_CHANGE) {
			System.out.println("No Embed");
			return blue;
		}

		increase = increase + NUMBER_BLOCK_CIPHER;

		checkGreenToChange = checkGreenToChange + 2;

		System.out.println("Embed");
		System.out.println("increase: " + increase);

		return blueNew;
	}

}
