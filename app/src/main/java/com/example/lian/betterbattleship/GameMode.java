package com.example.lian.betterbattleship;

public enum GameMode {
    EASY(4,3), NORMAL(5,3), HARD(6,3);

    private int boardSize;
    private int numOfShips;

    GameMode(int boardSize, int numOfShips) {
        this.boardSize = boardSize;
        this.numOfShips = numOfShips;
    }

    public int getNumOfShips() {
        return numOfShips;
    }

    public int getBoardSize() {
        return boardSize;
    }


}
