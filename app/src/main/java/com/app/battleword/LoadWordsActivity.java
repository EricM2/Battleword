package com.app.battleword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

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
                String prefCode = Utils.getGameLanguage(getApplicationContext(), Strings.GAME_LANGUAGE_PREF, Strings.LANGUAGE_PREF);
                String lang = prefCode.split("-")[0];
                    int nextStage = getNextStage();
                    String key = "stage"+String.valueOf(nextStage);
                    try {
                        words.put(key, Utils.getWordForStage(LoadWordsActivity.this,nextStage,lang));
                    }
                    catch (Exception e){
                        Log.d("Exception", e.getMessage());
                    }



                startActivity(gameStageIntent(nextStage));
            }
        });

    }

    private Intent gameStageIntent(int nextStage){



        //Intent intent = new Intent(this,GameScenarioActivity.class);
        Intent intent = new Intent(this,NextStageActivity.class);
        intent.putExtra(Strings.MODE,"solitare");
        intent.putExtra("nextStage",nextStage);

        intent.putExtra(Strings.GAMEWORDS, (Serializable) words);
        return  intent;
    }

    private int getNextStage(){
        int nextStage = 1;
        SharedPreferences prefs = this.getSharedPreferences(Strings.GAME_STATE_PREF, 0);
        if (prefs.contains("stage")) {
            nextStage= prefs.getInt("stage",1);
            nextStage = nextStage > 5? 1 : nextStage;
        }
        return  nextStage;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, BackgroundSoundService.class);
        stopService(intent);
        //NotificationManager manager = getSystemService(NotificationManager.class);
        NotificationManagerCompat.from(this).cancel(1);
        //manager.cancel(1);
    }
}
