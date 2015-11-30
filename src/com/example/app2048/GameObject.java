package com.example.app2048;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public abstract class GameObject {
    // ��������� ��� �����������
    public static final int DIR_LEFT = -1;
    public static final int DIR_RIGHT = 1;
    public static final int DIR_NONE = 0;

    /**
     * ���������� ������� �����
     */
    protected Point point;

    /**
     * ������ �����������
     */
    protected int height;

    /**
     * ������ �����������
     */
    protected int width;

    /**
     * �����������
     */
    private Drawable image;

    /**
     * ��������
     */
    protected int speed;

    /**
     * �����������
     *
     * @param image �����������, ������� ����� ���������� ������ ������
     */
    public GameObject(Drawable image) {
        this.image = image;
        point = new Point(0, 0);
        width = image.getIntrinsicWidth();
        height = image.getIntrinsicHeight();
    }

    /**
     * ����������� ������� �����
     */
    protected abstract void updatePoint();

    /**
     * ����������� �������
     */
    public void update() {
        updatePoint();
        image.setBounds(point.x, point.y, point.x + width, point.y + height);
    }

    /**
     * ��������� �������
     */
    public void draw(Canvas canvas) {
        image.draw(canvas);
    }

    /**
     * ������ ����� ������� �������
     */
    public void setLeft(int value) {
        point.x = value;
    }

    /**
     * ������ ������ ������� �������
     */
    public void setRight(int value) {
        point.x = value - width;
    }

    /**
     * ������ ������� ������� �������
     */
    public void setTop(int value) {
        point.y = value;
    }

    /**
     * ������ ������ ������� �������
     */
    public void setBottom(int value) {
        point.y = value - height;
    }

    /**
     * ������ �������� ������ �������
     */
    public void setCenterX(int value) {
        point.x = value - height / 2;
    }

    /**
     * ������ ����� �������� ������ �������
     */
    public void setCenterY(int value) {
        point.y = value - width / 2;
    }

    /**
     * ������� ������� �������
     */
    public int getTop() {
        return point.y;
    }

    /**
     * ������ ������� �������
     */
    public int getBottom() {
        return point.y + height;
    }

    /**
     * ����� ������� �������
     */
    public int getLeft() {
        return point.x;
    }

    /**
     * ������ ������� �������
     */
    public int getRight() {
        return point.x + width;
    }

    /**
     * ����������� ����� �������
     */
    public Point getCenter() {
        return new Point(point.x + width / 2, point.y + height / 2);
    }

    /**
     * ������ �������
     */
    public int getHeight() {
        return height;
    }

    /**
     * ������ �������
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return �������������, �������������� ������
     */
    public Rect getRect() {
        return image.getBounds();
    }

    /**
     * ���������, ������������ �� ��� ������� �������
     */
    public static boolean intersects(GameObject obj1, GameObject obj2) {
        return Rect.intersects(obj1.getRect(), obj2.getRect());
    }
}
