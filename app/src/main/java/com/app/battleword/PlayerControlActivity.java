package com.app.battleword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.app.battleword.viewmodels.ScreenTextViewModel;
import com.app.utils.Utils;

public class PlayerControlActivity extends AppCompatActivity {
    private ScreenTextViewModel   screenTextViewModel;
    //private  String gameText =Utils.getRandomWord();
    //String iniText = Utils.initScreemFromText(gameText);
    private MediaPlayer dingle;
    private  GameHeaderFragment gameHeaderFragment;
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
        dingle = Utils.playSound(this,R.raw.game_genericwav,true);
    }

    @Override
    protected void onPause() {
        super.onPause();
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
}
