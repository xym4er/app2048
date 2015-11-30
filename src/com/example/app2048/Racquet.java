package com.example.app2048;

import android.graphics.drawable.Drawable;

public class Racquet extends GameObject {
    private static final int DEFAULT_SPEED = 4;

    /**
     * ���������� ������������ �����
     */
    private int score;

    /**
     * ����������� ��������
     */
    private int direction;

    /**
     * ������� ����������� ��������
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * @see com.example.app2048.GameObject#GameObject(Drawable)
     */
    public Racquet(Drawable image) {
        super(image);
        direction = DIR_NONE; // ����������� �� ��������� - ���
        score = 0; // ����� ���� �� ����������
        speed = DEFAULT_SPEED; // ������ �������� �� ���������
    }

    /**
     * @see com.example.app2048.GameObject#updatePoint()
     */
    @Override
    protected void updatePoint() {
        point.x += direction * speed; // ������� ������� �� ��� Ox � ��������������� �������
    }

    public void incScore() {
        score++;
    }

    public int getScore() {
        return score;
    }
}
