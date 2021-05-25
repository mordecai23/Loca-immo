package com.example.projetandroid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import static com.example.projetandroid.Function.executeRequest;
import static java.lang.Boolean.FALSE;

public class Connexion extends Activity {
    Spinner type;
    EditText mdp, mail;
    String vmdp;
    String vmail;
    String vtype;
    Button b;

    private final TextWatcher connexionwatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String smdp = mdp.getText().toString().trim();
            String smail = mail.getText().toString().trim();


            b.setEnabled(!smdp.isEmpty() && !smail.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
    int nb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        overridePendingTransition(R.anim.fadein, R.anim.fadeout);


        setContentView(R.layout.activity_connexion);


        //fond d'écran
        ScrollView ll = findViewById(R.id.lay3);
        AnimationDrawable animationDrawable = (AnimationDrawable) ll.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        //Les liste déroulantes
        type = findViewById(R.id.spinner3);
        String[] typeclient = new String[]{"Annonceur", "Client"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, typeclient);
        type.setAdapter(adapter);


        mdp = findViewById(R.id.pwdcon);
        mail = findViewById(R.id.mailcon);
        b = findViewById(R.id.btnSend1);
        mdp.addTextChangedListener(connexionwatch);
        mail.addTextChangedListener(connexionwatch);
    }

    @SuppressLint("SetTextI18n")
    public void connexionweb(View v) {
        ProgressBar pb = findViewById(R.id.loadcon);
        pb.setVisibility(View.VISIBLE);
        b.setEnabled(FALSE);
        b.setText("En cours....");
        vmdp = mdp.getText().toString().trim();
        vmail = mail.getText().toString().trim();
        vtype = type.getSelectedItem().toString();

        // lien vers le php qui traitera les requetes pour l'insertion dans la BDD
        String lienbdd = "https://terl3recette.000webhostapp.com/connexionutilisateur.php";

        //requete d'insertion
        String queryconnection = "SELECT * FROM utilisateur WHERE password=" + vmdp + " AND mail='" + vmail + "' AND type='" + vtype + "';";

        nb = Integer.parseInt(Function.getresult(lienbdd, queryconnection));


        if (nb > 0) {

            String inserttoken = "INSERT INTO utilisateurtoken(idutilisateur,token) SELECT idUtilisateur,'" + FirebaseToken.getToken(Connexion.this) + "' FROM utilisateur WHERE mail='" + vmail + "'";
            String lientoken = "https://terl3recette.000webhostapp.com/inscription.php";
            String deletetoken = "DELETE FROM utilisateurtoken WHERE token='" + FirebaseToken.getToken(Connexion.this) + "'";
            executeRequest(lientoken, deletetoken);
            executeRequest(lientoken, inserttoken);
            Log.d("req", inserttoken);

            Toast.makeText(getApplicationContext(), "Vous etes désormais connecté.", Toast.LENGTH_LONG).show();
            Intent i1;
            if (vtype.equals("Annonceur")) {
                i1 = new Intent(this, Accueil_annonceur.class);
            } else {
                i1 = new Intent(this, Accueil_client.class);
            }
            i1.putExtra("mail", vmail);
            startActivity(i1);
        } else {

            Toast.makeText(getApplicationContext(), "La connexion a échoué, inscrivez vous ou resaisissez vos identifiants", Toast.LENGTH_LONG).show();
        }
        finish();
    }


}
