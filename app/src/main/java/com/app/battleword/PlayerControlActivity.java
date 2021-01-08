package com.app.battleword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.app.battleword.objects.Word;
import com.app.battleword.viewmodels.ScreenTextViewModel;
import com.app.utils.Strings;
import com.app.utils.Utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;


public class PlayerControlActivity extends AppCompatActivity {
    private ScreenTextViewModel   screenTextViewModel;

    //private  String gameText =Utils.getRandomWord();
    //String iniText = Utils.initScreemFromText(gameText);
    private MediaPlayer dingle;
    private  GameHeaderFragment gameHeaderFragment;
    private Callable dingleCallable ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dingle = null;
        if (savedInstanceState != null) {
            //Restore the fragment's instance
            gameHeaderFragment = (GameHeaderFragment) getSupportFragmentManager().getFragment(savedInstanceState, "gameHeaderFragment");
        }

        setContentView(R.layout.activity_player_control);
        screenTextViewModel =  ViewModelProviders.of(this).get(ScreenTextViewModel.class);

        dingleCallable = new Callable() {
            @Override
            public Object call() throws Exception {
                playDingle();
                return null;
            }
        };
        Utils.doAfter(200,dingleCallable);

        /*screenTextViewModel.setRequiredText(gameText);
        screenTextViewModel.initText(iniText);*/

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        getSupportFragmentManager().putFragment(outState, "gameHeaderFragment", gameHeaderFragment);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onResume() {
       super.onResume();
    }



    private void playDingle(){
        dingle = Utils.playSound(this,R.raw.battleword_generic,true);
    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if(dingle!=null){
                dingle.pause();
                dingle.stop();
                dingle.release();
            }
        }
        catch (Exception e)
        {

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,LeaveGameActivity.class);
        pauseGame();
        startActivity(intent);
    }

    private void pauseGame(){
        screenTextViewModel.updatePauseGame(true);
    }
}
