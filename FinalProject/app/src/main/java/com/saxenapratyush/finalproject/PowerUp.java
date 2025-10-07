package com.saxenapratyush.finalproject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class PowerUp {
    private float x, y, width, height;
    private Paint paint;

    public PowerUp(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        paint = new Paint();
        paint.setColor(Color.GREEN); // Change the color to indicate a power-up
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
        return (ballX + ballRadius > x && ballX - ballRadius < x + width && ballY + ballRadius > y && ballY - ballRadius < y + height);
    }
}