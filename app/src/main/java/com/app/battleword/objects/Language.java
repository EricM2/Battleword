package com.app.battleword.objects;

public class Language {
    private int flagImageResourceId;
    private String language;
    private  String code;

    public Language(int flagImageResourceId, String language,String code) {
        this.flagImageResourceId = flagImageResourceId;
        this.language = language;
        this.code = code;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
