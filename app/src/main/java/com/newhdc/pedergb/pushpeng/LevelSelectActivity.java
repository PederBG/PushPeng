package com.newhdc.pedergb.pushpeng;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class LevelSelectActivity extends AppCompatActivity {

    LinearLayout levelsLayout;

    ArrayList<Button> levelsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);

        levelsLayout = (LinearLayout) findViewById(R.id.levelList);

        for (int i = 0; i < MainActivity.levelTime.length; i++){
            final int FINAL_NUM = i;
            Button button = new Button(this);
            /*button.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f));*/
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            lp.setMargins(10, 5, 10, 5);
            button.setLayoutParams(lp);
            String tempScore;
            if (Globals.getHighscore(this, i) == -1){
                tempScore = "Not completed";
                button.setBackgroundColor(Color.parseColor("#a69e8c"));
                if (Globals.getHighscore(this, i-1) == -1){
                    button.setEnabled(false);
                }
            }
            else{
                tempScore = "" + Globals.getHighscore(this, i);
                button.setBackgroundColor(Color.parseColor("#bf6d01"));
            }
            button.setText("Level " + (i + 1) + "\n" + "\n" + "Highscore: " + tempScore + "\n");
            button.setPadding(0, 20, 0, 0);

            levelsLayout.addView(button);
            levelsList.add(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Globals.level = FINAL_NUM;
                    Intent startLevelIntent = new Intent(LevelSelectActivity.this, CountDownActivity.class);
                    LevelSelectActivity.this.startActivity(startLevelIntent);
                }
            });
        }
    }

    // ---------------- Toast message --------------------- \\
    public void showToastMessage(String text, int duration){
        final Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, duration);
    }
}
