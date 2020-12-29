package com.app.battle_word;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.app.battle_word.adapters.LanguageSpinnerAdapter;
import com.app.battle_word.objects.Language;
import com.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends Activity {
    private Spinner languageSpinner;
    public static String GAME_LANGUAGE_PREF="game_language_pref";
    public static String LANGUAGE_PREF="leanguage_pref";
    private Button okButton;
    private Button closeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        List<Language> languages = generateLanguages();
        LanguageSpinnerAdapter adapter = new LanguageSpinnerAdapter(this,R.layout.language_setting_items,languages);
        languageSpinner = findViewById(R.id.game_language_spinner);
        languageSpinner.setAdapter(adapter);
        closeButton = findViewById(R.id.close_but);
        okButton = findViewById(R.id.close_settings);
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Language l = (Language) parent.getItemAtPosition(position);
                Log.d("Language", l.getLanguage());
                Utils.saveStringSharedPreferences(getApplicationContext(),GAME_LANGUAGE_PREF,LANGUAGE_PREF,l.getCode());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private List<Language> generateLanguages(){
        List<Language> languages = new ArrayList<>();
        languages.add(new Language(R.drawable.france_flag,"Français","fr-fr"));
        languages.add(new Language(R.drawable.spain_flag,"Español","es-es"));
        languages.add(new Language(R.drawable.uk_flag,"English","en_uk"));
        languages.add(new Language(R.drawable.usa_flag,"English","en-us"));

        return languages;

    }
}
