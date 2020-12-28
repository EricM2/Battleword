package com.app.battle_word.objects;

public class Language {
    private int flagImageResourceId;
    private String language;

    public Language(int flagImageResourceId, String language) {
        this.flagImageResourceId = flagImageResourceId;
        this.language = language;
    }

    public int getFlagImageResourceId() {
        return flagImageResourceId;
    }

    public void setFlagImageResourceId(int flagImageResourceId) {
        this.flagImageResourceId = flagImageResourceId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
