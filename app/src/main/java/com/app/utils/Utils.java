package com.app.utils;

public class Utils {

    public static boolean testIfStrIsInt(String str){
        try {
            int v = Integer.valueOf(str);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
}
