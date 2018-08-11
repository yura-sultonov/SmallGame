package com.sorokin.dev.smallgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView bestResultTextView;
    private Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        bestResultTextView = findViewById(R.id.max_solve_value_text_view);
        playButton = findViewById(R.id.play_button);
        bestResultTextView.setText("" + ResultsRepository.getInstance(this).getBestResult());
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewGame();
            }
        });
    }

    private void startNewGame() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
}