package com.app.battleword;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class FirstTimeGameFragment extends Fragment {
    private Spinner languageSpinner;

    private Button okButton;
    private String languageCode;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_first_time_game, container, false);
        final Intent gameSetupIntent = new Intent(getActivity(),GameSetupActivity.class);
        List<Language> languages = generateLanguages();
        LanguageSpinnerAdapter adapter = new LanguageSpinnerAdapter(getActivity(),R.layout.language_setting_items,languages);
        languageSpinner = v.findViewById(R.id.game_language_spinner);
        languageSpinner.setAdapter(adapter);
        okButton = v.findViewById(R.id.close_settings);
        String prefCode = Utils.getGameLanguage(getActivity().getApplicationContext(), Strings.GAME_LANGUAGE_PREF, Strings.LANGUAGE_PREF);
        int defaultLaguange = findByCode(prefCode,languages);
        languageSpinner.setSelection(defaultLaguange);
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Language l = (Language) parent.getItemAtPosition(position);
                Log.d("Language", l.getLanguage());
                Utils.saveStringSharedPreferences(getActivity().getApplicationContext(),Strings.GAME_LANGUAGE_PREF,Strings.LANGUAGE_PREF,l.getCode());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(gameSetupIntent);
                getActivity().finish();
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();

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
}
