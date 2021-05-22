package com.example.projetandroid;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static com.example.projetandroid.Function.executeRequest;
import static java.lang.Boolean.FALSE;

public class deposer extends Activity {
    private static final int RESULT_LOAD_IMG = 1;
    ImageView image1;
    Spinner typebien, typecontacte;
    EditText titreannonce, description, dureedispo, villeannonce, adresseannonce, surfaceannonce, loyerannonce;
    Button b;
    private final TextWatcher deposerwatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String titre = titreannonce.getText().toString().trim();
            String desc = description.getText().toString().trim();
            String duree = dureedispo.getText().toString().trim();
            String ville = villeannonce.getText().toString().trim();
            String adresse = adresseannonce.getText().toString().trim();
            String surf = surfaceannonce.getText().toString().trim();
            String loyer = loyerannonce.getText().toString().trim();


            b.setEnabled(!titre.isEmpty() && !desc.isEmpty() && !duree.isEmpty() && !ville.isEmpty() && !adresse.isEmpty() && !surf.isEmpty() && !loyer.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
    int choix;
    long iddepot;
    int j = 0;
    Uri[] tabimage;
    ArrayList<String> tablienimage;
    String mail;
    StorageReference sr;
    DatabaseReference dr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_deposer);

        //fond d'écran
        ScrollView ll = findViewById(R.id.laydeposer);
        AnimationDrawable animationDrawable = (AnimationDrawable) ll.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();


        sr = FirebaseStorage.getInstance().getReference("images_annonces");
        dr = FirebaseDatabase.getInstance().getReference("images_annonces");

        tabimage = new Uri[2];
        tablienimage = new ArrayList<>();

        Intent accueuil = getIntent();
        mail = accueuil.getStringExtra("mail");

        image1 = findViewById(R.id.photo1);
        titreannonce = findViewById(R.id.titreannonce);
        description = findViewById(R.id.description);
        dureedispo = findViewById(R.id.dureedispo);
        villeannonce = findViewById(R.id.villeannonce);
        adresseannonce = findViewById(R.id.adresseannonce);
        surfaceannonce = findViewById(R.id.surfaceannonce);
        loyerannonce = findViewById(R.id.loyerannonce);
        b = findViewById(R.id.btndeposer);

        titreannonce.addTextChangedListener(deposerwatch);
        description.addTextChangedListener(deposerwatch);
        dureedispo.addTextChangedListener(deposerwatch);
        villeannonce.addTextChangedListener(deposerwatch);
        adresseannonce.addTextChangedListener(deposerwatch);
        surfaceannonce.addTextChangedListener(deposerwatch);
        loyerannonce.addTextChangedListener(deposerwatch);


        typebien = findViewById(R.id.spinnerdeposer);
        String[] typeb = new String[]{"Appartement", "Saisonnière", "Classique", "Studio", "Maison", "Villa"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, typeb);
        typebien.setAdapter(adapter);

