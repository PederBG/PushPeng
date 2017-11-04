package com.newhdc.pedergb.pushpeng;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LevelWonActivity extends AppCompatActivity {
    private MediaPlayer winSound;


    Button nextLevel;
    TextView movesCount, newHighscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_won);

        if(Globals.allowMusic){
            winSound = MediaPlayer.create(LevelWonActivity.this, R.raw.win_sound);
            winSound.start();
        }

        newHighscore = (TextView)findViewById(R.id.newHighscore);
        if ((Globals.movesCount < Globals.getHighscore(this, Globals.level) ||
                Globals.getHighscore(this, Globals.level) == -1) && Globals.movesCount > 0){ //(siste fikser liten bug)
            newHighscore.setText("It's a new highscore!");
            Globals.setHighscore(this, Globals.movesCount, Globals.level);
        }

        movesCount = (TextView)findViewById(R.id.movesCount);
        movesCount.setText("Number of moves: " + Globals.movesCount);


        nextLevel = (Button)findViewById(R.id.button5);
        nextLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.level += 1;
                if(Globals.allowMusic) winSound.stop();
                Intent newLevelIntent = new Intent(LevelWonActivity.this, CountDownActivity.class);
                LevelWonActivity.this.startActivity(newLevelIntent);
            }
        });
    }

    // ------------------ Handler for back press button ---------------- \\
    boolean twice;
    @Override
    public void onBackPressed() {
        if (twice){
            winSound.stop();
            winSound.release();
            winSound = null;
            Intent mainMenuIntent = new Intent(LevelWonActivity.this, StartupActivity.class);
            LevelWonActivity.this.startActivity(mainMenuIntent);
            finish();
        }
        twice = true;
        showToastMessage("Go to main menu?", 500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                twice = false;
            }
        }, 1500);
    }
    // ...................................................................... \\


    @Override
    protected void onPause() {
        super.onPause();
        if ((Globals.allowMusic) && (winSound != null)) winSound.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((Globals.allowMusic) && (winSound != null)){
            winSound.start();
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
