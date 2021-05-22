package com.example.projetandroid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.projetandroid.Function.executeRequest;
import static java.lang.Boolean.FALSE;

public class inscription extends Activity {
    EditText nom, prenom, mdp, mail, adresse, ville, quartier, num;
    String vnom, vprenom, vmdp, vmail, vadresse, vville, vquartier, vnum, vtype, vsituation;
    Spinner type, situation;
    Button b;
    private final TextWatcher inscriptionwatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String snom = nom.getText().toString().trim();
            String sprenom = prenom.getText().toString().trim();
            String smdp = mdp.getText().toString().trim();
            String smail = mail.getText().toString().trim();
            String sville = ville.getText().toString().trim();
            String sadresse = adresse.getText().toString().trim();
            String squartier = quartier.getText().toString().trim();
            String snum = num.getText().toString().trim();


            b.setEnabled(!snom.isEmpty() && !sprenom.isEmpty() && !smdp.isEmpty() && !smail.isEmpty() && !sville.isEmpty() && !sadresse.isEmpty() && !squartier.isEmpty() && !snum.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
    TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_inscription);

        //fond d'écran
        ScrollView ll = findViewById(R.id.lay2);
        AnimationDrawable animationDrawable = (AnimationDrawable) ll.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        //Les liste déroulantes
        type = findViewById(R.id.spinner1);
        String[] typeclient = new String[]{"Client", "Annonceur"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, typeclient);
        type.setAdapter(adapter);

        situation = findViewById(R.id.spinner2);
        String[] profession = new String[]{"Particulier", "Professionel"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, profession);
        situation.setAdapter(adapter1);

        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        mdp = findViewById(R.id.txtPwd);
        mail = findViewById(R.id.mail);
        adresse = findViewById(R.id.txtadresse);
        ville = findViewById(R.id.txtville);
        quartier = findViewById(R.id.txtquartier);
        num = findViewById(R.id.txtPhone);
        b = findViewById(R.id.btnSend);
        t = findViewById(R.id.txtsituation);

        nom.addTextChangedListener(inscriptionwatch);
        prenom.addTextChangedListener(inscriptionwatch);
        mdp.addTextChangedListener(inscriptionwatch);
        mail.addTextChangedListener(inscriptionwatch);
        ville.addTextChangedListener(inscriptionwatch);
        adresse.addTextChangedListener(inscriptionwatch);
        quartier.addTextChangedListener(inscriptionwatch);
        num.addTextChangedListener(inscriptionwatch);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (type.getSelectedItem().toString().equals("Annonceur")) {
                    t.setVisibility(View.VISIBLE);
                    situation.setVisibility(View.VISIBLE);
                } else {
                    t.setVisibility(View.INVISIBLE);
                    situation.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    //Lancement du thread lors de la validation
    public void inscriptionweb(View v) {
        ProgressBar pb = findViewById(R.id.loadins);
        pb.setVisibility(View.VISIBLE);
        b.setEnabled(FALSE);
        b.setText("En cours....");

        vnom = nom.getText().toString().trim();
        vprenom = prenom.getText().toString().trim();
        vmdp = mdp.getText().toString().trim();
        vmail = mail.getText().toString().trim();
        vadresse = adresse.getText().toString().trim();
        vville = ville.getText().toString().trim();
        vquartier = quartier.getText().toString().trim();
        vnum = num.getText().toString().trim();
        vnom = nom.getText().toString().trim();
        vtype = type.getSelectedItem().toString();
        vsituation = situation.getSelectedItem().toString();
        if (vtype.equals("Client")) {
            vsituation = "Client";
        }

        String checkmail = "SELECT * FROM utilisateur WHERE mail='" + vmail + "'";
        String checkbdd = "https://terl3recette.000webhostapp.com/connexionutilisateur.php";
        int result = Integer.parseInt(Function.getresult(checkbdd, checkmail));
        if (result == 0) {
            // lien vers le php qui traitera les requetes pour l'insertion dans la BDD
            String lienbdd = "https://terl3recette.000webhostapp.com/inscription.php";
            //requete d'insertion
            String query = "INSERT INTO utilisateur (adresse,mail,nom,numTel,prenom,quartier,situationPro,tarif,type,ville,password) values('" + vadresse + "','" + vmail + "','" + vnom + "'," + vnum + ",'" + vprenom + "','" + vquartier + "','" + vsituation + "',20,'" + vtype + "','" + vville + "'," + vmdp + ");";

            Function.executeRequest(lienbdd, query);

            Toast.makeText(getApplicationContext(), "Vous etes désormais inscrit !", Toast.LENGTH_LONG).show();

            String inserttoken = "INSERT INTO utilisateurtoken(idutilisateur,token) SELECT idUtilisateur,'" + FirebaseToken.getToken(inscription.this) + "' FROM utilisateur WHERE mail='" + mail + "'";
            String lientoken = "https://terl3recette.000webhostapp.com/inscription.php";
            String deletetoken = "DELETE FROM utilisateurtoken WHERE token='" + FirebaseToken.getToken(inscription.this) + "'";
            executeRequest(lientoken, deletetoken);
            executeRequest(lientoken, inserttoken);

            if (vtype.equals("Annonceur")) {
                Intent i1 = new Intent(this, Accueil_annonceur.class);
                i1.putExtra("mail", vmail);
                startActivity(i1);
                finish();
            } else {
                Intent i1 = new Intent(this, Accueil_client.class);
                i1.putExtra("mail", vmail);
                startActivity(i1);
                finish();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Cette adresse mail est déjà utilisée.", Toast.LENGTH_LONG).show();
            finish();
        }
    }


}
