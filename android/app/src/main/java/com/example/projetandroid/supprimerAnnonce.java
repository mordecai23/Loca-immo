package com.example.projetandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.projetandroid.Function.executeRequest;

public class supprimerAnnonce extends Activity {
    String mail;
    String query;
    String lienbdd;
    String json_string;
    ListView l;
    JSONObject json;
    JSONArray tabjson;
    annonceadapter ad;

    ArrayList<annonce> listannonce = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_supprimer_annonce);

        //fond d'écran
        LinearLayout ll = findViewById(R.id.laysupprimer);
        AnimationDrawable animationDrawable = (AnimationDrawable) ll.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();


        l = findViewById(R.id.listannoncesup);
        ad = new annonceadapter(this, R.layout.row_layout);


        Intent iconnection = getIntent();
        mail = iconnection.getStringExtra("mail");
        lienbdd = "https://terl3recette.000webhostapp.com/Getannonce.php";
        query = "SELECT Annonce_immo.idAnnonce,titre,description,typeBien FROM Annonce_immo INNER JOIN depot_consulte  on Annonce_immo.idAnnonce=depot_consulte.idAnnonce WHERE depot_consulte.type=1 AND depot_consulte.idutilisateur=(SELECT idUtilisateur FROM utilisateur WHERE mail='" + mail + "')";

        json_string = Function.getresult(lienbdd, query);

        String idAnnonce, titre, TypeBien, desc;
        if (json_string != null) {
            try {
                Log.d("JSON*****", json_string);
                json = new JSONObject(json_string);
                tabjson = json.getJSONArray("res");

                for (int i = 0; i < tabjson.length(); i++) {

                    JSONObject j = tabjson.getJSONObject(i);
                    idAnnonce = "Identifiant : " + j.getString("idAnnonce");
                    titre = j.getString("titre");
                    TypeBien = "Type : " + j.getString("typeBien");
                    desc = "Description : " + j.getString("description");
                    annonce a = new annonce(idAnnonce, titre, TypeBien, desc);
                    listannonce.add(a);
                    ad.add(a);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        l.setAdapter(ad);

        l.setOnItemClickListener((adapterView, view, i, l) -> {
            annonce annoncesupp = listannonce.get(i);
            final String idannonce = annoncesupp.getIdAnnonce().split(":")[1].trim();
            final String lienbd = "https://terl3recette.000webhostapp.com/inscription.php";
            final String querydel = "DELETE FROM Annonce_immo where idAnnonce='" + idannonce + "'";


            AlertDialog.Builder builder = new AlertDialog.Builder(supprimerAnnonce.this);
            builder.setMessage("Supprimer ou Renforcer");
            builder.setPositiveButton("Supprimer", (dialog, id) -> {
                AlertDialog.Builder buildersupp = new AlertDialog.Builder(supprimerAnnonce.this);
                buildersupp.setMessage("Voulez vous supprimer cette annonce?");
                buildersupp.setPositiveButton("Supprimer", (dialog1, id1) -> {
                    executeRequest(lienbd, querydel);
                    Toast.makeText(getApplicationContext(), "Votre annonce a été supprimée", Toast.LENGTH_LONG).show();
                    Intent i1 = new Intent(supprimerAnnonce.this, supprimerAnnonce.class);
                    i1.putExtra("mail", mail);
                    startActivity(i1);
                    finish();

                });
                buildersupp.setNegativeButton("Annuler", (dialog12, id12) -> Toast.makeText(supprimerAnnonce.this, "Supression annulée", Toast.LENGTH_LONG).show());
                buildersupp.show();


            });
            builder.setNeutralButton("Renforcer", (dialog, id) -> {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(supprimerAnnonce.this);
                builder1.setMessage("Sélectionnez la durée.");


                builder1.setPositiveButton("1 jours", (dialog13, id13) -> {
                    final String lienbd1 = "https://terl3recette.000webhostapp.com/inscription.php";
                    final String dureequery = "UPDATE Annonce_immo SET dateDepot=(SELECT DATE_ADD(CURRENT_TIMESTAMP(),INTERVAL 1 DAY)) where idAnnonce='" + idannonce + "'";
                    executeRequest(lienbd1, dureequery);
                    Toast.makeText(getApplicationContext(), "Votre annonce a été renforcée de 1 jours", Toast.LENGTH_LONG).show();

                });
                builder1.setNeutralButton("5 jours", (dialog14, id14) -> {

                    final String dureequery = "UPDATE Annonce_immo SET dateDepot=(SELECT DATE_ADD(CURRENT_TIMESTAMP(),INTERVAL 5 DAY)) where idAnnonce='" + idannonce + "'";
                    String lienbddd = "https://terl3recette.000webhostapp.com/inscription.php";

                    executeRequest(lienbddd, dureequery);
                    Toast.makeText(getApplicationContext(), "Votre annonce a été renforcée de 5 jours", Toast.LENGTH_LONG).show();

                });
                builder1.setNegativeButton("Annuler", (dialog15, id15) -> Toast.makeText(supprimerAnnonce.this, "Renforcement annulé", Toast.LENGTH_LONG).show());

                builder1.show();


            });

            builder.setNegativeButton("Annuler", (dialog, id) -> Toast.makeText(supprimerAnnonce.this, "Gestion annulée", Toast.LENGTH_LONG).show());
            builder.show();


        });


    }


}