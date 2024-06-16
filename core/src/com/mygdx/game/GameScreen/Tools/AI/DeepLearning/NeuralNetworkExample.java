package com.mygdx.game.GameScreen.Tools.AI.DeepLearning;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class NeuralNetworkExample {

    public static void main(String[] args) {
        int seed = 123;    // Random seed for reproducibility
        double learningRate = 0.001;
        int numInputs = 1;
        int numOutputs = 1;
        int numHiddenNodes = 64;

        // Create the network configuration
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(new Adam(learningRate))
                .list()
                .layer(0, new DenseLayer.Builder().nIn(numInputs).nOut(numHiddenNodes)
                        .activation(Activation.RELU)
                        .build())
                .layer(1, new DenseLayer.Builder().nIn(numHiddenNodes).nOut(numHiddenNodes)
                        .activation(Activation.RELU)
                        .build())
                .layer(2, new DenseLayer.Builder().nIn(numHiddenNodes).nOut(numHiddenNodes)
                        .activation(Activation.RELU)
                        .build())
                .layer(3, new DenseLayer.Builder().nIn(numHiddenNodes).nOut(numHiddenNodes)
                        .activation(Activation.RELU)
                        .build())
                .layer(4, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                        .activation(Activation.IDENTITY)
                        .nIn(numHiddenNodes).nOut(numOutputs).build())
                .build();

        // Create the network
        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();
        model.setListeners(new ScoreIterationListener(100));

        // Create the training data
        INDArray input = Nd4j.create(new double[]{1, 2, 3, 4, 5}, new int[]{5, 1});
        INDArray output = Nd4j.create(new double[]{2, 4, 6, 8, 10}, new int[]{5, 1});
        DataSet dataSet = new DataSet(input, output);

        // Train the network
        int nEpochs = 4500;
        for (int i = 0; i < nEpochs; i++) {
            model.fit(dataSet);
        }

        // Save the model
        File locationToSave = new File("trained_model.zip");
        try {
            model.save(locationToSave, true);
            System.out.println("Model saved successfully at: " + locationToSave.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a scanner to get user input
        Scanner scanner = new Scanner(System.in);

        // Prediction loop
        while (true) {
            System.out.print("Nombre : ");
            int x = scanner.nextInt();
            INDArray inputValue = Nd4j.create(new double[]{x}, new int[]{1, 1});
            INDArray prediction = model.output(inputValue, false);
            System.out.println("Prédiction : " + prediction.getDouble(0));
        }
    }
}
