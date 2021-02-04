package com.app.battleword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.utils.Strings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class PseudoSetupActivity extends AppCompatActivity {
    private String gameLink;
    private String pseudo;
    private EditText pseudoTextView;
    private Button setPseudoButton;
    private String gameId;
    private boolean isGameActive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pseudo_setup);
        setPseudoButton = findViewById(R.id.create_pseudo);
        pseudoTextView = findViewById(R.id.pseudo);
        pseudoTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pseudo = s.toString();
            }
        });

        if(savedInstanceState==null){
            gameLink = getIntent().getStringExtra(Strings.GAME_LINK);
        }
        else {
            pseudo = savedInstanceState.getString("pseudo");
            gameLink = savedInstanceState.getString(Strings.GAME_LINK);
            pseudoTextView.setText(pseudo);
        }

        gameId = getGameIdFromLink(gameLink);



    }

    private String getGameIdFromLink(String link){
        String[] parts = link.split("\\?");
        if(parts.length==2){
            return parts[1].split("=")[1];
        }
        else
            return null;

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(Strings.GAME_LINK,gameLink);
        outState.putString("pseudo",pseudo);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        if(gameId!=null){
            validateGameStatus(gameId);

        }
        else{

            startMainActivity();
        }
        super.onResume();
    }

    private void startMainActivity(){
        Intent i = new Intent(this , MainActivity.class);
        i.setAction("android.intent.action.MAIN");
        startActivity(i);
        finish();

    }

    private void validateGameStatus(String gameid){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("games")
                .whereEqualTo("gameid", gameid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getData().get(Strings.GAME_STATUS).equals(Strings.ACTIVE)){
                                    Log.d("GAME_STATUS", document.getId() + " => " + document.getData());
                                    isGameActive = true;
                                }
                            }
                        } else {
                                isGameActive = false;
                                startMainActivity();
                        }
                    }
                });

    }
}
