package com.example.projetandroid;

import android.annotation.SuppressLint;
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
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.projetandroid.Function.executeRequest;
import static com.example.projetandroid.Function.getIndex;
import static java.lang.Boolean.FALSE;

public class AlertActivity extends Activity {
    String mail;
    Spinner typebien;
    EditText ville, loyer, surface;
    Button b;
    private final TextWatcher alertwatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String sville = ville.getText().toString().trim();
            String sloyer = loyer.getText().toString().trim();
            String ssurface = surface.getText().toString().trim();

            b.setEnabled(!sville.isEmpty() && !sloyer.isEmpty() && !ssurface.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        //fond d'écran
        ScrollView ll = findViewById(R.id.layalert);
        AnimationDrawable animationDrawable = (AnimationDrawable) ll.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        Intent iconnection = getIntent();
        mail = iconnection.getStringExtra("mail");

        ville = findViewById(R.id.villealert);
        loyer = findViewById(R.id.alertloyer);
        surface = findViewById(R.id.alertsurface);
        b = findViewById(R.id.btnSendalert);

        typebien = findViewById(R.id.logementalert);
        String[] typeb = new String[]{"Appartement", "Saisonnière", "Classique", "Studio", "Maison", "Villa"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, typeb);
        typebien.setAdapter(adapter);

        String querygetalert = "SELECT loyer,surface,type,ville FROM Critere WHERE idutilisateur=(SELECT idUtilisateur FROM utilisateur WHERE mail='" + mail + "')";
        String bddalert = "https://terl3recette.000webhostapp.com/GetAlert.php";
        String jsonalert = Function.getresult(bddalert, querygetalert);
        if (jsonalert != null) {
            try {
                JSONObject json = new JSONObject(jsonalert);
                JSONArray tabjson = json.getJSONArray("res");

                for (int i = 0; i < tabjson.length(); i++) {

                    JSONObject j = tabjson.getJSONObject(i);
                    ville.setText(j.getString("ville"));
                    loyer.setText(j.getString("loyer"));
                    surface.setText(j.getString("surface"));
                    typebien.setSelection(getIndex(typebien, j.getString("type")));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        typebien.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                b.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        ville.addTextChangedListener(alertwatch);
        loyer.addTextChangedListener(alertwatch);
        surface.addTextChangedListener(alertwatch);

    }

    @SuppressLint("SetTextI18n")
    public void addalert(View v) {
        b.setEnabled(FALSE);
        b.setText("En cours....");
        String vville = ville.getText().toString().trim();
        String vloyer = loyer.getText().toString().trim();
        String vsurface = surface.getText().toString().trim();
        String vtype = typebien.getSelectedItem().toString();

        String deletequery = "DELETE FROM Critere WHERE idutilisateur=(SELECT idUtilisateur FROM utilisateur WHERE mail='" + mail + "')";
        String lienbdd = "https://terl3recette.000webhostapp.com/inscription.php";
        executeRequest(lienbdd, deletequery);

        String insertquery = "INSERT INTO Critere(idutilisateur,loyer,surface,type,ville)  SELECT idUtilisateur," + vloyer + "," + vsurface + ",'" + vtype + "','" + vville + "' FROM utilisateur WHERE mail='" + mail + "';";
        Function.executeRequest(lienbdd, insertquery);


        Toast.makeText(getApplicationContext(), "Changement de critères effectué", Toast.LENGTH_LONG).show();
        finish();

    }

    public void supprimeralert(View view) {
        String deletequery = "DELETE FROM Critere WHERE idutilisateur=(SELECT idUtilisateur FROM utilisateur WHERE mail='" + mail + "')";
        String lienbdd = "https://terl3recette.000webhostapp.com/inscription.php";
        executeRequest(lienbdd, deletequery);
        Toast.makeText(getApplicationContext(), "Vos alertes ont étaient désactivées !", Toast.LENGTH_LONG).show();
        finish();
    }
}