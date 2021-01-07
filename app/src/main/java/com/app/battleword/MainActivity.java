package com.app.battleword;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.app.battleword.tasks.UpdateWordTask;
import com.app.utils.Strings;
import com.app.utils.Utils;

import java.util.concurrent.Callable;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer flutMediaPlayer;
     private CountDownTimer countDownTimer;
     private FirstTimeGameFragment firstTimeGameFragment;
     private FragmentTransaction ft;
     //private MediaPlayer dingle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Callable c = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firstTimeGameFragment = (FirstTimeGameFragment) getSupportFragmentManager().findFragmentById(R.id.first_time_app_fragment);

        if(Utils.hasInternet(this))
            new UpdateWordTask(this).execute("");
        //Utils.resetGameStatePreferences(this,Strings.GAME_STATE_PREF);
        final Intent gameSetupIntent = new Intent(this,GameSetupActivity.class);
        boolean isFirstTimeAppInstall = Utils.getBooleanSharedPreferences(this, Strings.FIRST_TIME_APP_INSTALLED_PREF,Strings.FIRST_TIME_APP_KEY,true);

        hideFirstAppFragment();
        if (isFirstTimeAppInstall){
            c = new Callable() {
                @Override
                public Object call() throws Exception {
                    showFirstAppFragment();
                    return null;
                }
            };
            Utils.doAfter(3000,c);
        }
        else {
             c = new Callable() {
                 @Override
                 public Object call() throws Exception {

                         startActivity(gameSetupIntent);

                     return null;
                 }
             };
             Utils.doAfter(4000,c);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        //dingle = Utils.playSound(this,R.raw.game_generic,false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (flutMediaPlayer!=null){
            try {
                if(flutMediaPlayer.isPlaying()){
                    flutMediaPlayer.pause();
                    flutMediaPlayer.stop();
                    flutMediaPlayer.release();
                }

            }
            catch (Exception e){}
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }



    private void showFirstAppFragment(){
        if(firstTimeGameFragment!=null && firstTimeGameFragment.isHidden()){
            Utils.playSound(this,R.raw.new_activity_sound,false);
            flutMediaPlayer = Utils.playSound(this,R.raw.flute_sound,true);
            ft = getSupportFragmentManager().beginTransaction();
            ft.show(firstTimeGameFragment);
            ft.commit();
        }
    }

    private void hideFirstAppFragment(){
        if(firstTimeGameFragment!=null && !firstTimeGameFragment.isHidden()){
            ft = getSupportFragmentManager().beginTransaction();
            ft.hide(firstTimeGameFragment);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
