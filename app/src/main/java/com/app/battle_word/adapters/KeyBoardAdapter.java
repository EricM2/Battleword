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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.app.battle_word.R;
import com.app.battle_word.ScreenFragment;
import com.app.battle_word.objects.Letter;
import com.app.battle_word.publishers.WordFoundPublisher;
import com.app.battle_word.subscribers.WordFoundSubscriber;
import com.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KeyBoardAdapter extends ArrayAdapter  implements WordFoundPublisher {

    List<WordFoundSubscriber> subscribers = new ArrayList<>();
    List<Letter> letters = new ArrayList<>();
    ScreenFragment screen = null;
    Map<Integer,String> keys = Utils.getKeyString();
    public KeyBoardAdapter(@NonNull Context context, int resource, List<Letter> keyLetters, ScreenFragment screenFragment) {
        super(context, resource);
        letters = keyLetters;
        screen = screenFragment;
    }



    @Override
    public int getCount() {
        return letters.size();
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        View v = view;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.keyboard_items, null);
        Button keyButton =  v.findViewById(R.id.key_letter);
        ImageView imageView =  v.findViewById(R.id.key_background);
        keyButton.setBackgroundResource(letters.get(position).getButtonBackgroundResourceId());

        imageView.setImageResource(letters.get(position).getImageResourceId());
        keyButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                if(!(position==20 || position==28 || position == 29)) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            v.setBackgroundResource(R.drawable.dark_transparent);
                            v.invalidate();
                            break;
                        }
                        case MotionEvent.ACTION_UP: {
                            v.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    v.setBackgroundResource(R.drawable.transparent_key);
                                    v.invalidate();
                                }
                            }, 100L);
                            break;
                        }
                    }
            }
                return false;
            }
        });

        keyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScreen(screen,(Button)v,position);
            }

        });
        return v;

    }


    private void updateScreen(ScreenFragment screen, Button button, int position){
        TextView txt =  screen.getView().findViewById(R.id.screen_text);
        String screenText= txt.getText().toString();
        String l = getPressedCharacter(position);
        if(!l.isEmpty() && !Utils.isSreenTextComplete(screenText)) {
            Log.d("KEY_PRESSED", l);
            Toast.makeText(getContext(), l, Toast.LENGTH_SHORT).show();
            String newString = Utils.putCharInScreenText(l,"constitution",screenText);
            Log.d("NEW_STRING", newString);
            txt.setText(newString);



        }
    }

    private String getPressedCharacter(int position){
        return keys.get(position);
    }

    @Override
    public void publishWordFound() {
        for( int i=0 ; i<subscribers.size(); i++){
            subscribers.get(i).onWordFound();
        }
    }

    @Override
    public void subscribe(WordFoundSubscriber subscriber) {
        subscribers.add(subscriber);
    }
}
