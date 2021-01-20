package com.app.battleword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.app.battleword.objects.Word;
import com.app.utils.Strings;
import com.app.utils.Utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class GameSetupActivity extends AppCompatActivity {
    private Spinner gameLevelSpinner;
    private Button battleButton;
    private Button solitaireButton;
    private final  static  String MODE="mode";
    private final static String LEVEL = "leevel";
    private String selectedGameLevel;
    private Button settingsBut;
    private MediaPlayer dingle;
    private boolean wasPlaying;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_setup);
        solitaireButton = findViewById(R.id.play_solitaire);
        battleButton = findViewById(R.id.play_battle);
        settingsBut = findViewById(R.id.settings_button_setup);
        if(savedInstanceState==null) {
            selectedGameLevel = null;
            dingle = null;
            wasPlaying = false;

            Utils.playSound(this, R.raw.new_activity_sound, false);


        }
        Callable v = new Callable() {
            @Override
            public Object call() throws Exception {
                Intent sound = new Intent(GameSetupActivity.this,BackgroundSoundService.class);
                startService(sound);
                return null;
            }
        };
        Utils.doAfter(50, v);
        wasPlaying = true;
        settingsBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getSettingsIntent());
            }
        });

        solitaireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = gameLoadWordIntent();
                    Utils.playSound(getApplicationContext(), R.raw.play_button_sound,false);

                    //stopDingle();
                    stopBackgroundSoundService();
                    startActivity(intent);
                if(Utils.hasInternet(GameSetupActivity.this)){
                    String prefCode = Utils.getGameLanguage(getApplicationContext(), Strings.GAME_LANGUAGE_PREF, Strings.LANGUAGE_PREF);
                    String lang = prefCode.split("-")[0];
                    (new AsyncTask<String, Integer, Map<String, List<Word>>>() {
                        @Override
                        protected Map<String, List<Word>> doInBackground(String... str) {
                            return  Utils.getWordFromApiPost(str[0]);
                        }

                        @Override
                        protected void onPostExecute(Map<String, List<Word>> stringListMap) {

                            Utils.apiWords = stringListMap;
                        }
                    }).execute(lang);

                }
            }
        });

        battleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GameSetupActivity.this,BattleActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private Intent gameLoadWordIntent(){
        int nextStage = 1;
        SharedPreferences prefs = this.getSharedPreferences(Strings.GAME_STATE_PREF, 0);
        if (prefs.contains("stage")) {
             nextStage= prefs.getInt("stage",1);
        }
        Intent intent = new Intent(this,LoadWordsActivity.class);
        intent.putExtra(MODE,"solitare");
        intent.putExtra(Strings.NEXT_STAGE_TO_PLAY,nextStage);
        return  intent;
    }

    private Intent getSettingsIntent(){
        Intent i = new Intent(this,SettingsActivity.class);
        return i;
    }

    private boolean isFirstTime(){
        return  true;
    }



    private void playDingle(){
        dingle = Utils.playSound(this,R.raw.battleword_generic,true);
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("wasPlaying",wasPlaying);
    }




    private void stopDingle(){
        try {
            if(dingle!=null){
                dingle.pause();
                dingle.stop();
                dingle.release();
            }
        }
        catch (Exception e)
        {

        }
    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopBackgroundSoundService();
    }

    @Override
    public void onBackPressed() {

    }

    private void stopBackgroundSoundService(){
        Intent sound = new Intent(GameSetupActivity.this,BackgroundSoundService.class);
        stopService(sound);
    }


}
