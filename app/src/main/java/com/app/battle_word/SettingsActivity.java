package com.app.battle_word;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.app.battle_word.adapters.LanguageSpinnerAdapter;
import com.app.battle_word.objects.Language;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends Activity {
    private Spinner languageSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        List<Language> languages = generateLanguages();
        LanguageSpinnerAdapter adapter = new LanguageSpinnerAdapter(this,R.layout.language_setting_items,languages);
        languageSpinner = findViewById(R.id.game_language_spinner);
        languageSpinner.setAdapter(adapter);
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Language l = (Language) parent.getItemAtPosition(position);
                Log.d("Language", l.getLanguage());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private List<Language> generateLanguages(){
        List<Language> languages = new ArrayList<>();
        languages.add(new Language(R.drawable.france_flag,"Français"));
        languages.add(new Language(R.drawable.spain_flag,"Español"));
        languages.add(new Language(R.drawable.uk_flag,"English"));
        languages.add(new Language(R.drawable.usa_flag,"English"));

        return languages;

    }
}
