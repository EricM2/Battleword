package com.app.battleword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;

import com.app.battleword.objects.Word;
import com.app.battleword.viewmodels.GameStateViewModel;
import com.app.battleword.viewmodels.WordViewModel;
import com.app.utils.Strings;
import com.app.utils.Utils;

import java.util.concurrent.Callable;


public class PlayerControlActivity extends AppCompatActivity {
    private WordViewModel wordViewModel;

    //private  String gameText =Utils.getRandomWord();
    //String iniText = Utils.initScreemFromText(gameText);
    private MediaPlayer dingle;
    private  GameHeaderFragment gameHeaderFragment;
    private ScreenFragment screenFragment;
    private Callable dingleCallable ;
    private CloseGameListener closeGameListener;
    private IntentFilter closeGameIntentFilter;
    private boolean isAppWentToBg;
    private boolean  isWindowFocused ;
    private GameEngineService gameEngineService;
    private boolean mBound;
    private GameStateViewModel gameStateViewModel;
    private int currentStage;
    private boolean wasPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_control);
        closeGameIntentFilter = new IntentFilter(Strings.CLOSE_GAME_INTENT_FILTER);
        closeGameListener = new CloseGameListener(this);
        registerReceiver(closeGameListener,closeGameIntentFilter);
        isAppWentToBg = false;
        isWindowFocused = false;

        /*if (savedInstanceState != null) {
            screenFragment = (ScreenFragment)getSupportFragmentManager().getFragment(savedInstanceState, "screenFragment");
            gameHeaderFragment = (GameHeaderFragment) getSupportFragmentManager().getFragment(savedInstanceState, "gameHeaderFragment");
        }*/
       // else {
            gameHeaderFragment = (GameHeaderFragment)getSupportFragmentManager().findFragmentById(R.id.game_header_fragment);
            screenFragment = (ScreenFragment) getSupportFragmentManager().findFragmentById(R.id.screen_fragment);
            if(savedInstanceState == null ) {
                Intent intent1 = new Intent(getApplicationContext(), BackgroundSoundService.class);
                startService(intent1);
            }

            if(savedInstanceState!=null){
                wasPaused = savedInstanceState.getBoolean("was_paused");
            }
        //}
        /*dingleCallable = new Callable() {
            @Override
            public Object call() throws Exception {
                playDingle();
                return null;
            }
        };
        Utils.doAfter(100, dingleCallable);*/

        wordViewModel =  ViewModelProviders.of(this).get(WordViewModel.class);

        /*wordViewModel.getStopDingle().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean)
                    stopDingle();
            }
        });*/

        /*wordViewModel.setRequiredText(gameText);
        wordViewModel.initText(iniText);*/
        Intent intent = new Intent(this, GameEngineService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(wasPaused && gameEngineService!=null){
            gameEngineService.resumeGame();
            wasPaused = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!wasPaused && gameEngineService!=null){
            gameEngineService.pauseGame();
            wasPaused = true;
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("was_paused",wasPaused);
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        getSupportFragmentManager().putFragment(outState, "gameHeaderFragment", gameHeaderFragment);
        getSupportFragmentManager().putFragment(outState, "screenFragment", screenFragment);

        super.onSaveInstanceState(outState, outPersistentState);
    }



    private void playDingle(){
        dingle = Utils.playSound(getApplicationContext(),R.raw.battleword_generic,true);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        mBound = false;
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
        wordViewModel.updatePauseGame(true);
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
    public void pauseGeneric(){
        Intent i = new Intent(Strings.SOUND_ACTION_INTENT_FILTER);
        i.putExtra(Strings.SOUND_ACTION,Strings.PAUSE);
        sendBroadcast(i);
    }
    public void resumeGeneric(){

            Intent i = new Intent(Strings.SOUND_ACTION_INTENT_FILTER);
            i.putExtra(Strings.SOUND_ACTION,Strings.RESUME);
            sendBroadcast(i);

    }
    public  void  close(){
        finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        isWindowFocused = hasFocus;
        if(hasFocus)
            resumeGeneric();
        else
            pauseGeneric();
        super.onWindowFocusChanged(hasFocus);
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

    private void applicationWillEnterForeground() {
        if (isAppWentToBg) {
            isAppWentToBg = false;
        }
    }

    public void applicationdidenterbackground() {
        if (!isWindowFocused) {
            isAppWentToBg = true;

        }
    }



    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
           GameEngineService.GameEngineBinder binder = (GameEngineService.GameEngineBinder)service;

            gameEngineService = binder.getService();
            mBound = true;
            if(gameEngineService != null){
                gameStateViewModel = gameEngineService.getGameStateViewModel();
                String words = gameStateViewModel.getWordFound().getValue();
                int stage = gameStateViewModel.getStage().getValue();
                int time = gameStateViewModel.getTime().getValue();
                int score = gameStateViewModel.getScore().getValue();

                int lives = gameStateViewModel.getLives().getValue();
                String  screenText = gameStateViewModel.getScreenWord().getValue();
                screenFragment.setScreenText(screenText);

                gameHeaderFragment.buildWordLedsFromWord(words);
                gameHeaderFragment.setStage(stage);
                gameHeaderFragment.setLives(lives);
                gameHeaderFragment.setScore(score);
                gameHeaderFragment.setTime(time);
                gameStateViewModel.getGameOver().observe(gameEngineService, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if(aBoolean)
                            gameHeaderFragment.gameOver();
                    }
                });
                gameStateViewModel.getStageCompleted().observe(gameEngineService, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if(aBoolean)
                            gameHeaderFragment.startNextStageActivity();
                    }
                });
                gameStateViewModel.getTimeCompleted().observe(gameEngineService, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if(aBoolean)
                            screenFragment.setSegondaryScreen(gameStateViewModel.getWord().getValue().getWord());
                        else
                            screenFragment.setSegondaryScreen("");
                    }
                });
                gameStateViewModel.getWord().observe(gameEngineService, new Observer<Word>() {
                    @Override
                    public void onChanged(Word word) {
                        screenFragment.setHint(gameStateViewModel.getWord().getValue().getHint());
                    }
                });
                gameStateViewModel.getScreenWord().observe(gameEngineService, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        screenFragment.setScreenText(s);
                    }
                });
                gameStateViewModel.getWordFound().observe(gameEngineService, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        gameHeaderFragment.buildWordLedsFromWord(s);
                    }
                });

                gameStateViewModel.getLives().observe(gameEngineService, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        gameHeaderFragment.setLives(integer);
                    }
                });
                gameStateViewModel.getStage().observe(gameEngineService, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        gameHeaderFragment.setStage(integer);
                    }
                });
                gameStateViewModel.getScore().observe(gameEngineService, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        gameHeaderFragment.setScore(integer);
                    }
                });
                gameStateViewModel.getTime().observe(gameEngineService, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        gameHeaderFragment.setTime(integer);
                    }
                });
                gameEngineService.playGame();
                Word word  = gameStateViewModel.getWord().getValue();
                if(word!=null)
                    screenFragment.setHint(word.getHint());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    public void keyClicked(String key){
        if(gameStateViewModel!=null){
            gameStateViewModel.updateKeyClicked(key);
        }
    }

    public int getCurrentStage(){
        return currentStage;
    }


}
