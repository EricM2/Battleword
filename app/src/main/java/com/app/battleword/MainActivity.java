package com.app.battleword;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {


     private CountDownTimer countDownTimer;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("LANG", Locale.getDefault().getLanguage());
        Log.d("ISOLANG",Locale.getDefault().getISO3Language());
        Log.d("COUNTRY",Locale.getDefault().getCountry());
        Log.d("ISOCOUNTRY",Locale.getDefault().getISO3Country());
        Log.d("DISPLAYCOUNTRY",Locale.getDefault().getDisplayCountry());
        Log.d("DISPLAYNME",Locale.getDefault().getDisplayName());
        Log.d("LOCALE",Locale.getDefault().toString());
        Log.d("DISPLAYLANGUAGE",Locale.getDefault().getDisplayLanguage());
        Log.d("TAG",Locale.getDefault().toLanguageTag());

        final Intent gameSetupIntent = new Intent(this,GameSetupActivity.class);

        countDownTimer = new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startActivity(gameSetupIntent);
            }
        };
       countDownTimer.start();
    }
}
