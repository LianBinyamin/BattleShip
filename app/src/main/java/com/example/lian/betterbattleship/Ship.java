package com.example.lian.betterbattleship;

import java.util.ArrayList;
import java.util.Map;

public class Ship {

    private int shipSize;
    private boolean isSunk;
    private ArrayList<Integer> tilesTaken;

    public Ship(int shipSize) {
        this.shipSize = shipSize;
        this.isSunk = false;

        this.tilesTaken = new ArrayList<>();
    }

    public int getShipSize() {
        return shipSize;
    }

    public void setSunk(boolean sunk) {
        isSunk = sunk;
    }

    public boolean isSunk() {
        return isSunk;
    }

    public void checkShipSunk(Ship ship, Board board) {

        boolean isShipDown = true;

        for (Map.Entry<Tile, Ship> entry : board.getBoardMap().entrySet()) {
            if (entry.getValue() == ship) {
                if (entry.getKey().getTileStatus() != Board.TileState.HIT) {
                    isShipDown = false;
                    break;
                }
            }
        }

        if (isShipDown) {
            ship.setSunk(true);
        }

        else {
            ship.setSunk(false);
        }
    }

    public ArrayList<Integer> getTilesTaken() {
        return tilesTaken;
    }

    public void setTilesTaken(ArrayList<Integer> tilesTaken) {
        this.tilesTaken = tilesTaken;
    }
}
