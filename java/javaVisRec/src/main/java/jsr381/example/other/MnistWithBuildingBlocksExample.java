package jsr381.example.other;

import jsr381.example.image_recognition.CatDogRecognition;
import jsr381.example.util.DataSetExamples;

import javax.imageio.ImageIO;
import javax.visrec.ml.classification.ImageClassifier;
import javax.visrec.ml.classification.NeuralNetImageClassifier;
import javax.visrec.ml.model.ModelCreationException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

/**
 * Hand written digit recognition using MNIST data set - image classification hello world.
 *
 * @author Zoran Sevarac <zoran.sevarac@deepnetts.com>
 */
public class MnistWithBuildingBlocksExample {
	
	public static void main(String[] args) throws IOException, ModelCreationException, URISyntaxException {
		// Download the dataset and calculate how much time it took
		long start = System.currentTimeMillis();
		DataSetExamples.ExampleDataSet dataSet = DataSetExamples.getMnistDataSet();
		System.out.printf(
				"Took %d milliseconds to download and/or unzip the MNIST dataset%n",
				System.currentTimeMillis() - start);
		
		// Configuration to train the model
		ImageClassifier<BufferedImage> classifier = NeuralNetImageClassifier.builder()
				.inputClass(BufferedImage.class)
				.imageHeight(28)
				.imageWidth(28)
				.labelsFile(dataSet.getLabelsFile().toPath())
				.trainingFile(dataSet.getTrainingFile().toPath())
				.networkArchitecture(Path.of(Objects.requireNonNull(CatDogRecognition.class.getClassLoader()
								.getResource("mnist_arch.json"))
						.toURI())) //change this not in data set but from jsin file in resources folder?
				.exportModel(Paths.get("mnist.dnet"))
				.maxError(0.05f)
				.maxEpochs(3)
				.learningRate(0.01f)
				.build();
		
		// Get input imgae from resources and use the classifier.
		URL input = MnistWithBuildingBlocksExample.class.getClassLoader().getResource("00060.png");
		if (input == null) {
			throw new IOException("Input file not found in resources");
		}
		
		BufferedImage image = ImageIO.read(new File(input.getFile()));
		Map<String, Float> results = classifier.classify(image);
		
		// Print the outcome
		System.out.println(results);
	}
}
