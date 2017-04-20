package com.newhdc.pedergb.pushman;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class SettingsActivity extends AppCompatActivity {

    ToggleButton toggleMusic, toggleSound;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toggleMusic = (ToggleButton)findViewById(R.id.toggleMusic);
        toggleSound = (ToggleButton)findViewById(R.id.toggleSound);

        toggleMusic.setChecked(Globals.allowMusic);
        toggleSound.setChecked(Globals.allowSound);

        mContext = this;

        toggleMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        Globals.allowMusic = true;
                        Globals.setAllowMusic(mContext, true);
                    } else {
                        Globals.allowMusic = false;
                        Globals.setAllowMusic(mContext, false);
                    }
                }
            });

        toggleSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Globals.allowSound = true;
                    Globals.setAllowSound(mContext, true);
                } else {
                    Globals.allowSound = false;
                    Globals.setAllowSound(mContext, false);
                }
            }
        });

    }
}
