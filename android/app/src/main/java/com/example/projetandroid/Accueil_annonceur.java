package com.example.projetandroid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Accueil_annonceur extends Activity {
    String mail;
    TextView titre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_accueil_annonceur);
        //fond d'écran
        LinearLayout ll = findViewById(R.id.layannonceur);
        AnimationDrawable animationDrawable = (AnimationDrawable) ll.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        Intent iconnection = getIntent();
        mail = iconnection.getStringExtra("mail");
        titre = findViewById(R.id.titreannonce);

    }


    public void deposer(View v) {


        Intent intent = new Intent(this, deposer.class);
        intent.putExtra("mail", mail);
        startActivity(intent);
    }

    public void supprimer(View v) {
        Intent intent1 = new Intent(this, supprimerAnnonce.class);
        intent1.putExtra("mail", mail);
        startActivity(intent1);
    }

    public void modifier(View v) {
        Intent intent1 = new Intent(this, modifiergeneral.class);
        intent1.putExtra("mail", mail);
        startActivity(intent1);
    }

    public void consulter(View v) {
        Intent intent1 = new Intent(this, Consultation.class);
        intent1.putExtra("mail", mail);
        intent1.putExtra("clienttype", "annonceur");
        startActivity(intent1);
    }

    public void messagerie(View v) {
        Intent intent1 = new Intent(this, Messagerie.class);
        intent1.putExtra("mail", mail);
        startActivity(intent1);
    }

    public void statistic(View v) {
        Intent intent1 = new Intent(this, Statistics.class);
        intent1.putExtra("mail", mail);
        startActivity(intent1);
    }


}
