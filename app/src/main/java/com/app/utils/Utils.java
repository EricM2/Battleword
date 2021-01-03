package com.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.battleword.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

public class Utils {

    public static double[] stageWeight = new double[]{40d,55d,70d,80d,90d};

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

        if(requiredString.contains(c)) {
            char[] list = currentScreenText.toCharArray();
            List<Integer> indexes = getCharPosition(c,requiredString);
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
        return  EnWords.english[v];
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
        boolean res = getBooleanSharedPreferences(c,Strings.FIRST_TIME_STAGE_PREF,prefKey,false);
        return res;
    }
    public  static void setStageFirstTime(Context c, int stage){
        String prefKey = "stage"+String.valueOf(stage);
        saveBooleanSharedPreferences(c,Strings.FIRST_TIME_STAGE_PREF,prefKey,true);

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
                char c= text.charAt(i[0]);
                //Log.d("Strange",""+c);
                scenario.append(String.valueOf(c));
                i[0]++;
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




}
