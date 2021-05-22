package com.example.projetandroid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class Accueil_client extends Activity {
    String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_accueil_client);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        //fond d'écran
        LinearLayout ll = findViewById(R.id.layclient);
        AnimationDrawable animationDrawable = (AnimationDrawable) ll.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        Intent iconnection = getIntent();
        mail = iconnection.getStringExtra("mail");

    }

    public void nouveaute(View v) {
        Intent intent1 = new Intent(this, Consultation.class);
        intent1.putExtra("mail", mail);
        intent1.putExtra("client", "client");
        startActivity(intent1);
    }

    public void messageclient(View v) {
        Intent intent1 = new Intent(this, Messagerie.class);
        intent1.putExtra("mail", mail);
        startActivity(intent1);
    }

    public void maliste(View v) {
        Intent intent1 = new Intent(this, Consultation.class);
        intent1.putExtra("mail", mail);
        intent1.putExtra("clienttype", "client");
        startActivity(intent1);
    }

    public void alert(View v) {
        Intent intent1 = new Intent(this, AlertActivity.class);
        intent1.putExtra("mail", mail);
        startActivity(intent1);
    }
}
