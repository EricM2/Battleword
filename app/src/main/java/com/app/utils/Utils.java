package com.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

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



    public static  String putCharInScreenText(String c,String requiredString,String currentScreenText){

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
            return res;
        }
        else
            return currentScreenText;
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


    public static  String getRandomWord(){
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


}
