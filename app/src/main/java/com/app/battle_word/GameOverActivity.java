package com.app.battle_word;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameOverActivity extends AppCompatActivity {

    private Button playFromZeroButton;
    private Button playFromLastStageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        playFromLastStageButton = findViewById(R.id.replay_from_last_stage_but);
        playFromZeroButton = findViewById(R.id.replay_from_zero_but);
        playFromZeroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGameStatePreferences();
                startNewGame();
            }
        });

        playFromLastStageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGame();
            }
        });
    }

    private void startNewGame(){
        Intent i = new Intent(this,CountDownActivity.class);
        startActivity(i);
    }

    private void resetGameStatePreferences(){
        SharedPreferences pref = getSharedPreferences(GameHeaderFragment.PREFERENCES_NAME,0);
        pref.edit().clear().commit();
    }
}
