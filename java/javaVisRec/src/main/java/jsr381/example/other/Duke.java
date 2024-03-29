package jsr381.example.other;


import javax.imageio.ImageIO;
import javax.visrec.ml.classification.ImageClassifier;
import javax.visrec.ml.classification.NeuralNetImageClassifier;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import javax.visrec.ml.model.ModelCreationException;

/**
 * Hand written digit recognition using MNIST data set - image classification hello world.
 *
 * @author Zoran Sevarac <zoran.sevarac@deepnetts.com>
 */
public class Duke {

    public static void main(String[] args) throws IOException, ModelCreationException {
        
        // Configure and train ML model to classify images
        ImageClassifier<BufferedImage> classifier = NeuralNetImageClassifier.builder()
                .inputClass(BufferedImage.class)
                .imageHeight(64)
                .imageWidth(64)
                .labelsFile(Paths.get("datasets/duke_and_nonduke/labels.txt")) // category labels
                .trainingFile(Paths.get("datasets/duke_and_nonduke/index.txt")) // list of images
                .networkArchitecture(Paths.get("src/main/resources/duke_net.json"))
                .exportModel(Paths.get("duke.dnet"))
                .maxError(0.05f)
                .maxEpochs(1000)
                .learningRate(0.01f)
                .build();

        // recognize image with a train model
        BufferedImage image = ImageIO.read(new File("datasets/duke_and_nonduke/duke/duke1.jpg"));
        Map<String, Float> results = classifier.classify(image);
        System.out.println(results);
    }
}
