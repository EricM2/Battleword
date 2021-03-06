package com.app.battleword.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class ScreenTextViewModel extends ViewModel {
   private String requiredText ;

   private MutableLiveData<String> gameLanguage = new MutableLiveData<>();
    private  MutableLiveData<String> screenText = new MutableLiveData<String>();
    private MutableLiveData<String> gameScore = new MutableLiveData<>();
    private  MutableLiveData<String> secondScreenText = new MutableLiveData<String>();
    private MutableLiveData<List<Boolean>>  wordFoundBluPrint = new MutableLiveData<>();
    private MutableLiveData<Integer> numLives = new MutableLiveData<>();
    private MutableLiveData<Integer> currentTime = new MutableLiveData<>();
    private MutableLiveData<String>  gameStage = new MutableLiveData<>();
    private MutableLiveData<Boolean> turnOffSecondScreenText = new MutableLiveData<>();
    public  void  initStage(String stage){
        gameStage.setValue(stage);
    }
    public void  initText(String text){
        screenText.setValue(text);
    }
    public void updateSecondScreenText(String str){
        secondScreenText.setValue(str);
    }

    public void updateGameLanguage(String language){
        this.gameLanguage.setValue(language);
    }

    public void updateScore(String score){
        gameScore.setValue(score);
    }
    public  void updateTurnOffSecondScreenText(Boolean turnOff){
        turnOffSecondScreenText.setValue(turnOff);
    }
    public void updateScreenText(String string) {
        screenText.setValue(string);
    }
    public void wordFoundBluPrint(Boolean bool){
        List<Boolean> val = wordFoundBluPrint.getValue();
        if (val==null)
            val = new ArrayList<Boolean>();
        val.add(bool);
        wordFoundBluPrint.setValue(val);
    }
    public void updateNumLives(int num){
        numLives.setValue(num);
    }
    public  void updateCurrentTime(int time){
        currentTime.setValue(time);
    }
    public void updateStage(String stage){
        gameStage.setValue(stage);
    }

    public String getRequiredText() {
        return requiredText;
    }

    public void setRequiredText(String requiredText) {
        this.requiredText = requiredText;
    }

    public LiveData<String> getScreenText() {
        return screenText;
    }

    public MutableLiveData<List<Boolean>> getWordFoundBluPrint() {
        return wordFoundBluPrint;
    }

    public MutableLiveData<Integer> getNumLives() {
        return numLives;
    }

    public MutableLiveData<Integer> getCurrentTime() {
        return currentTime;
    }

    public MutableLiveData<String> getSecondScreenText() {
        return secondScreenText;
    }

    public MutableLiveData<String> getGameStage() {
        return gameStage;
    }

    public MutableLiveData<Boolean> getTurnOffSecondScreenText() {
        return turnOffSecondScreenText;
    }

    public MutableLiveData<String> getGameScore() {
        return gameScore;
    }

    public MutableLiveData<String> getGameLanguage() {
        return gameLanguage;
    }
}
