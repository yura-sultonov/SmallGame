package com.sorokin.dev.smallgame;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class ResultsRepository {

    private static ResultsRepository instance = new ResultsRepository();
    private static SharedPreferences sharedPreferences;

    private String bestResultKey = "best_result";

    private ResultsRepository(){}

    public static ResultsRepository getInstance(Context context){
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
        }
        return instance;
    }

    public int getBestResult(){
        return sharedPreferences.getInt(bestResultKey, 0);
    }

    public void setBestResultKey(int bestResult){
        if (bestResult > this.getBestResult()) {
            sharedPreferences.edit().putInt(bestResultKey, bestResult).apply();
        }
    }
}
