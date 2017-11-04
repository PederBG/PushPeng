package com.newhdc.pedergb.pushpeng;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;


/**
 * Created by PederGB on 10.04.2017.
 */

public class Globals {
    public static int level = 0; // ( + 1)
    public static int movesCount;

    // ------------ get stored data ------------- \\
    private SharedPreferences sharedPref;
    private static String PREF_NAME = "savedSettings";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static boolean getAllowMusic(Context context) {
        return getPrefs(context).getBoolean("allowMusic", true);
    }
    public static void setAllowMusic(Context context, boolean input) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putBoolean("allowMusic", input);
        editor.apply();
    }

    public static boolean getAllowSound(Context context) {
        return getPrefs(context).getBoolean("allowSound", true);
    }
    public static void setAllowSound(Context context, boolean input) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putBoolean("allowSound", input);
        editor.apply();
    }

    public static int getHighscore(Context context, int thisLevel) {
        return getPrefs(context).getInt("highscore" + thisLevel, -1);
    }
    public static void setHighscore(Context context, int input, int thisLevel) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putInt("highscore" + thisLevel, input);
        editor.apply();
    }
    // ................................................. \\



    public static MediaPlayer mainTheme, moveSound;
    public static boolean allowMusic;
    public static boolean allowSound= true;


    public static void stopTheme() {
        if (mainTheme != null) {
            mainTheme.stop();
            mainTheme.release();
            mainTheme = null;
        }
    }
    public static void stopSound() {
        if (moveSound != null) {
            moveSound.stop();
            moveSound.release();
            moveSound = null;
        }
    }

}
