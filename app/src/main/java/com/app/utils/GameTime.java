package com.app.utils;

public class GameTime {
    public static long TIME_STAGE_1=60000;
    public static long TIME_STAGE_2=60000;
    public static long TIME_STAGE_3=50000;
    public static long TIME_STAGE_4=40000;
    public  static long TIME_STAGE_5=30000;

    public static long getTime(int stage) throws Exception {
        switch (stage){
            case 1:
                return TIME_STAGE_1;
            case 2:
                return TIME_STAGE_2;
            case 3:
                return  TIME_STAGE_3;
            case 4:
                return TIME_STAGE_4;
            case 5:
                return  TIME_STAGE_5;
            default:
                throw new Exception("GAME TIME, Bad value of stage:" + stage);
        }
    }
}
