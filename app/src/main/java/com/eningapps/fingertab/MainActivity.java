package com.eningapps.fingertab;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    int lastBackColor;//added this to have different background color each time
    int triesLeft; // if(triesLeft==0) finish game

    TextView triesLeftTextView;// with every step this view is updated to show steps left for user
    ConstraintLayout layoutMain; // add to change background color
    ImageButton leftButton, rightButton;
    Button backButton;
    Button startButton;
    TextView scoreTextView;
    long timeLeft, timeRight; // this for scoring difference in time 
    int leftScore=0, rightScore=0;
    boolean leftPressed = false, rightPressed = false; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layoutMain=(ConstraintLayout) findViewById(R.id.mainlayout);
        triesLeft=getIntent().getIntExtra("maxSteps",5);
        leftButton = (ImageButton) findViewById(R.id.imageButtonLeft);
        rightButton = (ImageButton) findViewById(R.id.imageButtonRight);
        startButton = (Button) findViewById(R.id.buttonStartGame);
        backButton = (Button) findViewById(R.id.buttonBack);
        scoreTextView = (TextView) findViewById(R.id.scoretextView);
        triesLeftTextView = (TextView) findViewById(R.id.triesLeftTextView);
        triesLeftTextView.setText("Tries left : "+triesLeft);
        leftButton.setEnabled(false); // user can press this buttons only after pressing startButton and when background color changes
        rightButton.setEnabled(false);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startButton.setText("Go again!");
                startButton.setEnabled(false);//to avoid multiple starting game
                leftButton.setEnabled(false);// user can press this buttons only after pressing startButton and when background color changes
                rightButton.setEnabled(false);
                timeLeft=0;
                timeRight=0;
                layoutMain.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white)); // to make difference between steps
                ColorField colorField=new ColorField();//see this class below
                colorField.execute();
                Lock lock=new Lock();//see this class below
                lock.execute();
            }
        });
        leftButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                timeLeft = motionEvent.getEventTime();
                leftPressed = true;
                leftButton.setEnabled(false);
                return false;
            }
        });
        rightButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                timeRight = motionEvent.getEventTime();
                rightPressed = true;
                rightButton.setEnabled(false);
                return false;
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this, ScreenActivity.class);
                startActivity(i);
            }
        });
    }
    
    //This is a like an implementation of ThreadLock, it waits until both buttons are pressed 
    //and than in onPostExecute calculates who wins using difference in time and shows AllertDialog with such information.
    //If triesLeft-1==0 it ends game and lets user go back to ScreenActivity with backButton
    private class Lock extends AsyncTask<Void, Void, Integer>{
        @Override
        protected Integer doInBackground(Void... voids) {
            while(!leftPressed || !rightPressed){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            String s="";
                if(timeLeft>timeRight){
                    rightScore++;
                    s+="Правый ";
                }
                else {
                    leftScore++;
                    s+="Левый ";
                }
            s+="игрок был быстрее на "+ String.format("%.3f сек. !",Math.abs(timeLeft-timeRight)*0.001);
            builder.setMessage(s);
            builder.show();
            scoreTextView.setText(leftScore+":"+rightScore);
            if(triesLeft-1>0) {
                triesLeft--;
                triesLeftTextView.setText("Tries left : " + triesLeft);
            }else{
                String rez="";
                if(leftScore!=rightScore){
                    if(leftScore<rightScore)
                        rez+="Правый";
                    else
                        rez+="Левый";
                    rez+=" игрок выйграл !";
                } else
                    rez+="Ничья !";

                triesLeftTextView.setText(rez);
                triesLeftTextView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
                triesLeftTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                startButton.setVisibility(View.INVISIBLE);
                startButton.setEnabled(false);
                backButton.setVisibility(View.VISIBLE);
                backButton.setEnabled(true);
            }
            startButton.setEnabled(true);
            leftPressed=false;
            rightPressed=false;

        }
    }
    
    
    //This class made to change background color using random number
    private class ColorField extends AsyncTask<Void, Void, Integer>{

        @Override
        protected Integer doInBackground(Void... voids) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            int color=0;
            do{
                color=(int) (1 + Math.random()*5);
            }while (color==lastBackColor);
            lastBackColor=color;
            return color;
        }

        @Override
        protected void onPostExecute(Integer color) {
            leftButton.setEnabled(true);
            rightButton.setEnabled(true);
            switch (color) {
                case 1:
                    layoutMain.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.dontPressColor));
                    break;
                case 2:
                    layoutMain.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.PressColor));
                    break;
                case 3:
                    layoutMain.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color3));
                    break;
                case 4:
                    layoutMain.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color4));
                    break;
                case 5:
                    layoutMain.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color5));
                    break;
                default:
                    layoutMain.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.color5));
                    break;
            }
        }
    }



}
