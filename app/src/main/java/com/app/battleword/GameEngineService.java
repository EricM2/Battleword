package com.app.battleword;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.battleword.objects.Word;
import com.app.battleword.subscribers.WordTimeCompletedSubscriber;
import com.app.battleword.viewmodels.GameStateViewModel;
import com.app.battleword.viewmodels.WordViewModel;
import com.app.utils.GameTime;
import com.app.utils.Limit;
import com.app.utils.Strings;
import com.app.utils.Touch;
import com.app.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameEngineService extends LifecycleService  {
    private GameStateViewModel gameStateViewModel;
    private int lives=5;
    private  int score=0;
    private int stage =1;
    private final IBinder binder = new GameEngineBinder();
    private int  numLifes= 5;
    private CountDownTimer countDownTimer;
    private  int currentWordNum=0;
    private int currentStage = 1;
    private int currentScore = 0;
    private WordViewModel wordViewModel;
    private String currentScreenText;
    private int lastStageLifes = 5;
    private String lastStageScore="0";
    private int currenTime = 0;
    private String gameLanguage;
    private boolean fiveSecLeft;
    private MediaPlayer fiveSecLeftPlayer;
    private MediaPlayer stageWinPlayer;
    private String words;
    public int pausedTime = 0;
    private Word currentWord;
    private String currentWordHint;
    private Map<String, List<Word>> gameWords;
    private int lastTimeValue =0;
    private boolean wasPaused = false;
    private boolean isGameOver = false;
    private boolean isPlaying = false;
    private Integer numTouches;
    private GameStateReceiver gameStateReceiver;
    private IntentFilter gameStateIntentFilter;

    public GameEngineService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        super.onBind(intent);
        return  binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        retreiveprefs();
        gameStateIntentFilter = new IntentFilter(Strings.GAME_STATE_INTENT_FILTER);
        gameStateReceiver = new GameStateReceiver(this);
        registerReceiver(gameStateReceiver,gameStateIntentFilter);
        gameStateViewModel = new GameStateViewModel(lives,score,stage);
        gameStateViewModel.getClickedKey().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                updateScreen(s);
            }
        });

        gameStateViewModel.getScreenWord().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(Utils.isSreenTextComplete(s)) {
                    if ( gameStateViewModel.getTime().getValue() > 0){
                        gameStateViewModel.updateAllowWordUpdate(false);
                        words += "1";
                        gameStateViewModel.updateWordFound(words);
                        currentWordNum++;
                        updatScore(100);
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                            countDownTimer = null;
                        }
                        (new Handler()).postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                gameStateViewModel.updateTimeCompleted(false);
                                gameStateViewModel.updateAllowWordUpdate(true);
                                isPlaying = false;
                                if (!isGameOver) {
                                    playGame();
                                }
                            }
                        }, 2000);

                }


                }
            }
        });
        return START_STICKY;
    }



    public class GameEngineBinder extends Binder {
        GameEngineService getService() {
            // Return this instance of LocalService so clients can call public methods
            return GameEngineService.this;
        }
    }

    public GameStateViewModel getGameStateViewModel() {
        return gameStateViewModel;
    }

    private void initStage(){
        if(gameStateViewModel!=null &&gameStateViewModel.getStageCompleted().getValue()!=null
                && gameStateViewModel.getStageCompleted().getValue() ){
            gameStateViewModel.updateStageCompleted(false);
        }
        if(gameStateViewModel!=null &&gameStateViewModel.getGameOver().getValue()!=null
                && gameStateViewModel.getGameOver().getValue() ){
            gameStateViewModel.updateGameOver(false);
        }
    }

    public  void playGame(){
        initStage();
        if(gameWords== null ) {
            loadWords(stage);

        }

        if(!isPlaying && countDownTimer == null && currentWordNum< 10){
            try {

                //final long max1 = GameTime.getTime(stage);
                final long max1 = 8000;
                final long max = max1 - pausedTime * max1 / 100;
                if(!wasPaused) {
                    Word w = Utils.getNewWord(gameWords, stage, currentWordNum);
                    gameStateViewModel.updateWord(w);
                    String initext = Utils.initScreemFromText(w.getWord(), stage);
                    gameStateViewModel.updateScreenWord(initext);
                    pausedTime =0;
                }
                if(wasPaused)
                    wasPaused =! wasPaused;
                countDownTimer  = new CountDownTimer(max,100) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        long time = max1 - millisUntilFinished;
                        int t = (int)(time * 100 / max1);
                        gameStateViewModel.updateTime(t);
                        if ((millisUntilFinished <= 5000) && !fiveSecLeft) {
                            fiveSecLeftPlayer = Utils.playSound(GameEngineService.this, R.raw.fivesec_left_sound, false);
                            fiveSecLeft = true;
                        }
                    }

                    @Override
                    public void onFinish()
                    {    words +="0";
                        gameStateViewModel.updateTimeCompleted(true);
                        gameStateViewModel.updateWordFound(words);
                        currentWordNum++;
                        stopFiveSecLeftSound();
                        gameStateViewModel.updateAllowWordUpdate(false);
                        (new Handler()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(countDownTimer!=null){
                                    countDownTimer.cancel();
                                    countDownTimer = null;
                                }
                                gameStateViewModel.updateTimeCompleted(false);
                                gameStateViewModel.updateAllowWordUpdate(true);
                                isPlaying = false;
                                decrementLives();
                                if (!isGameOver) {
                                    playGame();
                                }
                            }
                        }, 2000);




                    }
                };
                countDownTimer.start();
                isPlaying = true;
            } catch (Exception e) {
                e.printStackTrace();
            }



        }
        else {
            if(currentWordNum == 10){

                if ((currentStage == 4 && getNumWorfound() < Limit.LIMIT_STAGE_4) || (currentStage == 5 && getNumWorfound() < Limit.LIMIT_STAGE_5)) {
                    //scoreTextView.setText(lastStageScore);
                    gameOver();
                } else {
                    if (currentStage < 5) {
                    if (!isGameOver) {
                        if (stageWinPlayer == null || (stageWinPlayer != null && !stageWinPlayer.isPlaying())){
                            stageWinPlayer = Utils.playSound(GameEngineService.this, R.raw.stage_win_sound, true);
                        stopGame();
                        (new Handler()).postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                currentWordNum = 0;
                                stage = stage + 1;
                                Utils.saveIntSharedPreferences(getApplicationContext(), Strings.GAME_STATE_PREF, "laststagelives", numLifes);
                                words = "";
                                stopStageWinSound();
                                gameStateViewModel.updateStage(stage);
                                gameStateViewModel.updateWordFound(words);
                                gameStateViewModel.updateTime(0);
                                gameStateViewModel.updateTouches(30);
                                gameStateViewModel.updateTimeCompleted(false);
                                gameWords = null;
                                startNextStageActivity();
                                lastStageLifes = lives;
                                lastStageScore = String.valueOf(score);
                                gameStateViewModel.updateStageCompleted(false);


                            }
                        }, 5000);

                    }

                    }


                } else {
                    if (currentStage == 5) {
                        if (!isGameOver) {
                            Intent gameWonIntent = new Intent(GameEngineService.this, GameWonActivity.class);
                            startActivity(gameWonIntent);
                            Intent soundServiceIntent = new Intent(GameEngineService.this, BackgroundSoundService.class);
                            stopService(soundServiceIntent);
                            stopStageWinSound();
                            stopFiveSecLeftSound();

                        }
                    }
                }
            }
        }

        }

    }

    private void loadWords(int stage){
        gameWords = new HashMap<>();
        if(Utils.apiWords!=null && Utils.apiWords.get("stage"+String.valueOf(stage))!=null
                && Utils.apiWords.get("stage"+String.valueOf(stage)).size()>= 10) {
            gameWords.put("stage"+String.valueOf(stage),Utils.apiWords.get("stage"+String.valueOf(stage)));

        }
        else {
            String prefCode = Utils.getGameLanguage(getApplicationContext(), Strings.GAME_LANGUAGE_PREF, Strings.LANGUAGE_PREF);
            String lang = prefCode.split("-")[0];

            String key = "stage"+String.valueOf(stage);
            try {
                List<Word> ws= Utils.innerGameWords.get("stage"+String.valueOf(stage));
                Collections.shuffle(ws);

                gameWords.put(key, ws);
            }
            catch (Exception e){
                Log.d("Exception", e.getMessage());
            }

        }
    }


    private void retreiveprefs() {
       // gameLanguage = Utils.getGameLanguage(getActivity().getApplicationContext(), Strings.GAME_LANGUAGE_PREF,Strings.LANGUAGE_PREF);
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(Strings.GAME_STATE_PREF, 0);
        if (prefs.contains("stage")) {
            stage = prefs.getInt("stage",1);
        }

        if (prefs.contains("lives")) {
            lives =  prefs.getInt("lives",5);
            lastStageLifes = lives;

        }
        if (prefs.contains("score")) {
            score =  Integer.valueOf(prefs.getString("score","0"));
            lastStageScore = prefs.getString("score","0");

        }
        words = "";

    }

    public void stopGeneric(){
        Intent i = new Intent(Strings.SOUND_ACTION_INTENT_FILTER);
        i.putExtra(Strings.SOUND_ACTION,Strings.STOP);
        sendBroadcast(i);
    }

    private void updateScreen(String key){
        if(gameStateViewModel.getWord().getValue()!=null
                && gameStateViewModel.getWord().getValue().getWord()!=null
                && gameStateViewModel.getScreenWord()!=null
                && gameStateViewModel.getScreenWord().getValue()!=null) {

            String screenText =  gameStateViewModel.getScreenWord().getValue();
            if(!key.isEmpty() && !Utils.isSreenTextComplete(screenText)) {
                numTouches = gameStateViewModel.getTouches().getValue();
                numTouches++;
                if (gameStateViewModel.getAllowWordUpdate().getValue()) {
                    if (Touch.getNumTouches(stage) != -1 && numTouches <= Touch.getNumTouches(stage)) {
                        Log.d("KEY_PRESSED", key);
                        //Toast.makeText(getContext(), l, Toast.LENGTH_SHORT).show();
                        String newString = Utils.putCharInScreenText(key, gameStateViewModel.getWord().getValue().getWord(), screenText, this);
                        Log.d("NEW_STRING", newString);
                        gameStateViewModel.updateScreenWord(newString);
                        gameStateViewModel.updateTouches(numTouches);
                    } else {
                        if (Touch.getNumTouches(stage) == -1) {
                            Log.d("KEY_PRESSED", key);
                            //Toast.makeText(getContext(), l, Toast.LENGTH_SHORT).show();
                            String newString = Utils.putCharInScreenText(key, gameStateViewModel.getWord().getValue().getWord(), screenText, this);
                            Log.d("NEW_STRING", newString);
                            gameStateViewModel.updateScreenWord(newString);
                        }
                    }
                }
            }


        }

    }

    private void stopFiveSecLeftSound(){
        if ( fiveSecLeftPlayer!= null) {
            try {
                if(fiveSecLeftPlayer.isPlaying()){
                    fiveSecLeftPlayer.pause();

                }
                fiveSecLeftPlayer.stop();
                fiveSecLeftPlayer.release();
            }
            catch (Exception e){

            }


            fiveSecLeftPlayer= null;
        }
        fiveSecLeft= false;
    }

    private void stopStageWinSound(){
        if ( stageWinPlayer!= null) {
            try {
                if(stageWinPlayer.isPlaying()){
                    stageWinPlayer.pause();
                    stageWinPlayer.stop();
                    stageWinPlayer.release();
                }
            }
            catch (Exception e){

            }


            stageWinPlayer= null;
        }

    }

    private void updatScore(final int score) {
        final int currentScore = gameStateViewModel.getScore().getValue();
        long cscore = 0;
        CountDownTimer cd = new CountDownTimer(1000, 1000 / score) {
            @Override
            public void onTick(long millisUntilFinished) {
                long cscore = currentScore + score * (1000 - millisUntilFinished) / 1000;
                gameStateViewModel.updateScore((int)cscore);
            }

            @Override
            public void onFinish() {
                int finScore = currentScore + score;
                if (finScore >= 1000) {
                    int numlifes = (int) (finScore / 1000);
                    for (int i = 0; i < numlifes; i++)
                        incrementLives();
                }
                int res = finScore - 1000 * (finScore / 1000);
                gameStateViewModel.updateScore((int)res);
            }
        }.start();
    }
    public int getNumWorfound(){
        int res = 0;
        if( words.length()==10){
            // String[] v = words.split("");
            for (int i = 0; i<10; i++){
                if(String.valueOf(words.charAt(i)).equalsIgnoreCase("1"))
                    res++;
            }
        }
        return res;
    }

    private void stopGame(){
        if (countDownTimer!=null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        isPlaying = false;
    }

    private void startNextStageActivity(){
        stopGame();
        gameStateViewModel.updateStageCompleted(true);

    }

    public void gameOver(){
        isGameOver = true;
        stopStageWinSound();
        stopFiveSecLeftSound();
        stopGeneric();
        stopGame();
        gameStateViewModel.updateGameOver(true);
        stopSelf();


    }

    private void incrementLives(){
        if( lives < 10 && lives > 0 ){
            lives++;
            gameStateViewModel.updateLives(lives);
            Utils.playSound(this,R.raw.new_life_sound,false);
        }


    }

    private void decrementLives(){
        if( lives < 10 && lives > 0 ){
            lives--;
            if(lives== 0)
                gameOver();
            else
                gameStateViewModel.updateLives(lives);
        }


    }

    private void savePrefs(int stage, String score, int numLifes) {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(Strings.GAME_STATE_PREF, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("stage", stage);
        editor.putString("score", score);
        editor.putInt("lives",numLifes);
        editor.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopGame();
        stopStageWinSound();
        stopFiveSecLeftSound();
        savePrefs(stage,lastStageScore,lastStageLifes);
        unregisterReceiver(gameStateReceiver);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }


    public void pauseGame(){
        if(countDownTimer!= null){
            pausedTime = gameStateViewModel.getTime().getValue();
            wasPaused = true;
            stopGame();
        }

    }

    public void resumeGame(){
        if(wasPaused){
            playGame();
        }
    }

   public void resetToLastStageState(){
        score = Integer.valueOf(lastStageScore);
        lives = lastStageLifes;
        words = "";
        pausedTime = 0;
        stopGame();
        if(gameStateViewModel!=null){
            gameStateViewModel.updateStage(stage);
            gameStateViewModel.updateGameOver(false);
            gameStateViewModel.updateTime(0);
            gameStateViewModel.updateScore(score);
            gameStateViewModel.updateLives(lives);
        }

   }


    public void resetToZero(){
        score = 0;
        lives = 5;
        stage = 1;
        words = "";
        pausedTime = 0;
        stopGame();
        if(gameStateViewModel!=null){
            gameStateViewModel.updateGameOver(false);
            gameStateViewModel.updateStage(stage);
            gameStateViewModel.updateTime(0);
            gameStateViewModel.updateScore(0);
            gameStateViewModel.updateLives(lives);
        }

    }

    private static class GameStateReceiver extends BroadcastReceiver {
        private GameEngineService contextService;
        public GameStateReceiver(GameEngineService c){
            this.contextService =c;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra(Strings.GAME_ENGINE_ACTION);
            if(action.equalsIgnoreCase(Strings.RESET_TO_ZERO))
                contextService.resetToZero();
            if(action.equalsIgnoreCase(Strings.RESET_TO_LAST_STAGE))
                contextService.resetToLastStageState();



        }
    }

    /*private void findNewWord() {
        stopFiveSecLeftSound();
        currentWordNum++;
        if (currentWordNum <= 10) {
            gameStateViewModel.updateTime(0);
            //screenTextViewModel.updateCurrentTime(timeProgressBar.getProgress());

            currentWord = Utils.getNewWord(gameWords,currentStage,currentWordNum-1);
            gameStateViewModel.updateWord(currentWord);
            gameStateViewModel.updateStage(stage);
            //String gameText =currentWord.getWord();
            //currentWordHint = currentWord.getHint();
            //screenTextViewModel.updateWordHint(currentWordHint);
            //int stage = Integer.valueOf(stageTextView.getText().toString());
            //String newInitWord = Utils.initScreemFromText(gameText,stage);
            // screenTextViewModel.initText(newInitWord);
            //screenTextViewModel.updateScreenText(newInitWord);
            //screenTextViewModel.setRequiredText(gameText);
            gameStateViewModel.updateTouches(0);
            //screenTextViewModel.updateNumTouch(0);
            if(numLifes > 0) {


                try {
                    startGame();
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }


            }
            else{
                if (numLifes<=0)
                    gameOver();
            }



        }

        else {
            if ((currentStage == 4 && getNumWorfound() < Limit.LIMIT_STAGE_4) || (currentStage == 5 && getNumWorfound() < Limit.LIMIT_STAGE_5)) {
                scoreTextView.setText(lastStageScore);
                gameOver();
            }

            else {
                if (currentStage < 5) {
                    if(!isGameOver) {
                        stageWinPlayer = Utils.playSound(this, R.raw.stage_win_sound, true);

                        (new Handler()).postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                currentWordNum = 1;
                                currentStage = currentStage + 1;
                                Utils.saveIntSharedPreferences(getApplicationContext(), Strings.GAME_STATE_PREF, "laststagelives", numLifes);
                                words = "";
                                stopStageWinSound();
                                startNextStageActivity();
                                lastStageLifes = numLifes;
                                lastStageScore = scoreTextView.getText().toString();

                                screenTextViewModel.updateStage(String.valueOf(currentStage));
                                setAllLedInvisible();
                                getActivity().finish();
                            }
                        }, 5000);

                    }


                }
                else {
                    if(currentStage == 5){
                        if(!isGameOver ){
                            Intent gameWonIntent = new Intent(getActivity(),GameWonActivity.class);
                            startActivity(gameWonIntent);
                            Intent soundServiceIntent = new Intent(getActivity(),BackgroundSoundService.class);
                            getActivity().stopService(soundServiceIntent);
                            stopStageWinSound();
                            stopFiveSecLeftSound();
                            getActivity().finish();
                        }
                    }
                }
            }

        }



    }


    }

    private void stopGame(){
        if (countDownTimer!=null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }





    private  void pauseGame(boolean pause){
        if(pause){
            lastTimeValue =timeProgressBar.getProgress();
            countDownTimer.cancel();
            wasPaused = true;

        }
    }

    private  void stopDingle(){
        ((PlayerControlActivity)getActivity()).stopGeneric();
    }

   */

}
