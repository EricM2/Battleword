package com.app.battleword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.utils.Strings;
import com.app.utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GameLinkActivity extends AppCompatActivity {
    private TextView linkTextView;
    private Button shareLink ;
    private String link = "";
    private String gameId ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_link);
        linkTextView = findViewById(R.id.game_link);
        shareLink = findViewById(R.id.share);
        if(savedInstanceState!=null) {
            link = savedInstanceState.getString("link");
            gameId = savedInstanceState.getString("gameid");
        }

        shareLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!link.equals(""))
                    openShareLinkChooser();
                else
                    Toast.makeText(GameLinkActivity.this,"wait for game link generation",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        if(link.equals(""))
            generateGameLink();
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("link",link);
        outState.putString("gameid",gameId);
        super.onSaveInstanceState(outState);
    }

    private void openShareLinkChooser(){
        Intent myIntent = new Intent(Intent.ACTION_SEND);
        myIntent.setType("text/plain");
        String shareBody = link;
        String shareSub = "Encuentrame en battleword";
        myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
        myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(myIntent, "Invitation"));
    }

    private void generateGameLink(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        final Map<String, Object> game = new HashMap<>();
        game.put(Strings.GAME_ID, Utils.generateGameId());
        game.put(Strings.GAME_STATUS, Strings.ACTIVE);
        game.put(Strings.CREATED, new Date());


// Add a new document with a generated ID
        db.collection("games")
                .add(game)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                         gameId=documentReference.getId();
                         String link1 = Strings.DOMAIN_URI_PREFIX+"/?gameid="+game.get(Strings.GAME_ID);
                         Uri uri = buildLink(link1);
                         link = uri.toString();
                         linkTextView.setText(link);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       gameId = "";
                    }
                });





    }

    private Uri buildLink(String link){
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(link))
                .setDomainUriPrefix(Strings.DOMAIN_URI_PREFIX)
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.app.battleword")
                                .build())
                .buildDynamicLink();

        return  dynamicLink.getUri();
    }
}
