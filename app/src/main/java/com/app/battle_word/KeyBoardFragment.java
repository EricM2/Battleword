package com.app.battle_word;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.app.battle_word.adapters.KeyBoardAdapter;
import com.app.battle_word.objects.Letter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KeyBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
//20,28,29 VIDES
public class KeyBoardFragment extends Fragment {
    private GridView gridView;
    private List<Integer> keyButtonResId;
    private KeyBoardAdapter keyBoardAdapter;
    private ScreenFragment screenFragment;
    private FragmentManager fm;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_key_board, container, false);
        gridView = v.findViewById(R.id.grid);
        List<Letter> letters = builtLetters();
        fm = getFragmentManager();
        screenFragment = (ScreenFragment) fm.findFragmentById(R.id.screen_fragment);
        keyBoardAdapter = new KeyBoardAdapter(v.getContext(),R.layout.keyboard_items,letters,screenFragment);
        gridView.setAdapter(keyBoardAdapter);
        return v;
    }



    private List<Integer> buildKeyboardBackground(){
        List<Integer> res = new ArrayList<>();
        res.add(R.drawable.q);
        res.add(R.drawable.w);
        res.add(R.drawable.e);
        res.add(R.drawable.r);
        res.add(R.drawable.t);
        res.add(R.drawable.y);
        res.add(R.drawable.u);
        res.add(R.drawable.i);
        res.add(R.drawable.o);
        res.add(R.drawable.p);
        res.add(R.drawable.a);
        res.add(R.drawable.s);
        res.add(R.drawable.d);
        res.add(R.drawable.f);
        res.add(R.drawable.g);
        res.add(R.drawable.h);
        res.add(R.drawable.j);
        res.add(R.drawable.k);
        res.add(R.drawable.l);
        res.add(R.drawable.enye);
        res.add(0);
        res.add(R.drawable.z);
        res.add(R.drawable.x);
        res.add(R.drawable.c);
        res.add(R.drawable.v);
        res.add(R.drawable.b);
        res.add(R.drawable.n);
        res.add(R.drawable.m);
        res.add(0);
        res.add(0);

        return res;
    }
    private List<Letter> builtLetters(){
        List<Integer> resIds = buildKeyboardBackground();
        List<Letter> result = new ArrayList<>();
        for (int i=0; i<resIds.size();i++){
            int resId = resIds.get(i);
            if(resId==0){
                Letter l = new Letter(R.drawable.fully_transparent_key,R.drawable.fully_transparent_key);
                result.add(l);
            }
            else {
                Letter l = new Letter(resId,R.drawable.transparent_key);
                result.add(l);
            }

        }
        return result;
    }

}
