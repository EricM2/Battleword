package com.app.battleword.objects;

import java.io.Serializable;

public class Word implements Serializable {
    String word;
    String tip;
    int stage;

    public Word(String word, int stage,String tip) {
        this.word = word;
        this.tip = tip;
        this.stage = stage;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }
}
