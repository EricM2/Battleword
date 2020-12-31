package com.app.battleword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageView;

import com.app.utils.Utils;

public class GameOverActivity extends AppCompatActivity {

    private Button playFromZeroButton;
    private Button playFromLastStageButton;
    private ImageView deadHead;
    private AnimationSet as;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        playFromLastStageButton = findViewById(R.id.replay_from_last_stage_but);
        playFromZeroButton = findViewById(R.id.replay_from_zero_but);
        deadHead = findViewById(R.id.loosed_head);
        as = new AnimationSet(true);
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
        animate(deadHead);
    }

    private void startNewGame(){
        Intent i = new Intent(this,CountDownActivity.class);
        startActivity(i);
    }

    private void resetGameStatePreferences(){
        Utils.resetGameStatePreferences(getApplicationContext(),GameHeaderFragment.PREFERENCES_NAME);
    }

    private void animate(View v){

        AlphaAnimation fadeIn = new AlphaAnimation(0.0f , 1.0f ) ;
        AlphaAnimation fadeOut = new AlphaAnimation( 1.0f , 0.0f ) ;
        fadeIn.setRepeatMode(Animation.INFINITE);
        fadeOut.setRepeatMode(Animation.INFINITE);

        fadeIn.setDuration(500);
        fadeOut.setDuration(500);
        v.setAnimation(fadeIn);
        v.setAnimation(fadeOut);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        as = null;
    }
}
