/*
Copyright 2012 Luiz Fernando Oliveira Corte Real

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package br.ime.usp.lreal.onedeefy;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

/**
 * Converts a 2D image into a 1D one and vice-versa
 *
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class Spiralizer {

	/**
	 * Runs the conversion given command-line arguments.
	 *
	 * The command-line may be either:
	 *
	 * <pre>
	 * &lt;input image&gt; &lt;output image&gt;
	 * </pre>
	 *
	 * in which case the input image will be transformed into 1D, or
	 *
	 * <pre>
	 * &lt;input image&gt; &lt;output width&gt; &lt;output height&gt; &lt;output image&gt;
	 * </pre>
	 *
	 * in which case the input image will be transformed into 2D
	 *
	 * @param args
	 *            The command-line arguments
	 * @throws IOException
	 *             If one of the files cannot be read or written
	 */
	public static void main(String[] args) throws IOException {
		if (args.length == 2) {
			BufferedImage image = ImageIO.read(new File(args[0]));
			BufferedImage linearized = despiralize(image);
			ImageIO.write(linearized, extensionOf(args[1]), new File(args[1]));
		} else if (args.length == 4) {
			BufferedImage image = ImageIO.read(new File(args[0]));
			BufferedImage spiralized = spiralize(image, Integer.parseInt(args[1]), Integer.parseInt(args[2]));
			ImageIO.write(spiralized, extensionOf(args[3]), new File(args[3]));
		} else {
			System.out.println("Usage:");
			System.out.println("\t<input image> <output image> - to encode the input image into 1D");
			System.out.println("\t<input image> <output width> <output height> <output image> - to encode the input image into 2D");
		}
	}

	private static String extensionOf(String fileName) {
		return fileName.substring(fileName.lastIndexOf('.') + 1);
	}

	/**
	 * Transforms a 1D image into a 2D one by folding it in a spiral fashion
	 *
	 * @param image
	 *            1D image to be converted
	 * @param w
	 *            Resulting image width
	 * @param h
	 *            Resulting image height
	 * @return A 2D image with the same type as the input image and the expected
	 *         width and height. Note that the resulting size is not verified to
	 *         be compatible to the input image's width, so an error may occur
	 *         if a wrong size is given.
	 */
	public static BufferedImage spiralize(BufferedImage image, int w, int h) {
		int curPos = 0;
		int x = 0;
		int y = 0;
		int dx = 1;
		int dy = 0;
		boolean[][] wrote = new boolean[h][w];
		for (int i = 0; i < h; i++) {
			Arrays.fill(wrote[i], false);
		}
		BufferedImage output = new BufferedImage(w, h, image.getType());
		while (curPos < image.getWidth()) {
			output.setRGB(x, y, image.getRGB(curPos++, 0));
			wrote[y][x] = true;
			if (x + dx == w || x + dx < 0 || y + dy == h || y + dy < 0 || wrote[y + dy][x + dx]) {
				int newDy = dx;
				dx = -dy;
				dy = newDy;
			}
			x += dx;
			y += dy;
		}
		return output;
	}

	/**
	 * Transforms a 2D image into a 1D image by taking pixels from the 2D image
	 * in a spiral sequence
	 *
	 * @param image
	 *            Image to be transformed
	 * @return An 1D image with the same type of the input image, width equal to
	 *         original's image width times its height, and height equal to one
	 */
	public static BufferedImage despiralize(BufferedImage image) {
		int h = image.getHeight();
		int w = image.getWidth();
		int newWidth = h * w;
		int newPos = 0;
		int x = 0;
		int y = 0;
		int dx = 1;
		int dy = 0;
		boolean[][] read = new boolean[h][w];
		for (int i = 0; i < h; i++) {
			Arrays.fill(read[i], false);
		}
		BufferedImage output = new BufferedImage(newWidth, 1, image.getType());
		while (x < w && y < h && !read[y][x]) {
			output.setRGB(newPos++, 0, image.getRGB(x, y));
			read[y][x] = true;
			if (x + dx == w || x + dx < 0 || y + dy == h || y + dy < 0 || read[y + dy][x + dx]) {
				int newDy = dx;
				dx = -dy;
				dy = newDy;
			}
			x += dx;
			y += dy;
		}
		return output;
	}

}
