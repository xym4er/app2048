package com.example.app2048;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.SurfaceHolder;

public class GameManagerInThread extends Thread {
    private static final int FIELD_WIDTH = 800;
    private static final int FIELD_HEIGHT = 500;

    /** �������, �� ������� ����� �������� */
    private SurfaceHolder surfaceHolder;

    /** ��������� ������ (����������� ��� ���. �����, ����� ���� ������� ��������� �����, ����� �����������) */
    private boolean running;

    /** ����� ��������� */
    private Paint paint;

    /** ������������� �������� ���� */
    private Rect field;

    /** ������� ���������� */
    private Resources res;

    /** ����� */
    private Ball ball;

    /** �������, ����������� ������� */
    private Racquet us;

    /** �������, ����������� �����������*/
    private Racquet them;

    /**
     * �����������
     * @param surfaceHolder ������� ���������
     * @param context �������� ����������
     */
    public GameManagerInThread(SurfaceHolder surfaceHolder, Context context)
    {
        this.surfaceHolder = surfaceHolder;
        running = false;

        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);

        field = new Rect();

            ball = new Ball(context.getDrawable(R.drawable.ball));
            us = new Racquet(context.getDrawable(R.drawable.us));
            them = new Racquet(context.getDrawable(R.drawable.them));

    }

    public void initPositions(int screenHeight, int screenWidth)
    {
        int left = (screenWidth - FIELD_WIDTH) / 2;
        int top = (screenHeight - FIELD_HEIGHT) / 2;

        field.set(left, top, left + FIELD_WIDTH, top + FIELD_HEIGHT);
        // ����� �������� � ����� ����
        ball.setCenterX(field.centerX());
        ball.setCenterY(field.centerY());

        // ������� ������ - ����� �� ������
        us.setCenterX(field.centerX());
        us.setBottom(field.bottom);

        // ������� ���������� - ������ �� ������
        them.setCenterX(field.centerX());
        them.setTop(field.top);
    }

    /** ���������� �������� �� ������ */
    private void refreshCanvas(Canvas canvas)
    {
        canvas.drawRGB(255,255,255);
        // ������ ������� ����
        canvas.drawRect(field, paint);

        // ������ ������� �������
        ball.draw(canvas);
        us.draw(canvas);
        them.draw(canvas);
    }

    /** ���������� ��������� ������� �������� */
    private void updateObjects()
    {
        ball.update();
        us.update();
        them.update();

        // �������� ������������ ������ � ������������� �������
        if (ball.getLeft() <= field.left)
        {
            ball.setLeft(field.left + Math.abs(field.left - ball.getLeft()));
            ball.reflectVertical();
        }
        else if (ball.getRight() >= field.right)
        {
            ball.setRight(field.right - Math.abs(field.right - ball.getRight()));
            ball.reflectVertical();
        }

        // �������� ������������ ������ � ��������������� �������
        if (ball.getTop() <= field.top)
        {
            ball.setTop(field.top + Math.abs(field.top - ball.getTop()));
            ball.reflectHorizontal();
        }
        else if (ball.getBottom() >= field.bottom)
        {
            ball.setBottom(field.bottom - Math.abs(field.bottom - ball.getBottom()));
            ball.reflectHorizontal();
        }
    }

    /**
     * ������� ��������� ������
     * @param running
     */
    public void setRunning(boolean running)
    {
        this.running = running;
    }

    /**
     * ��������� ������� ������
     * @param keyCode ��� ������� ������
     * @return ���� �� ���������� �������
     */
    public boolean doKeyDown(int keyCode)
    {
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                us.setDirection(GameObject.DIR_LEFT);
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                us.setDirection(GameObject.DIR_RIGHT);
                return true;
            default:
                return false;
        }
    }
    /**
     * ��������� ���������� ������
     * @param keyCode ��� ������
     * @return ���� �� ���������� ��������
     */
    public boolean doKeyUp(int keyCode)
    {
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT ||
                keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
        {
            us.setDirection(GameObject.DIR_NONE);
            return true;
        }
        return false;
    }

    @Override
    /** ��������, ����������� � ������ */
    public void run()
    {
        while (running)
        {
            Canvas canvas = null;
            try
            {
                // ���������� Canvas-�
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder)
                {
//                    canvas.drawRGB(255,255,255);
                    updateObjects(); // ��������� �������
                    refreshCanvas(canvas); // ��������� �����
                    sleep(20);
                }
            }
            catch (Exception e) { }
            finally
            {
                if (canvas != null)
                {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
