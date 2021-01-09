package com.app.utils;

public class Touch {
    public static int STAGE_4_TOUCHES= 25;
    public static int STAGE_5_TOUCHES= 20;
    public  static  int getNumTouches(int stage){
        if(stage<4)
            return -1;
        else
        {
            if (stage==4)
                return STAGE_4_TOUCHES;
            if (stage == 5)
                return STAGE_5_TOUCHES;
            else
                return -1;
        }

    }

}
