package com.example.projetandroid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import static com.example.projetandroid.Function.executeRequest;

public class ConsulteAnnonceIndividuelle extends Activity {
    ImageView i1, i2;
    String mail, lien1, lien2, typeclient;
    TextView titreannonce, description, dureedispo, villeannonce, adresseannonce, surfaceannonce, loyerannonce, type;
    Button b, bt, btsup;
    Long iddepot;
    String texttitre;
    String modeinvite;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulte_annonce_individuelle);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        ScrollView ll = findViewById(R.id.indi);
        AnimationDrawable animationDrawable = (AnimationDrawable) ll.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        titreannonce = findViewById(R.id.titreannonce3);
        description = findViewById(R.id.descriptionannonce1);
        dureedispo = findViewById(R.id.dureeannonce1);
        villeannonce = findViewById(R.id.villeannonce1);
        adresseannonce = findViewById(R.id.adresseannonce1);
        surfaceannonce = findViewById(R.id.surfaceannonce1);
        loyerannonce = findViewById(R.id.loyerannonce1);
        type = findViewById(R.id.typeannonce1);
        b = findViewById(R.id.btncontacter);
        bt = findViewById(R.id.btnajouteraliste);
        btsup = findViewById(R.id.btnsupprimerdeliste);


        i1 = findViewById(R.id.imageconsulte1);
        i2 = findViewById(R.id.imageconsulte2);
        Intent modif = getIntent();
        typeclient = modif.getStringExtra("clienttype");
        texttitre = modif.getStringExtra("titre");
        modeinvite = modif.getStringExtra("mode");


        String ref = modif.getStringExtra("client");
        if (ref == null) {
            bt.setVisibility(View.GONE);
        }
        mail = modif.getStringExtra("mail");
        titreannonce.setText(modif.getStringExtra("titre"), TextView.BufferType.EDITABLE);
        description.setText(modif.getStringExtra("desc"), TextView.BufferType.EDITABLE);
        dureedispo.setText("Durée : " + modif.getStringExtra("dureedispo") + " mois", TextView.BufferType.EDITABLE);
        villeannonce.setText(modif.getStringExtra("ville"), TextView.BufferType.EDITABLE);
        adresseannonce.setText("Adresse : " + modif.getStringExtra("Adresse"), TextView.BufferType.EDITABLE);
        surfaceannonce.setText(modif.getStringExtra("surface") + " mètres carrés.", TextView.BufferType.EDITABLE);
        loyerannonce.setText(modif.getStringExtra("Loyer") + " Euros", TextView.BufferType.EDITABLE);
        type.setText(modif.getStringExtra("typebien"));
        iddepot = Long.parseLong(modif.getStringExtra("idannonce"));
        lien1 = modif.getStringExtra("image1");
        lien2 = modif.getStringExtra("image2");
        if (!lien1.equals("")) {
            Picasso.get().load(lien1).into(i1);
        }
        if (lien2.equals("")) {
            i2.setVisibility(View.GONE);
        } else {
            Picasso.get().load(lien2).into(i2);
        }
        if (typeclient == null) {
            String queryconsult = "INSERT INTO depot_consulte(idutilisateur,idAnnonce,type) SELECT idUtilisateur,'" + iddepot + "',2 FROM utilisateur where mail='" + mail + "'";
            String lienex = "https://terl3recette.000webhostapp.com/inscription.php";
            executeRequest(lienex, queryconsult);
        } else if (typeclient.equals("client")) {
            String queryconsult = "INSERT INTO depot_consulte(idutilisateur,idAnnonce,type) SELECT idUtilisateur,'" + iddepot + "',2 FROM utilisateur where mail='" + mail + "'";
            String lienex = "https://terl3recette.000webhostapp.com/inscription.php";
            executeRequest(lienex, queryconsult);
        }


        String lienbdd = "https://terl3recette.000webhostapp.com/connexionutilisateur.php";
        String queryverif = "SELECT idAnnonce FROM depot_consulte d INNER JOIN utilisateur u ON u.idUtilisateur=d.idutilisateur WHERE mail='" + mail + "' AND d.idAnnonce='" + iddepot + "' AND d.type=3";
        int n = Integer.parseInt(Function.getresult(lienbdd, queryverif));
        if (n > 0) {
            bt.setVisibility(View.GONE);
            btsup.setVisibility(View.VISIBLE);
        } else {
            bt.setVisibility(View.VISIBLE);
            btsup.setVisibility(View.GONE);
        }
        if (typeclient != null && typeclient.equals("annonceur")) {
            b.setVisibility(View.GONE);
            bt.setVisibility(View.GONE);
            btsup.setVisibility(View.GONE);
        }

        if (modeinvite != null) {
            bt.setVisibility(View.GONE);
            btsup.setVisibility(View.GONE);
        }
    }


    @SuppressLint({"IntentReset", "QueryPermissionsNeeded"})
    public void composeEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("*/*");
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    public void contacter(View v) {
        String lienbdd1 = "https://terl3recette.000webhostapp.com/getContact.php";
        String lienbdd2 = "https://terl3recette.000webhostapp.com/getDetail.php";
        String query = "SELECT TypeContact FROM Annonce_immo WHERE idAnnonce='" + iddepot + "';";
        String query1 = "SELECT mail,numTel,u.idUtilisateur FROM depot_consulte d INNER JOIN utilisateur u ON u.idUtilisateur=d.idutilisateur WHERE d.idAnnonce='" + iddepot + "' AND d.type=1";

        String contact = Function.getresult(lienbdd1, query);
        String detail = Function.getresult(lienbdd2, query1);
        String email = detail.split("&&&&")[0];
        String num = detail.split("&&&&")[1];
        final String idannonceur = detail.split("&&&&")[2];

        switch (contact) {
            case "Mail":
                String[] tabmail = new String[1];
                tabmail[0] = email;
                composeEmail(tabmail, "Information sur la location");
                break;
            case "Téléphone":
                dialPhoneNumber(num);
                break;
            case "Application":
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Envoyer un message à l'annonceur");

                final EditText input = new EditText(this);

                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("Envoyer", (dialog, which) -> {
                    String message = input.getText().toString();
                    if (!(message.equals(""))) {
                        message = "REF-" + texttitre + " : \n" + message;
                        String query2 = "INSERT INTO Messages(envoyeur,destinataire,contenu)  SELECT idUtilisateur,'" + idannonceur + "','" + message + "' FROM utilisateur WHERE mail='" + mail + "'";
                        String lienbdd3 = "https://terl3recette.000webhostapp.com/inscription.php";
                        executeRequest(lienbdd3, query2);
                        Toast.makeText(getApplicationContext(), "Message envoyée !", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Annuler", (dialog, which) -> dialog.cancel());

                builder.show();
                break;
        }
    }

    public void ajouteralist(View v) {
        String queryajout = "INSERT INTO depot_consulte(idAnnonce,idutilisateur,type) SELECT '" + iddepot + "',idUtilisateur,3 FROM utilisateur WHERE mail='" + mail + "';";
        String bddajout = "https://terl3recette.000webhostapp.com/inscription.php";
        executeRequest(bddajout, queryajout);
        Toast.makeText(getApplicationContext(), "Annonce ajoutée à votre liste", Toast.LENGTH_LONG).show();
        bt.setVisibility(View.GONE);
        btsup.setVisibility(View.VISIBLE);
    }

    public void supprimerdelist(View v) {
        String querysupp = "DELETE FROM depot_consulte  WHERE type=3 AND idAnnonce='" + iddepot + "' AND idutilisateur=(SELECT idUtilisateur FROM utilisateur WHERE mail='" + mail + "')";
        String bddsupp = "https://terl3recette.000webhostapp.com/inscription.php";
        executeRequest(bddsupp, querysupp);
        Toast.makeText(getApplicationContext(), "Annonce supprimée de votre liste", Toast.LENGTH_LONG).show();
        bt.setVisibility(View.VISIBLE);
        btsup.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (typeclient != null && typeclient.equals("client")) {
            Intent intent1 = new Intent(this, Consultation.class);
            intent1.putExtra("mail", mail);
            intent1.putExtra("clienttype", typeclient);
            startActivity(intent1);
        }
        finish();
    }
}