package com.app.battleword;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;

import com.app.battleword.objects.Word;
import com.app.battleword.tasks.UpdateWordTask;
import com.app.utils.Strings;
import com.app.utils.Utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer flutMediaPlayer;
     private CountDownTimer countDownTimer;
     private FirstTimeGameFragment firstTimeGameFragment;
     private FragmentTransaction ft;
     Map<String, List<Word>> words = null;
     //private MediaPlayer dingle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Callable c = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean newGame = getIntent().getBooleanExtra(Strings.NEW_GAME,false);
        boolean isFirstTimeAppInstall = Utils.getBooleanSharedPreferences(this, Strings.FIRST_TIME_APP_INSTALLED_PREF,Strings.FIRST_TIME_APP_KEY,true);
        if(savedInstanceState!=null) {
            firstTimeGameFragment = (FirstTimeGameFragment) getSupportFragmentManager().getFragment(savedInstanceState, "firstTimeGameFragment");
            showFirstAppFragment();
        }

        else {
            final Intent gameSetupIntent = new Intent(this,GameSetupActivity.class);
            gameSetupIntent.putExtra(Strings.NEW_GAME,newGame);


            if (isFirstTimeAppInstall){
                Utils.saveBooleanSharedPreferences(getApplicationContext(),Strings.FIRST_TIME_APP_INSTALLED_PREF,Strings.FIRST_TIME_APP_KEY,false);
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
                        finish();

                        return null;
                    }
                };
                Utils.doAfter(4000,c);
            }
            firstTimeGameFragment = (FirstTimeGameFragment) getSupportFragmentManager().findFragmentById(R.id.first_time_app_fragment);
            hideFirstAppFragment();
        }


            //new UpdateWordTask(this).execute("");
        //Utils.resetGameStatePreferences(this,Strings.GAME_STATE_PREF);






    }

    @Override
    protected void onResume() {
        super.onResume();
        //dingle = Utils.playSound(this,R.raw.game_generic,false);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
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



    private void showFirstAppFragment(){
        flutMediaPlayer = Utils.playSound(this,R.raw.flute_sound,true);
        if(firstTimeGameFragment!=null && firstTimeGameFragment.isHidden()){
            Utils.playSound(this,R.raw.new_activity_sound,false);
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        getSupportFragmentManager().putFragment(outState, "firstTimeGameFragment", firstTimeGameFragment);

    }
}
