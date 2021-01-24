package com.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.JsonReader;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.battleword.BackgroundSoundService;
import com.app.battleword.R;
import com.app.battleword.objects.Word;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

public class Utils {

    public static double[] stageWeight = new double[]{40d,55d,70d,80d,90d};
    public static  Map<String,List<Word>> apiWords =null;
    public static Map<String,List<Word>> innerGameWords =null;

    public static boolean testIfStrIsInt(String str){
        try {
            int v = Integer.valueOf(str);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    //String[] wordbank = new String[] ()

    public static Map<Integer,String> getKeyString(){
        Map<Integer,String> res = new HashMap<>();
        res.put(0,"q");
        res.put(1,"w");
        res.put(2,"e");
        res.put(3,"r");
        res.put(4,"t");
        res.put(5,"y");
        res.put(6,"u");
        res.put(7,"i");
        res.put(8,"o");
        res.put(9,"p");
        res.put(10,"a");
        res.put(11,"s");
        res.put(12,"d");
        res.put(13,"f");
        res.put(14,"g");
        res.put(15,"h");
        res.put(16,"j");
        res.put(17,"k");
        res.put(18,"l");
        res.put(19,"ñ");
        res.put(20,"");
        res.put(21,"z");
        res.put(22,"x");
        res.put(23,"c");
        res.put(24,"v");
        res.put(25,"b");
        res.put(26,"n");
        res.put(27,"m");
        res.put(28,"");
        res.put(29,"");
        return res;

    }
    public static int roundNumber(double number){
        int dec = (int)number;
        double fl = number - dec;
        if(fl >= 0.5f)
            return  dec + 1;
        else
            return dec;

    }
    public static String initScreemFromText(String text, int stage){

        double centile =  (stageWeight[stage-1]/100d);

        int numWord = roundNumber(((double) text.length())*centile);

        numWord = numWord<1? 1:numWord;
        List<Integer> indexes = new ArrayList<>();
        for (int i =0; i<numWord;i++)
            indexes.add((new Random()).nextInt(text.length()));


        String ini = "";
        for (int i =0; i< text.length();i++){
            if(indexes.contains(i)){
                ini+="_";

            }
            else {
                ini+=text.charAt(i);
            }


        }


        return ini;
    }



    public static  String putCharInScreenText(String c,String requiredString,String currentScreenText,Context context){
        List<Integer> indexes = getCharPosition(c,requiredString);
        List<Integer> indexesFound = getCharPosition(c,currentScreenText);
        if(requiredString.contains(c) && indexes.size()>indexesFound.size()) {
            char[] list = currentScreenText.toCharArray();

            if (!indexes.isEmpty()){
                Iterator<Integer> it = indexes.iterator();
                while (it.hasNext()){
                    int v = it.next();
                    list[v]=c.charAt(0);

                }
            }

            String res = String.copyValueOf(list);
            playSound(context, R.raw.word_found_sound,false);
            return res;
        }
        else {
            playSound(context, R.raw.word_not_found_sound,false);
            return currentScreenText;
        }
    }

    public static boolean isSreenTextComplete(String screenText){
        return !screenText.contains("_");
    }

    public static List<Integer> getCharPosition(String c, String container){
        List<Integer> res = new ArrayList<>();
        if (container.contains(c)){
            for (int i=0; i< container.length();i++){
                if(String.valueOf(container.charAt(i)).equalsIgnoreCase(c))
                    res.add(i);
            }
        }
        return res;
    }


    public static  String getRandomWord(String language, int stage){
        Random r = new Random();
        int v = r.nextInt(EnWords.english.length);
        return  "anticonstitutionel" /*EnWords.english[v]*/;
    }
    public  static void waitFor(long milliseconds) {
        CountDownTimer countDownTimer = new CountDownTimer(milliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("waitting", "onTick: ");
            }

            @Override
            public void onFinish() {
                Log.d("waitting", "finish: ");
            }
        }.start();
    }

    public static void saveIntSharedPreferences(Context context,String prefName,String prefKey, int prevefVale ){
        SharedPreferences prefs = context.getSharedPreferences(prefName, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(prefKey, prevefVale);
        editor.commit();
    }
    public static void saveStringSharedPreferences(Context context,String prefName,String prefKey, String prevefVale ){
        SharedPreferences prefs = context.getSharedPreferences(prefName, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(prefKey, prevefVale);
        editor.commit();
    }
    public static int getIntSharedPreferences(Context context,String prefName,String prefKey){
        SharedPreferences prefs = context.getSharedPreferences(prefName, 0);
        if (prefs.contains(prefKey)) {
            return prefs.getInt(prefKey,-1);
        }
        else {
            return -1;
        }

    }
    public static String getStringSharedPreferences(Context context,String prefName,String prefKey,String defaultValue){
        SharedPreferences prefs = context.getSharedPreferences(prefName, 0);
        if (prefs.contains(prefKey)) {
            return prefs.getString(prefKey,defaultValue);
        }
        else {
            return defaultValue;
        }

    }
    public static boolean getBooleanSharedPreferences(Context context,String prefName,String prefKey,boolean defaultValue){
        SharedPreferences prefs = context.getSharedPreferences(prefName, 0);

        if (prefs.contains(prefKey)) {
            return prefs.getBoolean(prefKey,defaultValue);
        }
        else {
            return defaultValue;
        }

    }

    public static void saveBooleanSharedPreferences(Context context,String prefName,String prefKey, boolean prefValue ){
        SharedPreferences prefs = context.getSharedPreferences(prefName, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(prefKey, prefValue);
        editor.commit();
    }


    public static String getGameLanguage(Context context,String prefName,String prefKey){
        String defaultL = Locale.getDefault().getLanguage().trim()+"-"+Locale.getDefault().getCountry().trim();
        return getStringSharedPreferences(context,prefName,prefKey,defaultL);

    }

   public static void resetGameStatePreferences(Context context, String prefName){
        SharedPreferences pref = context.getSharedPreferences(prefName,0);
        pref.edit().clear().commit();
    }

    public  static boolean isStageFirstTime(Context c, int stage){
        String prefKey = "stage"+String.valueOf(stage);
        boolean res = getBooleanSharedPreferences(c,Strings.FIRST_TIME_STAGE_PREF,prefKey,true);
        return res;
    }
    public  static void setStageFirstTime(Context c, int stage){
        String prefKey = "stage"+String.valueOf(stage);
        saveBooleanSharedPreferences(c,Strings.FIRST_TIME_STAGE_PREF,prefKey,false);

    }


    public static void setTextViewText(final Context context, final TextView scenario, final String text, final int duration, final int soundId,final Timer t){
        final int length =  text.length();
        final int[] i = new int[1];
        i[0] = 0;



        final Handler handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(length>i[0]) {
                    char c = text.charAt(i[0]);
                    //Log.d("Strange",""+c);
                    scenario.append(String.valueOf(c));
                    i[0]++;
                }
            }
        };


        TimerTask taskEverySplitSecond = new TimerTask() {

            int count = 0;


            @Override
            public void run() {


                handler.sendEmptyMessage(0);

                if (i[0] == length - 1) {
                    t.cancel();

                }
                else{
                    if(soundId !=-1 && count==2 ){

                        MediaPlayer p = Utils.playSound(context,soundId,false);
                        count = 0;
                    }
                    else{
                        if(soundId != -1)
                            count++;
                    }
                }

            }
        };
        t.schedule(taskEverySplitSecond, 1, duration);

    }

    public static  MediaPlayer playSound(Context c, int rawid, boolean loop){
        if(c==null){
            return null;
        }
        else{

            MediaPlayer p = MediaPlayer.create(c, rawid);


            p.setLooping(loop);
            p.setVolume(100,100);
            p.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
            p.start();
            return p;
        }

    }

    public static void doAfter(long wait, final Callable func){
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    func.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },wait);
    }


