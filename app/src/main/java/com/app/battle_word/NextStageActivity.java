package com.app.battle_word;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class NextStageActivity extends AppCompatActivity {
    private int stage;
    private final static String MODE = "mode";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_stage);
         stage = getIntent().getIntExtra("nextStage",1);
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(gameScenarioIntent());
            }
        },5000);
    }

    private Intent gameScenarioIntent(){

        Intent intent = new Intent(this,GameScenarioActivity.class);

        intent.putExtra(MODE,"solitare");
        intent.putExtra("nextStage",stage);
        return  intent;
    }
}
