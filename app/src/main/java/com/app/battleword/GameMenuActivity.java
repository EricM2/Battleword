package com.app.battleword;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.app.utils.Strings;
import com.app.utils.Utils;

public class GameMenuActivity extends Activity {
    private Button resetLanguage;
    private Button continueGameBut;
    private  Button startNewGameButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);

        continueGameBut = findViewById(R.id.continue_game);
        startNewGameButton = findViewById(R.id.new_game);
        resetLanguage = findViewById(R.id.change_language);
        startNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeGameActivity();
                Utils.resetGameStatePreferences(getApplicationContext(), Strings.GAME_STATE_PREF);
                Intent i = new Intent(GameMenuActivity.this,GameSetupActivity.class);
                startActivity(i);
                Utils.stopSoundGenericService(GameMenuActivity.this);
                finish();
            }
        });
        continueGameBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        resetLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopGeneric();
                Utils.resetGameStatePreferences(getApplicationContext(), Strings.GAME_STATE_PREF);
                Utils.resetGameStatePreferences(getApplicationContext(), Strings.FIRST_TIME_APP_INSTALLED_PREF);
                gotoMainActivity();

            }
        });
    }

    public void stopGeneric(){
        Intent i = new Intent(this,BackgroundSoundService.class);
       stopService(i);
    }

    public void gotoMainActivity(){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }

    public void closeGameActivity(){
        Intent i = new Intent(Strings.CLOSE_GAME_INTENT_FILTER);
        sendBroadcast(i);
    }
}