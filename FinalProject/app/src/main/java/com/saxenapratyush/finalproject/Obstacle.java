package com.saxenapratyush.finalproject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Obstacle {
    private float x;
    private float y;
    private final float width;
    private final float height;
    private final Paint paint;

    public Obstacle(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.paint = new Paint();
        paint.setColor(Color.RED);
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(x, y, x + width, y + height, paint);
    }

    public void moveDown(float speed) {
        y += speed;
    }

    public float getY() {
        return y;
    }

    public boolean checkCollision(float ballX, float ballY, float ballRadius) {
        float closestX = clamp(ballX, x, x + width);
        float closestY = clamp(ballY, y, y + height);
        float distanceX = ballX - closestX;
        float distanceY = ballY - closestY;
        return (distanceX * distanceX + distanceY * distanceY) < (ballRadius * ballRadius);
    }

    private float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}