package com.example.app2048;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public abstract class GameObject {
    // Константы для направлений
    public static final int DIR_LEFT = -1;
    public static final int DIR_RIGHT = 1;
    public static final int DIR_NONE = 0;

    /**
     * Координаты опорной точки
     */
    protected Point point;

    /**
     * Высота изображения
     */
    protected int height;

    /**
     * Ширина изображения
     */
    protected int width;

    /**
     * Изображение
     */
    private Drawable image;

    /**
     * Скорость
     */
    protected int speed;

    /**
     * Конструктор
     *
     * @param image Изображение, которое будет обозначать данный объект
     */
    public GameObject(Drawable image) {
        this.image = image;
        point = new Point(0, 0);
        width = image.getIntrinsicWidth();
        height = image.getIntrinsicHeight();
    }

    /**
     * Перемещение опорной точки
     */
    protected abstract void updatePoint();

    /**
     * Перемещение объекта
     */
    public void update() {
        updatePoint();
        image.setBounds(point.x, point.y, point.x + width, point.y + height);
    }

    /**
     * Отрисовка объекта
     */
    public void draw(Canvas canvas) {
        image.draw(canvas);
    }

    /**
     * Задает левую границу объекта
     */
    public void setLeft(int value) {
        point.x = value;
    }

    /**
     * Задает правую границу объекта
     */
    public void setRight(int value) {
        point.x = value - width;
    }

    /**
     * Задает верхнюю границу объекта
     */
    public void setTop(int value) {
        point.y = value;
    }

    /**
     * Задает нижнюю границу объекта
     */
    public void setBottom(int value) {
        point.y = value - height;
    }

    /**
     * Задает абсциссу центра объекта
     */
    public void setCenterX(int value) {
        point.x = value - height / 2;
    }

    /**
     * Задает левую ординату центра объекта
     */
    public void setCenterY(int value) {
        point.y = value - width / 2;
    }

    /**
     * Верхняя граница объекта
     */
    public int getTop() {
        return point.y;
    }

    /**
     * Нижняя граница объекта
     */
    public int getBottom() {
        return point.y + height;
    }

    /**
     * Левая граница объекта
     */
    public int getLeft() {
        return point.x;
    }

    /**
     * Правая граница объекта
     */
    public int getRight() {
        return point.x + width;
    }

    /**
     * Центральная точка объекта
     */
    public Point getCenter() {
        return new Point(point.x + width / 2, point.y + height / 2);
    }

    /**
     * Высота объекта
     */
    public int getHeight() {
        return height;
    }

    /**
     * Ширина объекта
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return Прямоугольник, ограничивающий объект
     */
    public Rect getRect() {
        return image.getBounds();
    }

    /**
     * Проверяет, пересекаются ли два игровых объекта
     */
    public static boolean intersects(GameObject obj1, GameObject obj2) {
        return Rect.intersects(obj1.getRect(), obj2.getRect());
    }
}
