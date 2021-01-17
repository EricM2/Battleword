package com.app.battleword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.app.utils.Strings;
import com.app.utils.Utils;

import java.util.concurrent.Callable;

public class GameWonActivity extends AppCompatActivity {
    private Button followInsta;
    private Button followTwitter;
    private Button followFacbook;
    private Button followYoutube;
    private Button playAgain;
    private Button close;
    private EditText email;
    private MediaPlayer gameWonSound ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_won);
        playAgain = findViewById(R.id.play);
        close = findViewById(R.id.close);
        email = findViewById(R.id.email_edit_text);
        gameWonSound = null;
        Callable c = new Callable() {
            @Override
            public Object call() throws Exception {
                gameWonSound = Utils.playSound(GameWonActivity.this,R.raw.african_safari_loop,true);
                return null;
            }
        };
        Utils.doAfter(200,c);

        if(savedInstanceState!=null){
            email.setText(savedInstanceState.getString("email"));
        }
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGame();
                finishAffinity();
            }
        });

        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGame();
                Intent i = new Intent(GameWonActivity.this,GameSetupActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
    private void updateGame(){
        Utils.resetGameStatePreferences(getApplicationContext(), Strings.GAME_STATE_PREF);
        String em = email.getText().toString();
        if(em.length() > 0 && Utils.hasInternet(GameWonActivity.this))
            Utils.subscribeEmail(this,em);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("email",email.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (gameWonSound != null) {
            try {
                if(gameWonSound.isPlaying()){
                    gameWonSound.stop();
                }
                gameWonSound.release();
                gameWonSound=null;
            }
            catch (Exception e){

            }
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        updateGame();
        Intent i = new Intent(GameWonActivity.this,GameSetupActivity.class);
        startActivity(i);
        finish();
    }
}
