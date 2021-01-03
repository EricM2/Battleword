package com.app.battleword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import com.app.utils.Utils;

import java.util.concurrent.Callable;

public class NextStageActivity extends AppCompatActivity {
    private int stage;
    private final static String MODE = "mode";
    private MediaPlayer nextStageSoundPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_stage);
        nextStageSoundPlayer = null;
         stage = getIntent().getIntExtra("nextStage",1);
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(true/*Utils.isStageFirstTime(getApplicationContext(),stage)*/)
                    startActivity(gameScenarioIntent());
                else{
                    Intent intent = countDownIntent();
                    stopNextSagePlayer();
                    startActivity(intent);
                }
            }
        },5000);
    }

    private Intent gameScenarioIntent(){

        Intent intent = new Intent(this,GameScenarioActivity.class);

        intent.putExtra(MODE,"solitare");
        intent.putExtra("nextStage",stage);
        return  intent;
    }

    private Intent countDownIntent(){
        Intent intent = new Intent(this,CountDownActivity.class);
        intent.putExtra("mode","solitare");
        //intent.putExtra(LEVEL, selectedGameLevel);
        return  intent;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Callable c = new Callable() {
            @Override
            public Object call() throws Exception {
                nextStageSoundPlayer = Utils.playSound(NextStageActivity.this,R.raw.nextstage_sound,false);
                return null;
            }
        };
        Utils.doAfter(200,c);
    }

    private void stopNextSagePlayer(){
        try {
            if(nextStageSoundPlayer!=null && nextStageSoundPlayer.isPlaying()){
                nextStageSoundPlayer.pause();
                nextStageSoundPlayer.stop();
                nextStageSoundPlayer.release();
                nextStageSoundPlayer = null;
            }
        }
        catch (Exception e){

        }
    }
}
