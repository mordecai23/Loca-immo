package com.example.projetandroid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;

public class Splashscreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        LinearLayout ll = findViewById(R.id.splashscreen);
        AnimationDrawable animationDrawable = (AnimationDrawable) ll.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.start();

        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(Splashscreen.this, MainActivity.class);
            Splashscreen.this.startActivity(mainIntent);
            Splashscreen.this.finish();
        }, 2000);
    }
}