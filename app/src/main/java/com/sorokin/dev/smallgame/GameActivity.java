package com.sorokin.dev.smallgame;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends Activity {

    Timer tmr;
    class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvScores.setText("Scores: " + gameView.scores);
                    if(gameView.isFail){

                    }
                }
            });
        }
    }

    GameView gameView;
    TextView tvScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameView = findViewById(R.id.gameView);
        tvScores = findViewById(R.id.tvScores);

        tmr = new Timer();
        tmr.scheduleAtFixedRate(new MyTimerTask(), 0, 500);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //gameView.resume();

    }

}
