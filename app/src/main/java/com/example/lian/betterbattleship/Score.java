package com.example.lian.betterbattleship;

public class Score {

    private String name;
    private int score;

    public Score(String name, int score) {
        this.name = name;
        this.score = score;
    }
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toStringName() {
        return "Name: " + getName();
    }

    public String toStringScore() {
        return "" + getScore();
    }
}
