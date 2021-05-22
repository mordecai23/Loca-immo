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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static com.example.projetandroid.Function.executeRequest;
import static com.example.projetandroid.Function.getIndex;
import static java.lang.Boolean.FALSE;

public class modifierannonce extends Activity {
    private static final int RESULT_LOAD_IMG = 1;
    ImageView image1, i1, i2;
    String[] liensup;
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
    String mail, query;
    StorageReference sr;
    DatabaseReference dr;
    TextView titre;
    ArrayList<String> lienimage = new ArrayList<>();
    String resjson, json_string;
    JSONObject json;
    JSONArray tabjson;

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

        liensup = new String[2];
        liensup[0] = "";
        liensup[1] = "";

        titre = findViewById(R.id.titredeposer);
        titre.setText("Modifier annonce");

        image1 = findViewById(R.id.photo1);
        tabimage = new Uri[2];
        tablienimage = new ArrayList<>();
        titreannonce = findViewById(R.id.titreannonce);
        description = findViewById(R.id.description);
        dureedispo = findViewById(R.id.dureedispo);
        villeannonce = findViewById(R.id.villeannonce);
        adresseannonce = findViewById(R.id.adresseannonce);
        surfaceannonce = findViewById(R.id.surfaceannonce);
        loyerannonce = findViewById(R.id.loyerannonce);
        b = findViewById(R.id.btndeposer);
        b.setText("Enregistrer modifications");

        i1 = findViewById(R.id.photo1);
        i2 = findViewById(R.id.photo2);

        typebien = findViewById(R.id.spinnerdeposer);
        String[] typeb = new String[]{"Appartement", "Saisonnière", "Classique", "Studio", "Maison", "Villa"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, typeb);
        typebien.setAdapter(adapter);


        typecontacte = findViewById(R.id.contacteannonce);
        String[] typec = new String[]{"Téléphone", "Application", "Mail"};
        ArrayAdapter<String> adaptercontacte = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, typec);
        typecontacte.setAdapter(adaptercontacte);

        Intent modif = getIntent();
        mail = modif.getStringExtra("mail");
        titreannonce.setText(modif.getStringExtra("titre"), TextView.BufferType.EDITABLE);
        description.setText(modif.getStringExtra("desc"), TextView.BufferType.EDITABLE);
        dureedispo.setText(modif.getStringExtra("dureedispo"), TextView.BufferType.EDITABLE);
        villeannonce.setText(modif.getStringExtra("ville"), TextView.BufferType.EDITABLE);
        adresseannonce.setText(modif.getStringExtra("Adresse"), TextView.BufferType.EDITABLE);
        surfaceannonce.setText(modif.getStringExtra("surface"), TextView.BufferType.EDITABLE);
        loyerannonce.setText(modif.getStringExtra("Loyer"), TextView.BufferType.EDITABLE);
        typebien.setSelection(getIndex(typebien, modif.getStringExtra("typebien")));
        typecontacte.setSelection(getIndex(typecontacte, modif.getStringExtra("TypeContacte")));
        iddepot = Long.parseLong(modif.getStringExtra("idannonce"));

        query = "SELECT lien FROM image WHERE idAnnonce='" + modif.getStringExtra("idannonce") + "'";
        String lienbdd = "https://terl3recette.000webhostapp.com/Getimage.php";


        sr = FirebaseStorage.getInstance().getReference("images_annonces");
        dr = FirebaseDatabase.getInstance().getReference("images_annonces");

        titreannonce.addTextChangedListener(deposerwatch);
        description.addTextChangedListener(deposerwatch);
        dureedispo.addTextChangedListener(deposerwatch);
        villeannonce.addTextChangedListener(deposerwatch);
        adresseannonce.addTextChangedListener(deposerwatch);
        surfaceannonce.addTextChangedListener(deposerwatch);
        loyerannonce.addTextChangedListener(deposerwatch);

        json_string = Function.getresult(lienbdd, query);
        if (json_string != null) {
            try {
                json = new JSONObject(json_string);
                tabjson = json.getJSONArray("res");

                for (int i = 0; i < tabjson.length(); i++) {

                    JSONObject j = tabjson.getJSONObject(i);
                    lienimage.add(j.getString("lien"));


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (lienimage.size() == 1) {
            Picasso.get().load(lienimage.get(0)).into(i1);
        }
        if (lienimage.size() == 2) {
            Log.d("llllllllll", lienimage.get(0));
            Picasso.get().load(lienimage.get(0)).into(i1);
            Picasso.get().load(lienimage.get(1)).into(i2);
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
        typecontacte.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                b.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    public void deposerannonce(View v) {
        b.setEnabled(FALSE);
        b.setText("modifications en cours....");
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


        // lien vers le php qui traitera les requetes pour l'insertion dans la BDD
        String lienbdd = "https://terl3recette.000webhostapp.com/inscription.php";
        for (int i = 0; i < liensup.length; i++) {
            if (!liensup[i].equals("")) {
                String q = "DELETE FROM image WHERE lien='" + liensup[i] + "'";
                executeRequest(lienbdd, q);
            }
        }

        //requete d'insertion
        String query = "UPDATE Annonce_immo SET Adresse='" + adresse + "',description='" + desc + "',dureedispo=" + duree + ",Loyer=" + loyer + ",surface=" + surf + ",titre='" + titre + "',typeBien='" + vtypebien + "',TypeContact='" + vtypecontact + "',ville='" + ville + "' WHERE idAnnonce='" + iddepot + "'";
        executeRequest(lienbdd, query);

        uploadimages();

        Toast.makeText(this, "Votre annonce été modifiée", Toast.LENGTH_LONG).show();
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
                if (choix == 1) {
                    if (lienimage.size() >= 1) {
                        liensup[0] = lienimage.get(0);
                    }

                }
                if (choix == 2) {
                    if (lienimage.size() >= 2) {
                        liensup[1] = lienimage.get(1);
                    }
                    b.setEnabled(true);
                }
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