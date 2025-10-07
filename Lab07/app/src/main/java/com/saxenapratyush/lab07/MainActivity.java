package com.saxenapratyush.lab07;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    FragmentA fragmentA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.main_textview);
        fragmentA = (FragmentA)getSupportFragmentManager().findFragmentById(R.id.a_fragment);
    }

    public void update_main(View view) {
        textView.setText("This is Slide 2: It is represented by a !");
        ((Button)view).setText("This is post-click");
    }
}