package com.example.lian.betterbattleship;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class MainActivity extends AppCompatActivity {

    static SharedPreferences sp;
    final static String BUNDLE_KEY = "BUNDLE_KEY";
    final static String KEY_STRING = "string";
    public static DataBaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("com.example.lian.betterbattleship", MODE_PRIVATE);

        //init the view with the start fragment
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.mainContainer, new FragmentStart());
        ft.commit();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                myDb = new DataBaseHelper(getApplicationContext());
            }
        });

        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}



