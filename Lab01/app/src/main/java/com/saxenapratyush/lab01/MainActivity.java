package com.saxenapratyush.lab01;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button incrementButton;
    TextView greetingDisplay;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        incrementButton = findViewById(R.id.increment_button);
        greetingDisplay = findViewById(R.id.greeting_textview);
        incrementButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                System.out.println("incrementing: "+ ++count);
                Log.i("incrementing", String.valueOf(count));
                greetingDisplay.setText(String.valueOf(count));
            }
        });
    }
    public void decrement(View view) {
        System.out.println("decrementing: "+ --count);
        greetingDisplay.setText(String.valueOf(count));
    }
}