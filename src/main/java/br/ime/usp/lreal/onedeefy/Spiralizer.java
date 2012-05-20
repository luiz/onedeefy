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
import java.util.Arrays;

/**
 * Converts a 2D image into a 1D one and vice-versa using a spiral ordering of
 * the pixels. For instance, the pixels in a 5x5 image will be ordered like:
 *
 * <pre>
 * 1  2  3  4  5
 * 16 17 18 19 6
 * 15 24 25 20 7
 * 14 23 22 21 8
 * 13 12 11 10 9
 * </pre>
 *
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class Spiralizer implements Linearizer {

	@Override
	public BufferedImage delinearize(BufferedImage image, int w, int h) {
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

	@Override
	public BufferedImage linearize(BufferedImage image) {
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
