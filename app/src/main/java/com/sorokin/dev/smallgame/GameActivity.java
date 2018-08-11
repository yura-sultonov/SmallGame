package com.sorokin.dev.smallgame;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class GameActivity extends Activity {

    GameView gameView;

    private MyLooper mLooper = new MyLooper();;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        mLooper.start();


    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

    }
    private class MyLooper extends Thread {
        public Handler mHandler;

        public void run() {

            while (true) {
                Music music = new Music();
                try {
                    music.playTrack(music.musicToPlay());
                    Thread.sleep(5320L);

                } catch (InterruptedException ex) {
                    //Treat the exception
                }
            }
        }
    }

}
