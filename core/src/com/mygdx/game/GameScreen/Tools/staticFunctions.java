package com.mygdx.game.GameScreen.Tools;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreen.Entity.Entity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class staticFunctions {

    public static void sortByY(ArrayList<Entity> entities) {
        entities.sort(new Comparator<Entity>() {
            @Override
            public int compare(Entity entity1, Entity entity2) {
                return Float.compare(entity2.position.y, entity1.position.y); // Inversion de l'ordre de comparaison
            }
        });
    }

    public static List<Rectangle> getCollisionsTile(String pathFile, int id) {
        List<Rectangle> rectangles = new ArrayList<>();

        try {
            // Charger le fichier XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(pathFile);

            // Récupérer la liste des tuiles
            NodeList tileList = doc.getElementsByTagName("tile");

            // Parcourir les tuiles
            for (int i = 0; i < tileList.getLength(); i++) {
                Element tile = (Element) tileList.item(i);
                int Id = Integer.parseInt(tile.getAttribute("id"));

                if (Id == id) {
                    // Récupérer la liste des objets de la tuile
                    NodeList objectList = tile.getElementsByTagName("object");

                    // Parcourir les objets de la tuile
                    for (int j = 0; j < objectList.getLength(); j++) {
                        Element object = (Element) objectList.item(j);
                        float x = Float.parseFloat(object.getAttribute("x"));
                        float y = Float.parseFloat(object.getAttribute("y"));
                        float width = Float.parseFloat(object.getAttribute("width"));
                        float height = Float.parseFloat(object.getAttribute("height"));

                        // Ajouter le rectangle à la liste
                        rectangles.add(new Rectangle(x, y, width, height));
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rectangles;
    }

    public static HashMap<Integer, Float> getAnimationTile(String pathFile, int id) {
        HashMap<Integer, Float> animation = new HashMap<>();

        try {
            // Charger le fichier XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(pathFile);

            // Récupérer la liste des tuiles
            NodeList tileList = doc.getElementsByTagName("tile");

            // Parcourir les tuiles
            for (int i = 0; i < tileList.getLength(); i++) {
                Element tile = (Element) tileList.item(i);
                int Id = Integer.parseInt(tile.getAttribute("id"));

                if (Id == id) {
                    // Récupérer la liste des objets de la tuile
                    NodeList objectList = tile.getElementsByTagName("frame");

                    // Parcourir les objets de la tuile
                    for (int j = 0; j < objectList.getLength(); j++) {
                        Element object = (Element) objectList.item(j);
                        int tileId = (int) Float.parseFloat(object.getAttribute("tileid"));
                        float duration = Float.parseFloat(object.getAttribute("duration"));

                        // ajouter la frame et la durée au dico
                        animation.put(tileId, duration);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return animation;
    }

    public static int getRegionId(int regionX, int regionY, int regionWidth, int regionHeight, int textureWidth) {
        return (regionY / regionHeight) * (textureWidth / regionWidth) + (regionX / regionWidth);
    }


    private boolean compareImages(Texture image1, Texture image2) {
        TextureData textureData1 = image1.getTextureData();
        TextureData textureData2 = image2.getTextureData();

        if (!textureData1.isPrepared() || !textureData2.isPrepared()) {
            textureData1.prepare();
            textureData2.prepare();
        }

        Pixmap pixmap1 = textureData1.consumePixmap();
        Pixmap pixmap2 = textureData2.consumePixmap();

        int width = pixmap1.getWidth();
        int height = pixmap1.getHeight();

        // Vérifiez si les dimensions des images sont les mêmes
        if (width != pixmap2.getWidth() || height != pixmap2.getHeight()) {
            return false;
        }

        // Comparez les pixels
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel1 = pixmap1.getPixel(x, y);
                int pixel2 = pixmap2.getPixel(x, y);

                if (pixel1 != pixel2) {
                    return false; // Les images sont différentes dès qu'un pixel diffère
                }
            }
        }

        return true; // Les images sont identiques si aucune différence de pixel n'est trouvée
    }

    public static List<String> parcourirPackages(String strToCheck, String projectPath) throws IOException {
        List<String> classesWithEntity = new ArrayList<>();
        Files.walk(Paths.get(projectPath))
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".java"))
                .forEach(p -> {
                    try {
                        if (contientExtendsEntity(strToCheck, p)) {
                            String className = p.getFileName().toString().replace(".java", "");
                            classesWithEntity.add(className);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        return classesWithEntity;
    }

    public static boolean contientExtendsEntity(String strToCheck, Path filePath) throws IOException {
        String content = Files.readString(filePath);
        return content.contains(strToCheck);
    }

    public static int getDirection(Vector2 targetPosition, Vector2 startPosition)
    {
        // Calcul du vecteur direction
        Vector2 directionVector = new Vector2(targetPosition.x - startPosition.x, targetPosition.y - startPosition.y);

        // Calcul de l'angle en radians entre les deux points
        float angleRad = directionVector.angleRad();

        // Convertit l'angle en degrés
        float angleDeg = (float) Math.toDegrees(angleRad);

        // mettre à jour la direction

        return (int) angleDeg;
    }

    public static <K, V> Map.Entry<K, V> getRandomEntry(Map<K, V> map) {
        if (map.isEmpty()) {
            return null; // Retourne null si la map est vide
        }

        // Convertir la Map en une liste d'entrées
        int size = map.size();
        int item = new Random().nextInt(size); // Sélectionner un index au hasard
        int i = 0;

        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (i == item) {
                return entry;
            }
            i++;
        }
        return null;
    }
}
