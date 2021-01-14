package com.app.battleword.objects;

import java.io.Serializable;

public class Word implements Serializable {
    String word;
    String hint;
    int stage;

    public Word(String word, int stage,String hint) {
        this.word = word;
        this.hint = hint;
        this.stage = stage;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }
}
