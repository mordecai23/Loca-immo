package com.example.projetandroid;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class annonceadapter2 extends ArrayAdapter {
    public ArrayList<annonce> list = new ArrayList();
    public ArrayList<annonce> listannonce = new ArrayList<>();

    public annonceadapter2(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    public void add(@Nullable annonce object) {
        super.add(object);
        list.add(object);
        listannonce.add(object);
    }

    public int getcount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row;
        row = convertView;
        annonceholder ah;
        if (row == null) {
            LayoutInflater li = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = li.inflate(R.layout.annonce2, parent, false);
            ah = new annonceholder();
            ah.titre = row.findViewById(R.id.titreannonce2);
            ah.typebien = row.findViewById(R.id.typeannonce2);
            ah.loyer = row.findViewById(R.id.Loyerannonce2);
            ah.ville = row.findViewById(R.id.villeannonce2);
            ah.im = row.findViewById(R.id.photoannonce);
            row.setTag(ah);
        } else {
            ah = (annonceholder) row.getTag();
        }
        annonce a = (annonce) this.getItem(position);

        ah.titre.setText(a.getTitre());
        ah.typebien.setText(a.getTypeBien());
        ah.ville.setText(a.getVille());
        ah.loyer.setText(a.getLoyer() + " Euros");
        if (!(a.getLienimage()[0].equals("")))
            Picasso.get().load(a.getLienimage()[0]).into(ah.im);
        else {
            ah.im.setImageResource(R.drawable.camera);
        }

        return row;
    }

    // Filter Class
    public ArrayList<annonce> filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        list.clear();
        ArrayList<annonce> trie = new ArrayList<>();

        if (charText.length() == 0) {
            list.addAll(listannonce);
        } else {
            for (int i = 0; i < listannonce.size(); i++) {
                annonce x = listannonce.get(i);
                if ((x.getTitre() + x.getVille() + x.getDescription() + x.getTypeBien()).toLowerCase(Locale.getDefault()).contains(charText)) {
                    trie.add(x);
                }
            }
            list.addAll(trie);

        }
        //notifyDataSetChanged();
        return list;
    }

    // search Class
    public ArrayList<annonce> search(String loyer, String surface, String ville, String type) {

        ville = ville.toLowerCase(Locale.getDefault());
        list.clear();
        ArrayList<annonce> la = new ArrayList<>();
        la.addAll(listannonce);

        if (ville.length() > 0) {
            ArrayList<annonce> la1 = new ArrayList<>();
            la1.addAll(la);
            la.clear();
            for (int i = 0; i < la1.size(); i++) {
                annonce x = la1.get(i);
                if ((x.getVille()).toLowerCase(Locale.getDefault()).contains(ville)) {
                    list.add(x);
                    la.add(x);
                }
            }

        }
        if (!type.equals("Tous")) {
            list.clear();
            ArrayList<annonce> la1 = new ArrayList<>();
            la1.addAll(la);
            la.clear();
            for (int i = 0; i < la1.size(); i++) {
                annonce x = la1.get(i);
                if ((x.getTypeBien()).equals(type)) {
                    list.add(x);
                    la.add(x);
                }
            }

        }
        if (loyer.length() > 0) {
            list.clear();
            ArrayList<annonce> la1 = new ArrayList<>();
            la1.addAll(la);
            la.clear();
            for (int i = 0; i < la1.size(); i++) {
                annonce x = la1.get(i);
                if ((Integer.parseInt(x.getLoyer()) <= Integer.parseInt(loyer))) {
                    list.add(x);
                    la.add(x);
                }
            }

        }
        if (surface.length() > 0) {
            list.clear();
            ArrayList<annonce> la1 = new ArrayList<>();
            la1.addAll(la);
            la.clear();
            for (int i = 0; i < la1.size(); i++) {
                annonce x = la1.get(i);
                if ((Integer.parseInt(x.getSurface()) <= Integer.parseInt(surface))) {
                    list.add(x);
                    la.add(x);
                }
            }

        }

        list.clear();
        list.addAll(la);
        //notifyDataSetChanged();
        Log.d("list", String.valueOf(list.size()));
        Log.d("listannonce", String.valueOf(listannonce.size()));
        return list;
    }

    static class annonceholder {
        TextView titre, typebien, loyer, ville;
        ImageView im;

    }

}
