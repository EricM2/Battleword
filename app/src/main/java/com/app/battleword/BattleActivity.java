package com.app.battleword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.utils.Utils;

public class BattleActivity extends Activity {
   private EditText emailEditText;
   private Button playSolitaire;
   //private TextView constructionText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);
        emailEditText = findViewById(R.id.email_edit_text);
        playSolitaire  = findViewById(R.id.play);
        if(savedInstanceState !=null)
            emailEditText.setText(savedInstanceState.getString("email"));
        playSolitaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = emailEditText.getText().toString();
                if(text.length()> 0 && Utils.hasInternet(BattleActivity.this))
                    Utils.subscribeEmail(BattleActivity.this,text);
                finish();
            }
        });

        Utils.playSound(this,R.raw.settings_activity_sound,false);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outState.putString("email",emailEditText.getText().toString());
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
