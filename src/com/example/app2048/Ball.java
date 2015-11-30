package com.example.app2048;

import android.graphics.drawable.Drawable;

import java.util.Random;

public class Ball extends GameObject {
    private static final int DEFAULT_SPEED = 3;
    private static final int PI = 180;

    /**
     * ����, ������� ���������� ����������� ������ ������ � ���� Ox
     */
    private int angle;

    /**
     * @see com.example.app2048.GameObject#GameObject(Drawable)
     */
    public Ball(Drawable image) {
        super(image);

        speed = DEFAULT_SPEED; // ������ �������� �� ���������
        angle = getRandomAngle(); // ������ ��������� ��������� ����
    }

    /**
     * @see com.example.app2048.GameObject#updatePoint()
     */
    @Override
    protected void updatePoint() {
        double rAngle = Math.toRadians(angle);

        point.x += (int) Math.round(speed * Math.cos(rAngle));
        point.y -= (int) Math.round(speed * Math.sin(rAngle));
    }

    /**
     * ��������� ���������� ���� � ���������� [95, 110]U[275,290]
     */
    private int getRandomAngle() {
        Random rnd = new Random(System.currentTimeMillis());

        return rnd.nextInt(1) * PI + PI / 2 + rnd.nextInt(15) + 5;
    }

    /**
     * ��������� ������ �� ���������
     */
    public void reflectVertical() {
        if (angle > 0 && angle < PI)
            angle = PI - angle;
        else
            angle = 3 * PI - angle;
    }

    /**
     * ��������� ������ �� �����������
     */
    public void reflectHorizontal() {
        angle = 2 * PI - angle;
    }

    public void resetAngle() {
        angle = getRandomAngle();
    }
}
