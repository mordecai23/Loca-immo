package com.example.projetandroid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class modifiergeneral extends Activity {
    String mail, resjson, query, lienbdd, json_string;
    ListView l;
    JSONObject json;
    JSONArray tabjson;
    annonceadapter ad;
    int supp;

    ArrayList<annonce> listannonce = new ArrayList<annonce>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_modifiergeneral);
        //fond d'écran
        LinearLayout ll = findViewById(R.id.laymodifiergeneral);
        AnimationDrawable animationDrawable = (AnimationDrawable) ll.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        l = findViewById(R.id.listmodifier);
        ad = new annonceadapter(this, R.layout.row_layout);

        Intent iconnection = getIntent();
        mail = iconnection.getStringExtra("mail");
        lienbdd = "https://terl3recette.000webhostapp.com/Getannonce.php";

        query = "SELECT Annonce_immo.idAnnonce,titre,description,typeBien,dureedispo,Adresse,TypeContact,ville,Loyer,surface FROM Annonce_immo INNER JOIN depot_consulte on Annonce_immo.idAnnonce=depot_consulte.idAnnonce WHERE depot_consulte.type=1 AND depot_consulte.idutilisateur=(SELECT idUtilisateur FROM utilisateur WHERE mail='" + mail + "')";
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
                    a.setDureedispo(j.getString("dureedispo"));
                    a.setAdresse(j.getString("Adresse"));
                    a.setTypeContact(j.getString("TypeContact"));
                    a.setVille(j.getString("ville"));
                    a.setLoyer(j.getString("Loyer"));
                    a.setSurface(j.getString("surface"));
                    listannonce.add(a);
                    ad.add(a);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        l.setAdapter(ad);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                annonce annoncesupp = listannonce.get(i);
                final String idannonce = annoncesupp.getIdAnnonce().split(":")[1].trim();
                final String typebien = annoncesupp.getTypeBien().split(":")[1].trim();
                final String desc = annoncesupp.getDescription().split(":")[1].trim();
                final String t = annoncesupp.getTitre().trim();
                Intent i1 = new Intent(modifiergeneral.this, modifierannonce.class);
                i1.putExtra("mail", mail);
                i1.putExtra("idannonce", idannonce);
                i1.putExtra("typebien", typebien);
                i1.putExtra("desc", desc);
                i1.putExtra("titre", t);
                i1.putExtra("dureedispo", annoncesupp.getDureedispo());
                i1.putExtra("Adresse", annoncesupp.getAdresse());
                i1.putExtra("TypeContact", annoncesupp.getTypeContact());
                i1.putExtra("ville", annoncesupp.getVille());
                i1.putExtra("Loyer", annoncesupp.getLoyer());
                i1.putExtra("surface", annoncesupp.getSurface());
                startActivity(i1);
                finish();

            }
        });
    }
}