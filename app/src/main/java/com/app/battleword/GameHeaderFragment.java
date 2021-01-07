package com.app.battleword;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.app.battleword.viewmodels.ScreenTextViewModel;
import com.app.utils.GameTime;
import com.app.utils.Strings;
import com.app.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
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
    private String score = "0";
    private ScreenTextViewModel screenTextViewModel;
    private String currentScreenText;
    private boolean[] wordsMask = new boolean[]{};
    //private int lastStageLifes = 5;
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

        gameLanguage = Utils.getGameLanguage(getActivity().getApplicationContext(),Strings.GAME_LANGUAGE_PREF,Strings.LANGUAGE_PREF);
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
            currenTime =  prefs.getInt("paused_time",0);
            if(currenTime==100)
                currenTime =0;
        }
        if(prefs.contains("words")){
            words = prefs.getString("words","");
            buildWordLedsFromWord(words);
            currentWordNum = words.length();
        }


            if (savedInstanceState != null) {
                Log.d("onCreateView", "onCreateView: ");

                currentStage = savedInstanceState.getInt("stage");
                numLifes = savedInstanceState.getInt("lives");
                currentWordNum = savedInstanceState.getInt("wordNum");
                score = savedInstanceState.getString("score");
                wordsMask = savedInstanceState.getBooleanArray("wordmask");
                currenTime = savedInstanceState.getInt("time");

                redrawLeds(wordsMask);
                setLives(numLifes);
            }
            else {

                setLives(numLifes);
            }


        timeProgressBar.setProgress(currenTime);
        stageTextView.setText(String.valueOf(currentStage));
        scoreTextView.setText(score);
        timeProgressBar.setProgress(0);
        gameWords =  (Map<String,List<Word>>)getActivity().getIntent().getSerializableExtra(Strings.GAMEWORDS);

        screenTextViewModel = new ViewModelProvider(requireActivity()).get(ScreenTextViewModel.class);
        screenTextViewModel.updateGameWords(gameWords);

        screenTextViewModel.getScreenText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                currentScreenText = s;
                if(Utils.isSreenTextComplete(s) && timeProgressBar.getProgress()<100){
                    if(countDownTimer!=null) {
                        countDownTimer.cancel();
                        lastTimeValue = 0;
                    }
                    updatScore(100);
                    (new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            updateWorFoundStatus(true,currentWordNum);
                            findNewWord();
                        }
                    }, 2000);


                }
            }
        });

        screenTextViewModel.getPauseGame().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                pauseGame(aBoolean);
            }
        });


        screenTextViewModel.getGameStage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                stageTextView.setText(s);
            }
        });
        screenTextViewModel.getGameScore().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                score = s;
                scoreTextView.setText(s);
            }
        });

        /*String iniText = Utils.initScreemFromText(gameText,currentStage);
        screenTextViewModel.setRequiredText(gameText);
        screenTextViewModel.initText(iniText);*/


        findNewWord();
        return  v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(wasPaused){
            try {
                startGame();
                wasPaused = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("stage", currentStage);
        outState.putInt("lives", numLifes);
        outState.putInt("wordNum",currentWordNum);
        outState.putString("score",scoreTextView.getText().toString());
        outState.putInt("time",timeProgressBar.getProgress());

        outState.putBooleanArray("wordmask",wordsMask);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        score = scoreTextView.getText().toString();
        SharedPreferences prefs = getActivity().getSharedPreferences(Strings.GAME_STATE_PREF, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("stage", currentStage);
        editor.putString("score", score);
        editor.putString("words", words);
        editor.putInt("lives",numLifes);
        editor.putInt("paused_time",timeProgressBar.getProgress());


        editor.commit();
    }


    private void setAllLedInvisible(){
        if (leds!=null) {
            for (int i = 0; i < 10; i++) {
                leds[i].setVisibility(View.INVISIBLE);
            }
        }
        wordsMask = new boolean[]{};
    }
    private void setLedVisible(int wordIndex){
        if(wordIndex >0 && wordIndex<=10 && leds != null)
        {

                leds[wordIndex+1].setVisibility(View.VISIBLE);


        }
    }

    private void incrementSlifeLife(){
        if( numLifes < 10 && numLifes > 0 ){
            lifes[numLifes-1].setImageResource(R.drawable.diamond_life);
            numLifes++;
            Utils.playSound(getActivity(),R.raw.new_life_sound,false);
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
        final long max1= GameTime.getTime(currentStage);
        final long max = max1-lastTimeValue*max1/100;

        countDownTimer = new CountDownTimer(max,10) {
            @Override
            public void onTick(long millisUntilFinished) {

                    long time = max1 - millisUntilFinished;
                    long progressValue = time*100/max1;
                    timeProgressBar.setProgress((int) progressValue);
                    if((millisUntilFinished <= 5000) && !fiveSecLeft){
                        fiveSecLeftPlayer = Utils.playSound(getActivity(),R.raw.fivesec_left_sound,false);
                        fiveSecLeft= true;
                    }


            }

            @Override
            public void onFinish() {
                if(currentScreenText!=null && !Utils.isSreenTextComplete(currentScreenText)){

                    screenTextViewModel.updateSecondScreenText(screenTextViewModel.getRequiredText());
                    (new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            screenTextViewModel.updateTurnOffSecondScreenText(true);
                            lastTimeValue = 0;
                            updateWorFoundStatus(false,currentWordNum);
                            findNewWord();
                        }
                    }, 2000);

                }
                stopFiveSecLeftSound();


                //notifyTimeCompleted();
                //timeProgressBar.setProgress(0);
            }
        }.start();
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
           screenTextViewModel.updateCurrentTime(timeProgressBar.getProgress());

           currentWord = Utils.getNewWord(gameWords,currentStage,currentWordNum-1);
           String gameText =currentWord.getWord();
           currentWordHint = currentWord.getTip();
           screenTextViewModel.updateWordHint(currentWordHint);
           int stage = Integer.valueOf(stageTextView.getText().toString());
           String newInitWord = Utils.initScreemFromText(gameText,stage);
           // screenTextViewModel.initText(newInitWord);
           screenTextViewModel.updateScreenText(newInitWord);
           screenTextViewModel.setRequiredText(gameText);
           if(numLifes > 0) {
               try {
                   startGame();
               }
               catch (Exception e){
                   Log.d("Exception", e.getMessage());
               }

           }
           else{
               if (numLifes<=0)
                   gameOver();
           }



       }

       else  {

           stageWinPlayer = Utils.playSound(getActivity(),R.raw.stage_win_sound,true);

           if (currentStage < 5) {

               (new Handler()).postDelayed(new Runnable() {
                   @Override
                   public void run() {

                       currentWordNum = 1;
                       currentStage = currentStage + 1;
                       Utils.saveIntSharedPreferences(getActivity().getApplicationContext(),Strings.GAME_STATE_PREF,"laststagelives",numLifes);
                       words="";
                       stopStageWinSound();
                       startNextStageActivity();
                       screenTextViewModel.updateStage(String.valueOf(currentStage));
                       setAllLedInvisible();
                   }
               },5000);

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

   private void redrawLeds(boolean[] mask){
       if (mask != null && mask.length> 0){
           for (int i=0; i<mask.length ;i++){
               if(mask[i]){
                   leds[i].setVisibility(View.VISIBLE);
                   leds[i].setImageResource(R.drawable.word_found_led);
               }
               else {
                   leds[i].setVisibility(View.VISIBLE);
                   leds[i].setImageResource(R.drawable.word_not_found_led);
               }
               }

           }
       if(mask!=null && mask.length==0)
           setAllLedInvisible();
       }

       public void gameOver(){
           stopGame();
           Intent intent = new Intent(getContext(),GameOverActivity.class);
           startActivity(intent);

       }
       private void startNextStageActivity(){
           stopGame();
           Intent intent = new Intent(getActivity(), LoadWordsActivity.class);
           intent.putExtra("mode","solitare");
           intent.putExtra("nextStage",currentStage);
           startActivity(intent);
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

    private void buildWordLedsFromWord(String word){
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
        if(wordfound)
        {
            leds[ledIndex].setImageResource(R.drawable.word_found_led);
            words +="1";
            screenTextViewModel.wordFoundBluPrint(true);
            int index = wordsMask.length;
            wordsMask = Arrays.copyOf(wordsMask,wordsMask.length+1);
            wordsMask[index] = true;
        }
        else {
            leds[ledIndex].setImageResource(R.drawable.word_not_found_led);
            words +="0";
            screenTextViewModel.wordFoundBluPrint(false);
            decrementlifeLife();
            int index = wordsMask.length;
            wordsMask = Arrays.copyOf(wordsMask, wordsMask.length + 1);
            wordsMask[index] = false;
        }

        leds[ledIndex].setVisibility(View.VISIBLE);
    }

    private  void pauseGame(boolean pause){
        if(pause){
            lastTimeValue =timeProgressBar.getProgress();
            countDownTimer.cancel();
            wasPaused = true;

        }
    }





   }








