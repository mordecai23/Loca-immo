package com.example.projetandroid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Consultation extends Activity implements SearchView.OnQueryTextListener {
    SearchView searchview;
    String mail;
    String query;
    String lienbdd;
    String json_string;
    String typeclient;
    ListView l;
    JSONObject json;
    JSONArray tabjson;
    annonceadapter2 ad;
    boolean visible = false;
    EditText searchville;
    EditText searchloyer;
    EditText searchsurface;
    Spinner searchtype;
    Button btngosearch;
    SwipeRefreshLayout ll;

    ArrayList<annonce> listannonce1 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        //fond d'écran
        ll = findViewById(R.id.swiperefreshconsultation);
        AnimationDrawable animationDrawable = (AnimationDrawable) findViewById(R.id.layconsulter).getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        searchview = findViewById(R.id.searchview);
        searchview.setOnQueryTextListener(this);
        final ProgressBar p = findViewById(R.id.loadan);

        searchville = findViewById(R.id.searchville);
        searchloyer = findViewById(R.id.searchloyer);
        searchsurface = findViewById(R.id.searchsurface);
        searchtype = findViewById(R.id.spinnersearch);
        btngosearch = findViewById(R.id.btngosearch);

        l = findViewById(R.id.listannonconsulte);
        ad = new annonceadapter2(this, R.layout.annonce2);
        final Intent iconnection = getIntent();
        mail = iconnection.getStringExtra("mail");
        typeclient = iconnection.getStringExtra("clienttype");
        lienbdd = "https://terl3recette.000webhostapp.com/Getannonce.php";


        String[] typeb = new String[]{"Tous", "Appartement", "Saisonnière", "Classique", "Studio", "Maison", "Villa"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, typeb);
        searchtype.setAdapter(adapter);

        if (typeclient == null) {
            query = "SELECT * FROM Annonce_immo WHERE 1 ORDER BY dateDepot DESC";
        } else if (typeclient.equals("client")) {
            query = "SELECT * FROM Annonce_immo a INNER JOIN depot_consulte d on d.idAnnonce=a.idAnnonce WHERE d.type=3 AND d.idutilisateur=(SELECT idUtilisateur FROM utilisateur WHERE mail='" + mail + "')";
        } else if (typeclient.equals("annonceur")) {
            query = "SELECT * FROM Annonce_immo a INNER JOIN depot_consulte d on d.idAnnonce=a.idAnnonce WHERE d.type=1 AND d.idutilisateur=(SELECT idUtilisateur FROM utilisateur WHERE mail='" + mail + "')";
        }

        l.setOnItemClickListener((adapterView, view, i, l) -> {
            annonce annoncesupp = listannonce1.get(i);
            final String idannonce = annoncesupp.getIdAnnonce();
            final String typebien = annoncesupp.getTypeBien();
            final String desc = annoncesupp.getDescription();
            final String t = annoncesupp.getTitre().trim();
            Intent i1 = new Intent(Consultation.this, ConsulteAnnonceIndividuelle.class);
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
            i1.putExtra("image1", annoncesupp.getLienimage()[0]);
            i1.putExtra("image2", annoncesupp.getLienimage()[1]);
            i1.putExtra("client", iconnection.getStringExtra("client"));
            i1.putExtra("mode", iconnection.getStringExtra("mode"));
            i1.putExtra("clienttype", typeclient);
            startActivity(i1);
            if (typeclient != null && typeclient.equals("client")) {
                finish();
            }

        });
        final Thread thread = new Thread(() -> {
            try {
                json_string = Function.getresult(lienbdd, query);
                if (json_string != null) {
                    try {
                        json = new JSONObject(json_string);
                        tabjson = json.getJSONArray("res");
                        String idAnnonce, titre, TypeBien, desc;
                        for (int i = 0; i < tabjson.length(); i++) {

                            JSONObject j = tabjson.getJSONObject(i);
                            String lienbdd1 = "https://terl3recette.000webhostapp.com/Getimage.php";
                            String queryimage = "select lien from image where idAnnonce='" + j.getString("idAnnonce") + "'";

                            String res = Function.getresult(lienbdd1, queryimage);
                            JSONObject json1 = new JSONObject(res);
                            JSONArray tabjson1 = json1.getJSONArray("res");


                            idAnnonce = j.getString("idAnnonce");
                            titre = j.getString("titre");
                            TypeBien = j.getString("typeBien");
                            desc = j.getString("description");
                            annonce a = new annonce(idAnnonce, titre, TypeBien, desc);
                            if (tabjson1.length() == 1) {
                                String[] arr = {tabjson1.getJSONObject(0).getString("lien"), ""};
                                a.setLienimage(arr);
                            } else if (tabjson1.length() == 2) {
                                String[] arr = {tabjson1.getJSONObject(0).getString("lien"), tabjson1.getJSONObject(1).getString("lien")};
                                a.setLienimage(arr);
                            } else {
                                String[] arr = {"", ""};
                                a.setLienimage(arr);
                            }
                            a.setDureedispo(j.getString("dureedispo"));
                            a.setAdresse(j.getString("Adresse"));
                            a.setTypeContact(j.getString("TypeContact"));
                            a.setVille(j.getString("ville"));
                            a.setLoyer(j.getString("Loyer"));
                            a.setSurface(j.getString("surface"));
                            listannonce1.add(a);
                            ad.add(a);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                runOnUiThread(() -> {
                    try {
                        p.setVisibility(View.GONE);
                        l.setVisibility(View.VISIBLE);
                        l.setAdapter(ad);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();

        ll.setOnRefreshListener(() -> {
            ArrayList<annonce> ref = new ArrayList<>();

            final Thread thread1 = new Thread(() -> {
                try {
                    json_string = Function.getresult(lienbdd, query);
                    if (json_string != null) {
                        try {
                            JSONObject json = new JSONObject(json_string);
                            JSONArray tabjson = json.getJSONArray("res");
                            String idAnnonce, titre, TypeBien, desc;
                            for (int i = 0; i < tabjson.length(); i++) {

                                JSONObject j = tabjson.getJSONObject(i);
                                String lienbdd1 = "https://terl3recette.000webhostapp.com/Getimage.php";
                                String queryimage = "select lien from image where idAnnonce='" + j.getString("idAnnonce") + "'";

                                String res = Function.getresult(lienbdd1, queryimage);
                                JSONObject json1 = new JSONObject(res);
                                JSONArray tabjson1 = json1.getJSONArray("res");


                                idAnnonce = j.getString("idAnnonce");
                                titre = j.getString("titre");
                                TypeBien = j.getString("typeBien");
                                desc = j.getString("description");
                                annonce a = new annonce(idAnnonce, titre, TypeBien, desc);
                                if (tabjson1.length() == 1) {
                                    String[] arr = {tabjson1.getJSONObject(0).getString("lien"), ""};
                                    a.setLienimage(arr);
                                } else if (tabjson1.length() == 2) {
                                    String[] arr = {tabjson1.getJSONObject(0).getString("lien"), tabjson1.getJSONObject(1).getString("lien")};
                                    a.setLienimage(arr);
                                } else {
                                    String[] arr = {"", ""};
                                    a.setLienimage(arr);
                                }
                                a.setDureedispo(j.getString("dureedispo"));
                                a.setAdresse(j.getString("Adresse"));
                                a.setTypeContact(j.getString("TypeContact"));
                                a.setVille(j.getString("ville"));
                                a.setLoyer(j.getString("Loyer"));
                                a.setSurface(j.getString("surface"));
                                ref.add(a);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            thread1.start();
            try {
                thread1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {
                try {
                    ad.listannonce.clear();
                    ad.list.clear();
                    ad.list.addAll(ref);
                    ad.listannonce.addAll(ref);
                    l.setAdapter(ad);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            listannonce1.clear();
            listannonce1.addAll(ref);
            ll.setRefreshing(false);
        });

    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        hide();
        listannonce1 = ad.filter(newText.trim());
        l.setAdapter(ad);
        return false;
    }

    @SuppressLint("SetTextI18n")
    public void moreoptions(View view) {
        Button b = findViewById(R.id.searchoptions);
        if (visible) {
            visible = false;
            searchloyer.setVisibility(View.GONE);
            searchville.setVisibility(View.GONE);
            searchsurface.setVisibility(View.GONE);
            searchtype.setVisibility(View.GONE);
            btngosearch.setVisibility(View.GONE);
            b.setText("Plus d'options");
        } else {
            visible = true;
            searchloyer.setVisibility(View.VISIBLE);
            searchville.setVisibility(View.VISIBLE);
            searchsurface.setVisibility(View.VISIBLE);
            searchtype.setVisibility(View.VISIBLE);
            btngosearch.setVisibility(View.VISIBLE);
            b.setText("Moins d'options");
        }

    }

    public void searchannonce(View view) {
        String loyer = searchloyer.getText().toString().trim();
        String surface = searchsurface.getText().toString().trim();
        String ville = searchville.getText().toString().trim();
        String type = searchtype.getSelectedItem().toString();
        hide();
        listannonce1 = ad.search(loyer, surface, ville, type);
        Log.d("hddgf", String.valueOf(listannonce1.size()));
        l.setAdapter(ad);

    }

    @SuppressLint("SetTextI18n")
    public void hide() {
        Button b = findViewById(R.id.searchoptions);
        visible = false;
        searchloyer.setVisibility(View.GONE);
        searchville.setVisibility(View.GONE);
        searchsurface.setVisibility(View.GONE);
        searchtype.setVisibility(View.GONE);
        btngosearch.setVisibility(View.GONE);
        b.setText("Plus d'options");
    }
}