        typecontacte = findViewById(R.id.contacteannonce);
        String[] typec = new String[]{"Téléphone", "Application", "Mail"};
        ArrayAdapter<String> adaptercontacte = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, typec);
        typecontacte.setAdapter(adaptercontacte);


    }

    public void deposerannonce(View v) {
        ProgressBar pb = findViewById(R.id.loaddeposer);
        pb.setVisibility(View.VISIBLE);

        b.setEnabled(FALSE);
        b.setText("Dépot en cours....");

        iddepot = System.currentTimeMillis();

        String vtypebien, vtypecontact;
        vtypecontact = typecontacte.getSelectedItem().toString();
        vtypebien = typebien.getSelectedItem().toString();
        String titre = titreannonce.getText().toString().trim();
        String desc = description.getText().toString().trim();
        String duree = dureedispo.getText().toString().trim();
        String ville = villeannonce.getText().toString().trim();
        String adresse = adresseannonce.getText().toString().trim();
        String surf = surfaceannonce.getText().toString().trim();
        String loyer = loyerannonce.getText().toString().trim();

        String lienbdd = "https://terl3recette.000webhostapp.com/inscription.php";
        java.util.Date dt = new java.util.Date();

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String currentTime = sdf.format(dt);
        //requete d'insertion
        String query = "INSERT into Annonce_immo(Adresse,description,dureedispo,idAnnonce,Loyer,surface,titre,typeBien,TypeContact,ville,dateDepot) VALUES" + "('" + adresse + "','" + desc + "'," + duree + ",'" + iddepot + "'," + loyer + "," + surf + ",'" + titre + "','" + vtypebien + "','" + vtypecontact + "','" + ville + "','" + currentTime + "');";
        String query1 = "INSERT into depot_consulte(idAnnonce,idutilisateur) SELECT '" + iddepot + "',idUtilisateur from utilisateur where mail='" + mail + "'";

        Function.executeRequest(lienbdd, query);
        Function.executeRequest(lienbdd, query1);


        final Thread thread1 = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    String queryAlert = "SELECT u.idutilisateur AS nom,token as nb FROM utilisateurtoken u INNER JOIN Critere c ON c.idutilisateur=u.idutilisateur WHERE c.loyer>=" + loyer + " AND c.surface>=" + surf + " AND c.type='" + vtypebien + "' AND c.ville='" + ville + "'";
                    Log.d("qqqq", queryAlert);
                    String bddAlert = "https://terl3recette.000webhostapp.com/GetGlobalStats.php";
                    String json_string = Function.getresult(bddAlert, queryAlert);
                    String title = "Une annonce vous correspond !";
                    String notif = "\"" + titre + "\" a été ajoutée à votre liste de préférence.";
                    Log.d("jjjjjj", json_string);
                    if (json_string != null) {
                        try {
                            JSONObject json = new JSONObject(json_string);
                            JSONArray tabjson = json.getJSONArray("res");

                            for (int i = 0; i < tabjson.length(); i++) {

                                JSONObject j = tabjson.getJSONObject(i);
                                String idutilisateur = j.getString("nom");
                                String token = j.getString("nb");
                                Function.SendPushNotification(token, title, notif, deposer.this);

                                String query2 = "INSERT INTO Messages(envoyeur,destinataire,contenu)  SELECT idUtilisateur,'" + idutilisateur + "','" + notif + "' FROM utilisateur WHERE mail='Alerte Loca-immo'";
                                String lienbdd3 = "https://terl3recette.000webhostapp.com/inscription.php";
                                Log.d("eeeeeeeeeeeeeeeeeeeeeee", query2);
                                executeRequest(lienbdd3, query2);

                                String queryajout = "INSERT INTO depot_consulte(idAnnonce,idutilisateur,type) SELECT '" + iddepot + "',idUtilisateur,3 FROM utilisateur WHERE idUtilisateur=" + idutilisateur + ";";
                                String bddajout = "https://terl3recette.000webhostapp.com/inscription.php";
                                executeRequest(bddajout, queryajout);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread1.start();
        final Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    uploadimages();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Toast.makeText(this, "Votre annonce été déposée", Toast.LENGTH_LONG).show();
        finish();


    }

    //méthode lors de l'appui pour ajouter une image
    public void ajoutphoto(View v) {
        image1 = findViewById(v.getId());
        if (v.getId() == R.id.photo1) {
            choix = 1;
        }
        if (v.getId() == R.id.photo2) {
            choix = 2;
        }
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
    }

    //permet de choisir image dans la gallerie photo
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                if (choix == 1) {
                    tabimage[0] = imageUri;
                }
                if (choix == 2) {
                    tabimage[1] = imageUri;
                }
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                image1.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Une erreur s'est produite.", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "Vous avez sélectionné aucune image.", Toast.LENGTH_LONG).show();
        }
    }

    //uploader les images sur  firebase
    public void uploadimages() {
        for (int i = 0; i < tabimage.length; i++) {
            if (tabimage[i] != null) {
                final StorageReference imageref = sr.child(iddepot + "_" + i + "." + getextension(tabimage[i]));


                imageref.putFile(tabimage[i])
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Uri downloadUrl = uri;
                                        tablienimage.add(downloadUrl.toString());
                                        final String lieni = downloadUrl.toString();

                                        String lienbdd = "https://terl3recette.000webhostapp.com/inscription.php";
                                        String queryimage = "INSERT INTO image(idAnnonce,lien) VALUES ('" + iddepot + "','" + lieni + "');";
                                        Function.executeRequest(lienbdd, queryimage);

                                    }

                                });
                            }
                        });


            }

        }
    }


    public String getextension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }


}


