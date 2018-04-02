/**
 * 
 */
package doAn;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author Minato
 *
 */
public class SentSite3 {
	public static void main(String args[]) throws IOException {
		BufferedImage img = null;
		File f = null;

		// read image
		try {
			f = new File("C:\\Users\\Minato\\Pictures\\222.jpg");
			img = ImageIO.read(f);
		} catch (IOException e) {
			System.out.println(e);
		}

		// get image width and height
		int width = img.getWidth();
		int height = img.getHeight();

		System.out.println("Width: " + width);
		System.out.println("Height: " + height);

		/**
		 * Since, Sample.jpg is a single pixel image so, we will not be using the width
		 * and height variable in this project.
		 */

		// get pixel value
		int p = img.getRGB(0, 0);
		
		System.out.println("P: " + p);

		// get alpha
		int a = (p >> 24) & 0xff;

		// get red
		int r = (p >> 16) & 0xff;

		// get green
		int g = (p >> 8) & 0xff;

		// get blue
		int b = p & 0xff;

		System.out.println("Red: " + r + " Green: " + g + " Blue: " + b + " Alpha: " + a);

		/**
		 * to keep the project simple we will set the ARGB value to 255, 100, 150 and
		 * 200 respectively.
		 */
		a = 255;
		r = 0;
		g = 255;
		b = 0;

		System.out.println("Red: " + r + " Green: " + g + " Blue: " + b + " Alpha: " + a);

		// set the pixel value
		p = (r << 16) | (g << 8) | b;
		img.setRGB(0, 0, p);


		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (i == j) {
					r = 0;
					g = 255;
					b = 0;
					p = (r << 16) | (g << 8) | b;
					img.setRGB(j, i, p);
				}
				
				if (j == width - i) {
					r = 255;
					g = 0;
					b = 255;
					p = (r << 16) | (g << 8) | b;
					img.setRGB(j, i, p);
				}
			}
			r = 0;
			g = 0;
			b = 255;
			p = (r << 16) | (g << 8) | b;
			img.setRGB(height - i, i, p);
		}

		// write image
		try {
			File ff = new File("C:\\Users\\Minato\\Pictures\\Image\\Output.png");
			ImageIO.write(img, "png", ff);
		} catch (IOException e) {
			System.out.println(e);
		}

		System.out.println("Done");
	}
}
