package com.sorokin.dev.smallgame;

import android.app.Activity;
import android.os.Bundle;

public class GameActivity extends Activity {

    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameView = findViewById(R.id.gameView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();

    }

}
