/**
 * 
 */
package doAn;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * @author Minato
 *
 */
public class SentSite {

	BufferedImage image;
	int width;
	int height;

	public SentSite() {
		try {
			File input = new File("C:\\Users\\Minato\\Pictures\\Test.png");
			image = ImageIO.read(input);
			width = image.getWidth();
			height = image.getHeight();

			int count = 0;

			for (int i = 0; i < 3; i++) {

				for (int j = 0; j < 3; j++) {

					count++;
					Color c = new Color(image.getRGB(j, i));
					System.out.println("S.No: " + count + " Red: " + c.getRed() + "  Green: " + c.getGreen() + " Blue: "
							+ c.getBlue());
					
					int color = image.getRGB(j, i);
					
					System.out.println(color);
					
					int blue = color & 0xff;
					int green = (color & 0xff00) >> 8;
					int red = (color & 0xff0000) >> 16;
					
					System.out.println("S.No: " + count + " Red: " + red + " Green: " + green + " Blue: " + blue);
				}
			}

		} catch (Exception e) {
			System.out.println("Error read image!");
		}
	}

	static public void main(String args[]) throws Exception {
		SentSite obj = new SentSite();
		System.out.println("Done");
	}
}
