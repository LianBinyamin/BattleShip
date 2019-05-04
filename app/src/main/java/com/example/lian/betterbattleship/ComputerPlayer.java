package com.example.lian.betterbattleship;


public class ComputerPlayer extends Player {

    private final int COMPUTER_SLEEPS = 2000;

    public void think() {
        try {
            Thread.sleep(COMPUTER_SLEEPS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int playTurn(Board board) {

        think();

        int randomValue = (board.getSizeOfBoard()*board.getSizeOfBoard());

        int positionToPlay = (int)(Math.random()*(randomValue));

        while(board.getTile(positionToPlay).isMarkedBefore()) {

            positionToPlay = (int)(Math.random()*(randomValue));

        }

        return positionToPlay;
    }
}
