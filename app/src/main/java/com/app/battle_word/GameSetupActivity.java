package com.app.battle_word;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class GameSetupActivity extends AppCompatActivity {
    private Spinner gameLevelSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);
        gameLevelSpinner = findViewById(R.id.select_game_level_spinner);
        String[] gameLevels = getResources().getStringArray(R.array.game_levels);
        gameLevelSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,gameLevels));
        gameLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                Toast.makeText(adapterView.getContext(), (String) adapterView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // vacio

            }
        });
    }
}
