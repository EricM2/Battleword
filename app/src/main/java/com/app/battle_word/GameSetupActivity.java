package com.app.battle_word;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.utils.Utils;

public class GameSetupActivity extends AppCompatActivity {
    private Spinner gameLevelSpinner;
    private Button battleButton;
    private Button solitaireButton;
    private final  static  String MODE="mode";
    private final static String LEVEL = "leevel";
    private String selectedGameLevel;
    private Button settingsBut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);
        selectedGameLevel = null;
        solitaireButton = findViewById(R.id.play_solitaire);
        settingsBut = findViewById(R.id.settings_button_setup);
        settingsBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getSettingsIntent());
            }
        });
        //gameLevelSpinner = findViewById(R.id.select_game_level_spinner);
        String[] gameLevels = getResources().getStringArray(R.array.game_levels);
        //gameLevelSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,gameLevels));
        /*gameLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                String value = adapterView.getItemAtPosition(position).toString();
                if (Utils.testIfStrIsInt(value)){
                    selectedGameLevel = value;
                }

                Toast.makeText(adapterView.getContext(), (String) adapterView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                selectedGameLevel = null;

            }
        });*/

        solitaireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent;
                    if(isFirstTime()){
                       intent = gameScenarioIntent();

                    }
                    else{
                        intent = solitaireIntent();

                    }
                startActivity(intent);


            }
        });
    }

    private Intent solitaireIntent(){
        Intent intent = new Intent(this,CountDownActivity.class);

        intent.putExtra(MODE,"solitare");
        //intent.putExtra(LEVEL, selectedGameLevel);
      return  intent;
    }
    private Intent gameScenarioIntent(){
        int nextStage = 1;
        SharedPreferences prefs = this.getSharedPreferences(GameHeaderFragment.PREFERENCES_NAME, 0);
        if (prefs.contains("stage")) {
             nextStage= prefs.getInt("stage",1);
        }

        //Intent intent = new Intent(this,GameScenarioActivity.class);
        Intent intent = new Intent(this,NextStageActivity.class);
        intent.putExtra(MODE,"solitare");
        intent.putExtra("nextStage",nextStage);
        return  intent;
    }

    private Intent getSettingsIntent(){
        Intent i = new Intent(this,SettingsActivity.class);
        return i;
    }

    private boolean isFirstTime(){
        return  true;
    }
}
