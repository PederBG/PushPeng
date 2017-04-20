package com.newhdc.pedergb.pushman;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CountDownActivity extends AppCompatActivity {

    TextView countDown, nextLevel, nextLevelTime, highscore;

    final CountDownTimer timer = new CountDownTimer(4500, 1000) {

        public void onTick(long millisUntilFinished) {
            if (millisUntilFinished < 3800)countDown.setText("" + millisUntilFinished / 1000);

        }

        public void onFinish() {
            countDown.setText("Go !");
            Intent startLevelIntent = new Intent(CountDownActivity.this, MainActivity.class);
            //startLevelIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //Not needed ?
            CountDownActivity.this.startActivity(startLevelIntent);
            finish(); //Solving wrong activity on restart bug
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);

        countDown = (TextView)findViewById(R.id.btCountDown);
        countDown.setText("Get ready...");

        nextLevel = (TextView)findViewById(R.id.nextLevel);
        nextLevel.setText("Level " + (Globals.level+1));

        highscore = (TextView)findViewById(R.id.highscore);
        if (Globals.getHighscore(this, Globals.level) != -1){
            highscore.setText("Highscore: " + Globals.getHighscore(this, Globals.level));
        }

        nextLevelTime = (TextView)findViewById(R.id.nextLevelTime);
        nextLevelTime.setText("Timer: " + (MainActivity.levelTime[Globals.level] / 1000) + " seconds");

        timer.start();
    }

    @Override
    public void onBackPressed() {
        //Won't be doing nothing brother
    }
}
