package com.app.battleword.objects;

public class Word {
    String word;
    String lang;
    String tip;
    int stage;

    public Word(String word, String lang, String tip, int stage) {
        this.word = word;
        this.lang = lang;
        this.tip = tip;
        this.stage = stage;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
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
