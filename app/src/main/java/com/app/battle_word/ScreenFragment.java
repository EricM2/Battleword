package com.app.battle_word;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.app.battle_word.adapters.ScreenAdapter;
import com.app.battle_word.objects.Letter;
import com.app.battle_word.subscribers.WordFoundSubscriber;
import com.app.battle_word.subscribers.WordTimeCompletedSubscriber;
import com.app.battle_word.viewmodels.ScreenTextViewModel;
import com.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScreenFragment extends Fragment  {
    private GridView gridView;
    private ScreenAdapter screenAdapter;
    private TextView screenTextView ;
    private TextView secondaryTextView;
    private String gameText ;
    private ScreenTextViewModel screenTextViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_screen, container, false);
        screenAdapter = new ScreenAdapter(getContext(),R.layout.screen_items,builtLetters());
        screenTextView=v.findViewById(R.id.screen_text);
        secondaryTextView = v.findViewById(R.id.segondary_screen_text);
        secondaryTextView.setVisibility(View.INVISIBLE);
        gameText = Utils.getRandomWord();
       // String iniText = Utils.initScreemFromText(gameText);
        //screenTextView.setText(iniText);
        screenTextViewModel = new ViewModelProvider(requireActivity()).get(ScreenTextViewModel.class);
        screenTextViewModel.getScreenText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                screenTextView.setText(s);
            }
        }

    );

        screenTextViewModel.getSecondScreenText().observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        secondaryTextView.setText(s);
                        secondaryTextView.setVisibility(View.VISIBLE);

                    }
                }
        );
        screenTextViewModel.getTurnOffSecondScreenText().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean)
                    secondaryTextView.setVisibility(View.INVISIBLE);
                else
                    secondaryTextView.setVisibility(View.VISIBLE);
            }
        });


       // gridView = v.findViewById(R.id.screen_grid);
//        gridView.setAdapter(screenAdapter);

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
