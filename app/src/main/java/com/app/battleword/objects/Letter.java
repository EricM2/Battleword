package com.app.battleword.objects;

public class Letter {
    private int imageResourceId;
    private int buttonBackgroundResourceId;


    public Letter(int imageResourceId, int buttonBackgroundResourceId) {
        this.imageResourceId = imageResourceId;
        this.buttonBackgroundResourceId = buttonBackgroundResourceId;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public int getButtonBackgroundResourceId() {
        return buttonBackgroundResourceId;
    }

    public void setButtonBackgroundResourceId(int buttonBackgroundResourceId) {
        this.buttonBackgroundResourceId = buttonBackgroundResourceId;
    }
}
