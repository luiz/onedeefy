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
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import javax.imageio.ImageIO;

/**
 * Command-line interface for all of the algorithms implemented
 *
 * @author Luiz Fernando Oliveira Corte Real
 */
public final class Main {
	private static final Map<String, Linearizer> algorithms = new HashMap<String, Linearizer>();

	static {
		ServiceLoader<Linearizer> linearizers = ServiceLoader.load(Linearizer.class);
		for (Linearizer linearizer : linearizers) {
			algorithms.put(linearizer.getName(), linearizer);
		}
	}

	/**
	 * Runs the conversion given command-line arguments.
	 *
	 * The command-line may be either:
	 *
	 * <pre>
	 * &lt;algorithm&gt; &lt;input image&gt; &lt;output image&gt;
	 * </pre>
	 *
	 * in which case the input image will be transformed into 1D using the
	 * selected algorithm, or
	 *
	 * <pre>
	 * &lt;algorithm&gt; &lt;input image&gt; &lt;output width&gt; &lt;output height&gt; &lt;output image&gt;
	 * </pre>
	 *
	 * in which case the input image will be transformed into 2D.
	 *
	 * If the number of arguments is incorrect, a help message will be printed
	 * to the standard output. This way, the user can get more help by simply
	 * invoking the program without arguments, for example.
	 *
	 * @param args
	 *            The command-line arguments
	 * @throws IOException
	 *             If one of the files cannot be read or written
	 */
	public static void main(String[] args) throws IOException {
		if (args.length > 2) {
			String algorithmName = args[0];
			Linearizer algorithm = algorithms.get(algorithmName);
			BufferedImage inputImage = ImageIO.read(new File(args[1]));
			if (algorithm == null) {
				System.out.println("Invalid algorithm: " + algorithmName);
				printUsage();
			} else if (args.length == 3) {
				writeOutput(args[2], algorithm.linearize(inputImage));
			} else if (args.length == 5) {
				int destWidth = Integer.parseInt(args[2]);
				int destHeight = Integer.parseInt(args[3]);
				String destFileName = args[4];
				BufferedImage delinearized = algorithm.delinearize(inputImage, destWidth, destHeight);
				writeOutput(destFileName, delinearized);
			} else {
				printUsage();
			}
		} else {
			printUsage();
		}
	}

	private static void writeOutput(String destFileName, BufferedImage linearized) throws IOException {
		ImageIO.write(linearized, extensionOf(destFileName), new File(destFileName));
	}

	private static void printUsage() {
		System.out.println("Usage:");
		System.out.println("\t<algorithm> <input image> <output image> - to encode the input image into 1D");
		System.out.println("\t<algorithm> <input image> <output width> <output height> <output image> - to encode the input image into 2D");
		System.out.println("Available algorithms:");
		for (String algorithmName : algorithms.keySet()) {
			System.out.println("\t" + algorithmName);
		}
	}

	private static String extensionOf(String fileName) {
		return fileName.substring(fileName.lastIndexOf('.') + 1);
	}
}
