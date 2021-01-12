package com.app.battleword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import com.app.battleword.objects.Word;
import com.app.utils.Strings;
import com.app.utils.Utils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class NextStageActivity extends AppCompatActivity {
    private int stage;
    private final static String MODE = "mode";
    private MediaPlayer nextStageSoundPlayer;
    private StageProgressFragment stageProgressFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_stage);
        if(savedInstanceState == null) {
            stageProgressFragment = (StageProgressFragment) getSupportFragmentManager().findFragmentById(R.id.game_progress_fragment);
            nextStageSoundPlayer = null;
            stage = getIntent().getIntExtra("nextStage", 1);
            Callable c = new Callable() {
                @Override
                public Object call() throws Exception {
                    nextStageSoundPlayer = Utils.playSound(NextStageActivity.this,R.raw.nextstage_sound,false);
                    return null;
                }
            };

            Utils.doAfter(200,c);
            (new Handler()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopNextSagePlayer();
                    if (Utils.isStageFirstTime(getApplicationContext(),stage))
                        startActivity(nextActivityIntent(GameScenarioActivity.class));
                    else {
                        startActivity(nextActivityIntent(CountDownActivity.class));
                    }
                }
            }, 5000);
        }
        else{
            stageProgressFragment = (StageProgressFragment) getSupportFragmentManager().getFragment(savedInstanceState, "stageProgressFragment");
        }
    }

    private Intent nextActivityIntent(Class nextActivityClass){

        Intent intent = new Intent(this,nextActivityClass);

        intent.putExtra(MODE,"solitare");
        intent.putExtra("nextStage",stage);
        Map<String,List<Word>> gameWords = (Map<String, List<Word>>) getIntent().getSerializableExtra(Strings.GAMEWORDS);
        intent.putExtra(Strings.GAMEWORDS,(Serializable) gameWords);
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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        getSupportFragmentManager().putFragment(outState, "stageProgressFragment", stageProgressFragment);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {

    }
}
