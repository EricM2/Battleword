package com.app.battle_word.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.app.battle_word.R;
import com.app.battle_word.objects.Letter;

import java.util.ArrayList;

public class KeyBoardAdapter extends ArrayAdapter {

    ArrayList<Letter> letters = new ArrayList<>();
    Fragment screen = null;
    public KeyBoardAdapter(@NonNull Context context, int resource, ArrayList keyLetters, Fragment screenFragment) {
        super(context, resource);
        letters = keyLetters;
        screen = screenFragment;
    }



    @Override
    public int getCount() {
        return letters.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View v = view;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.keyboard_items, null);
        Button keyButton =  v.findViewById(R.id.key_letter);
        ImageView imageView = (ImageView) v.findViewById(R.id.key_background);
        keyButton.setBackgroundResource(letters.get(position).getButtonBackgroundResourceId());
        imageView.setImageResource(letters.get(position).getImageResourceId());
        keyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScreen(screen,(Button)v);
            }
        });
        return v;

    }


    private void updateScreen(Fragment screen,Button button){
        //Log.d("Button Pressed", button.getBackground() );
    }
}
