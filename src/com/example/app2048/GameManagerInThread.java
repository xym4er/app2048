package com.example.app2048;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;

public class GameManagerInThread extends Thread {
    private static final int FIELD_WIDTH = 800;
    private static final int FIELD_HEIGHT = 500;
    private static final long LOSE_PAUSE = 2000;
    private Paint scorePaint;
    private boolean initialized;

    /**
     * ������ ��� ����������� ������
     */
    private DrawHelper drawScreen;

    /**
     * ������ ��� ��������� ���������� ����
     */
    private DrawHelper drawGameover;

    /**
     * �������, �� ������� ����� ��������
     */
    private SurfaceHolder surfaceHolder;

    /**
     * ��������� ������ (����������� ��� ���. �����, ����� ���� ������� ��������� �����, ����� �����������)
     */
    private boolean running;

    /**
     * ����� ���������
     */
    private Paint paint;

    /**
     * ������������� �������� ����
     */
    private Rect field;

    /**
     * ������� ����������
     */
    private Resources res;

    /**
     * �����
     */
    private Ball ball;

    /**
     * �������, ����������� �������
     */
    private Racquet us;

    /**
     * �������, ����������� �����������
     */
    private Racquet them;

    /**
     * ������������ ����� �����, �� �������� ���� ����
     */
    private static int maxScore = 5;

    /**
     * ����� �� ���������� �� �����
     */
    private boolean paused;
    private Paint pausePaint;
    private DrawHelper drawPause;

    public static void setMaxScore(int value) {
        maxScore = value;
    }

    /**
     * �����������
     *
     * @param surfaceHolder ������� ���������
     * @param context       �������� ����������
     */
    public GameManagerInThread(SurfaceHolder surfaceHolder, Context context) {
        this.surfaceHolder = surfaceHolder;
        running = false;

        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);

        // ����� ��� ������ �����
        scorePaint = new Paint();
        scorePaint.setTextSize(40);
        scorePaint.setStrokeWidth(3);
        scorePaint.setStyle(Paint.Style.FILL);
        scorePaint.setTextAlign(Paint.Align.CENTER);
        initialized = false;
        field = new Rect();

        // ����� ��� ��������� �����
        pausePaint = new Paint();
        pausePaint.setStyle(Paint.Style.FILL);
        pausePaint.setColor(Color.argb(100, 50, 50, 80));

        drawScreen = new DrawHelper() {
            public void draw(Canvas canvas) {
                refreshCanvas(canvas);
            }
        };

        drawPause = new DrawHelper() {
            public void draw(Canvas canvas) {
                canvas.drawRect(field, pausePaint);
            }
        };

        // ������� ��� ��������� ����������� ����
        drawGameover = new DrawHelper() {
            public void draw(Canvas canvas) {
                // ������ ��������� ��������� ����
                refreshCanvas(canvas);

                // �������, ��� ������� � ������� ��������������� ���������
                String message = "";
                if (us.getScore() > them.getScore()) {
                    scorePaint.setColor(Color.GREEN);
                    message = "You won";
                } else {
                    scorePaint.setColor(Color.RED);
                    message = "You lost";
                }
                scorePaint.setTextSize(60);
                canvas.drawText(message, field.centerX(), field.centerY(), scorePaint);
            }
        };

