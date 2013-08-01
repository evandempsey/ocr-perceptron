/* NeuralNetOCR.java
 * Author: Evan Dempsey
 * Last Modified: 30/Dec/2012
 */

package org.ucd.neuralnet;

import java.io.File;

public class NeuralNetOCR {

	public static void main(String args[]) {
		System.out.println("Neural Net OCR");
		
		// Read the training and test data from the files in the data folder.
		String trainingFile = "data" + File.separator + "ocr-train.txt";
		String testFile = "data" + File.separator + "ocr-test.txt";
		DatasetReader reader = new DatasetReader(trainingFile, testFile);
		
		// Initialize the perceptron and set the learning rate.
		Perceptron perceptron = new Perceptron(63, 7);
		perceptron.setLearningRate(0.2);
		
		// Train and test 1000 times and report the pass rate.
		System.out.println("Testing...");
		int pass = 0;
		for (int i=0; i<1000; i++) {
			perceptron.train(reader.trainingData, 21);
			if (perceptron.test(reader.testData, 3)) {
				pass++;
			}
		}
		
		System.out.println(String.format("Pass percentage: %2.2f %%", pass / 10.0));
	}
}
