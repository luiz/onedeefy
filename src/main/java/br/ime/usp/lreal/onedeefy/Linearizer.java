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
import java.util.ServiceLoader;

/**
 * <p>
 * This interface describes an algorithm to transform a 2D image into 1D and
 * vice-versa.
 * </p>
 *
 * <p>
 * <b>NOTE</b>: when implementing this class, add also its full qualified name
 * (FQN) to a file that describes the implementers of this interface. This file
 * has the name equal to this interface's FQN and is located inside a
 * <tt>META-INF</tt> folder in the classpath. This ensures that {@link Main}
 * will automatically find the new implementation.
 * </p>
 *
 * @author Luiz Fernando Oliveira Corte Real
 * @see ServiceLoader
 */
public interface Linearizer {

	/**
	 * Transforms a 2D image into 1D.
	 *
	 * @param inputImage
	 *            Image to be transformed
	 * @return An 1D image with the same type of the input image, width equal to
	 *         original's image width times its height, and height equal to one
	 */
	BufferedImage linearize(BufferedImage inputImage);

	/**
	 * Transforms a 1D image into a 2D one with the given dimensions.
	 *
	 * @param inputImage
	 *            1D image to be converted
	 * @param destWidth
	 *            Resulting image width
	 * @param destHeight
	 *            Resulting image height
	 * @return A 2D image with the same type as the input image and the expected
	 *         width and height. Note that the resulting size is not verified to
	 *         be compatible to the input image's width, so an error may occur
	 *         if a wrong size is given.
	 */
	BufferedImage delinearize(BufferedImage inputImage, int destWidth,
			int destHeight);

	/**
	 * @return A simple user-friendly name to identify the algorithm in the
	 *         command line
	 */
	String getName();

}
