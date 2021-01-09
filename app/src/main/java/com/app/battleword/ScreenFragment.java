package com.app.battleword;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.TextView;

import com.app.battleword.adapters.LanguageSpinnerAdapter;
import com.app.battleword.objects.Letter;
import com.app.battleword.viewmodels.ScreenTextViewModel;
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
    private ScreenTextViewModel screenTextViewModel;
    private Button tipButton;
    private TextView tipTextView;
    private boolean isHintOn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_screen, container, false);
        screenTextView=v.findViewById(R.id.screen_text);
        secondaryTextView = v.findViewById(R.id.segondary_screen_text);
        secondaryTextView.setVisibility(View.INVISIBLE);
        tipButton = v.findViewById(R.id.tip_button);
        tipTextView = v.findViewById(R.id.tip_text);
        tipTextView.setVisibility(View.INVISIBLE);
        isHintOn = false;

        screenTextViewModel = new ViewModelProvider(requireActivity()).get(ScreenTextViewModel.class);
        screenTextViewModel.getScreenText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                screenTextView.setText(s);
            }
        }



    );

        screenTextViewModel.getWordHint().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tipTextView.setText(s);
            }
        });


        screenTextViewModel.getSecondScreenText().observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {

                        secondaryTextView.setVisibility(View.VISIBLE);
                        Timer t = new Timer();
                        Utils.setTextViewText(getActivity(),secondaryTextView,s,30,-1,t);


                    }
                }
        );
        screenTextViewModel.getTurnOffSecondScreenText().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
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
        if(savedInstanceState!=null){
            screenTextView.setText(savedInstanceState.getString("current_text"));
            tipTextView.setText(savedInstanceState.getString("current_hint"));
            isHintOn = savedInstanceState.getBoolean("is_hint_on");
            if(isHintOn){
                tipTextView.setVisibility(View.VISIBLE);
                closeTipAfter(4000);
            }

        }




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
