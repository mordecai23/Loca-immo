package com.example.projetandroid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
    Button b1, b2, b3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.mainAppTheme);
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_main);
        b1 = findViewById(R.id.button);
        b2 = findViewById(R.id.button2);
        b3 = findViewById(R.id.button3);


        LinearLayout ll = findViewById(R.id.laymain);
        AnimationDrawable animationDrawable = (AnimationDrawable) ll.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

    }

    public void inscription(View v) {
        startActivity(new Intent(this, inscription.class));

    }

    public void connexion(View v) {
        startActivity(new Intent(this, Connexion.class));

    }

    public void modeinvite(View view) {
        Intent intent1 = new Intent(this, Consultation.class);
        intent1.putExtra("mail", "modeinvite");
        intent1.putExtra("client", "client");
        intent1.putExtra("mode", "invite");
        startActivity(intent1);

    }
}
