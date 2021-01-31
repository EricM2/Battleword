package com.app.battleword;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.battleword.adapters.LanguageSpinnerAdapter;
import com.app.battleword.objects.Letter;
import com.app.battleword.viewmodels.WordViewModel;
import com.app.utils.Touch;
import com.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;


public class ScreenFragment extends Fragment  {
    private GridView gridView;
    private LanguageSpinnerAdapter screenAdapter;
    private TextView screenTextView ;
    private TextView secondaryTextView;
    private String gameText ;
    private WordViewModel wordViewModel;
    private Button tipButton;
    private TextView tipTextView;
    private boolean isHintOn;
    private int currentStage=-1;
    private int touches;
    private TextView touchesLeftTextView;
    private LinearLayout touchesCountLayout;
    private String screenText;
    private String hint;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_screen, container, false);
        screenTextView=v.findViewById(R.id.screen_text);
        secondaryTextView = v.findViewById(R.id.segondary_screen_text);
        //secondaryTextView.setVisibility(View.INVISIBLE);
        tipButton = v.findViewById(R.id.tip_button);
        tipTextView = v.findViewById(R.id.tip_text);
        touchesLeftTextView = v.findViewById(R.id.touch_left_value);
        tipTextView.setVisibility(View.INVISIBLE);
        touchesCountLayout = v.findViewById(R.id.touch_left);
        isHintOn = false;

        wordViewModel = new ViewModelProvider(requireActivity()).get(WordViewModel.class);
        wordViewModel.getScreenText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                screenTextView.setText(s);
            }
        }



    );

        wordViewModel.getWordHint().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tipTextView.setText(s);
            }
        });


        wordViewModel.getSecondScreenText().observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {

                        secondaryTextView.setVisibility(View.VISIBLE);
                        secondaryTextView.setText("");
                        Timer t = new Timer();
                        Utils.setTextViewText(getActivity(),secondaryTextView,s,30,-1,t);


                    }
                }
        );
        wordViewModel.getTurnOffSecondScreenText().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    secondaryTextView.setVisibility(View.INVISIBLE);
                    secondaryTextView.setText("");
                }
                else
                    secondaryTextView.setVisibility(View.VISIBLE);
            }
        });
        wordViewModel.getGameStage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                currentStage = Integer.valueOf(s);
                if(currentStage >=4)
                    touchesCountLayout.setVisibility(View.VISIBLE);
                else
                    touchesCountLayout.setVisibility(View.INVISIBLE);

            }
        });

        wordViewModel.getNumTouch().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if( (currentStage!=-1) && currentStage>=4){
                     int v = Touch.getNumTouches(currentStage)-integer.intValue();
                     if(v>=0)
                        touchesLeftTextView.setText(String.valueOf(v));

                }
            }
        });

        tipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isHintOn)
                    tipTextView.setVisibility(View.INVISIBLE);
                else{

                        tipTextView.setVisibility(View.VISIBLE);
                        Utils.playSound(getActivity(),R.raw.hint_sound,false);


                    closeTipAfter(4000);

                }
                isHintOn = !isHintOn;
            }
        });
        /*if(savedInstanceState!=null){
            screenTextView.setText(savedInstanceState.getString("current_text"));
            tipTextView.setText(savedInstanceState.getString("current_hint"));
            secondaryTextView.setText(savedInstanceState.getString("secondary_text"));
            if(Touch.getNumTouches(currentStage)!=-1) {
                touchesLeftTextView.setText(savedInstanceState.getString("touches_lefts"));
                touchesLeftTextView.setVisibility(View.VISIBLE);
            }
            isHintOn = savedInstanceState.getBoolean("is_hint_on");
            if(isHintOn){
                tipTextView.setVisibility(View.VISIBLE);
                closeTipAfter(4000);
            }

        }*/




        return v;

    }
    private List<Integer> buildScreenCharBackground(){
        List<Integer> res = new ArrayList<>();
        for(int i=0;i<31;i++)
            res.add(R.drawable.line);
        return res;
    }
    private List<Letter> builtLetters(){
        List<Integer> resIds = buildScreenCharBackground();
        List<Letter> result = new ArrayList<>();
        for (int i=0; i<resIds.size();i++){
            int resId = resIds.get(i);
            if(resId==0){
                Letter l = new Letter(R.drawable.fully_transparent_key,R.drawable.fully_transparent_key);
                result.add(l);
            }
            else {
                Letter l = new Letter(resId,R.drawable.fully_transparent_key);
                Log.d("ID", String.valueOf(resId));
                result.add(l);
            }

        }

        return result;
    }

    private void closeTipAfter(long millis){
        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                tipTextView.setVisibility(View.INVISIBLE);
            }
        },millis);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("current_text",screenTextView.getText().toString());
        outState.putBoolean("is_hint_on",isHintOn);
        outState.putString("current_hint",tipTextView.getText().toString());
        outState.putString("touches_lefts",touchesLeftTextView.getText().toString());
        outState.putString("secondary_text",secondaryTextView.getText().toString());

    }

    public void setHint(String hint){
        this.hint = hint;
        tipTextView.setText(hint);
    }
    public void setScreenText(final String text){
        if(getActivity()!=null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    screenTextView.setText(text);
                }
            });
        }
    }

    public void setSegondaryScreen(final String text){
        if(getActivity()!=null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    secondaryTextView.setText("");
                    Timer t = new Timer();
                    if(!text.equals("")) {
                        secondaryTextView.setVisibility(View.VISIBLE);
                        secondaryTextView.setText(text);
                        //Utils.setTextViewText(getActivity(), secondaryTextView, text, 30, -1, t);
                    }
                    else {
                        secondaryTextView.setText("");
                        secondaryTextView.setVisibility(View.INVISIBLE);
                    }

                }
            });


        }
    }

    public void updateTouchesLeft(final int touchesL){
        if(getActivity()!=null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    touchesLeftTextView.setText(String.valueOf(touchesL));
                }
            });
        }
    }


    /* @Override
    public void onWordFound() {
        resetScreenForNewWord();
    }

    @Override
    public void onTimeCompleted() {
        resetScreenForNewWord();
    }

    private void resetScreenForNewWord(){
        gameText = Utils.getRandomWord();
        String iniText = Utils.initScreemFromText(gameText);
        screenTextView.setText(iniText);
    }*/
}
