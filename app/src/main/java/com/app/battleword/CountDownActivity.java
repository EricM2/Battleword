package com.app.battleword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.TextView;

import com.app.battleword.objects.Word;
import com.app.utils.Strings;
import com.app.utils.Utils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class CountDownActivity extends AppCompatActivity {

    private CountDownTimer countDownTimer;
    private TextView countDownTextView;
    private long currentCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);
        countDownTextView = findViewById(R.id.count_down_text);
        countDownTimer = new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                currentCount = millisUntilFinished/1000;
                countDownTextView.setText(String.valueOf(currentCount));
                if(millisUntilFinished > 0)
                    Utils.playSound(CountDownActivity.this,R.raw.word_not_found_sound,false);
                else
                    Utils.playSound(CountDownActivity.this,R.raw.last_count_down_sound,false);

            }

            @Override
            public void onFinish() {
                Utils.playSound(CountDownActivity.this,R.raw.last_count_down_sound,false);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startGameActivity();
                        finish();
                    }
                },500);

            }
        };
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if(countDownTimer!=null)
            countDownTimer.cancel();
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        countDownTimer.start();
    }

    private void startGameActivity(){
        Intent i = nextActivityIntent(PlayerControlActivity.class);
        startActivity(i);
    }
    private Intent nextActivityIntent(Class nextActivityClass){

        Intent intent = new Intent(this,nextActivityClass);
        intent.putExtra(Strings.MODE,"solitare");
        Map<String, List<Word>> gameWords = (Map<String, List<Word>>) getIntent().getSerializableExtra(Strings.GAMEWORDS);
        intent.putExtra(Strings.GAMEWORDS,(Serializable) gameWords);
        return  intent;
    }

    @Override
    public void onBackPressed() {

    }
}
