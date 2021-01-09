package com.app.battleword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
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
    private ScreenFragment screenFragment;
    private Callable dingleCallable ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_control);


        if (savedInstanceState != null) {
            //Restore the fragment's instance
            screenFragment = (ScreenFragment)getSupportFragmentManager().getFragment(savedInstanceState, "screenFragment");
            gameHeaderFragment = (GameHeaderFragment) getSupportFragmentManager().getFragment(savedInstanceState, "gameHeaderFragment");
        }
        else {
            gameHeaderFragment = (GameHeaderFragment)getSupportFragmentManager().findFragmentById(R.id.game_header_fragment);
            screenFragment = (ScreenFragment) getSupportFragmentManager().findFragmentById(R.id.screen_fragment);



        }
        dingleCallable = new Callable() {
            @Override
            public Object call() throws Exception {
                playDingle();
                return null;
            }
        };
        Utils.doAfter(100, dingleCallable);

        screenTextViewModel =  ViewModelProviders.of(this).get(ScreenTextViewModel.class);

        /*screenTextViewModel.getStopDingle().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean)
                    stopDingle();
            }
        });*/

        /*screenTextViewModel.setRequiredText(gameText);
        screenTextViewModel.initText(iniText);*/

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        getSupportFragmentManager().putFragment(outState, "gameHeaderFragment", gameHeaderFragment);
        getSupportFragmentManager().putFragment(outState, "screenFragment", screenFragment);

        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onResume() {
       super.onResume();
    }



    private void playDingle(){
        dingle = Utils.playSound(getApplicationContext(),R.raw.battleword_generic,true);
    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ///stopDingle();
    }

    public void stopDingle() {
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
