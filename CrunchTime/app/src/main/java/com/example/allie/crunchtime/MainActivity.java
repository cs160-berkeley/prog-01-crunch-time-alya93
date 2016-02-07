package com.example.allie.crunchtime;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    int selected_exercise = 0;
    int target_calories = 0;
    boolean display_equivalent = false;
    static int[] exercise_per_hun_calories = {
            350, 200, 225, 25, 25, 10,
            100, 12, 20, 12, 13, 15
    };
    static String[] exercise_string = {
            "Pushups","Situps", "Squats", "Leg-lifts", "Planks", "Jumping Jacks",
            "Pullups", "Cycling", "Walking", "Jogging", "Swimming", "Stair-Climbing"

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Text View + Edit View
        EditText amount = (EditText) findViewById(R.id.amount);
        amount.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                display_equivalent = true;
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void afterTextChanged(Editable s) {
                calculate_exercise();

            }


        });

        TextView type_exercise = (TextView) findViewById(R.id.type_mes);
        type_exercise.setText("@string/default_mes");

        //Drop down menu
        Spinner spinner = (Spinner) findViewById(R.id.exercises_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.exercises_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void calculate_exercise() {
        int amount_int = 0;
        EditText amount = (EditText) findViewById(R.id.amount);
        try {
            amount_int = Integer.parseInt(amount.getText().toString());
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }
        TextView cal = (TextView) findViewById(R.id.calories);
        target_calories = (amount_int*100/exercise_per_hun_calories[selected_exercise]);
        cal.setText(target_calories + " Calories ");
        displayOtherExercises();
    }

    private void displayOtherExercises() {
        LinearLayout myLayout = (LinearLayout) findViewById(R.id.exercises_list);
        myLayout.removeAllViews();
        TextView[] pairs=new TextView[12];
        for(int l=0; l<12; l++)
        {
            //innerPairs
            pairs[l] = new TextView(this);
            pairs[l].setTextSize(15);
            pairs[l].setId(l);
            int equivalent= (target_calories * exercise_per_hun_calories[l])/100;
            String text;
            if(l == 0 || l == 1 || l == 2 || l == 6) {
                text = " Reps";
            } else {
                text = " Minutes";
            }
            pairs[l].setTextColor(Color.parseColor("#000000"));
            pairs[l].setText(exercise_string[l] + ": " + equivalent + text);
            pairs[l].setVisibility(View.INVISIBLE);
            if(display_equivalent) {
                pairs[l].setVisibility((View.VISIBLE));
            }
            myLayout.addView(pairs[l]);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        TextView type_exercise = (TextView) findViewById(R.id.type_mes);
        selected_exercise = pos;
        if(pos == 0 || pos == 1 || pos == 2 || pos == 6){
            type_exercise.setText(getString(R.string.reps));
        } else {
            type_exercise.setText(getString(R.string.minutes));
        }
        calculate_exercise();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Do Nothing
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
