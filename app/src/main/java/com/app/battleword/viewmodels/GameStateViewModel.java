package com.app.battleword.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.battleword.objects.Word;

public class GameStateViewModel extends ViewModel {
    private MutableLiveData<Integer> lives = new MutableLiveData<>(5);
    private MutableLiveData<Integer> time = new MutableLiveData<>(0);
    private MutableLiveData<Integer> score = new MutableLiveData<>(0);
    private MutableLiveData<Word> word = new MutableLiveData<>();
    private MutableLiveData<Integer> touches = new MutableLiveData<>(30);
    private MutableLiveData<String> wordFound = new MutableLiveData<>("");
    private MutableLiveData<Integer> stage = new MutableLiveData<>(1);
    private MutableLiveData<String> screenWord = new MutableLiveData<>("");
    private MutableLiveData<String> clickedKey = new MutableLiveData<>();
    private MutableLiveData<Boolean> allowWordUpdate = new MutableLiveData<>(true);
    private MutableLiveData<Boolean> timeCompleted = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> stageCompleted = new MutableLiveData<>();
    private MutableLiveData<Boolean> gameWon = new MutableLiveData<>();
    private MutableLiveData<Boolean> gameOver = new MutableLiveData<>();



    public GameStateViewModel(int lives, int score, int stage){
        this.lives.setValue(lives);
        this.score.setValue(score);
        this.stage.setValue(stage);
    }

    public void updateStageCompleted(boolean isCompleted){
        stageCompleted.setValue(isCompleted);
    }
    public void updateGameWon(boolean hasWon){
        gameWon.setValue(hasWon);
    }

    public void updateGameOver(boolean isGameOver){
        gameOver.setValue(isGameOver);
    }

    public void updateTimeCompleted(boolean isTimeCompleted){
        timeCompleted.setValue(isTimeCompleted);

    }
    public void updateAllowWordUpdate(boolean allow){
        allowWordUpdate.setValue(allow);
    }
    public void updateKeyClicked(String key){
        clickedKey.setValue(key);
    }

    public void updateScreenWord (String word){
        screenWord.setValue(word);
    }

    public void updateWordFound(String wordFound){
        this.wordFound.setValue(wordFound);
    }

    public void updateStage(int stage){
        this.stage.setValue(stage);
    }


    public MutableLiveData<String> getWordFound() {
        return wordFound;
    }

    public MutableLiveData<Integer> getStage() {
        return stage;
    }

    public MutableLiveData<Integer> getLives() {
        return lives;
    }

    public void updateLives(int lives) {
        this.lives.setValue(lives);
    }

    public MutableLiveData<Integer> getTime() {
        return time;
    }

    public void updateTime(int time) {
        this.time.setValue(time);
    }

    public MutableLiveData<Integer> getScore() {
        return score;
    }

    public void updateScore(int score) {
        this.score.setValue(score);
    }

    public MutableLiveData<Word> getWord() {
        return word;
    }

    public void updateWord(Word word) {
        this.word.setValue(word);
    }

    public MutableLiveData<Integer> getTouches() {
        return touches;
    }

    public void updateTouches(int touches) {
        this.touches.setValue(touches);
    }

    public MutableLiveData<String> getScreenWord() {
        return screenWord;
    }

    public MutableLiveData<String> getClickedKey() {
        return clickedKey;
    }
    public MutableLiveData<Boolean> getAllowWordUpdate() {
        return allowWordUpdate;
    }

    public MutableLiveData<Boolean> getTimeCompleted() {
        return timeCompleted;
    }

    public MutableLiveData<Boolean> getStageCompleted() {
        return stageCompleted;
    }

    public MutableLiveData<Boolean> getGameWon() {
        return gameWon;
    }

    public MutableLiveData<Boolean> getGameOver() {
        return gameOver;
    }
}
