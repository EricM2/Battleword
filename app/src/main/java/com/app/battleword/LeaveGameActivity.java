package com.app.battleword;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.app.utils.Strings;
import com.app.utils.Utils;

public class LeaveGameActivity extends Activity {
    private Button continueGameBut;
    private Button leaveGameButton;
    private Button startNewGameButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_game);
        continueGameBut = findViewById(R.id.continue_game);
        startNewGameButton = findViewById(R.id.start_new_game);
        leaveGameButton = findViewById(R.id.leave_game);
        startNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeGameActivity();
                Intent i = new Intent(LeaveGameActivity.this,GameSetupActivity.class);
                Utils.stopSoundGenericService(LeaveGameActivity.this);
                i.putExtra(Strings.NEW_GAME,true);
                Utils.resetGameStatePreferences(getApplicationContext(), Strings.GAME_STATE_PREF);
                startActivity(i);


                finish();
            }
        });
        continueGameBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        leaveGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopGeneric();
                finishAffinity();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
    public void stopGeneric(){
        Intent i = new Intent(Strings.SOUND_ACTION_INTENT_FILTER);
        i.putExtra(Strings.SOUND_ACTION,Strings.STOP);
        sendBroadcast(i);
    }

    public void closeGameActivity(){
        Intent i = new Intent(Strings.CLOSE_GAME_INTENT_FILTER);
        sendBroadcast(i);
    }
}
