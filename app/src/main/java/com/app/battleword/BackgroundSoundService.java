package com.app.battleword;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.app.battleword.R;
import com.app.utils.Strings;
import com.app.utils.Utils;

import java.util.concurrent.Callable;

public class BackgroundSoundService extends Service {
    private static final String TAG = "BackgroundSoundService";
    private IntentFilter soundActionIntentFilter;
    private SoundActionsRecever soundActionsRecever;
    private Notification notification;
    MediaPlayer player;
    private boolean isSoundPaused;
    public IBinder onBind(Intent arg0) {

        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        player =null;
        isSoundPaused = false;


    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        soundActionIntentFilter = new IntentFilter(Strings.SOUND_ACTION_INTENT_FILTER);
        soundActionsRecever = new SoundActionsRecever(this);
        this.registerReceiver(soundActionsRecever,soundActionIntentFilter);

        (new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] objects) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    notification = createNotification();
                else
                    notification = new Notification();
                startForeground(1,notification);
                return  null;
            }
        }).execute();

        Callable c = new Callable() {
            @Override
            public Object call() throws Exception {
                play();
                return null;
            }
        };
        Utils.doAfter(600,c);

        return START_STICKY;
    }

    public IBinder onUnBind(Intent arg0) {
        // TO DO Auto-generated method
        return null;
    }

    @Override
    public void onDestroy() {
        stop();
        this.unregisterReceiver(soundActionsRecever);
        stopForeground(true);
    }

    public  void play(){
        if(player != null){
            stop();
        }
        player = MediaPlayer.create(this,R.raw.battleword_generic);
        player.setLooping(true);
        player.setVolume(30,30);
        player.start();
        isSoundPaused = false;
    }



    public void stop(){
        if(player != null){
            try {
                if(player.isPlaying()) {
                    player.stop();
                    isSoundPaused = false;
                }
                player.release();
                player = null;
            }
            catch (Exception e){}
        }
    }

    public void pause(){
        if(player != null){
            try {
                if(player.isPlaying()) {
                    player.pause();
                    isSoundPaused = true;
                    //stopForeground(true);
                }
            }
            catch (Exception e){}
        }
    }

    public void resume(){
        if(player != null){
            try {
                if(isSoundPaused && !player.isPlaying()) {
                    player.start();
                    isSoundPaused = false;

                }
            }
            catch (Exception e){}
        }
    }

    private static class SoundActionsRecever extends BroadcastReceiver{
        private BackgroundSoundService contextService;
        public SoundActionsRecever(BackgroundSoundService c){
            this.contextService =c;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra(Strings.SOUND_ACTION);
            if(action.equalsIgnoreCase(Strings.STOP))
                contextService.stop();
            if(action.equalsIgnoreCase(Strings.PLAY))
                contextService.play();
            if(action.equalsIgnoreCase(Strings.PAUSE))
                contextService.pause();
            if(action.equalsIgnoreCase(Strings.RESUME))
                contextService.resume();


        }
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notification createNotification(){
        String NOTIFICATION_CHANNEL_ID = Strings.CHANNEL_ID;
        String channelName = "BATTLEWORDSERVICE";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notif = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.diamond_life)
                .setContentTitle("BATTLEWORD")
                .setPriority(NotificationManager.IMPORTANCE_LOW)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        return notif;
    }


}
