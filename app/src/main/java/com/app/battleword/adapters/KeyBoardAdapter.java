package com.app.battleword.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.app.battleword.KeyBoardFragment;
import com.app.battleword.PlayerControlActivity;
import com.app.battleword.R;
import com.app.battleword.objects.Letter;
import com.app.battleword.subscribers.WordFoundSubscriber;
import com.app.battleword.viewmodels.WordViewModel;
import com.app.utils.Touch;
import com.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KeyBoardAdapter extends ArrayAdapter   {

    List<WordFoundSubscriber> subscribers = new ArrayList<>();
    private Context context;
    List<Letter> letters = new ArrayList<>();
    private int numTouches = 0;
    private int currentStage;
    WordViewModel screen = null;
    Map<Integer,String> keys = Utils.getKeyString();
    public KeyBoardAdapter(@NonNull Context context, int resource, List<Letter> keyLetters, WordViewModel wordViewModel, int stage) {
        super(context, resource);
        letters = keyLetters;
        screen = wordViewModel;
        this.context = context;
        currentStage = stage;
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
                String l = getPressedCharacter(position);
                if(context!=null)
                    ((PlayerControlActivity)context).keyClicked(l);
                //updateScreen(screen,(Button)v,position,context);
            }

        });
        return v;

    }


    private void updateScreen(WordViewModel screen, Button button, int position, Context c){
       String screenText =  screen.getScreenText().getValue();
        String l = getPressedCharacter(position);
        if(!l.isEmpty() && !Utils.isSreenTextComplete(screenText)) {
            numTouches = screen.getNumTouch().getValue();
            numTouches ++;
            if(screen.getAllowWordUpdate().getValue()){
                if(Touch.getNumTouches(currentStage)!=-1 && numTouches <=Touch.getNumTouches(currentStage)) {
                    Log.d("KEY_PRESSED", l);
                    //Toast.makeText(getContext(), l, Toast.LENGTH_SHORT).show();
                    String newString = Utils.putCharInScreenText(l, screen.getRequiredText(), screenText, c);
                    Log.d("NEW_STRING", newString);
                    screen.updateScreenText(newString);
                    screen.updateNumTouch(numTouches);
                }
                else{
                    if(Touch.getNumTouches(currentStage)==-1){
                        Log.d("KEY_PRESSED", l);
                        //Toast.makeText(getContext(), l, Toast.LENGTH_SHORT).show();
                        String newString = Utils.putCharInScreenText(l, screen.getRequiredText(), screenText, c);
                        Log.d("NEW_STRING", newString);
                        screen.updateScreenText(newString);
                    }
                }
            }


        }

    }

    private String getPressedCharacter(int position){
        return keys.get(position);
    }

   /* @Override
    public void publishWordFound() {
        for( int i=0 ; i<subscribers.size(); i++){
            subscribers.get(i).onWordFound();
        }
    }

    @Override
    public void subscribe(WordFoundSubscriber subscriber) {
        subscribers.add(subscriber);
    }*/
}
