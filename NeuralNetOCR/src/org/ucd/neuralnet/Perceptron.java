/* Perceptron.java
 * Author: Evan Dempsey <evan.dempsey@ucdconnect.ie>
 * Last Modified: 30/Dec/2012
 */

package org.ucd.neuralnet;

public class Perceptron {

	public double alpha = 0.2;
	public int inputSize;
	public int outputSize;
	public int[] inputLayer;
	public int[] outputLayer;
	public double[] biases;
	public double[][] weights;

	// Constructor
	public Perceptron(int n, int m) {
		inputSize = n;
		outputSize = m;
		inputLayer = new int[inputSize];
		outputLayer = new int[outputSize];
		biases = new double[outputSize];
		weights = new double[inputSize][outputSize];
	}
	
	// Initialize weights and biases with small random values
	private void initializeNetwork() {
		
		// Initialize synaptic weights
		for (int i=0; i<inputSize; i++) {
			for (int j=0; j<outputSize; j++) {
				weights[i][j] = (Math.random() / 5) - 0.1;
			}
		}
		
		// Initialize biases
		for (int i=0; i<outputSize; i++) {
			biases[i] = (Math.random() / 5);
		}
	}

	// Set the learning rate alpha
	public void setLearningRate(double newAlpha) {
		if (newAlpha > 0.0 && newAlpha <= 1.0) {
			alpha = newAlpha;
		}
	}
	
	// Convert array of binary values into array of 1 and -1
	public int[] polarize(int[] values) {
		for(int i=0; i<values.length; i++) {
			if (values[i] == 0)
				values[i] = -1;
		}
		
		return values;
	}

	// Compute the response of the network
	public int[] computeResponse(int[] inputs) {
		
		double[] rawResponses = new double[outputSize];
		int[] outputs = new int[outputSize];
		
		// Compute weighted sum of inputs
		for (int j=0; j<outputSize; j++) {
			rawResponses[j] = biases[j];

			for (int i=0; i<inputSize; i++) {
				rawResponses[j] += inputs[i] * weights[i][j];
			}
		}
		
		for (int j=0; j<outputSize; j++) {
			if (rawResponses[j] >= 0)
				outputs[j] = 1;
			else
				outputs[j] = -1;
		}
		
		// Take highest response as class indicator
		double maxResponse = rawResponses[0];
		int maxIndex = 0;
		for (int j=0; j<outputSize; j++)
			if (rawResponses[j] > maxResponse) {
				maxResponse = rawResponses[j];
				maxIndex = j;
			}
		
		// Build output array
		for (int j=0; j<outputSize; j++) {
			if (j == maxIndex)
				outputs[j] = 1;
			else
				outputs[j] = -1;
		}
		
		return outputs; 
	}
	
	// Train with the provided input-output pairs
	public void train(int[][] trainingData, int examples) {
		
		// Initialize weights and bias
		initializeNetwork();
		
		// Stopping condition: network weights not updated between iterations
		boolean weightsChanged = true;

		while (weightsChanged) {
			weightsChanged = false;
			
			// For each training example
			for (int i=0; i<examples; i++) {
				
				trainingData[i] = polarize(trainingData[i]);
				
				// Get input and expected output
				int[] inputs = new int[inputSize];
				int[] outputs = new int[outputSize];
				
				// Decompose array into inputs and outputs
				for (int j=0; j<inputSize; j++)
					inputs[j] = trainingData[i][j];
				
				for (int j=inputSize; j<(inputSize+outputSize); j++)
					outputs[j-inputSize] = trainingData[i][j];

				// Compute response of network
				int[] response = computeResponse(inputs);
				
				// Check for error and update weights if one occurred
				for (int j=0; j<outputSize; j++) {
					if (outputs[j] != response[j]) {
						weightsChanged = true;
						
						for (int k=0; k<inputSize; k++)
							weights[k][j] += alpha * inputs[k] * outputs[j];

						biases[j] += alpha * outputs[j];
					}
				}
			}
		}
	}
	
	// Test the network's response with provided test pairs
	public boolean test(int[][] testData, int examples) {
		
		for (int i=0; i<examples; i++) {
			
			testData[i] = polarize(testData[i]);
			
			// Get input and expected output
			int[] inputs = new int[inputSize];
			int[] outputs = new int[outputSize];
			
			for (int j=0; j<inputSize; j++)
				inputs[j] = testData[i][j];
			
			for (int j=inputSize; j<(inputSize+outputSize); j++)
				outputs[j-inputSize] = testData[i][j];
			
			// Compute network response
			int[] response = new int[inputSize];
			response = computeResponse(inputs);
			
			for (int j=0; j<outputSize; j++)
				if (response[j] != outputs[j])
					return false;
		}

		return true;
	}
}
