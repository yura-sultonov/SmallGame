package com.sorokin.dev.smallgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends Activity {

    private class MyLooper extends Thread {
        public Handler mHandler;

        public boolean playing = true;

        public void run() {

            while (playing) {
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

    Timer tmr;
    class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvScores.setText("Scores: " + gameView.scores);
                    if(gameView.isFail){

                        ResultsRepository repo = ResultsRepository.getInstance(GameActivity.this);
                        if(repo.getBestResult() < gameView.scores){
                            repo.setBestResultKey(gameView.scores);
                        }
                        Intent intent = new Intent(GameActivity.this, ResultActivity.class);

                        intent.putExtra("result", gameView.scores);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        tmr.cancel();
                        mLooper.playing = false;
                        GameActivity.this.finish();
                    }
                }
            });
        }
    }

    GameView gameView;
    TextView tvScores;
    private MyLooper mLooper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameView = findViewById(R.id.gameView);
        tvScores = findViewById(R.id.tvScores);
        mLooper = new MyLooper();

        tmr = new Timer();
        tmr.scheduleAtFixedRate(new MyTimerTask(), 0, 500);
        mLooper.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //gameView.resume();

    }

}
