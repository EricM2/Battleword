package com.app.battleword;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class StageProgressFragment extends Fragment {
   private int stage;
  private ProgressBar linearProgressStage1;
  private ProgressBar linearProgressStage2;
  private ProgressBar linearProgressStage3;
  private ProgressBar linearProgressStage4;
  private ProgressBar linearProgressStage5;
    private ProgressBar circularProgressStage1;
    private ProgressBar circularProgressStage2;
    private ProgressBar circularProgressStage3;
    private ProgressBar circularProgressStage4;
    private ProgressBar circularProgressStage5;
    private ImageView lockStage1;
    private ImageView lockStage2;
    private ImageView lockStage3;
    private ImageView lockStage4;
    private ImageView lockStage5;
    private ProgressBar[] linearProgressArray;
    private ProgressBar[] circularProgressArray;
    private ImageView[] lockArray;
    private int[] locksBackgrounds;
    private int[] unlocksBackgrounds;
    private TextView stageTitleTextView;
    private Animation zoomin;
    private Animation zoomout;
    private AnimationSet animationSet;
    private int[] lineaProgressValues;
    private int[] circularProgressValues;
    private boolean[] locksStates;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v = inflater.inflate(R.layout.fragment_stage_progress, container, false);
       getViews(v);
       if(savedInstanceState==null) {
           initViews(v);
           stage = getActivity().getIntent().getIntExtra("nextStage", 1);
           Log.d("NEXTSTAGE", String.valueOf(stage));
           gotoNextStage(stage);

           zoomin = AnimationUtils.loadAnimation(getContext(), R.anim.zoomin);
           zoomout = AnimationUtils.loadAnimation(getContext(), R.anim.zoomout);
           animationSet = new AnimationSet(true);
           animationSet.addAnimation(zoomin);
           animationSet.addAnimation(zoomout);
           stageTitleTextView.startAnimation(animationSet);
       }
       else {
           lineaProgressValues = savedInstanceState.getIntArray("linear_progress_state");
           circularProgressValues = savedInstanceState.getIntArray("circular_progress_state");
           locksStates = savedInstanceState.getBooleanArray("lock_states");
           stage = savedInstanceState.getInt("nextStage");
           updateCircularProgress();
           updateLinearProgress();
           updateLock();

       }
        stageTitleTextView.setText(getString(R.string.stage)+" " + String.valueOf(stage));
        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        genLinearProgressValues();
        genCircularProgressValues();
        genLockStateValues(stage);
        outState.putInt("nextStage",stage);
        outState.putIntArray("linear_progress_state",lineaProgressValues);
        outState.putIntArray("circular_progress_state",circularProgressValues);
        outState.putBooleanArray("lock_states",locksStates);
        outState.putBoolean("test",true);
    }

    @Override
    public void onStart() {
        super.onStart();



    }

    private void getViews(View parent){
        stageTitleTextView = parent.findViewById(R.id.stage_title);
        linearProgressStage1 = parent.findViewById(R.id.linear_progress_stage1);
        linearProgressStage2 = parent.findViewById(R.id.linear_progress_stage2);
        linearProgressStage3 = parent.findViewById(R.id.linear_progress_stage3);
        linearProgressStage4 = parent.findViewById(R.id.linear_progress_stage4);
        linearProgressStage5= parent.findViewById(R.id.linear_progress_stage5);
        circularProgressStage1 = parent.findViewById(R.id.circular_progress_stage1);
        circularProgressStage2 = parent.findViewById(R.id.circular_progress_stage2);
        circularProgressStage3 = parent.findViewById(R.id.circular_progress_stage3);
        circularProgressStage4 = parent.findViewById(R.id.circular_progress_stage4);
        circularProgressStage5= parent.findViewById(R.id.circular_progress_stage5);
        lockStage1 = parent.findViewById(R.id.lock_stage_1);
        lockStage2 = parent.findViewById(R.id.lock_stage_2);
        lockStage3 = parent.findViewById(R.id.lock_stage_3);
        lockStage4 = parent.findViewById(R.id.lock_stage_4);
        lockStage5 = parent.findViewById(R.id.lock_stage_5);

        linearProgressArray = new ProgressBar[]{linearProgressStage1,linearProgressStage2,linearProgressStage3,linearProgressStage4,linearProgressStage5};
        circularProgressArray  = new ProgressBar[]{circularProgressStage1,circularProgressStage2,circularProgressStage3,circularProgressStage4,circularProgressStage5};
        lockArray = new ImageView[]{lockStage1,lockStage2,lockStage3,lockStage4,lockStage5};
        locksBackgrounds = new int[]{R.drawable.stage_1_lock,R.drawable.stage_2_lock,R.drawable.stage_3_lock,R.drawable.stage_4_lock,
                R.drawable.stage_5_lock};
        unlocksBackgrounds = new int[]{R.drawable.stage_1_unlock,R.drawable.stage_2_unlock,R.drawable.stage_3_unlock,R.drawable.stage_4_unlock,
                R.drawable.stage_5_unlock};

    }

    private void initViews(View parent){

        locksStates = new boolean[]{false,false,false,false,false};
        lineaProgressValues = new int[]{0,0,0,0,0,};
        circularProgressValues = new int[]{0,0,0,0,0,};

        linearProgressStage1.setProgress(0);
        linearProgressStage2.setProgress(0);
        linearProgressStage3.setProgress(0);
        linearProgressStage4.setProgress(0);
        linearProgressStage5.setProgress(0);
        circularProgressStage1.setProgress(0);
        circularProgressStage2.setProgress(0);
        circularProgressStage3.setProgress(0);
        circularProgressStage4.setProgress(0);
        circularProgressStage5.setProgress(0);


    }

    private void unlockStage(int stage){
        if(stage > 0 && stage <=5){
            int index = stage-1;
            linearProgressArray[index].setProgress(100);
            circularProgressArray[index].setProgress(100);
            lockArray[index].setImageResource(unlocksBackgrounds[index]);
        }
    }

    private void partialUnlockStage(int stage){
        if(stage > 0 && stage <=5){
            int index = stage-1;
            linearProgressArray[index].setProgress(100);
            //circularProgressArray[index].setProgress(100);
        }
    }



    private void lockStage(int stage){
        if(stage > 0 && stage <=5){
            int index = stage-1;
            linearProgressArray[index].setProgress(0);
            circularProgressArray[index].setProgress(0);
            lockArray[index].setImageResource(locksBackgrounds[index]);
        }
    }

    private void gotoNextStage(int nextStage){

        if(nextStage > 2){
                int prevStage = nextStage-1;
                for (int i = 1; i < prevStage; i++) {
                    unlockStage(i);
                }
                partialUnlockStage(prevStage);
                unlockAnimation(prevStage,1000);
        }
        else {
            if (nextStage==2){
                partialUnlockStage(1);
                unlockAnimation(1,1000);
            }
        }
        animateStage(nextStage);


    }

    private void animateStage(int stage){
        final int index = stage - 1;
        CountDownTimer c = new CountDownTimer(3000,10) {
            @Override
            public void onTick(long millisUntilFinished) {
                long t = 3000-millisUntilFinished;
                linearProgressArray[index].setProgress((int)(t*100/3000));
            }
            @Override
            public void onFinish() {
                animateProgress(circularProgressArray[index],500);
            }
        };
        c.start();
    }

    private void animateProgress(final ProgressBar p, final long millis){
        CountDownTimer c = new CountDownTimer(millis,10) {
            @Override
            public void onTick(long millisUntilFinished) {
                long t = millis-millisUntilFinished;
                p.setProgress((int)(t*100/millis));
            }

            @Override
            public void onFinish() {
                p.setProgress(100);
            }
        }.start();
    }

    private  void unlockAnimation(int stage,final long millis){
        final int index = stage -1;
        final  ProgressBar p = circularProgressArray[index];
        CountDownTimer c = new CountDownTimer(millis,10) {
            @Override
            public void onTick(long millisUntilFinished) {
                long t = millis-millisUntilFinished;
                p.setProgress((int)(t*100/millis));
            }

            @Override
            public void onFinish() {
                p.setProgress(100);
                lockArray[index].setImageResource(unlocksBackgrounds[index]);
            }
        }.start();

    }
    private  void genLinearProgressValues(){
        for(int i =0; i<5; i++){
            lineaProgressValues[i] = linearProgressArray[i].getProgress();
        }
    }

    private  void genCircularProgressValues(){
        for(int i =0; i<5; i++){
            circularProgressValues[i] = circularProgressArray[i].getProgress();
        }
    }

    private void genLockStateValues(int nextStage){
        for(int i=0; i<nextStage-1; i++){
            locksStates[i] = true;
        }
    }

    private void updateLinearProgress(){
        if(lineaProgressValues!=null) {
            for (int i = 0; i < 5; i++) {
                linearProgressArray[i].setProgress(lineaProgressValues[i]);
            }
        }
    }

    private void updateCircularProgress(){
        if(circularProgressValues!=null) {
            for (int i = 0; i < 5; i++) {
                circularProgressArray[i].setProgress(circularProgressValues[i]);
            }
        }
    }

    private void  updateLock(){

        if(locksStates!=null) {
            for (int i = 0; i < 5; i++) {
                if (locksStates[i])
                    lockArray[i].setImageResource(unlocksBackgrounds[i]);
            }
        }
    }








}
