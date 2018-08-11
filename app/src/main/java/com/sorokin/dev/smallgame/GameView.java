package com.sorokin.dev.smallgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements Runnable {

    int fWidth = 5;
    int fHeight = 0;

    int lineWidth = 0;
    int cubePadding = 8;

    volatile boolean playing;
    Thread gameThread = null;

    Paint cubePaint;
    Paint paint;
    Canvas canvas;
    SurfaceHolder surfaceHolder;

    List<Cube> cubes;

    public GameView(Context context) {
        super(context);

        cubes = new ArrayList<>();

        surfaceHolder = getHolder();
        paint = new Paint();

        cubePaint = new Paint();
        cubePaint.setStyle(Paint.Style.FILL_AND_STROKE);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = getMeasuredHeight();
        int width = getMeasuredWidth();

        lineWidth = this.getMeasuredWidth() / fWidth;
        height = lineWidth * fHeight;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        super.onSizeChanged(xNew, yNew, xOld, yOld);

        lineWidth = this.getMeasuredWidth() / fWidth;
    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }

    private void update() {
        int flyCount = 0;
        for(int i = 0; i < cubes.size(); i++){
            Cube cube = cubes.get(i);
            if(cube.isFly){
                cube.y++;

                if(cube.y == fHeight){
                    cube.isFly = false;
                }

            }

            if(cube.isFly){
                ++flyCount;
            }
        }

        boolean generateNewCube;
        if(flyCount == 1){
            if(Math.random() > 0.7){
                generateNewCube = true;
            }else{
                generateNewCube = false;
            }
        }else if(flyCount == 0){
            generateNewCube = true;
        }else{
            generateNewCube = false;
        }

        if(generateNewCube){
            cubes.add(new Cube(fWidth));

        }
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);

            cubePaint.setColor(Color.RED);

            for (Cube cube: cubes) {
                canvas.drawRoundRect(cubeToRect(cube), 8f, 8f, paint);
            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            gameThread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public RectF cubeToRect(Cube cube){
        RectF rect = new RectF();
        rect.left = (cube.x * lineWidth) + cubePadding;
        rect.right = ((cube.x + 1) * lineWidth) - cubePadding;
        rect.top = (cube.y * lineWidth) + cubePadding;
        rect.bottom = ((cube.y + 1) * lineWidth) - cubePadding;

        return rect;
    }
}