        ball = new Ball(context.getDrawable(R.drawable.ball));
        us = new Racquet(context.getDrawable(R.drawable.us));
        them = new Racquet(context.getDrawable(R.drawable.them));

    }

    public void initPositions(int screenHeight, int screenWidth) {
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
        initialized = true;
    }

    /**
     * ���������� �������� �� ������
     */
    private void refreshCanvas(Canvas canvas) {
        canvas.drawRGB(255, 255, 255);
        // ������ ������� ����
        canvas.drawRect(field, paint);

        // ������ ������� �������
        ball.draw(canvas);
        us.draw(canvas);
        them.draw(canvas);

        // ����� �����
        scorePaint.setColor(Color.RED);
        canvas.drawText(String.valueOf(them.getScore()), field.centerX(), field.top - 10, scorePaint);
        scorePaint.setColor(Color.GREEN);
        canvas.drawText(String.valueOf(us.getScore()), field.centerX(), field.bottom + 45, scorePaint);
    }

    private void placeInBounds(Racquet r) {
        if (r.getLeft() < field.left)
            r.setLeft(field.left);
        else if (r.getRight() > field.right)
            r.setRight(field.right);
    }

    /**
     * ���������� ��������� ������� ��������
     */
    private void updateObjects() {
        ball.update();
        us.update();
        moveAI();
        placeInBounds(us);
        placeInBounds(them);

        // �������� ������������ ������ � ������������� �������
        if (ball.getLeft() <= field.left) {
            ball.setLeft(field.left + Math.abs(field.left - ball.getLeft()));
            ball.reflectVertical();
        } else if (ball.getRight() >= field.right) {
            ball.setRight(field.right - Math.abs(field.right - ball.getRight()));
            ball.reflectVertical();
        }

        // �������� ������������ ������ � ��������������� �������
        if (ball.getTop() <= field.top) {
            ball.setTop(field.top + Math.abs(field.top - ball.getTop()));
            ball.reflectHorizontal();
        } else if (ball.getBottom() >= field.bottom) {
            ball.setBottom(field.bottom - Math.abs(field.bottom - ball.getBottom()));
            ball.reflectHorizontal();
        }

        if (GameObject.intersects(ball, us)) {
            ball.setBottom(us.getBottom() - Math.abs(us.getBottom() - ball.getBottom()));
            ball.reflectHorizontal();
        } else if (GameObject.intersects(ball, them)) {
            ball.setTop(them.getTop() + Math.abs(them.getTop() - ball.getTop()));
            ball.reflectHorizontal();
        }

        if (ball.getBottom() <= them.getBottom() + 4) {
            us.incScore();
            reset();
        }

        if (ball.getTop() >= us.getTop() - 4) {
            them.incScore();
            reset();
        }

        // �������� ��������� ����
        if (us.getScore() == maxScore || them.getScore() == maxScore) {
            this.running = false;
        }
    }

    private interface DrawHelper {
        void draw(Canvas canvas);
    }

    private void draw(DrawHelper helper) {
        Canvas canvas = null;
        try {
            // ���������� Canvas-�
            canvas = surfaceHolder.lockCanvas();
            synchronized (surfaceHolder) {
                if (initialized) {
                    helper.draw(canvas);
                }
            }
        } catch (Exception e) {
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }


    private void reset() {
        // ������ ����� � �����
        ball.setCenterX(field.centerX());
        ball.setCenterY(field.centerY());
        // ������ ��� ����� ��������� ����
        ball.resetAngle();

        // ������ ������� � �����
        us.setCenterX(field.centerX());
        them.setCenterX(field.centerX());

        // ������ �����
        try {
            sleep(LOSE_PAUSE);
        } catch (InterruptedException iex) {
        }
    }

    /**
     * ������� ��������� ������
     *
     * @param running
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * ��������� ������� ������
     *
     * @param keyCode ��� ������� ������
     * @return ���� �� ���������� �������
     */
    public boolean doKeyDown(int keyCode) {
        switch (keyCode) {
            case -1:
                us.setDirection(GameObject.DIR_LEFT);
                return true;
            case 1:
                us.setDirection(GameObject.DIR_RIGHT);
                return true;
            case 5:
                paused = !paused;
                draw(drawPause);
                return true;
            default:
                return false;
        }
    }

    /**
     * ��������� ���������� ������
     *
     * @param keyCode ��� ������
     * @return ���� �� ���������� ��������
     */
    public boolean doKeyUp(int keyCode) {
        if (keyCode == 1 ||
                keyCode == -1) {
            us.setDirection(GameObject.DIR_NONE);
            return true;
        }
        return false;
    }

    private void moveAI() {
        if (them.getLeft() > ball.getRight())
            them.setDirection(GameObject.DIR_LEFT);
        else if (them.getRight() < ball.getLeft())
            them.setDirection(GameObject.DIR_RIGHT);
        them.update();
    }

    @Override
    /** ��������, ����������� � ������ */
    public void run() {
        while (running) {
            if (paused) continue;

            if (initialized) {
                updateObjects(); // ��������� �������
                draw(drawScreen);
            }
        }
        draw(drawGameover);
    }
}
