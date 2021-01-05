package com.app.battle_word;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.app.battleword.NextStageActivity;
import com.app.battleword.R;
import com.app.battleword.objects.Word;
import com.app.utils.Strings;
import com.app.utils.Utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadWordsActivity extends AppCompatActivity {
    private Map<String, List<Word>> words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_words);
        words = new HashMap<>();
    }

    @Override
    protected void onResume() {
        super.onResume();



        (new Handler()).post(new Runnable() {
            @Override
            public void run() {
                for(int i=1;i<=5; i++){
                    String key = "stage"+String.valueOf(i);
                    try {
                        words.put(key, Utils.getWordForStage(LoadWordsActivity.this,1,"fr"));
                    }
                    catch (Exception e){}


                }
                startActivity(gameStageIntent());
            }
        });

    }

    private Intent gameStageIntent(){
        int nextStage = 1;
        SharedPreferences prefs = this.getSharedPreferences(Strings.GAME_STATE_PREF, 0);
        if (prefs.contains("stage")) {
            nextStage= prefs.getInt("stage",1);
        }

        //Intent intent = new Intent(this,GameScenarioActivity.class);
        Intent intent = new Intent(this,NextStageActivity.class);
        intent.putExtra(Strings.MODE,"solitare");
        intent.putExtra("nextStage",nextStage);

        intent.putExtra(Strings.GAMEWORDS, (Serializable) words);
        return  intent;
    }
}
