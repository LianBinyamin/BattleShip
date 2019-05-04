package com.example.lian.betterbattleship;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity implements ServiceConnection, ServiceSensor.ServiceCallbacks {

    private GridView gvHuman;
    private GridView gvAI;
    private Game game;
    private TextView enemyShipsText;
    private TextView playerShipsText;
    private View transparentView;
    private TextView playerNumberOfShots;
    private ServiceSensor serviceSensor;
    private SensorManager sensorManager;
    private TextView warning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);

        Bundle b = getIntent().getExtras();

        final GameMode gameMode = (GameMode) b.getSerializable(MainActivity.BUNDLE_KEY);

        Intent intent = new Intent(getApplicationContext(), ServiceSensor.class);
        getApplicationContext().startService(intent);

        game = new Game(gameMode, this);
        enemyShipsText = (TextView)findViewById(R.id.numOfShipsCompTv);
        playerShipsText = (TextView)findViewById(R.id.numOfShipsHumanTv);
        transparentView = (View)findViewById(R.id.view);
        playerNumberOfShots = (TextView) findViewById(R.id.numOfShots);
        warning = (TextView) findViewById(R.id.warning);
        warning.setVisibility(View.INVISIBLE);
        transparentView.setVisibility(View.GONE);

        gvHuman = (GridView)findViewById(R.id.gridviewHuman);
        gvAI = (GridView)findViewById(R.id.gridviewAI);

        gvHuman.setNumColumns(gameMode.getBoardSize());
        gvAI.setNumColumns(gameMode.getBoardSize());

        gvHuman.setAdapter(new TileAdapter(getApplicationContext(),game.getBoardHuman(), gameMode, game));
        gvAI.setAdapter(new TileAdapter(getApplicationContext(), game.getBoardAI(), gameMode, game));

        setNumberOfShipsTextView();
        setNumberOfShots();

        gvAI.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (game.playTile(position)) {
                    transparentView.setVisibility(View.VISIBLE);

                    setNumberOfShipsTextView();
                    setNumberOfShots();

                    ((TileAdapter) gvAI.getAdapter()).notifyDataSetChanged();

                    ((TextView) findViewById(R.id.turnText)).setText(R.string.computers_turn);
                    (findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);

                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            game.playComputer();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setNumberOfShipsTextView();
                                    ((TileAdapter) gvHuman.getAdapter()).notifyDataSetChanged();
                                    ((TextView) findViewById(R.id.turnText)).setText(R.string.players_turn);
                                    (findViewById(R.id.progressBar)).setVisibility(View.INVISIBLE);
                                    findViewById(R.id.view).setVisibility(View.GONE);
                                }
                            });
                        }
                    });

                    t.start();
                }
            }
        });
    }

    public void setNumberOfShots() {
        playerNumberOfShots.setText(getResources().getString(R.string.player_num_shots,
                game.getUserShots()));
    }

    public void setNumberOfShipsTextView() {
        enemyShipsText.setText(getResources().getString(R.string.num_enemy_ships,
                game.getBoardAI().getNumOfLivingShips()));
        playerShipsText.setText(getResources().getString(R.string.num_human_ships,
                game.getBoardHuman().getNumOfLivingShips()));
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        ServiceSensor.MyBinder b = (ServiceSensor.MyBinder) service;
        serviceSensor = b.getService();
        //Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
        serviceSensor.setCallbacks(GameActivity.this); // register
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        serviceSensor = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(getApplicationContext(), ServiceSensor.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (serviceSensor != null) {
            serviceSensor.getmSensorManager().unregisterListener(serviceSensor);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(this);
    }

    @Override
    public void react() {
        game.getBoardAI().shuffleShip(game.getBoardAI());
        ((TileAdapter) gvAI.getAdapter()).notifyDataSetChanged();
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.ship_relocated), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void warningOn() {
        warning = (TextView)findViewById(R.id.warning);
        warning.setVisibility(View.VISIBLE);
        runAnimation();
    }

    @Override
    public void warningOff() {
        Log.d("GAME ACTIVITY", "WARNING OFF");
        warning = (TextView)findViewById(R.id.warning);
        warning.clearAnimation();
        warning.setVisibility(View.INVISIBLE);
    }

    private void runAnimation() {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.blink);
        a.reset();
        TextView tv = (TextView) findViewById(R.id.warning);
        tv.clearAnimation();
        tv.startAnimation(a);

    }
}
