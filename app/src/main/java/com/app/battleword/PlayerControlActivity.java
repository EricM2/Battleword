package com.app.battleword;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
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
    private CloseGameListener closeGameListener;
    private IntentFilter closeGameIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_control);
        closeGameIntentFilter = new IntentFilter(Strings.CLOSE_GAME_INTENT_FILTER);
        closeGameListener = new CloseGameListener(this);
        registerReceiver(closeGameListener,closeGameIntentFilter);

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            screenFragment = (ScreenFragment)getSupportFragmentManager().getFragment(savedInstanceState, "screenFragment");
            gameHeaderFragment = (GameHeaderFragment) getSupportFragmentManager().getFragment(savedInstanceState, "gameHeaderFragment");
        }
        else {
            gameHeaderFragment = (GameHeaderFragment)getSupportFragmentManager().findFragmentById(R.id.game_header_fragment);
            screenFragment = (ScreenFragment) getSupportFragmentManager().findFragmentById(R.id.screen_fragment);
            Intent intent = new Intent(getApplicationContext(), BackgroundSoundService.class);
            startService(intent);
        }
        /*dingleCallable = new Callable() {
            @Override
            public Object call() throws Exception {
                playDingle();
                return null;
            }
        };
        Utils.doAfter(100, dingleCallable);*/

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
    protected void onDestroy() {
        super.onDestroy();
        if(closeGameListener!=null)
            unregisterReceiver(closeGameListener);

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

    public void pauseGame(){
        screenTextViewModel.updatePauseGame(true);
    }

    public void stopGeneric(){
        Intent i = new Intent(Strings.SOUND_ACTION_INTENT_FILTER);
        i.putExtra(Strings.SOUND_ACTION,Strings.STOP);
        sendBroadcast(i);
    }
    public void playGeneric(){
        Intent i = new Intent(Strings.SOUND_ACTION_INTENT_FILTER);
        i.putExtra(Strings.SOUND_ACTION,Strings.PLAY);
        sendBroadcast(i);
    }
    public  void  close(){
        finish();
    }


    private static  class CloseGameListener extends BroadcastReceiver{
        private Activity a;
         public  CloseGameListener(Activity activity){
             a = activity;
         }
        @Override
        public void onReceive(Context context, Intent intent) {
            a.finish();
        }
    }

}
