package com.newhdc.pedergb.pushpeng;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static com.newhdc.pedergb.pushpeng.Globals.stopTheme;

public class StartupActivity extends AppCompatActivity {

    Button btStart, btLevelSelect, btOptions, btExit;

    final String TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        btStart = (Button)findViewById(R.id.btStart);
        btLevelSelect = (Button)findViewById(R.id.btLevelSelect);
        btOptions = (Button)findViewById(R.id.btOptions);
        btExit = (Button)findViewById(R.id.btExit);

        Globals.allowMusic = Globals.getAllowMusic(this);
        Globals.allowSound = Globals.getAllowSound(this);

        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(StartupActivity.this, CountDownActivity.class);
                StartupActivity.this.startActivity(startIntent);
            }
        });
        btLevelSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(StartupActivity.this, LevelSelectActivity.class);
                StartupActivity.this.startActivity(startIntent);
            }
        });
        btOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(StartupActivity.this, SettingsActivity.class);
                StartupActivity.this.startActivity(startIntent);
            }
        });
        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTheme();
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                finish();
                System.exit(0);
            }
        });

    }

    boolean twice;
    @Override
    public void onBackPressed() {
        if (twice){
            stopTheme();
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            finish();
            System.exit(0);
        }
        twice = true;
        showToastMessage("Press again to exit.", 1000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                twice = false;
            }
        }, 2000);
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
