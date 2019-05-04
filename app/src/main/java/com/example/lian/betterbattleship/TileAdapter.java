
package com.example.lian.betterbattleship;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

public class TileAdapter extends BaseAdapter {

    private Context context;
    private Board board;
    private GameMode gm;
    private Game game;
    private AnimationDrawable anim;
    private final int PADDING = 1;

    private final int EASY_WIDTH_ENEMY = 170;
    private final int EASY_HEIGHT_ENEMY = 160;
    private final int EASY_WIDTH_HUMAN = 125;
    private final int EASY_HEIGHT_HUMAN = 110;

    private final int NORMAL_WIDTH_ENEMY = 135;
    private final int NORMAL_HEIGHT_ENEMY = 120;
    private final int NORMAL_WIDTH_HUMAN = 105;
    private final int NORMAL_HEIGHT_HUMAN = 90;

    private final int HARD_WIDTH_ENEMY = 112;
    private final int HARD_HEIGHT_ENEMY = 110;
    private final int HARD_WIDTH_HUMAN = 82;
    private final int HARD_HEIGHT_HUMAN = 70;




    public TileAdapter(Context context, Board board, GameMode gm, Game game) {
        this.game = game;
        this.board = board;
        this.context = context;
        this.gm = gm;
    }

    @Override
    public int getCount() {
        return (board.getSizeOfBoard()) * (board.getSizeOfBoard());
    }

    @Override
    public Object getItem(int position) {
        return board.getTile(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TileView tileView;

        if (convertView == null) {

            tileView = new TileView(context);

            switch(gm) {
                case EASY:
                    default:
                        if (board.equals(game.getBoardHuman())) {
                            tileView.setLayoutParams(new GridView.LayoutParams(EASY_WIDTH_HUMAN, EASY_HEIGHT_HUMAN));
                        }
                        else {
                            tileView.setLayoutParams(new GridView.LayoutParams(EASY_WIDTH_ENEMY, EASY_HEIGHT_ENEMY));
                        }
                        break;
                case NORMAL:
                    if (board.equals(game.getBoardHuman())) {
                        tileView.setLayoutParams(new GridView.LayoutParams(NORMAL_WIDTH_HUMAN, NORMAL_HEIGHT_HUMAN));
                    }
                    else {
                        tileView.setLayoutParams(new GridView.LayoutParams(NORMAL_WIDTH_ENEMY, NORMAL_HEIGHT_ENEMY));
                    }
                    break;
                case HARD:
                    if (board.equals(game.getBoardHuman())) {
                        tileView.setLayoutParams(new GridView.LayoutParams(HARD_WIDTH_HUMAN, HARD_HEIGHT_HUMAN));
                    }
                    else {
                        tileView.setLayoutParams(new GridView.LayoutParams(HARD_WIDTH_ENEMY, HARD_HEIGHT_ENEMY));
                    }
                    break;
            }

//            if (gm == GameMode.EASY) {
//                tileView.setLayoutParams(new GridView.LayoutParams(EASY_WEIDTH, EASY_HEIGHT));
//            }
//
//            else if (gm == GameMode.NORMAL) {
//                tileView.setLayoutParams(new GridView.LayoutParams(NORMAL_WEIDTH, NORMAL_HEIGHT));
//            }
//
//            else {
//                tileView.setLayoutParams(new GridView.LayoutParams(HARD_WEIDTH, HARD_HEIGHT));
//            }


            tileView.setPadding(PADDING, PADDING, PADDING, PADDING);
        }

        else {
            tileView = (TileView) convertView;

        }

        //check shuffle ship
//        if (board == game.getBoardAI() && board.getTile(position).getTileStatus() == Board.TileState.TAKEN) {
//            tileView.imageView.setBackgroundColor(Color.RED);
//        }
//
//        else if (board == game.getBoardAI() && board.getTile(position).getTileStatus() == Board.TileState.EMPTY) {
//            tileView.imageView.setBackgroundColor(Color.LTGRAY);
//        }

        if (board.getTile(position).getTileStatus() == Board.TileState.HIT ) {
            tileView.imageView.setBackgroundResource(R.drawable.ship);
            tileView.imageView.setImageResource(R.drawable.border);
        }

        else if(board.getTile(position).getTileStatus() == Board.TileState.SUNK) {
            tileView.imageView.setBackgroundResource(R.drawable.explosion);
            anim = (AnimationDrawable) tileView.imageView.getBackground();
            anim.start();
            tileView.imageView.setImageResource(R.drawable.border);
        }

        else if(board.getTile(position).getTileStatus() == Board.TileState.MISS) {
            tileView.imageView.setBackgroundResource(R.drawable.splash);
            anim = (AnimationDrawable) tileView.imageView.getBackground();
            anim.start();
            tileView.imageView.setImageResource(R.drawable.border);
        }

         else {
            tileView.imageView.setBackgroundColor(Color.LTGRAY);
            tileView.imageView.setImageResource(R.drawable.border);
        }

        if (board.getTile(position).getTileStatus() == Board.TileState.TAKEN &&
                board == game.getBoardHuman()) {

            tileView.imageView.setBackgroundColor(Color.GRAY);
        }

        return tileView;
    }
}
