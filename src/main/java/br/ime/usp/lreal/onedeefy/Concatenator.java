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
 * Converts a 2D image into a 1D one and vice-versa by concatenating all the
 * lines of the image. For instance, the pixels in a 5x5 image will be ordered
 * like:
 *
 * <pre>
 * 1  2  3  4  5
 * 6  7  8  9  10
 * 11 12 13 14 15
 * 16 17 18 19 20
 * 21 22 23 24 25
 * </pre>
 *
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class Concatenator implements Linearizer {

	@Override
	public BufferedImage linearize(BufferedImage image) {
		int h = image.getHeight();
		int w = image.getWidth();
		int newWidth = h * w;
		BufferedImage output = new BufferedImage(newWidth, 1, image.getType());
		int x = 0;
		int y = 0;
		for (int t = 0; t < newWidth; t++) {
			output.setRGB(t, 0, image.getRGB(x, y));
			x++;
			if (x == w) {
				x = 0;
				y++;
			}
		}
		return output;
	}

	@Override
	public BufferedImage delinearize(BufferedImage image, int w, int h) {
		BufferedImage output = new BufferedImage(w, h, image.getType());
		int x = 0;
		int y = 0;
		for (int t = 0; t < image.getWidth(); t++) {
			output.setRGB(x, y, image.getRGB(t, 0));
			x++;
			if (x == w) {
				x = 0;
				y++;
			}
		}
		return output;
	}

	@Override
	public String getName() {
		return "lines";
	}

}