   public static List<Word> getWordForStage(Context c, int stage, String language) throws Exception{
        Set<Word> uniqWords= new HashSet<>();
       List<Word> words = new ArrayList<>();
        if(!(language.equalsIgnoreCase("fr")|| language.equalsIgnoreCase("en")
                || language.equalsIgnoreCase("es"))){
            throw new Exception("bad language parameter: "+ language);
        }
        else{
            //if(stage < 4) {
                int st = stage >= 4 ? 4 : stage;
                String fileName = "stage_" + String.valueOf(st) + "_" + language + ".csv";
                InputStreamReader is = new InputStreamReader(c.getAssets()
                        .open(fileName));

                BufferedReader reader = new BufferedReader(is);
                reader.readLine();
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length != 3)
                        throw new Exception("bad line in " + fileName);
                    else
                        uniqWords.add(new Word(parts[0].toLowerCase().trim(), Integer.valueOf(parts[1].toLowerCase().trim()), parts[2]));

                }
            //}
            /*else{
                for(int i = 0; i< 15; i++)
                    words.add(new Word("testword",stage,""));
            }*/

        }
        //if(stage<4)
            words.addAll(uniqWords);
       Collections.shuffle(words);
       Collections.shuffle(words);
        return  words;
   }


   public static Word getNewWord(Map<String,List<Word>> words,int stage,int wordIndex){
        //int s = stage > 4 ? 4 : stage;
        String key = "stage"+String.valueOf(stage);
        /*if(key.equalsIgnoreCase("stage5"))
            return new Word("test",5,"RAS");
        else*/
            return words.get(key).get(wordIndex);
   }

   public static String getTextScenarioForStage(Context context,int stage){
        switch (stage){
            case 1:
                return context.getString(R.string.text_scenario_sample_stage_1);
            case 2:
                return context.getString(R.string.text_scenario_sample_stage_2);
            case 3:
                return context.getString(R.string.text_scenario_sample_stage_3);
            case 4:
                return context.getString(R.string.text_scenario_sample_stage_4);
            case 5:
                return context.getString(R.string.text_scenario_sample_stage_5);
            default:
                return "";
        }

   }

   public static Boolean hasInternet(Context context){
       boolean connected = false;
       ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);


       if((connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null &&
          connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)||
         (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null &&
          connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED))
       {
           //we are connected to a network
           connected = true;
       }
       else
           connected = false;

       return connected;
   }

   public static void stopSoundGenericService(Context context){
        Intent i = new Intent(context, BackgroundSoundService.class);
        context.stopService(i);
   }

   public static  void subscribeEmail(Context context,String email){
        sendPost(email);
   }




    private static void sendPost(final String email) {
        (new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    URL url = new URL(Strings.POST_SUBSCRIBER_URL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("email", email);
                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }).execute();
        /*Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(Strings.POST_SUBSCRIBER_URL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("email", email);
                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , conn.getResponseMessage());

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();*/
    }

    public static Map<String,List<Word>>  getWordFromApiPost(String lang) {


                Map<String,List<Word>> res = new HashMap<>();
                try {
                    for (int i = 1; i <= 5; i++){
                        String inline = "";
                        List<Word> ws = new ArrayList<>();
                        URL url = new URL(Strings.GET_WORD_BASE_URL+i+"/10/"+lang+"/");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                        conn.setRequestProperty("Accept", "application/json");
                        conn.setDoOutput(true);
                        conn.setDoInput(true);
                        conn.connect();
                        int responsecode = conn.getResponseCode();
                        if(true/*responsecode==200*/){
                            Scanner sc = new Scanner(url.openStream());
                            while (sc.hasNext()){
                                inline+= sc.nextLine();
                            }
                            JSONArray j = new JSONArray(inline);
                            for (int l = 0; l< j.length(); l++ ){
                                JSONObject b = (JSONObject) j.get(l);
                                String word = b.getString("word").toLowerCase().trim();
                                String hint = b.getString("hint");
                                int stage = b.getInt("stage");
                                Word w = new Word(word,stage,hint);
                                ws.add(w);
                            }
                            res.put("stage"+i,ws);



                        }

                        conn.disconnect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return res;
            }









    }
