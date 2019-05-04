package com.example.lian.betterbattleship;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import static com.example.lian.betterbattleship.MainActivity.KEY_STRING;


public class FragmentStart extends Fragment {

    private static final String KEY_LEVEL = "key_level";
    private Button btnPlay;
    private Button btnHighScores;
    private RadioGroup radioGroup;
    private RadioButton rbEasy;
    private RadioButton rbNormal;
    private RadioButton rbHard;

    public FragmentStart() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start, container, false);

        btnHighScores = (Button)view.findViewById(R.id.highScore_button);
        btnPlay = (Button) view.findViewById(R.id.play_button);
        radioGroup = (RadioGroup) view.findViewById(R.id.rg);
        rbEasy = (RadioButton) view.findViewById(R.id.easyRB);
        rbNormal = (RadioButton) view.findViewById(R.id.normalRB);
        rbHard = (RadioButton) view.findViewById(R.id.hardRB);

        int index = MainActivity.sp.getInt(KEY_LEVEL, 0);

        if (index != 0) {
            if (index == R.id.easyRB) {
                rbEasy.setChecked(true);
            }
            else if (index == R.id.normalRB) {
                rbNormal.setChecked(true);
            }
            else {
                rbHard.setChecked(true);
            }
            
        }

        btnHighScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHighScoreButtonClicked();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlayButtonClicked(v);
            }
        });

        return view;
    }

    public void onHighScoreButtonClicked() {
        //Log.e("Main Activity", "BUTTON HIGH SCORES PRESSED");
        Fragment fragment = new HighScoresFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.mainContainer, fragment);
        ft.addToBackStack(KEY_STRING);
        ft.commit();
    }

    public void onPlayButtonClicked(View view) {
        Intent intent = new Intent(getContext(), GameActivity.class);

        // get selected radio button from radioGroup
        int selectedId = radioGroup.getCheckedRadioButtonId();

        switch(selectedId) {
            case R.id.easyRB:
            default:    //easy
                intent.putExtra(MainActivity.BUNDLE_KEY, GameMode.EASY);
                break;
            case R.id.normalRB: //normal
                intent.putExtra(MainActivity.BUNDLE_KEY, GameMode.NORMAL);
                break;
            case R.id.hardRB:   //hard
                intent.putExtra(MainActivity.BUNDLE_KEY, GameMode.HARD);
                break;
        }

        MainActivity.sp.edit().putInt(KEY_LEVEL, selectedId).apply();

        startActivity(intent);
    }
}
