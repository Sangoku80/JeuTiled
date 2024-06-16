package com.mygdx.game.GameScreen.Tools.AI.DeepLearning;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class LoadModelExample {

    public static void main(String[] args) {
        // Chemin où le modèle a été sauvegardé
        File locationToLoad = new File("trained_model.zip");

        try {
            // Charger le modèle
            MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(locationToLoad);

            // Utilisation du modèle chargé pour la prédiction
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("Entrez un nombre : ");
                int x = scanner.nextInt();
                INDArray inputValue = Nd4j.create(new double[]{x}, new int[]{1, 1});
                INDArray prediction = model.output(inputValue, false);
                System.out.println("Prédiction : " + prediction.getDouble(0));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
