package com.example.lian.betterbattleship;

public class Tile {

    private Board.TileState tileState;
    private boolean isMarkedBefore;

    public Tile() {
        tileState = Board.TileState.EMPTY;
        isMarkedBefore = false;
    }

    public void setTileStatus(Board.TileState status) {
        this.tileState = status;
    }

    public Board.TileState getTileStatus() {
        return tileState;
    }

    public boolean isMarkedBefore() {
        return isMarkedBefore;
    }

    public void setMarkedBefore(boolean markedBefore) {
        isMarkedBefore = markedBefore;
    }
}
