package com.example.lian.betterbattleship;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private List<Integer> markedHitTilesList;


    public Player() {
        markedHitTilesList = new ArrayList<Integer>();
    }

    public List<Integer> getMarkedTilesList() {
        return markedHitTilesList;
    }
}
