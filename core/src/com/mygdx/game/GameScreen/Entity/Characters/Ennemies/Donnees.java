package com.mygdx.game.GameScreen.Entity.Characters.Ennemies;

import java.util.List;

public class Donnees {

    private List<String> liste1;
    private List<Integer> liste2;
    private List<Double> liste3;
    private List<Boolean> liste4;

    // Constructeur par défaut (nécessaire pour Jackson)
    public Donnees() {}

    // Getters et setters
    public List<String> getListe1() {
        return liste1;
    }

    public void setListe1(List<String> liste1) {
        this.liste1 = liste1;
    }

    public List<Integer> getListe2() {
        return liste2;
    }

    public void setListe2(List<Integer> liste2) {
        this.liste2 = liste2;
    }

    public List<Double> getListe3() {
        return liste3;
    }

    public void setListe3(List<Double> liste3) {
        this.liste3 = liste3;
    }

    public List<Boolean> getListe4() {
        return liste4;
    }

    public void setListe4(List<Boolean> liste4) {
        this.liste4 = liste4;
    }
}
