package com.app.battleword;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.battleword.objects.Word;
import com.app.battleword.subscribers.WordTimeCompletedSubscriber;
import com.app.battleword.viewmodels.WordViewModel;
import com.app.utils.GameTime;
import com.app.utils.Limit;
import com.app.utils.Strings;
import com.app.utils.Touch;
import com.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class GameHeaderFragment extends Fragment   {
    private ImageView life1;
    private ImageView life2;
    private ImageView life3;
    private ImageView life4;
    private ImageView life5;
    private ImageView life6;
    private ImageView life7;
    private ImageView life8;
    private ImageView life9;
    private ImageView life10;
    private ImageView led1;
    private ImageView led2;
    private ImageView led3;
    private ImageView led4;
    private ImageView led5;
    private ImageView led6;
    private ImageView led7;
    private ImageView led8;
    private ImageView led9;
    private ImageView led10;
    private Button settingsButton;
    private ProgressBar timeProgressBar;
    private TextView scoreTextView;
    private TextView stageTextView;
    private int  numLifes= 5;
    private ImageView[] leds;
    private ImageView[] lifes;
    private CountDownTimer countDownTimer;
    private List<WordTimeCompletedSubscriber> subscibers = new ArrayList<>();
    private  int currentWordNum=0;
    private int currentStage = 1;
    private int currentScore = 0;
    private String score = "0";
    private WordViewModel wordViewModel;
    private String currentScreenText;
    //private boolean[] wordsMask = new boolean[]{};
    private int lastStageLifes = 5;
    private String lastStageScore="0";
    private int currenTime = 0;
    private String gameLanguage;
    private boolean fiveSecLeft;
    private MediaPlayer fiveSecLeftPlayer;
    private MediaPlayer stageWinPlayer;
    private String words;
    private Word currentWord;
    private String currentWordHint;
    private Map<String, List<Word>> gameWords;
    private int lastTimeValue =0;
    private boolean wasPaused = false;
    private Boolean settingBut;
    private boolean isGameOver = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_game_header, container, false);
        fiveSecLeft = false;
        fiveSecLeftPlayer = null;
        stageWinPlayer = null;
        currentWordHint = "";
        life1 = v.findViewById(R.id.life_1);
        life2 = v.findViewById(R.id.life_2);
        life3 = v.findViewById(R.id.life_3);
        life4 = v.findViewById(R.id.life_4);
        life5 = v.findViewById(R.id.life_5);
        life6 = v.findViewById(R.id.life_6);
        life7 = v.findViewById(R.id.life_7);
        life8 = v.findViewById(R.id.life_8);
        life9 = v.findViewById(R.id.life_9);
        life10 = v.findViewById(R.id.life_10);
        led1 = v.findViewById(R.id.word_found_led_1);
        led2 = v.findViewById(R.id.word_found_led_2);
        led3 = v.findViewById(R.id.word_found_led_3);
        led4 = v.findViewById(R.id.word_found_led_4);
        led5 = v.findViewById(R.id.word_found_led_5);
        led6 = v.findViewById(R.id.word_found_led_6);
        led7 = v.findViewById(R.id.word_found_led_7);
        led8 = v.findViewById(R.id.word_found_led_8);
        led9 = v.findViewById(R.id.word_found_led_9);
        led10 = v.findViewById(R.id.word_found_led_10);
        leds = new ImageView[]{led1,led2,led3,led4,led5, led6, led7, led8,led9,led10};
        lifes = new ImageView[]{life1,life2,life3,life4,life5,life6,life7,life8,life9,life10};
        timeProgressBar = v.findViewById(R.id.time_progressbar);
        settingsButton = v.findViewById(R.id.settings_button);
        scoreTextView  = v.findViewById(R.id.score);
        stageTextView = v.findViewById(R.id.stage_value);
        words = "";
        currentWord = null;
        setAllLedInvisible();

        retreiveprefs();
        lastStageLifes = numLifes;
        lastStageScore = score;

        /*if (savedInstanceState != null) {
                Log.d("onCreateView", "onCreateView: ");

                currentStage = savedInstanceState.getInt("stage");
                numLifes = savedInstanceState.getInt("lives");
                currentWordNum = savedInstanceState.getInt("wordNum");
                score = savedInstanceState.getString("score");
                words = savedInstanceState.getString("words");
                lastTimeValue = savedInstanceState.getInt("time");
                currenTime = lastTimeValue;
                buildWordLedsFromWord(words);
                currentWordNum = words.length();
                //redrawLeds(wordsMask);
                setLives(numLifes);
            }
            else {


            }*/

        /*setLives(numLifes);
        timeProgressBar.setProgress(currenTime);
        stageTextView.setText(String.valueOf(currentStage));
        scoreTextView.setText(score);*/
        currentScore = Integer.valueOf(score);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menuActivityIntent = new Intent(getActivity(),GameMenuActivity.class);
                startActivity(menuActivityIntent);
                ((PlayerControlActivity) getActivity()).pauseGame();
            }
        });
        gameWords =  (Map<String,List<Word>>)getActivity().getIntent().getSerializableExtra(Strings.GAMEWORDS);

        wordViewModel = new ViewModelProvider(requireActivity()).get(WordViewModel.class);
        wordViewModel.updateGameWords(gameWords);
        wordViewModel.updateStage(String.valueOf(currentStage));
        wordViewModel.getNumTouch().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if( Touch.getNumTouches(currentStage)!=-1 && integer >= Touch.getNumTouches(currentStage)){

                    if(currentScreenText!=null && !Utils.isSreenTextComplete(currentScreenText)){

                        if(countDownTimer!=null) {
                            countDownTimer.cancel();
                            countDownTimer = null;
                            lastTimeValue = 0;
                        }
                        wordViewModel.updateSecondScreenText(wordViewModel.getRequiredText());
                        wordViewModel.updateAllowWordUpdate(false);
                        (new Handler()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                wordViewModel.updateTurnOffSecondScreenText(true);
                                lastTimeValue = 0;
                                updateWorFoundStatus(false,currentWordNum);
                                if(!isGameOver) {
                                    wordViewModel.updateAllowWordUpdate(true);
                                    if(!wasPaused)
                                        findNewWord();

                                }
                            }
                        }, 2000);

                    }
                }
            }
        });

        wordViewModel.getScreenText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                currentScreenText = s;
                if(Utils.isSreenTextComplete(s) && timeProgressBar.getProgress()<100){
                    if(countDownTimer!=null) {
                        countDownTimer.cancel();
                        lastTimeValue = 0;
                        countDownTimer = null;
                    }
                    updatScore(100);
                    (new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            updateWorFoundStatus(true,currentWordNum);
                            if(!isGameOver && !wasPaused)
                                findNewWord();
                        }
                    }, 2000);


                }
            }
        });

        wordViewModel.getPauseGame().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                pauseGame(aBoolean);
            }
        });


        wordViewModel.getGameStage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                stageTextView.setText(s);
            }
        });
        wordViewModel.getGameScore().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                score = s;
                scoreTextView.setText(s);
            }
        });

        /*String iniText = Utils.initScreemFromText(gameText,currentStage);
        wordViewModel.setRequiredText(gameText);
        wordViewModel.initText(iniText);*/

       /* if(savedInstanceState== null) {
            if(!isGameOver && !wasPaused)
                findNewWord();
        }
        else{

            try {
                currentWordNum++;
                startGame();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
        return  v;
    }

    private void retreiveprefs() {
        gameLanguage = Utils.getGameLanguage(getActivity().getApplicationContext(), Strings.GAME_LANGUAGE_PREF,Strings.LANGUAGE_PREF);
        SharedPreferences prefs = getActivity().getSharedPreferences(Strings.GAME_STATE_PREF, 0);
        if (prefs.contains("stage")) {
            currentStage = prefs.getInt("stage",1);
        }
        if (prefs.contains("lives")) {
            numLifes =  prefs.getInt("lives",5);

        }
        if (prefs.contains("score")) {
            score =  prefs.getString("score","0");

        }
        if (prefs.contains("paused_time")) {
            lastTimeValue =  prefs.getInt("paused_time",0);
            /*if(currenTime ==100)
                currenTime =0;*/
            currenTime  = lastTimeValue;
        }
        if(prefs.contains("words")){
            words = prefs.getString("words","");
            buildWordLedsFromWord(words);
            if(words.length()>0)
                currentWordNum = words.length()+1;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if(wasPaused){
            try {


                    retreiveprefs();
                    startGame();

                wasPaused = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

    }
    //@Override
    /*public void onActivityCreated(Bundle savedInstanceState) {


        if (savedInstanceState != null) {
            currentStage = savedInstanceState.getInt("stage");
            numLifes = savedInstanceState.getInt("lives");
            currentWordNum = savedInstanceState.getInt("words");
            scoreTextView.setText(savedInstanceState.getString("score"));
            stageTextView.setText(String.valueOf(currentStage));
            setLives(numLifes);

            Log.d("onActivityCreated", "onActivityCreated: ");



        }
        super.onActivityCreated(savedInstanceState);

    }*/

    /*@Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("stage", currentStage);
        outState.putInt("lives", numLifes);
        outState.putInt("wordNum",currentWordNum);
        outState.putString("score",scoreTextView.getText().toString());
        outState.putInt("time",timeProgressBar.getProgress());

        outState.putString("words",words);
        stopGame();
        super.onSaveInstanceState(outState);

    }*/

   /* @Override
    public void onPause() {
        super.onPause();
        score = scoreTextView.getText().toString();
        savePrefs(currentStage,score,words,numLifes,timeProgressBar.getProgress());
        stopGame();
        wasPaused = true;
    }*/

   /* @Override
    public void onDestroy() {
        super.onDestroy();
        savePrefs(currentStage,lastStageScore,"",lastStageLifes,0);
    }*/

    private void savePrefs(int stage, String score, String words, int numLifes, int pausedTime ) {
        SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences(Strings.GAME_STATE_PREF, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("stage", stage);
        editor.putString("score", score);
        editor.putString("words", words);
        editor.putInt("lives",numLifes);
        editor.putInt("paused_time",pausedTime);
        editor.commit();
    }


    private void setAllLedInvisible(){
        if (leds!=null) {
            for (int i = 0; i < 10; i++) {
                leds[i].setVisibility(View.INVISIBLE);
            }
        }
        words="";
    }
    private void setLedVisible(int wordIndex){
        if(wordIndex >0 && wordIndex<=10 && leds != null)
        {

                leds[wordIndex+1].setVisibility(View.VISIBLE);


        }
    }

    private void incrementSlifeLife(){
        if( numLifes < 10 && numLifes > 0 ){
            lifes[numLifes].setImageResource(R.drawable.diamond_life);
            numLifes++;
            Utils.playSound(getActivity(),R.raw.new_life_sound,false);
        }


    }

    public void setTime(final int time){
        if(getActivity()!=null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    timeProgressBar.setProgress(time);
                }
            });
        }
    }

    public void setScore(final int score){
        if(getActivity()!=null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    scoreTextView.setText(String.valueOf(score));
                }
            });
        }
    }
    public void setStage(final int stage){
        currentStage = stage;
        if(getActivity()!=null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    stageTextView.setText(String.valueOf(stage));
                }
            });
        }
    }

    public void setLives(int num){
        for(int i =0 ; i<10 ; i++) {
            if(i< num)
                lifes[i].setImageResource(R.drawable.diamond_life);
            else
                lifes[i].setImageResource(R.drawable.diamond_no_life);
        }

    }
    private void decrementlifeLife(){
        if( numLifes > 0 ){
            lifes[numLifes-1].setImageResource(R.drawable.diamond_no_life);
            numLifes--;
        }


    }

    private  void startGame() throws Exception {
        timeProgressBar.setProgress(lastTimeValue);
        final long max1 = GameTime.getTime(currentStage);
        final long max = max1 - lastTimeValue * max1 / 100;

        if (countDownTimer == null){
            countDownTimer = new CountDownTimer(max, 10) {
                @Override
                public void onTick(long millisUntilFinished) {

                    long time = max1 - millisUntilFinished;
                    long progressValue = time * 100 / max1;
                    timeProgressBar.setProgress((int) progressValue);
                    if ((millisUntilFinished <= 5000) && !fiveSecLeft) {
                        fiveSecLeftPlayer = Utils.playSound(getActivity(), R.raw.fivesec_left_sound, false);
                        fiveSecLeft = true;
                    }


                }

                @Override
                public void onFinish() {
                    if (currentScreenText != null && !Utils.isSreenTextComplete(currentScreenText)) {

                        wordViewModel.updateSecondScreenText(wordViewModel.getRequiredText());
                        wordViewModel.updateAllowWordUpdate(false);
                        (new Handler()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                wordViewModel.updateTurnOffSecondScreenText(true);
                                lastTimeValue = 0;
                                if(countDownTimer!=null){
                                    countDownTimer.cancel();
                                    countDownTimer = null;
                                }
                                updateWorFoundStatus(false, currentWordNum);
                                if (!isGameOver) {
                                    wordViewModel.updateAllowWordUpdate(true);
                                    if(!wasPaused)
                                        findNewWord();
                                }
                            }
                        }, 2000);

                    }
                    stopFiveSecLeftSound();


                    //notifyTimeCompleted();
                    //timeProgressBar.setProgress(0);
                }
            }.start();
        }
    }


   /* @Override
    public void subscribe(WordTimeCompletedSubscriber subscriber) {
        subscibers.add(subscriber);

    }

    @Override
    public void notifyTimeCompleted() {
        if (subscibers.size() > 0){
            for(int i = 0; i< subscibers.size(); i++){
                subscibers.get(i).onTimeCompleted();
            }
        }
    }

    @Override
    public void onWordFound() {
        leds[currentWordNum].setImageResource(R.drawable.word_found_led);
        leds[currentWordNum].setVisibility(View.VISIBLE);

    }

    private  void startNewGame(){

    }*/

   private void findNewWord() {
       stopFiveSecLeftSound();
       currentWordNum++;
       if (currentWordNum <= 10) {
           timeProgressBar.setProgress(0);
           wordViewModel.updateCurrentTime(timeProgressBar.getProgress());

           currentWord = Utils.getNewWord(gameWords,currentStage,currentWordNum-1);
           String gameText =currentWord.getWord();
           currentWordHint = currentWord.getHint();
           wordViewModel.updateWordHint(currentWordHint);
           int stage = Integer.valueOf(stageTextView.getText().toString());
           String newInitWord = Utils.initScreemFromText(gameText,stage);
           // wordViewModel.initText(newInitWord);
           wordViewModel.updateScreenText(newInitWord);
           wordViewModel.setRequiredText(gameText);
           wordViewModel.updateNumTouch(0);
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
                       stageWinPlayer = Utils.playSound(getActivity(), R.raw.stage_win_sound, true);

                       (new Handler()).postDelayed(new Runnable() {
                           @Override
                           public void run() {

                               currentWordNum = 1;
                               currentStage = currentStage + 1;
                               Utils.saveIntSharedPreferences(getActivity().getApplicationContext(), Strings.GAME_STATE_PREF, "laststagelives", numLifes);
                               words = "";
                               stopStageWinSound();
                               startNextStageActivity();
                               lastStageLifes = numLifes;
                               lastStageScore = scoreTextView.getText().toString();

                               wordViewModel.updateStage(String.valueOf(currentStage));
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

   private void updatScore(final int score){
       final int currentScore = Integer.valueOf(scoreTextView.getText().toString());
       long cscore = 0;
       CountDownTimer cd = new CountDownTimer(3000,3000/score) {
           @Override
           public void onTick(long millisUntilFinished) {
               long cscore =  currentScore+score*(3000-millisUntilFinished)/3000;
               scoreTextView.setText(String.valueOf(cscore));
           }

           @Override
           public void onFinish() {
               int finScore= currentScore+score;
               if(finScore>= 1000) {
                   int numlifes = (int)(finScore/1000);
                   for (int i=0; i< numlifes; i++)
                       incrementSlifeLife();
               }
               int res =   finScore - 1000*(finScore/1000);
               scoreTextView.setText(String.valueOf(res));
           }
       }.start();
   }



       public void gameOver(){
            isGameOver = true;
           stopStageWinSound();
           stopFiveSecLeftSound();
           stopDingle();
           stopGame();
           numLifes= lastStageLifes;
           words ="";
           score = lastStageScore;
           Intent intent = new Intent(getActivity(),GameOverActivity.class);
           intent.putExtra(Strings.NEXT_STAGE_TO_PLAY,currentStage);
           startActivity(intent);
           //getActivity().finish();

       }
       public void startNextStageActivity(){
           //stopGame();
           if(getActivity()!= null) {
               Intent intent = new Intent(getActivity(), LoadWordsActivity.class);
               intent.putExtra("mode", "solitare");
               intent.putExtra(Strings.NEXT_STAGE_TO_PLAY, currentStage);
               //stopDingle();
               startActivity(intent);
           }
           //getActivity().finish();
       }

       private void stopGame(){
            if (countDownTimer!=null) {
                countDownTimer.cancel();
                countDownTimer = null;
            }
       }

       private void stopFiveSecLeftSound(){
           if ( fiveSecLeftPlayer!= null) {
               try {
                   if(fiveSecLeftPlayer.isPlaying()){
                       fiveSecLeftPlayer.pause();
                       fiveSecLeftPlayer.stop();
                       fiveSecLeftPlayer.release();
                   }
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

    /**
     *
     * @param word is like '0101010' 0 is word not found and 1 is word previously found
     */

    public void buildWordLedsFromWord(String word){
        int length = word.length();
        if ( length > 0){
            for(int i = 0; i< length; i++){
                if(String.valueOf(word.charAt(i)).equals("1")){

                    leds[i].setImageResource(R.drawable.word_found_led);
                }
                else {
                    if(String.valueOf(word.charAt(i)).equals("0"))
                        leds[i].setImageResource(R.drawable.word_not_found_led);
                }
                leds[i].setVisibility(View.VISIBLE);
            }

        }
    }

    private void updateWorFoundStatus(boolean wordfound, int wordIndex){
        int ledIndex = wordIndex -1;
        if(wordIndex <=10 && wordIndex >= 1){
            if(wordfound)
            {
                leds[ledIndex].setImageResource(R.drawable.word_found_led);
                words +="1";
                wordViewModel.wordFoundBluPrint(true);
            }
            else {
                leds[ledIndex].setImageResource(R.drawable.word_not_found_led);
                words +="0";
                wordViewModel.wordFoundBluPrint(false);
                decrementlifeLife();
                if(numLifes <= 0){
                    gameOver();
                    stopStageWinSound();
                    stopFiveSecLeftSound();

                }

            }

            leds[ledIndex].setVisibility(View.VISIBLE);
        }
    }

    private  void pauseGame(boolean pause){
        if(pause){
            lastTimeValue =timeProgressBar.getProgress();
            //countDownTimer.cancel();
            wasPaused = true;

        }
    }

    private  void stopDingle(){
        ((PlayerControlActivity)getActivity()).stopGeneric();
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





   }








