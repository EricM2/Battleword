package com.app.battleword.tasks;

import com.app.battleword.GameEngineService;

public class Game implements Runnable {
    private GameEngineService context;
    public Game (GameEngineService context){
        this.context = context;
    }
    @Override
    public void run() {

    }
}
