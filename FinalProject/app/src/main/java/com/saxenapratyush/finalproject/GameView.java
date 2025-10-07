package com.saxenapratyush.finalproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameView extends View implements SensorEventListener {

    private float ballX;
    private float ballY;
    private final float ballRadius = 50f;
    private final SensorManager sensorManager;
    private final Sensor accelerometer;
    private final Paint paint;
    private final List<Obstacle> obstacles;
    private final List<PowerUp> powerUps;
    private boolean collisionDetected = false;
    private final Random random;
    private static final int OBSTACLE_SPAWN_INTERVAL = 2000; // in milliseconds
    private static final int POWERUP_SPAWN_INTERVAL = 10000; // in milliseconds
    private long lastSpawnTime = 0;
    private long lastPowerUpTime = 0;
    private float obstacleSpeed = 5f; // Initial speed at which obstacles move down
    private static final float SPEED_INCREMENT = 0.01f; // Speed increment per update
    private static final float MAX_OBSTACLE_SPEED = 20f; // Maximum obstacle speed
    private static final int INITIAL_LIVES = 3;
    private int lives = INITIAL_LIVES;
    private int score = 0;
    private boolean gameOver = false;
    private boolean shieldActive = false;
    private long shieldActivatedTime = 0;
    private static final int SHIELD_DURATION = 5000; // Shield lasts for 5 seconds
    private int highestLocalScore = 0;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.BLUE);
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        obstacles = new ArrayList<>();
        powerUps = new ArrayList<>();
        random = new Random();
        initializeObstacles();
        startGameLoop();
    }

    private void initializeObstacles() {
        // Initial obstacles can be added here
    }

    private void startGameLoop() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!gameOver) {
                    updateGame();
                    invalidate();
                }
                postDelayed(this, 16); // Approx. 60 FPS
            }
        }, 16);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event != null) {
            ballX -= event.values[0] * 2; // Scale factor to adjust sensitivity
            // Keep ball within screen bounds
            if (ballX < ballRadius) ballX = ballRadius;
            if (ballX > getWidth() - ballRadius) ballX = getWidth() - ballRadius;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);

        // Draw ball in the vertical center
        if (collisionDetected) {
            paint.setColor(Color.RED);
        } else {
            paint.setColor(Color.BLUE);
        }
        ballY = getHeight() / 2;
        canvas.drawCircle(ballX, ballY, ballRadius, paint);

        // Draw obstacles
        for (Obstacle obstacle : obstacles) {
            obstacle.draw(canvas);
        }

        // Draw power-ups
        for (PowerUp powerUp : powerUps) {
            powerUp.draw(canvas);
        }

        // Draw life counter
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        canvas.drawText("Lives: " + lives, 50, 100, paint);

        // Draw score
        canvas.drawText("Score: " + score, getWidth() - 250, 100, paint);

        // Draw game over message
        if (gameOver) {
            paint.setColor(Color.RED);
            paint.setTextSize(100);
            canvas.drawText("Game Over! Tap to play again", getWidth() / 2 - 500, getHeight() / 2, paint);
        }

        // Draw shield active indicator
        if (shieldActive) {
            paint.setColor(Color.GREEN);
            paint.setTextSize(50);
            canvas.drawText("Shield Active", getWidth() / 2 - 150, 100, paint);
        }

        // Draw highest local score
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        canvas.drawText("High Score: " + highestLocalScore, getWidth() - 367, 50, paint);
    }

    private void updateGame() {
        // Move obstacles down
        Iterator<Obstacle> iterator = obstacles.iterator();
        while (iterator.hasNext()) {
            Obstacle obstacle = iterator.next();
            obstacle.moveDown(obstacleSpeed);
            if (obstacle.getY() > getHeight()) {
                iterator.remove(); // Remove obstacles that are out of view
                score++; // Increment score when an obstacle is passed
            }
        }

        // Move power-ups down
        Iterator<PowerUp> powerUpIterator = powerUps.iterator();
        while (powerUpIterator.hasNext()) {
            PowerUp powerUp = powerUpIterator.next();
            powerUp.moveDown(obstacleSpeed);
            if (powerUp.getY() > getHeight()) {
                powerUpIterator.remove(); // Remove power-ups that are out of view
            }
        }

        // Check for collisions
        checkCollisions();

        // Spawn new obstacles
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastSpawnTime > OBSTACLE_SPAWN_INTERVAL) {
            spawnObstacle();
            lastSpawnTime = currentTime;
        }

        // Spawn new power-ups
        if (currentTime - lastPowerUpTime > POWERUP_SPAWN_INTERVAL) {
            spawnPowerUp();
            lastPowerUpTime = currentTime;
        }

        // Gradually increase the obstacle speed
        if (obstacleSpeed < MAX_OBSTACLE_SPEED) {
            obstacleSpeed += SPEED_INCREMENT;
        }

        // Deactivate shield if duration is over
        if (shieldActive && currentTime - shieldActivatedTime > SHIELD_DURATION) {
            shieldActive = false;
        }
    }

    private void checkCollisions() {
        boolean collisionOccurred = false;
        for (Obstacle obstacle : obstacles) {
            if (obstacle.checkCollision(ballX, ballY, ballRadius)) {
                collisionOccurred = true;
                break; // Exit the loop after the first collision
            }
        }
        if (collisionOccurred) {
            if (!collisionDetected) {
                collisionDetected = true;
                if (!shieldActive) {
                    lives--; // Decrement lives once per collision
                    if (lives <= 0) {
                        gameOver = true; // Game over if no lives left
                    }
                }
            }
        } else {
            collisionDetected = false;
        }

        // Check for power-up collisions
        Iterator<PowerUp> powerUpIterator = powerUps.iterator();
        while (powerUpIterator.hasNext()) {
            PowerUp powerUp = powerUpIterator.next();
            if (powerUp.checkCollision(ballX, ballY, ballRadius)) {
                shieldActive = true; // Activate shield
                shieldActivatedTime = System.currentTimeMillis(); // Record activation time
                powerUpIterator.remove(); // Remove power-up after collection
            }
        }
        // Update highest local score if necessary
        if (score > highestLocalScore) {
            highestLocalScore = score;
        }
    }

    private void spawnObstacle() {
        float obstacleWidth = 100 + random.nextInt(200);
        float obstacleHeight = 100 + random.nextInt(200);
        float obstacleX = random.nextInt(getWidth() - (int) obstacleWidth);
        float obstacleY = -obstacleHeight; // Spawn at the top of the screen
        obstacles.add(new Obstacle(obstacleX, obstacleY, obstacleWidth, obstacleHeight));
    }

    private void spawnPowerUp() {
        float powerUpSize = 100;
        float powerUpX = random.nextInt(getWidth() - (int) powerUpSize);
        float powerUpY = -powerUpSize; // Spawn at the top of the screen
        powerUps.add(new PowerUp(powerUpX, powerUpY, powerUpSize, powerUpSize));
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        sensorManager.unregisterListener(this);
    }

    public void setInitialBallPosition(float x, float y) {
        ballX = x;
        ballY = y;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && gameOver) {
            resetGame();
            invalidate(); // Redraw the view
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void resetGame() {
        lives = INITIAL_LIVES;
        score = 0;
        gameOver = false;
        shieldActive = false;
        obstacles.clear();
        powerUps.clear();
        initializeObstacles(); // Reset obstacles
        obstacleSpeed = 5f; // Reset obstacle speed
    }
}