package com.example.lian.betterbattleship;

import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Board {

    private int sizeOfBoard;
    private int numOfLivingShips;
    private int numOfShips;
    private Tile tiles[];
    private ArrayList<Ship> ships;
    private final int SHIP_SIZE_2 = 2;
    private final int SHIP_SIZE_3 = 3;
    private int numOfTakenTiles;
    private Map<Tile, Ship> boardMap;
    private GameMode gameMode;


    public enum TileState {
        TAKEN, MISS, HIT, EMPTY, SUNK;
    }

    public Board(GameMode gameMode) {
        this.gameMode = gameMode;
        this.sizeOfBoard = gameMode.getBoardSize();
        this.numOfShips = gameMode.getNumOfShips();
        this.numOfTakenTiles = 0;
        this.numOfLivingShips = gameMode.getNumOfShips();
        this.boardMap = new HashMap<Tile, Ship>();

        tiles = new Tile[(gameMode.getBoardSize())*(gameMode.getBoardSize())];

        //init tiles
        for(int i = 0 ; i < tiles.length ; i++) {
            tiles[i] = new Tile();
        }

        ships = new ArrayList<Ship>(gameMode.getNumOfShips());

        //init ships
        initShips(gameMode);

        placeShipsOnBoard(ships, this, gameMode);
    }

    public void initShips(GameMode gm) {
        switch(gm) {
            case EASY:
            default:
                ships.add(new Ship(SHIP_SIZE_2));
                ships.add(new Ship(SHIP_SIZE_3));
                ships.add(new Ship(SHIP_SIZE_2));
                break;
            case NORMAL:
                ships.add(new Ship(SHIP_SIZE_2));
                ships.add(new Ship(SHIP_SIZE_2));
                ships.add(new Ship(SHIP_SIZE_3));
                break;
            case HARD:
                ships.add(new Ship(SHIP_SIZE_2));
                ships.add(new Ship(SHIP_SIZE_2));
                ships.add(new Ship(SHIP_SIZE_3));
                break;
        }
    }

    public int getNumOfLivingShips() {
        return numOfLivingShips;
    }

    public Map<Tile, Ship> getBoardMap() {
        return boardMap;
    }

    public void setNumOfLivingShips(int numOfLivingShips) {
        this.numOfLivingShips = numOfLivingShips;
    }

    public int getNumOfTakenTiles() {
        return numOfTakenTiles;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public int getSizeOfBoard() {
        return sizeOfBoard;
    }

    public Tile getTile(int position) {
        return tiles[position];
    }

    public void shuffleShip(Board board) {
        boolean randomShipFlag = true;
        int randomShip, randomTile;

        while (randomShipFlag) {
            randomShip = (int) (Math.random() * board.getShips().size());
            if (!board.getShips().get(randomShip).isSunk()) {
                //int shipSize = board.getShips().get(randomShip).getShipSize();
                boolean isTossAgain = true;

                while (isTossAgain) {
                    randomTile = (int) (Math.random() * (board.getSizeOfBoard()*board.getSizeOfBoard()));

                    //check if the tile is empty
                    if (board.getTile(randomTile).getTileStatus() == TileState.EMPTY) {
                        if (checkRight(board.getShips().get(randomShip), board, randomTile)) {
                            setTilesAgain(board.getShips().get(randomShip), board);
                            placeShipRight(board.getShips().get(randomShip), board, randomTile);
                            isTossAgain = false;
                            randomShipFlag = false;
                        }
                        else if (checkUp(getShips().get(randomShip), board, randomTile)) {
                            setTilesAgain(getShips().get(randomShip), board);
                            placeShipUp(getShips().get(randomShip), board, randomTile);
                            isTossAgain = false;
                            randomShipFlag = false;
                        }
                        else if (checkLeft(getShips().get(randomShip), board, randomTile)) {
                            setTilesAgain(getShips().get(randomShip), board);
                            placeShipLeft(getShips().get(randomShip), board, randomTile);
                            isTossAgain = false;
                            randomShipFlag = false;
                        }
                        else if (checkDown(getShips().get(randomShip), board, randomTile)) {
                            setTilesAgain(getShips().get(randomShip), board);
                            placeShipDown(getShips().get(randomShip), board, randomTile);
                            isTossAgain = false;
                            randomShipFlag = false;
                        }
                    }
                }
            }
        }
    }

    public void setTilesAgain(Ship ship, Board board) {

        for (int i=0 ; i<ship.getTilesTaken().size() ; i++) {
            board.getTile(ship.getTilesTaken().get(i)).setTileStatus(TileState.EMPTY);
            board.getTile(ship.getTilesTaken().get(i)).setMarkedBefore(false);
            board.numOfTakenTiles--;
        }

        ship.getTilesTaken().clear();

        Iterator<Map.Entry<Tile, Ship>> iterator = board.getBoardMap().entrySet().iterator();
        while(iterator.hasNext()) {
            if (iterator.next().getValue() == ship) {
                iterator.remove();
            }
        }
    }

    public boolean setTile(int position) {

        if (!tiles[position].isMarkedBefore()) {
            if (tiles[position].getTileStatus() == TileState.EMPTY) { //no ship in this tile
                tiles[position].setTileStatus(TileState.MISS);
                tiles[position].setMarkedBefore(true);
                return true;
            }

            else if(tiles[position].getTileStatus() == TileState.TAKEN) {   //there's a ship in this tile
                tiles[position].setTileStatus(TileState.HIT);
                tiles[position].setMarkedBefore(true);
                return true;
            }
        }

        return false;
    }

    public void placeShipsOnBoard(ArrayList<Ship> ships, Board board, GameMode gameMode) {
        int randomTile;

        for (int i = 0 ; i < ships.size() ; i++) {

            boolean isTossAgain = true;

            while(isTossAgain) {
                randomTile = (int)(Math.random()*(gameMode.getBoardSize()*gameMode.getBoardSize()));

                //check if the tile is empty
                if(board.getTile(randomTile).getTileStatus() == TileState.EMPTY) {
                    if(isShipCanBePlaced(ships.get(i), board, randomTile)) {
                        isTossAgain = false;
                    }
                }
            }
        }
    }

    public boolean isShipCanBePlaced(Ship ship, Board board, int position) {

        //check if the ship can be placed to the right
        if (checkRight(ship, board, position)) {
            Log.e("placing ship", "########## RIGHT ###########");
            placeShipRight(ship, board, position);
            return true;
        }

        //else, check if the ship can be placed up
        else if (checkUp(ship, board, position)) {
            Log.e("placing ship", "########## UP ###########");
            placeShipUp(ship, board, position);
            return true;
        }

        //else, check if the ship can be placed to the left
        else if (checkLeft(ship, board, position)) {
            Log.e("placing ship", "########## LEFT ###########");
            placeShipLeft(ship, board, position);
            return true;
        }

        //else, check if the ship can be placed down
        else if (checkDown(ship, board, position)){
            Log.e("placing ship", "########## DOWN ###########");
            placeShipDown(ship, board, position);
            return true;
        }

        //if we got up to here then the ship couldn't be placed
        return false;
    }

    public boolean checkDown(Ship ship, Board board, int position) {

        if (ship.getShipSize() > board.getSizeOfBoard() - (position/board.getSizeOfBoard())) {    //if there is no place for the ship
            return false;
        }

        //check if maybe there is a tile that is TAKEN
        for (int index = position, i=0 ; i < ship.getShipSize() ; index+=board.getSizeOfBoard(), i++) {
            if (board.getTile(index).getTileStatus() == TileState.TAKEN ||
                    board.getTile(index).getTileStatus() == TileState.SUNK) {
                return false;
            }
        }
        return true;
    }

    public boolean checkUp(Ship ship, Board board, int position) {

        if (ship.getShipSize() > (position/board.getSizeOfBoard()) + 1) { //if there is no place for the ship
            return false;
        }

        for (int index = position, i=0 ; i < ship.getShipSize() ; index-=board.getSizeOfBoard(), i++) {
            //check if all tiles are available
            if (board.getTile(index).getTileStatus() == TileState.TAKEN ||
                    board.getTile(index).getTileStatus() == TileState.SUNK) {
                return false;
            }
        }
        return true;
    }

    public boolean checkRight(Ship ship, Board board, int position) {

        if (ship.getShipSize() > board.getSizeOfBoard() - (position%board.getSizeOfBoard())) {
            return false;
        }

        for (int index = position, i=0 ; i < ship.getShipSize() ; index++, i++) {
            //check if all tiles are available
            if (board.getTile(index).getTileStatus() == TileState.TAKEN ||
                    board.getTile(index).getTileStatus() == TileState.SUNK) {
                return false;
            }
        }
        return true;
    }

    public boolean checkLeft(Ship ship, Board board, int position) {
        if (ship.getShipSize() > (position%board.getSizeOfBoard()) + 1) { //if there is no place for the ship
            return false;
        }

        for (int index = position, i=0 ; i < ship.getShipSize() ; index--, i++) {
            //check if all tiles are available
            if (board.getTile(index).getTileStatus() == TileState.TAKEN ||
                    board.getTile(index).getTileStatus() == TileState.SUNK) {
                return false;
            }
        }
        return true;
    }

    public void placeShipRight(Ship ship, Board board, int position) {

        for (int index = position, i=0 ; i < ship.getShipSize() ; index++, i++) {
            board.getTile(index).setTileStatus(TileState.TAKEN);
            board.getTile(index).setMarkedBefore(false);
            board.numOfTakenTiles++;
            board.getBoardMap().put(tiles[index], ship);
            ship.getTilesTaken().add(index);
        }
    }

    public void placeShipDown(Ship ship, Board board, int position) {

        for (int index = position, i=0 ; i < ship.getShipSize() ; index+=board.getSizeOfBoard(), i++) {
            board.getTile(index).setTileStatus(TileState.TAKEN);
            board.getTile(index).setMarkedBefore(false);
            board.numOfTakenTiles++;
            boardMap.put(tiles[index], ship);
            ship.getTilesTaken().add(index);
        }
    }

    public void placeShipUp(Ship ship, Board board, int position) {

        for (int index = position, i=0 ; i < ship.getShipSize() ; index-=board.getSizeOfBoard(), i++) {
            board.getTile(index).setTileStatus(TileState.TAKEN);
            board.getTile(index).setMarkedBefore(false);
            this.numOfTakenTiles++;
            boardMap.put(tiles[index], ship);
            ship.getTilesTaken().add(index);
        }
    }

    public void placeShipLeft(Ship ship, Board board, int position) {

        for (int index = position, i=0 ; i < ship.getShipSize() ; index--, i++) {
            board.getTile(index).setTileStatus(TileState.TAKEN);
            board.getTile(index).setMarkedBefore(false);
            board.numOfTakenTiles++;
            boardMap.put(tiles[index], ship);
            ship.getTilesTaken().add(index);
        }
    }

}
