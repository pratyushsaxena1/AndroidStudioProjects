package com.saxenapratyush.finalproject;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameView = findViewById(R.id.game_view);
        gameView.post(new Runnable() {
            @Override
            public void run() {
                gameView.setInitialBallPosition(gameView.getWidth() / 2f, gameView.getHeight() / 2f);
            }
        });
    }
}
