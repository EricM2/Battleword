package com.app.battle_word;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.battle_word.publishers.WordTimeCompletedPublisher;
import com.app.battle_word.subscribers.WordFoundSubscriber;
import com.app.battle_word.subscribers.WordTimeCompletedSubscriber;
import com.app.battle_word.viewmodels.ScreenTextViewModel;
import com.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;


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
    private  int currentWordNum=1;
    private int currentStage = 1;
    private ScreenTextViewModel screenTextViewModel;
    private String currentScreenText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_game_header, container, false);
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
        stageTextView.setText(String.valueOf(currentStage));
        scoreTextView.setText("0");
        timeProgressBar.setProgress(0);
        screenTextViewModel = new ViewModelProvider(requireActivity()).get(ScreenTextViewModel.class);
        screenTextViewModel.getScreenText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                currentScreenText = s;
                if(Utils.isSreenTextComplete(s) && timeProgressBar.getProgress()<100){
                    if(countDownTimer!=null)
                        countDownTimer.cancel();
                    updatScore(10);
                    (new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            findNewWord(true);
                        }
                    }, 2000);


                }
            }
        });
        screenTextViewModel.getGameStage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                stageTextView.setText(s);
            }
        });
        setAllLedInvisible();
        startGame();
        return  v;
    }


    private void setAllLedInvisible(){
        if (leds!=null) {
            for (int i = 0; i < 10; i++) {
                leds[i].setVisibility(View.INVISIBLE);
            }
        }
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
        }


    }
    private void decrementlifeLife(){
        if( numLifes > 0 ){
            lifes[numLifes-1].setImageResource(R.drawable.diamond_no_life);
            numLifes--;
        }


    }

    private  void startGame(){

        final long max = 5000;
        countDownTimer = new CountDownTimer(max,10) {
            @Override
            public void onTick(long millisUntilFinished) {

                    long time = max -millisUntilFinished;
                    long progressValue = time*100/max;
                    timeProgressBar.setProgress((int) progressValue);

            }

            @Override
            public void onFinish() {
                if(currentScreenText!=null && !Utils.isSreenTextComplete(currentScreenText)){

                    screenTextViewModel.updateSecondScreenText(screenTextViewModel.getRequiredText());
                    (new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            screenTextViewModel.updateTurnOffSecondScreenText(true);
                            findNewWord(false);
                        }
                    }, 2000);

                }



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

   private void findNewWord(Boolean foundPrev) {
       if (currentWordNum <= 10) {
           int ledIndex = currentWordNum -1;
           screenTextViewModel.wordFoundBluPrint(foundPrev);
           timeProgressBar.setProgress(0);
           screenTextViewModel.updateCurrentTime(timeProgressBar.getProgress());
           screenTextViewModel.wordFoundBluPrint(foundPrev);
           //Utils.waitFor(4200);

           String newWord = Utils.getRandomWord();
           String newInitWord = Utils.initScreemFromText(newWord);
           // screenTextViewModel.initText(newInitWord);
           screenTextViewModel.updateScreenText(newInitWord);
           screenTextViewModel.setRequiredText(newWord);
           if (foundPrev)
               leds[ledIndex].setImageResource(R.drawable.word_found_led);
           else {
               leds[ledIndex].setImageResource(R.drawable.word_not_found_led);
               decrementlifeLife();
           }
           leds[ledIndex].setVisibility(View.VISIBLE);

           currentWordNum++;

       }
       if (currentWordNum == 11) {
           currentWordNum = 1;
           if (currentStage < 5) {
               currentStage = currentStage + 1;
               screenTextViewModel.updateStage(String.valueOf(currentStage));
               setAllLedInvisible();
           }

       }
       startGame();
   }

   private void updatScore(int score){
       final int currentScore = Integer.valueOf(scoreTextView.getText().toString());
       CountDownTimer cd = new CountDownTimer(1500,1500/score) {
           @Override
           public void onTick(long millisUntilFinished) {
               scoreTextView.setText(String.valueOf(currentScore+1500/millisUntilFinished));

           }

           @Override
           public void onFinish() {

           }
       }.start();
   }







   }
