package com.sorokin.dev.smallgame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class GameView extends SurfaceView implements SurfaceHolder.Callback {
    int[] numbers = new int[]{
            2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096
    };

    int[] colors = new int[]{
        Color.GREEN, 0xFF01FFFF, 0xFFFFFF01, 0xFFFF7F00, 0xFFE34166, 0xFFFFD800, Color.GREEN, 0xFF01FFFF, 0xFFFFFF01, 0xFFFF7F00, 0xFFE34166, 0xFFFFD800
    };

    int fWidth = 4;
    int fHeight = 5;

    int lineWidth = 0;
    int cubePadding = 16;
    int cubeStrokeWidth = 20;
    int textSize = 80;

    int startingMax = 3;

    int frameNum = 0;

    boolean isFail = false;

    Paint cubePaint;
    TextPaint textPaint;
    Paint paint;

    int[][] gameField;

    int updateGap = 25;

    private DrawThread drawThread;

    int scores = 0;

    @SuppressLint("ClickableViewAccessibility")
    public void init(){
        gameField = new int[fWidth][fHeight];
        for(int i = 0; i < fWidth; ++i){
            for(int j = 0; j < fHeight; ++j){
                gameField[i][j] = 0;
            }
        }
        paint = new Paint();
        cubePaint = new Paint();
        cubePaint.setStyle(Paint.Style.STROKE);
        cubePaint.setStrokeWidth(cubeStrokeWidth);

        textPaint = new TextPaint();
        textPaint.setTextSize(textSize);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);

        setBackgroundColor(Color.TRANSPARENT);
        setZOrderOnTop(true); //necessary
        getHolder().setFormat(PixelFormat.TRANSPARENT);
        getHolder().addCallback(this);
        this.setOnTouchListener(new OnSwipeTouchListener(this.getContext()) {

            @Override
            public void onSwipeLeft(View view) {
                super.onSwipeLeft(view);
                Log.e("SWIPE", "LEFT");
                for(int x = 1; x < fWidth; ++x){
                    for(int y = fHeight - 1; y >= 0; --y){
                        if(gameField[x][y] == 0){
                            continue;
                        }
                        if(gameField[x-1][y] == 0){
                            gameField[x-1][y] = gameField[x][y];
                            gameField[x][y] = 0;
                        }else if(gameField[x-1][y] == gameField[x][y]){
                            gameField[x-1][y] *= 2;
                            scores += gameField[x][y];
                            gameField[x][y] = 0;
                        }
                    }
                }
            }

            @Override
            public void onSwipeRight(View view) {
                super.onSwipeRight(view);
                for(int x = fWidth - 2; x >= 0; --x){
                    for(int y = fHeight - 1; y >= 0; --y){
                        if(gameField[x][y] == 0){
                            continue;
                        }
                        if(gameField[x+1][y] == 0){
                            gameField[x+1][y] = gameField[x][y];
                            gameField[x][y] = 0;
                        }else if(gameField[x+1][y] == gameField[x][y]){
                            gameField[x+1][y] *= 2;
                            scores += gameField[x][y];
                            gameField[x][y] = 0;
                        }
                    }
                }
            }
        });
    }

    public void update(){

        int flyCount = 0;

        for(int x = 0; x < fWidth; ++x){
            int y = fHeight - 2;
            while(y >= 0){
                if(gameField[x][y+1] == 0){
                    gameField[x][y+1] = gameField[x][y];
                    gameField[x][y] = 0;
                }
                --y;
            }
        }

        for(int x = 0; x < fWidth; ++x) {
            int y = fHeight - 1;
            while (y > 0) {
                if(gameField[x][y-1] == gameField[x][y]){
                    scores += gameField[x][y];
                    gameField[x][y] *= 2;
                    gameField[x][y-1] = 0;
                }
                y--;
            }
        }

        for(int x = 0; x < fWidth; ++x){
            int flyingStack = 0;
            for(int y = 0; y < fHeight; ++y) {
                if (gameField[x][y] != 0) {
                    flyingStack++;
                }
                if(y == fHeight - 1) {
                    flyingStack = 0;
                }else if(gameField[x][y] == 0){
                    flyCount += flyingStack;
                    flyingStack = 0;
                }
            }
            flyCount += flyingStack;
        }


        boolean generateNewCube;
        if(flyCount == 1){
            generateNewCube = Math.random() > 0.85;
        }else generateNewCube = flyCount == 0;

        if(generateNewCube){
            int newCubeX;
            int newCubeValue = (int)(Math.random() * startingMax);
            newCubeX = (int)(Math.random() * fWidth);
            gameField[newCubeX][0] = numbers[newCubeValue];
        }

        boolean isFail = true;
        for(int x = 0; x < fWidth; ++x){
            if(gameField[x][0] == 0){
                isFail = false;
            }
        }
        if(isFail){
            Log.e("FAIL", "IS FAIL");
            isFail = false;
        }
    }

    public void drawGame(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        cubePaint.setColor(Color.RED);
        for (int x = 0; x < fWidth; ++x) {
            for (int y = 0; y < fHeight; ++y) {
                if(gameField[x][y] != 0){
                    for(int j = 0; j < numbers.length; ++j){
                        if(numbers[j] == gameField[x][y]){
                            cubePaint.setColor(colors[j]);
                            //cubePaint.setShadowLayer(20, 0, 0, colors[j]);
                            textPaint.setColor(colors[j]);
                            //textPaint.setShadowLayer(20, 0, 0, colors[j]);

                            break;
                        }
                    }
                    Rect textBounds = new Rect();
                    String text = String.valueOf(gameField[x][y]);
                    textPaint.getTextBounds(text, 0, text.length(), textBounds);


                    RectF cubeRect = cubeToRect(x, y);
                    canvas.drawRoundRect(cubeRect, 8f, 8f, cubePaint);

                    RectF textRect = cubeRect;
                    textRect.left = cubeRect.left + cubeRect.width() / 2 - textBounds.width() / 2;
                    textRect.right = cubeRect.left + cubeRect.width() / 2 + textBounds.width() / 2;

                    textRect.top = cubeRect.top + cubeRect.height() / 2 - ((textPaint.descent() + textPaint.ascent()) / 2);
                    textRect.bottom = cubeRect.top + cubeRect.height() / 2 + textBounds.height() / 2;
                    canvas.drawText(text, textRect.left, textRect.top, textPaint);

                }
            }
        }

    }

    public void control(){

    }

    public RectF cubeToRect(int x, int y){
        RectF rect = new RectF();

        int offsetX = 0;
        int offsetY = 0;
        if(y != fHeight - 1){
            offsetY = (int)((1.0 * frameNum / updateGap) * lineWidth);
        }
        if(y == fHeight - 1){
            offsetY = 0;
        }else{
            boolean shouldOffset = false;
            for(int i = y + 1; i < fHeight; ++i){
                if(gameField[x][i] == 0){
                    shouldOffset = true;
                }
            }
            if(!shouldOffset){
                offsetY = 0;
            }
        }

        rect.left = (x * lineWidth) + cubePadding + offsetX;
        rect.right = ((x + 1) * lineWidth) - cubePadding + offsetX;

        rect.top = (y * lineWidth) + cubePadding + offsetY;
        rect.bottom = ((y + 1) * lineWidth) - cubePadding + offsetY;

        return rect;
    }

    class DrawThread extends Thread {
        private boolean running = false;
        private SurfaceHolder surfaceHolder;

        public DrawThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        @Override
        public void run() {
            Canvas canvas;
            while (running) {
                canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas(null);
                    if (canvas == null)
                        continue;

                    frameNum++;
                    frameNum = frameNum % updateGap;
                    if(frameNum == 0) {
                        update();
                    }

                    drawGame(canvas);

                    control();

                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawThread = new DrawThread(getHolder());
        drawThread.setRunning(true);
        drawThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        drawThread.setRunning(false);
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
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


    public class OnSwipeTouchListener implements OnTouchListener {

        public
        OnSwipeTouchListener(Context ctx)
        {
            m_gestureDetector = new GestureDetector(ctx, new GestureListener());
        }

        public boolean
        onTouch(final View view, final MotionEvent motionEvent)
        {
            m_view = view;
            return m_gestureDetector.onTouchEvent(motionEvent);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean
            onDown(MotionEvent e)
            {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight(m_view);
                            } else {
                                onSwipeLeft(m_view);
                            }
                        }
                        result = true;
                    }
                    else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom(m_view);
                        } else {
                            onSwipeTop(m_view);
                        }
                    }
                    result = true;

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }

        /**
         * @brief Override to process swipe-right event
         */
        public void onSwipeRight(View view)
        {
        }

        /**
         * @brief Override to process swipe-left event
         */
        public void onSwipeLeft(View view)
        {
        }

        /**
         * @brief Override to process swipe-top event
         */
        public void onSwipeTop(View view)
        {
        }

        /**
         * @brief Override to process swipe-bottom event
         */
        public void onSwipeBottom(View view)
        {
        }

        /////////////////////////////////////////////////////////////////////////////
        private final GestureDetector m_gestureDetector;
        private View m_view;
    }

}