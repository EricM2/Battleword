package com.app.battleword;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.utils.Utils;

import java.util.Timer;
import java.util.TimerTask;

public class GameScenarioActivity extends Activity {

    private Button playNow;
    private ImageView rollPaper;
    private  Button slideLeft;
    private  Button slideRight;
    private ImageView slideIndicator1;
    private ImageView slideIndicator2;
    private ImageView slideIndicator3;
    private ImageView slideIndicator4;
    private TextView stageTitleTextView;
    private TextView stageMessageTextView;
    private int pageIndex =1;
    private int stage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_scenario);
        playNow = findViewById(R.id.play_now);
        slideLeft = findViewById(R.id.slide_left);
        slideLeft.setVisibility(View.INVISIBLE);
        slideRight = findViewById(R.id.slide_right);
        //rollPaper = findViewById(R.id.roll_message_paper);
        slideIndicator1 = findViewById(R.id.slide_indicator_1);
        slideIndicator2 = findViewById(R.id.slide_indicator_2);
        slideIndicator3 = findViewById(R.id.slide_indicator_3);
        slideIndicator4 = findViewById(R.id.slide_indicator_4);
        stageTitleTextView = findViewById(R.id.stage_title);
        stageMessageTextView=findViewById(R.id.scenario_text);
        stage = getIntent().getIntExtra("nextStage",1);
        stageTitleTextView.setText(getString(R.string.stage)+" "+ String.valueOf(stage));


        /*rollPaper.setOnTouchListener(new OnSwipeListener(GameScenarioActivity.this){
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                slideNext();
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                slidePrev();
            }
        });*/

        playNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = solitaireIntent();
                Utils.setStageFirstTime(getApplicationContext(),stage);
                Utils.playSound(getApplicationContext(), R.raw.play_button_sound,false);
                startActivity(intent);
            }
        });
        slideRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slideNext();
            }
        });
        slideLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidePrev();
            }
        });

        Utils.setTextViewText(stageMessageTextView,getString(R.string.text_scenario_sample),50);
    }

    private Intent solitaireIntent(){
        Intent intent = new Intent(this,CountDownActivity.class);
        intent.putExtra("mode","solitare");
        //intent.putExtra(LEVEL, selectedGameLevel);
        return  intent;
    }

    private int getPageId(int currentPageIndex) throws Exception{
        switch (currentPageIndex){
            case 1:
                return R.drawable.roll_message_background_1;
            case 2:
                return R.drawable.roll_message_background_2;

            case 3:
                return R.drawable.roll_message_background_3;

            case 4:
                return R.drawable.roll_message_background_4;
            default:
                throw new Exception("The page index must be bettween 1 and 4");

        }
    }

    private ImageView getIndicatorView(int currentPageIndex) throws Exception{
        switch (currentPageIndex){
            case 1:
                return slideIndicator1;
            case 2:
                return slideIndicator2;
            case 3:
                return slideIndicator3;
            case 4:
                return slideIndicator4;
            default:
                throw new Exception("The page index must be bettween 1 and 4");

        }
    }

    private void slideNext(){
        try {

            if(pageIndex < 4){
                ImageView prevIndicator = getIndicatorView(pageIndex);

                prevIndicator.setImageResource(R.drawable.slide_indicator_transparent);
               /* prevIndicator.getLayoutParams().height=10;
                prevIndicator.getLayoutParams().width=10;*/
                prevIndicator.requestLayout();

                pageIndex +=1;
                int id = getPageId(pageIndex);
                int rollMessageBackground = getPageId(pageIndex);
                ImageView slideIndicator = getIndicatorView(pageIndex);
                slideIndicator.setImageResource(R.drawable.slide_indicator_black);
                /*slideIndicator.getLayoutParams().height=11;
                slideIndicator.getLayoutParams().width=11;
                slideIndicator.requestLayout();*/
                //rollPaper.setImageResource(rollMessageBackground);
                if(pageIndex==4)
                    slideRight.setVisibility(View.INVISIBLE);
                if (pageIndex==2)
                    slideLeft.setVisibility(View.VISIBLE);



            }
        }
        catch (Exception e){
            Log.d("SlideError", e.getMessage());
        }

    }

    private void slidePrev(){
        try {

            if(pageIndex > 1){

                ImageView prevIndicator = getIndicatorView(pageIndex);
                prevIndicator.setImageResource(R.drawable.slide_indicator_transparent);
                /*prevIndicator.getLayoutParams().height=10;
                prevIndicator.getLayoutParams().width=10;
                prevIndicator.requestLayout();*/

                pageIndex -=1;
                int rollMessageBackground = getPageId(pageIndex);
                //rollPaper.setImageResource(rollMessageBackground);
                int id = getPageId(pageIndex);

                ImageView slideIndicator = getIndicatorView(pageIndex);
                slideIndicator.setImageResource(R.drawable.slide_indicator_black);
                /*slideIndicator.getLayoutParams().height=11;
                slideIndicator.getLayoutParams().width=11;*/
                slideIndicator.requestLayout();
                if(pageIndex==1)
                     slideLeft.setVisibility(View.INVISIBLE);
                if (pageIndex==3)
                    slideRight.setVisibility(View.VISIBLE);



            }
        }
        catch (Exception e){
            Log.d("SlideError", e.getMessage());
        }



    }

}
