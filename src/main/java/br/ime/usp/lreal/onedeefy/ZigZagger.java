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

/**
 * Converts a 2D image into a 1D one and vice-versa using a zig-zag ordering of
 * the pixels. For instance, the pixels in a 5x5 image will be ordered like:
 *
 * <pre>
 * 1  2  6  7  15
 * 3  5  8  14 16
 * 4  9  13 17 22
 * 10 12 18 21 23
 * 11 19 20 24 25
 * </pre>
 *
 * See <a href="http://en.wikipedia.org/wiki/File:JPEG_ZigZag.svg">this
 * image</a> for a better visualization.
 *
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class ZigZagger implements Linearizer {
	// code adapted from here: http://refactormycode.com/codes/451-zig-zag-ordering-of-array#refactor_15373

	@Override
	public BufferedImage delinearize(BufferedImage image, int w, int h) {
		BufferedImage output = new BufferedImage(w, h, image.getType());
		int x = 1;
		int y = 1;
		for (int t = 0; t < image.getWidth(); t++) {
			output.setRGB(x - 1, y - 1, image.getRGB(t, 0));
			if ((x + y) % 2 == 0) {
				// Even stripes
				if (y < h) {
					y++;
				} else {
					x += 2;
				}
				if (x > 1) {
					x--;
				}
			} else {
				// Odd stripes
				if (x < w) {
					x++;
				} else {
					y += 2;
				}
				if (y > 1) {
					y--;
				}
			}
		}
		return output;
	}

	@Override
	public BufferedImage linearize(BufferedImage image) {
		int h = image.getHeight();
		int w = image.getWidth();
		int newWidth = h * w;
		BufferedImage output = new BufferedImage(newWidth, 1, image.getType());
		int x = 1;
		int y = 1;
		for (int t = 0; t < newWidth; t++) {
			output.setRGB(t, 0, image.getRGB(x - 1, y - 1));
			if ((x + y) % 2 == 0) {
				// Even stripes
				if (y < h) {
					y++;
				} else {
					x += 2;
				}
				if (x > 1) {
					x--;
				}
			} else {
				// Odd stripes
				if (x < w) {
					x++;
				} else {
					y += 2;
				}
				if (y > 1) {
					y--;
				}
			}
		}
		return output;
	}

	@Override
	public String getName() {
		return "zigzag";
	}
}
