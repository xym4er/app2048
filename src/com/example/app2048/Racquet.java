package com.example.app2048;

import android.graphics.drawable.Drawable;

public class Racquet extends GameObject {
    private static final int DEFAULT_SPEED = 4;

    /**
     * Количество заработанных очков
     */
    private int score;

    /**
     * Направление движения
     */
    private int direction;

    /**
     * Задание направления движения
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     * @see com.example.app2048.GameObject#GameObject(Drawable)
     */
    public Racquet(Drawable image) {
        super(image);
        direction = DIR_NONE; // Направление по умолчанию - нет
        score = 0; // Очков пока не заработали
        speed = DEFAULT_SPEED; // Задали скорость по умолчанию
    }

    /**
     * @see com.example.app2048.GameObject#updatePoint()
     */
    @Override
    protected void updatePoint() {
        point.x += direction * speed; // двигаем ракетку по оси Ox в соответствующую сторону
    }

    public void incScore() {
        score++;
    }

    public int getScore() {
        return score;
    }
}
