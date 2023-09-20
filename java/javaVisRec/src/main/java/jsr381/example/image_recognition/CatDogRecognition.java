package jsr381.example.image_recognition;

import jsr381.example.util.DataSetExamples;

import javax.imageio.ImageIO;
import javax.visrec.ml.classification.ImageClassifier;
import javax.visrec.ml.classification.NeuralNetImageClassifier;
import javax.visrec.ml.model.ModelCreationException;
import javax.visrec.spi.ServiceProvider;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

import static jsr381.example.util.DataSetExamples.downloadZip;

/**
 * @author Nishant Raut
 * modified by Christoph Ahlers
 */
public class CatDogRecognition {
	/*
		https://github.com/JavaVisRec/visrec-api/wiki/Getting-Started-Guide#Overview
	 */
	public static void main(String[] args) throws IOException, ModelCreationException, URISyntaxException {
		System.out.println("VisRec API (JSR 381) implementation: " + getImplementationString());
		
		Path modelFile = Paths.get("java\\javaVisRec\\target\\output\\catdog.dnet");
		System.out.println("model file path: " + modelFile.toAbsolutePath());
		
		trainModel(modelFile);
		
		applyModel(modelFile,"cat_leila.png", "cat_36.png", "dog_23.png");
	}
	
	private static String getImplementationString() {
		return ServiceProvider.current().getImplementationService().toString();
	}
	
	private static ImageClassifier<BufferedImage> trainModel(Path modelFile)
			throws IOException, ModelCreationException, URISyntaxException {
		
		// Download the dataset and calculate how much time it took
		long start = System.currentTimeMillis();
		DataSetExamples.ExampleDataSet dataSet = getCatDogDataSet();
		System.out.printf("Took %d milliseconds to download and/or unzip the CatDog dataset%n",
				System.currentTimeMillis() - start);
		
		// Configure model output
		createDirectories(modelFile);
		
		// Train the model
		ImageClassifier<BufferedImage> classifier = NeuralNetImageClassifier.builder()
				.inputClass(BufferedImage.class)
				.imageHeight(128)
				.imageWidth(128)
				.labelsFile(dataSet.getLabelsFile().toPath())
				.trainingFile(dataSet.getTrainingFile().toPath())
				// describes the network architecture
				.networkArchitecture(Path.of(Objects.requireNonNull(CatDogRecognition.class.getClassLoader()
						.getResource("catdog_arch.json")).toURI()))
				//.importModel(modelFile) // loading a model for more training not supported?
				.exportModel(modelFile)
				.maxError(0.03f) // stops training at that error rate
				.maxEpochs(10) // maximum amount of training iterations
				.learningRate(0.01f) // how fast it learns (does not necessarily improve accuracy)
				.build();
		
		System.out.printf("Model output file: %s, size: %s kb%n",
				modelFile.getFileName(),
				Files.size(modelFile) / 1024);
		
		return classifier;
	}
	
	private static void applyModel(Path modelFile, String... resourceFileNames)
			throws IOException, ModelCreationException {

		ImageClassifier<BufferedImage> classifier = NeuralNetImageClassifier.builder()
				.inputClass(BufferedImage.class)
				.imageHeight(128)
				.imageWidth(128)
				.importModel(modelFile)
				.build();
		
		// Use the model with another image to rate it:
		for (var resourceFileName : resourceFileNames) {
			URL input = CatDogRecognition.class.getClassLoader().getResource(resourceFileName);
			if (input == null) {
				throw new IOException("Input file not found in resources");
			}
			BufferedImage image = ImageIO.read(new File(input.getFile()));
			
			Map<String, Float> results = classifier.classify(image);
			
			System.out.printf("result for %s: %s%n", input.getFile(), results);
		}
	}
	
	public static DataSetExamples.ExampleDataSet getCatDogDataSet() throws IOException {
		Path mnistPath = Paths.get("java", "javaVisRec", "target", "datasets", "cats_and_dogs");
		File mnistFolder = mnistPath.toFile();
		System.out.printf("Downloading and/or unpacking cat_and_dog training set to: %s - this may take a while!%n",
				mnistFolder.getAbsolutePath());
		if (!mnistFolder.exists() && !mnistFolder.mkdirs()) {
			throw new IOException("Couldn't create temporary directories to download the cat_and_dog training dataset.");
		}
		
		// only download once
		Path zipPath = Paths.get(mnistPath.toString(), "dataset.zip");
		if (!Files.exists(zipPath)) {
			downloadZip(
					"https://github.com/JavaVisRec/jsr381-examples-datasets/raw/master/cats_and_dogs_training_data_png.zip",
					zipPath);
		}
		
		File trainingIndexFile = new File(Paths.get(mnistFolder.getAbsolutePath(), "training").toFile(), "train.txt");
		if (!trainingIndexFile.exists()) {
			throw new FileNotFoundException(trainingIndexFile + " not properly downloaded");
		}
		
		File trainingLabelsFile = new File(Paths.get(mnistFolder.getAbsolutePath(), "training").toFile(), "labels.txt");
		if (!trainingLabelsFile.exists()) {
			throw new FileNotFoundException(trainingLabelsFile + " not properly downloaded");
		}
		
		URL archUrl = DataSetExamples.class.getClassLoader().getResource("catdog_arch.json");
		if (archUrl == null) {
			throw new FileNotFoundException("Architecture file not found");
		}
		File architectureFile = new File(archUrl.getFile());
		if (!architectureFile.exists()) {
			throw new FileNotFoundException(architectureFile + " does not exist");
		}
		
		return new DataSetExamples.ExampleDataSet().setLabelsFile(trainingLabelsFile)
				.setTrainingFile(trainingIndexFile);
	}
	
	private static void createDirectories(Path modelOutput) throws IOException {
		Path modelOutputDir = modelOutput.getParent();
		if (!Files.exists(modelOutputDir)) {
			Files.createDirectory(modelOutputDir);
		}
	}
	
}
