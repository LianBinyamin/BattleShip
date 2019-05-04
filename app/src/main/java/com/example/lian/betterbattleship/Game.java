package com.example.lian.betterbattleship;

import android.content.Intent;

import java.util.Map;

enum Turn {
    PLAYER_HUMAN,PLAYER_AI;
}

public class Game {

    private Board boardHuman;
    private Board boardAI;
    private Turn currentTurn;
    private ComputerPlayer playerAI;
    private Player playerHuman;
    private GameMode gm;
    private GameActivity activity;
    private int userShots;
    private int finalScore;
    final static String GAME_MODE_BUNDLE_KEY = "GAME_MODE_BUNDLE_KEY";
    final static String CURRENT_TURN_BUNDLE_KEY = "CURRENT_TURN_BUNDLE_KEY";
    final static String PLAYER_SCORE_BUNDLE_KEY = "SCORE";


    public Game(final GameMode gameMode, GameActivity activity) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                boardHuman = new Board(gameMode);
            }
        });

        t.start();


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                boardAI = new Board(gameMode);
            }
        });

        thread.start();


        try {
            t.join();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.activity = activity;
        currentTurn = Turn.PLAYER_HUMAN;
        playerAI = new ComputerPlayer();
        playerHuman = new Player();
        this.gm = gameMode;
        this.userShots = 0;
        this.finalScore = 0;
    }

    public Turn getCurrentTurn() {
        return currentTurn;
    }

    public double getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
    }

    public Board getBoardHuman() {
        return boardHuman;
    }

    public Board getBoardAI() {
        return boardAI;
    }

    public Player getPlayerHuman() {
        return playerHuman;
    }

    public ComputerPlayer getPlayerAI() {
        return playerAI;
    }

    public boolean playTile(int position) {

        boolean isHit = false;
        boolean isPlayerPlayed = false;
        boolean isShipDown = false;
        boolean isWinning = false;

        if (currentTurn == Turn.PLAYER_HUMAN) {
            isPlayerPlayed = getBoardAI().setTile(position);
            setUserShots(getUserShots()+1);

            if (getBoardAI().getTile(position).getTileStatus() == Board.TileState.HIT) {
                isShipDown = checkShipDown(position, getBoardAI());
                isHit = true;
            }
        }

        else {  //Computers turn
            isPlayerPlayed = getBoardHuman().setTile(position);

            if (getBoardHuman().getTile(position).getTileStatus() == Board.TileState.HIT) {
                isShipDown = checkShipDown(position, getBoardHuman());
                isHit = true;
            }
        }

        if (isPlayerPlayed) {
            if (isShipDown) {
                switch(currentTurn) {
                    case PLAYER_HUMAN:
                        default:
                            getBoardAI().setNumOfLivingShips(getBoardAI().getNumOfLivingShips()-1);
                            break;
                    case PLAYER_AI:
                        getBoardHuman().setNumOfLivingShips(getBoardHuman().getNumOfLivingShips()-1);
                        break;
                }
            }

            if (isHit) {
                if (currentTurn == Turn.PLAYER_HUMAN) {
                    isWinning = checkPlayerWinning();
                    if (isWinning) {
                        setFinalScore(calculateScore(getUserShots(), (getBoardAI().getNumOfTakenTiles())));
                        gameWon(currentTurn);
                    }
                    else {
                        toggleTurn();
                    }
                }

                else {  //computer's turn
                    isWinning = checkComputerWinning();
                    if (isWinning) {
                        gameWon(currentTurn);
                    }
                    else {
                        toggleTurn();
                    }
                }
            }
            else {
                toggleTurn();
            }

            return true;
        }

        return false;
    }

    public int calculateScore(int shots, int takenTiles) {
        double casting = (double)(takenTiles);
        double res = ((casting/shots)*100);
        return (int)(res);
    }

    public boolean checkShipDown(int position, Board board) {

        Ship tempShip = null;

        tempShip = board.getBoardMap().get(board.getTile(position));

        if (tempShip != null) {

            tempShip.checkShipSunk(tempShip, board);

            if (tempShip.isSunk()) {    //check if this ship is down
                for (Map.Entry<Tile, Ship> entry : board.getBoardMap().entrySet()) {
                    if (entry.getValue() == tempShip) {
                        entry.getKey().setTileStatus(Board.TileState.SUNK);
                    }
                }
                return true;
            }
        }
        return false;
    }

    public void toggleTurn() {

        if(currentTurn == Turn.PLAYER_HUMAN)
            currentTurn = Turn.PLAYER_AI;
        else
            currentTurn = Turn.PLAYER_HUMAN;

    }

    public void playComputer() {

        int positionToPlay = playerAI.playTurn(boardHuman);

        playTile(positionToPlay);

    }

    public boolean checkPlayerWinning() {
        if (getBoardAI().getNumOfLivingShips() == 0) {
            return true;
        }
        return false;
    }

    public boolean checkComputerWinning() {
        if (getBoardHuman().getNumOfLivingShips() == 0) {
                return true;
        }
        return false;
    }

    public int getUserShots() {
        return userShots;
    }

    public void setUserShots(int userShots) {
        this.userShots = userShots;
    }

    public void gameWon(Turn currentTurn) {
        //moving to win activity!
        Intent intent = new Intent(activity.getApplicationContext(), WinActivity.class);
        intent.putExtra(GAME_MODE_BUNDLE_KEY, gm);
        intent.putExtra(CURRENT_TURN_BUNDLE_KEY, currentTurn);
        intent.putExtra(PLAYER_SCORE_BUNDLE_KEY, finalScore);
        activity.startActivity(intent);
        activity.finish();
    }

}
