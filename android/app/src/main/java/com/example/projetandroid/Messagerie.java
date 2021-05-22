package com.example.projetandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.projetandroid.Function.executeRequest;

public class Messagerie extends Activity {
    String mail;
    ListView l;
    RadioButton r, e;
    TextView t;
    SwipeRefreshLayout ll;
    int ix = 0;
    List<Map<String, String>> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_messagerie);
        //fond d'écran
        ll = findViewById(R.id.swiperefreshmssg);
        AnimationDrawable animationDrawable = (AnimationDrawable) findViewById(R.id.laymessage).getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        l = findViewById(R.id.listmessage);
        r = findViewById(R.id.Reception);
        e = findViewById(R.id.envoie);
        r.setEnabled(false);
        r.setBackgroundColor(Color.WHITE);
        r.setTextColor(Color.BLACK);
        e.setBackgroundColor(Color.RED);
        e.setTextColor(Color.WHITE);
        Intent iconnection = getIntent();
        mail = iconnection.getStringExtra("mail");
        t = findViewById(R.id.titremessage);
        String query = "SELECT contenu,u.mail FROM Messages m INNER JOIN utilisateur u on m.envoyeur=u.idUtilisateur  WHERE destinataire=(SELECT idUtilisateur FROM utilisateur WHERE mail='" + mail + "') ORDER BY m.date DESC";
        String lienbdd = "https://terl3recette.000webhostapp.com/getMessages.php";
        String jsonres1 = Function.getresult(lienbdd, query);
        if (jsonres1 != null) {
            try {
                JSONObject json = new JSONObject(jsonres1);
                JSONArray tabjson = json.getJSONArray("res");

                for (int i = 0; i < tabjson.length(); i++) {

                    JSONObject j = tabjson.getJSONObject(i);
                    String message = j.getString("contenu");
                    String envoyeur = j.getString("mail");
                    Map<String, String> datum = new HashMap<String, String>(2);
                    datum.put("First Line", envoyeur);
                    datum.put("Second Line", message);
                    data.add(datum);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        SimpleAdapter adapter = new SimpleAdapter(this, data,
                android.R.layout.simple_list_item_2,
                new String[]{"First Line", "Second Line"},
                new int[]{android.R.id.text1, android.R.id.text2});

        l.setAdapter(adapter);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (ix == 0) {
                    final String maildest = data.get(i).get("First Line");

                    String lienbdd2 = "https://terl3recette.000webhostapp.com/getDetail.php";
                    String query1 = "SELECT mail,numTel,idUtilisateur FROM utilisateur where mail='" + maildest + "'";
                    String detail = Function.getresult(lienbdd2, query1);

                    final String idannonceur = detail.split("&&&&")[2];

                    AlertDialog.Builder builder = new AlertDialog.Builder(Messagerie.this);
                    builder.setTitle("Répondre au message");

                    final EditText input = new EditText(Messagerie.this);

                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    builder.setPositiveButton("Envoyer", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String message = input.getText().toString();
                            if (!(message.equals(""))) {
                                String query2 = "INSERT INTO Messages(envoyeur,destinataire,contenu)  SELECT idUtilisateur,'" + idannonceur + "','" + message + "' FROM utilisateur WHERE mail='" + mail + "'";
                                String lienbdd3 = "https://terl3recette.000webhostapp.com/inscription.php";
                                executeRequest(lienbdd3, query2);
                                Toast.makeText(getApplicationContext(), "Message envoyée !", Toast.LENGTH_LONG).show();
                                Intent i1 = new Intent(Messagerie.this, Messagerie.class);
                                i1.putExtra("mail", mail);
                                startActivity(i1);
                                finish();
                            }
                        }
                    });
                    builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();


                }
            }
        });

        ll.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data.clear();
                t.setText("Méssages reçus");
                r.setBackgroundColor(Color.WHITE);
                r.setTextColor(Color.BLACK);
                e.setBackgroundColor(Color.RED);
                e.setTextColor(Color.WHITE);
                r.setEnabled(false);
                e.setEnabled(true);
                String jsonres1 = Function.getresult(lienbdd, query);
                if (jsonres1 != null) {
                    try {
                        JSONObject json = new JSONObject(jsonres1);
                        JSONArray tabjson = json.getJSONArray("res");

                        for (int i = 0; i < tabjson.length(); i++) {

                            JSONObject j = tabjson.getJSONObject(i);
                            String message = j.getString("contenu");
                            String envoyeur = j.getString("mail");
                            Map<String, String> datum = new HashMap<String, String>(2);
                            datum.put("First Line", envoyeur);
                            datum.put("Second Line", message);
                            data.add(datum);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                SimpleAdapter adapter = new SimpleAdapter(Messagerie.this, data,
                        android.R.layout.simple_list_item_2,
                        new String[]{"First Line", "Second Line"},
                        new int[]{android.R.id.text1, android.R.id.text2});

                l.setAdapter(adapter);
                ll.setRefreshing(false);
            }
        });
    }

    public void getreception(View v) {
        t.setText("Méssages reçus");
        r.setBackgroundColor(Color.WHITE);
        r.setTextColor(Color.BLACK);
        e.setBackgroundColor(Color.RED);
        e.setTextColor(Color.WHITE);
        r.setEnabled(false);
        e.setEnabled(true);
        data.clear();
        String query1 = "SELECT contenu,u.mail FROM Messages m INNER JOIN utilisateur u on m.envoyeur=u.idUtilisateur  WHERE destinataire=(SELECT idUtilisateur FROM utilisateur WHERE mail='" + mail + "') ORDER BY m.date DESC";
        String lienbdd1 = "https://terl3recette.000webhostapp.com/getMessages.php";
        String jsonres1 = Function.getresult(lienbdd1, query1);
        if (jsonres1 != null) {
            try {
                JSONObject json = new JSONObject(jsonres1);
                JSONArray tabjson = json.getJSONArray("res");

                for (int i = 0; i < tabjson.length(); i++) {

                    JSONObject j = tabjson.getJSONObject(i);
                    String message = j.getString("contenu");
                    String envoyeur = j.getString("mail");
                    Map<String, String> datum = new HashMap<String, String>(2);
                    datum.put("First Line", envoyeur);
                    datum.put("Second Line", message);
                    data.add(datum);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        SimpleAdapter adapter = new SimpleAdapter(this, data,
                android.R.layout.simple_list_item_2,
                new String[]{"First Line", "Second Line"},
                new int[]{android.R.id.text1, android.R.id.text2});

        l.setAdapter(adapter);
        ix = 0;
    }

    public void getenvoie(View v) {
        t.setText("Méssages envoyés");
        r.setEnabled(true);
        e.setEnabled(false);
        e.setBackgroundColor(Color.WHITE);
        e.setTextColor(Color.BLACK);
        r.setBackgroundColor(Color.RED);
        r.setTextColor(Color.WHITE);
        data.clear();
        String query2 = "SELECT contenu,u.mail FROM Messages m INNER JOIN utilisateur u on m.destinataire=u.idUtilisateur  WHERE envoyeur=(SELECT idUtilisateur FROM utilisateur WHERE mail='" + mail + "') ORDER BY m.date DESC";
        String lienbdd2 = "https://terl3recette.000webhostapp.com/getMessages.php";
        String jsonres1 = Function.getresult(lienbdd2, query2);
        if (jsonres1 != null) {
            try {
                JSONObject json = new JSONObject(jsonres1);
                JSONArray tabjson = json.getJSONArray("res");

                for (int i = 0; i < tabjson.length(); i++) {

                    JSONObject j = tabjson.getJSONObject(i);
                    String message = j.getString("contenu");
                    String envoyeur = j.getString("mail");
                    Map<String, String> datum = new HashMap<String, String>(2);
                    datum.put("First Line", envoyeur);
                    datum.put("Second Line", message);
                    data.add(datum);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        SimpleAdapter adapter = new SimpleAdapter(this, data,
                android.R.layout.simple_list_item_2,
                new String[]{"First Line", "Second Line"},
                new int[]{android.R.id.text1, android.R.id.text2});

        l.setAdapter(adapter);
        ix = 1;
    }


}