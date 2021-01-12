package com.app.battleword;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.app.battleword.adapters.LanguageSpinnerAdapter;
import com.app.battleword.objects.Language;
import com.app.utils.Strings;
import com.app.utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

public class SettingsActivity extends Activity {
    private Spinner languageSpinner;

    private Button okButton;
    private Button closeButton;
    private String languageCode;
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
        String prefCode = Utils.getGameLanguage(getApplicationContext(),Strings.GAME_LANGUAGE_PREF, Strings.LANGUAGE_PREF);
        int defaultLaguange = findByCode(prefCode,languages);
        languageSpinner.setSelection(defaultLaguange);
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Language l = (Language) parent.getItemAtPosition(position);
                Log.d("Language", l.getLanguage());
                Utils.saveStringSharedPreferences(getApplicationContext(),Strings.GAME_LANGUAGE_PREF,Strings.LANGUAGE_PREF,l.getCode());
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
        languages.add(new Language(R.drawable.france_flag,"Français","fr-FR"));
        languages.add(new Language(R.drawable.spain_flag,"Español","es-ES"));
        languages.add(new Language(R.drawable.uk_flag,"English","en-GB"));
        languages.add(new Language(R.drawable.usa_flag,"English","en-US"));

        return languages;

    }

    private int findByCode(String languageCode, List<Language> languages){
        Iterator<Language> it = languages.iterator();
        int res=0;
        for (int i= 0; i<languages.size(); i++){
            Language l = languages.get(i);
            if (l.getCode().trim().equalsIgnoreCase(languageCode.trim())) {
                res = i;
                break;
            }
        }
        return res;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Callable c = new Callable() {
            @Override
            public Object call() throws Exception {
                Utils.playSound(SettingsActivity.this,R.raw.settings_activity_sound,false);
                return null;
            }
        };
        Utils.doAfter(200,c);


    }
}
