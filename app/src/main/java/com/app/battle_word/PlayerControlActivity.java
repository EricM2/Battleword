package com.app.battle_word;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;


import android.os.Bundle;
import android.os.PersistableBundle;

import com.app.battle_word.viewmodels.ScreenTextViewModel;
import com.app.utils.Utils;

public class PlayerControlActivity extends AppCompatActivity {
    private ScreenTextViewModel   screenTextViewModel;
    //private  String gameText =Utils.getRandomWord();
    //String iniText = Utils.initScreemFromText(gameText);

    private  GameHeaderFragment gameHeaderFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
}
