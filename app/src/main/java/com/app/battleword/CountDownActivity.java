package com.app.battleword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.app.utils.Utils;

public class CountDownActivity extends AppCompatActivity {

    private CountDownTimer countDownTimer;
    private TextView countDownTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);
        countDownTextView = findViewById(R.id.count_down_text);
        countDownTimer = new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countDownTextView.setText(String.valueOf(millisUntilFinished/1000));
                if(millisUntilFinished > 0)
                    Utils.playSound(CountDownActivity.this,R.raw.word_not_found_sound,false);
                else
                    Utils.playSound(CountDownActivity.this,R.raw.last_count_down_sound,false);
            }

            @Override
            public void onFinish() {

                startGameActivity();
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        countDownTimer.start();
    }

    private void startGameActivity(){
        Intent i = new Intent(this,PlayerControlActivity.class);
        startActivity(i);
    }
}
