package com.example.flappybird;

public class scoreData {
    int score;
    String kulcad;

    scoreData(){

    }

    public scoreData(int score, String kulcad) {
        this.score = score;
        this.kulcad = kulcad;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getKulcad() {
        return kulcad;
    }

    public void setKulcad(String kulcad) {
        this.kulcad = kulcad;
    }

}