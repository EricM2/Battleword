package com.app.battleword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageView;

import com.app.utils.Strings;
import com.app.utils.Utils;

import java.util.concurrent.Callable;

public class GameOverActivity extends AppCompatActivity {

    private Button playFromZeroButton;
    private Button playFromLastStageButton;
    private ImageView deadHead;
    private AnimationSet as;
    private MediaPlayer flutMediaPlayer;

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
                stopGameOverSound();
                startNewGame();
            }
        });


        playFromLastStageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int numLifes = Utils.getIntSharedPreferences(getApplicationContext(),Strings.GAME_STATE_PREF,"laststagelives");
               if (numLifes!=-1)
                    Utils.saveIntSharedPreferences(getApplicationContext(),Strings.GAME_STATE_PREF,"lives",numLifes);
               Utils.saveStringSharedPreferences(getApplicationContext(),Strings.GAME_STATE_PREF,"words","");
               Utils.saveIntSharedPreferences(getApplicationContext(),Strings.GAME_STATE_PREF,"paused_time",0);

               stopGameOverSound();
               startNewGame();
            }
        });
        animate(deadHead);
    }

    private void startNewGame(){
        Intent i = new Intent(this, LoadWordsActivity.class);
        int stage = getIntent().getIntExtra(Strings.NEXT_STAGE_TO_PLAY,1);
        i.putExtra(Strings.NEXT_STAGE_TO_PLAY,stage);
        startActivity(i);
    }

    private void resetGameStatePreferences(){
        Utils.resetGameStatePreferences(getApplicationContext(), Strings.GAME_STATE_PREF);
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
    protected void onResume() {
        super.onResume();
        Callable c = new Callable() {
            @Override
            public Object call() throws Exception {
                Utils.playSound(GameOverActivity.this,R.raw.loose_sound,false);
                return null;
            }
        };
        Callable c1 = new Callable() {
            @Override
            public Object call() throws Exception {
               flutMediaPlayer = Utils.playSound(GameOverActivity.this,R.raw.flute_sound,true);
                return null;
            }
        };
        Utils.doAfter(200,c);
        Utils.doAfter(400,c1);

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopGameOverSound();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        as = null;
    }
    private void stopGameOverSound(){
        if (flutMediaPlayer!=null){
            try {
                if(flutMediaPlayer.isPlaying()){
                    flutMediaPlayer.stop();
                    flutMediaPlayer.pause();
                }
                flutMediaPlayer.release();

            }
            catch (Exception e){}
        }
    }
}
