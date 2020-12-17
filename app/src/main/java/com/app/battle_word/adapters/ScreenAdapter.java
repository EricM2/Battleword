package com.app.battle_word.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.app.battle_word.R;
import com.app.battle_word.objects.Letter;
import com.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScreenAdapter  extends ArrayAdapter<Letter> {

    List<Letter> letters = new ArrayList<>();
    Map<Integer,String> keys = Utils.getKeyString();

    public ScreenAdapter(@NonNull Context context, int resource, @NonNull List<Letter> objects) {
        super(context, resource, objects);
        letters = objects;
    }

    @Override
    public int getCount() {
        return letters.size();
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        View v = view;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.screen_items, null);
        ImageView imageView =  v.findViewById(R.id.screen_chaaracter);
        imageView.setImageResource(letters.get(position).getImageResourceId());
        return v;

    }




    private String getPressedCharacter(int position){
        return keys.get(position);
    }
}
