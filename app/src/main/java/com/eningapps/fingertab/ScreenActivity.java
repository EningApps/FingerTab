package com.eningapps.fingertab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ScreenActivity extends AppCompatActivity {

    TextView maxStepsView;
    int steps=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_activity);
        maxStepsView=(TextView) findViewById(R.id.textViewMaxSteps);
        Button button=(Button) findViewById(R.id.button);
        Button buttonPlus=(Button) findViewById(R.id.buttonPlus);
        final Button buttonMinus=(Button) findViewById(R.id.buttonMinus);

        buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                steps=Integer.parseInt(maxStepsView.getText().toString());
                if(steps-1>0) {
                    steps--;
                    maxStepsView.setText(String.valueOf(steps));
                }
            }
        });
        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                steps=Integer.parseInt(maxStepsView.getText().toString());
                steps++;
                maxStepsView.setText(String.valueOf(steps));
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ScreenActivity.this, MainActivity.class);
                i.putExtra("maxSteps", steps);
                startActivity(i);
            }
        });
    }
}
