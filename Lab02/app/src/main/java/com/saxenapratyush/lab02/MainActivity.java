package com.saxenapratyush.lab02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button incrementButton;
    Button changeName;
    TextView greetingDisplay;
    EditText editTextContent;
    int count=0;
    String[] planets;
    String[] food;
    RadioButton leftRadBut, rightRadBut, foodRadBut, planetsRadBut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        incrementButton = findViewById(R.id.increment_button);
        changeName = findViewById(R.id.change_name_button);
        greetingDisplay = findViewById(R.id.greeting_textview);
        editTextContent = findViewById(R.id.enter_your_name_edittext);
        planets = getResources().getStringArray(R.array.planets_array);
        food = getResources().getStringArray(R.array.food_array);
        leftRadBut = findViewById(R.id.radio_left);
        rightRadBut = findViewById(R.id.radio_right);
        foodRadBut = findViewById(R.id.radio_food);
        planetsRadBut = findViewById(R.id.radio_planets);
        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rightRadBut.isChecked())
                    count = ++count % planets.length;
                if(planetsRadBut.isChecked())
                    greetingDisplay.setText(planets[count]);
                else
                    greetingDisplay.setText(food[count]);
            }
        });
        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                greetingDisplay.setText("Hey " + editTextContent.getText().toString());
            }
        });
    }
    public void decrement(View view) {
        if(leftRadBut.isChecked())
            if(--count <0)count=planets.length-1;
        if(planetsRadBut.isChecked())
            greetingDisplay.setText(planets[count]);
        else
            greetingDisplay.setText(food[count]);
    }
}