package com.sorokin.dev.smallgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends Activity {

    private TextView currentResultTextView;
    private TextView bestResultTextView;
    private Button backToHomeButton;
    private Button restartButton;
    private int result = 0;
    private int bestResult = 0;

    private class MyLooper extends Thread {
        public Handler mHandler;

        public boolean playing = true;

        public void run() {
            Music music = new Music();
            music.playTrack(music.musicToLoose());

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initialValues();
        initView();
        MyLooper mLooper = new MyLooper();
        mLooper.start();

    }

    private void initialValues() {
        bestResult = ResultsRepository.getInstance(this).getBestResult();
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        result = extras.getInt("result", 0);
        ResultsRepository.getInstance(this).setBestResultKey(result);
    }

    private void initView() {
        currentResultTextView = findViewById(R.id.current_result_value_text_view);
        bestResultTextView = findViewById(R.id.max_solve_value_text_view);
        backToHomeButton = findViewById(R.id.home_button);
        restartButton = findViewById(R.id.restart_button);

        currentResultTextView.setText(String.format("%d", result));
        bestResultTextView.setText(String.format("%d", bestResult));
        backToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
                ResultActivity.this.finish();
            }
        });
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, GameActivity.class);
                startActivity(intent);
                ResultActivity.this.finish();
            }
        });
    }

}
