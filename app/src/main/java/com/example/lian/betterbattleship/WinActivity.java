package com.example.lian.betterbattleship;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class WinActivity extends AppCompatActivity {

    private Button btnHome;
    private Button btnPlayAgain;
    private TextView winnerTextView;
    private Button btnInsertData;
    private EditText editName;
    private int playerScore;
    private TextView scoreText;
    public static final double AMPLITUDE = 0.2;
    public static final double FREQUENCY = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning);

        findViewById(R.id.winnerText).setVisibility(View.INVISIBLE);
        findViewById(R.id.editText).setVisibility(View.INVISIBLE);
        findViewById(R.id.btnData).setVisibility(View.INVISIBLE);
        findViewById(R.id.score).setVisibility(View.INVISIBLE);
        findViewById(R.id.looser).setVisibility(View.INVISIBLE);

        Bundle b = getIntent().getExtras();

        final GameMode gameMode = (GameMode) b.getSerializable(Game.GAME_MODE_BUNDLE_KEY);
        Turn turn = (Turn) b.getSerializable(Game.CURRENT_TURN_BUNDLE_KEY);
        this.playerScore = b.getInt(Game.PLAYER_SCORE_BUNDLE_KEY);

        btnHome = (Button)findViewById(R.id.homeBtn);
        btnPlayAgain = (Button)findViewById(R.id.playAgainBtn);
        winnerTextView = (TextView) findViewById(R.id.winnerText);
        btnInsertData = (Button)findViewById(R.id.btnData);
        editName = (EditText)findViewById(R.id.editText);
        scoreText = (TextView)findViewById(R.id.score);

        btnInsertData.setEnabled(true);

        if (turn == Turn.PLAYER_HUMAN) {
            RunAnimation();
            findViewById(R.id.score).setVisibility(View.VISIBLE);
            findViewById(R.id.btnData).setVisibility(View.VISIBLE);
            findViewById(R.id.editText).setVisibility(View.VISIBLE);
            winnerTextView.setText(R.string.winner);
            scoreText.setText(getResources().getString(R.string.score, playerScore));
            findViewById(R.id.winnerText).setVisibility(View.VISIBLE);
        }

        else {
            RunAnimation();
            winnerTextView.setText(R.string.game_over);
            findViewById(R.id.looser).setVisibility(View.VISIBLE);
            findViewById(R.id.winnerText).setVisibility(View.VISIBLE);
        }

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btnPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                intent.putExtra(MainActivity.BUNDLE_KEY, gameMode);
                startActivity(intent);
            }
        });

        btnInsertData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //animation
                final Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(AMPLITUDE, FREQUENCY);
                myAnim.setInterpolator(interpolator);
                btnInsertData.startAnimation(myAnim);

                String name = editName.getText().toString();

                if (name.equals("") || name == null) {
                    Toast.makeText(getApplicationContext(), R.string.enter_name, Toast.LENGTH_LONG).show();
                }
                else {
                    if (gameMode == GameMode.EASY) {
                        MainActivity.myDb.insertDataToEasyTable(name, playerScore);
                    }
                    else if (gameMode == GameMode.NORMAL) {
                        MainActivity.myDb.insertDataToNormalTable(name, playerScore);
                    }
                    else { //game mode is hard
                        MainActivity.myDb.insertDataToHardTable(name, playerScore);
                    }

                    Toast.makeText(getApplicationContext(), R.string.data_inserted, Toast.LENGTH_SHORT).show();
                    btnInsertData.setEnabled(false);
                }
            }
        });

    }

    private void RunAnimation()
    {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.blink);
        a.reset();
        TextView tv = (TextView) findViewById(R.id.winnerText);
        tv.clearAnimation();
        tv.startAnimation(a);
    }
}
