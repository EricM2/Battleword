package com.app.battleword;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.app.battleword.objects.Word;
import com.app.utils.Strings;
import com.app.utils.Utils;

import java.util.List;
import java.util.Map;
import java.util.Timer;

public class GameService extends Service {

    private CountDownActivity gameTimer;
    private String currentWord;
    private Map<String, List<Word>> words;
    public GameService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public  void play(int num){

    }

    private void loadWords(){
        String prefCode = Utils.getGameLanguage(getApplicationContext(), Strings.GAME_LANGUAGE_PREF, Strings.LANGUAGE_PREF);
        String lang = prefCode.split("-")[0];
        for ( int i = 1 ; i<=5; i++) {
            String key = "stage" + String.valueOf(i);
            try {
                words.put(key, Utils.getWordForStage(this, i, lang));
            } catch (Exception e) {
                Log.d("Exception", e.getMessage());
            }
        }
    }




}
