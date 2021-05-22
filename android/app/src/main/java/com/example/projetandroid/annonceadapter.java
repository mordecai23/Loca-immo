package com.example.projetandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class annonceadapter extends ArrayAdapter {
    public List list = new ArrayList();

    public annonceadapter(@NonNull Context context, int resource) {
        super(context, resource);
    }


    public void add(@Nullable annonce object) {
        super.add(object);
        list.add(object);
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
            row = li.inflate(R.layout.row_layout, parent, false);
            ah = new annonceholder();
            ah.idann = row.findViewById(R.id.idann);
            ah.titre = row.findViewById(R.id.titreann);
            ah.desc = row.findViewById(R.id.descann);
            ah.typebien = row.findViewById(R.id.typebien);
            row.setTag(ah);
        } else {
            ah = (annonceholder) row.getTag();
        }
        annonce a = (annonce) this.getItem(position);
        ah.idann.setText(a.getIdAnnonce());
        ah.titre.setText(a.getTitre());
        // ah.desc.setText(a.getDescription());
        ah.typebien.setText(a.getTypeBien());

        return row;
    }

    static class annonceholder {
        TextView idann, titre, desc, typebien;
    }
}
