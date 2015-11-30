package com.example.app2048;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SurfaceGameView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder surfaceHolder;
    private GameManagerInThread thread;

    public SurfaceGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        thread = new GameManagerInThread(surfaceHolder, context);
        setFocusable(true);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        return thread.doKeyDown(keyCode);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        return thread.doKeyUp(keyCode);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int keyCode;
        if((event.getY()<this.getY()/3)&(event.getX()>this.getWidth()/4)&(event.getX()<(this.getWidth()/4)*3)){
            keyCode=5;
        }else{
            if (event.getX()<this.getWidth()/2){
                keyCode=-1;
            }else{
                if (event.getX()>=this.getWidth()/2){
                    keyCode=1;
                }else{keyCode=0;}
            }
        }



        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            return thread.doKeyDown(keyCode);
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            return thread.doKeyDown(keyCode);
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            return thread.doKeyUp(keyCode);
        }
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        thread.initPositions(height, width);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        while (retry){
            try {
                thread.join();
                retry = false;

            }catch (InterruptedException e){}
        }
    }
}
