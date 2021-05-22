package com.example.projetandroid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Statistics extends Activity {
    String mail;
    final Thread thread = new Thread(new Runnable() {

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void run() {
            try {
                String querylistannonce = "SELECT a.idAnnonce as 'nom',a.titre as nb FROM depot_consulte d INNER JOIN utilisateur u on u.idUtilisateur=d.idutilisateur INNER JOIN Annonce_immo a on a.idAnnonce=d.idAnnonce WHERE u.mail='" + mail + "' AND d.type=1;";
                String bdd = "https://terl3recette.000webhostapp.com/GetGlobalStats.php";
                String jsonres = Function.getresult(bdd, querylistannonce);
                if (jsonres != null) {
                    try {
                        JSONObject json = new JSONObject(jsonres);
                        JSONArray tabjson = json.getJSONArray("res");

                        for (int i = 0; i < tabjson.length(); i++) {

                            JSONObject j = tabjson.getJSONObject(i);
                            long idannonce = Long.parseLong(j.getString("nom"));
                            String titre = j.getString("nb");
                            String q = "SELECT ville as nom, COUNT(*) as nb FROM depot_consulte d INNER JOIN utilisateur u ON d.idutilisateur=u.idUtilisateur where d.type=2 and d.idAnnonce='" + idannonce + "' GROUP BY ville";
                            String jsonstat = Function.getresult(bdd, q);
                            float res = 0f;
                            if (jsonstat != null) {
                                try {
                                    JSONObject jsonstatob = new JSONObject(jsonstat);
                                    JSONArray tabjsonstat = jsonstatob.getJSONArray("res");
                                    final ArrayList<PieEntry> entry = new ArrayList<>();
                                    for (int k = 0; k < tabjsonstat.length(); k++) {

                                        JSONObject j1 = tabjsonstat.getJSONObject(k);
                                        String nom = j1.getString("nom");
                                        float nb = Float.parseFloat(j1.getString("nb"));
                                        entry.add(new PieEntry(nb, nom, k));
                                        res += nb;
                                    }
                                    final String desc = "Nombre total de consultations : " + Math.round(res);
                                    final TextView valueTV = new TextView(Statistics.this);
                                    valueTV.setTextColor(Color.parseColor("#FFFFFF"));
                                    valueTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                                    valueTV.setGravity(Gravity.CENTER | Gravity.BOTTOM);
                                    valueTV.setTypeface(null, Typeface.BOLD);
                                    valueTV.setLayoutParams(new ViewGroup.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT));
                                    valueTV.setText(titre);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    params.setMargins(0, 60, 0, 0);
                                    valueTV.setLayoutParams(params);
                                    final LinearLayout statscroll = findViewById(R.id.linearstat);

                                    runOnUiThread(() -> {
                                        try {
                                            if (valueTV.getParent() != null) {
                                                ((ViewGroup) valueTV.getParent()).removeView(valueTV);
                                            }
                                            statscroll.addView(valueTV);

                                            PieChart b = new PieChart(Statistics.this);
                                            if (b.getParent() != null) {
                                                ((ViewGroup) b.getParent()).removeView(b);
                                            }
                                            statscroll.addView(createpie(entry, b, desc));

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    });


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

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
    BarChart barcharstat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        //fond d'écran
        ScrollView ll = findViewById(R.id.laystats);
        AnimationDrawable animationDrawable = (AnimationDrawable) ll.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        Intent iconnection = getIntent();
        mail = iconnection.getStringExtra("mail");
        final PieChart pieCharttype = findViewById(R.id.pieCharttype);
        final PieChart pieChartville = findViewById(R.id.pieChartville);

        barcharstat = findViewById(R.id.barchart);
        String queryannoncepie = "SELECT typeBien AS 'nom', COUNT(*) AS nb FROM depot_consulte d INNER JOIN Annonce_immo a ON d.idAnnonce=a.idAnnonce WHERE a.idAnnonce IN (SELECT idAnnonce FROM depot_consulte d1 INNER JOIN utilisateur u on d1.idutilisateur=u.idUtilisateur WHERE u.mail='" + mail + "' AND d1.type=1) AND d.type=2 GROUP BY typeBien";
        String bddpie = "https://terl3recette.000webhostapp.com/GetGlobalStats.php";
        createpiechart(bddpie, queryannoncepie, pieCharttype);
        queryannoncepie = "SELECT a.ville AS 'nom', COUNT(*) AS nb FROM depot_consulte d INNER JOIN Annonce_immo a ON d.idAnnonce=a.idAnnonce WHERE a.idAnnonce IN (SELECT idAnnonce FROM depot_consulte d1 INNER JOIN utilisateur u on d1.idutilisateur=u.idUtilisateur WHERE u.mail='" + mail + "' AND d1.type=1) AND d.type=2 GROUP BY a.ville";
        createpiechart(bddpie, queryannoncepie, pieChartville);

        String querybar = "SELECT ville as nom, COUNT(*) as nb FROM depot_consulte d INNER JOIN utilisateur u ON d.idutilisateur=u.idUtilisateur where d.type=2 and d.idAnnonce IN(SELECT idAnnonce FROM depot_consulte d1 INNER JOIN utilisateur u1 ON u1.idUtilisateur=d1.idutilisateur WHERE d1.type=1 AND u1.mail='" + mail + "') GROUP BY ville;";

        createbarchart(bddpie, querybar, barcharstat);
        thread.start();

    }

    private void adddrawChart(ArrayList<PieEntry> values, PieChart pieChart, String desc) {
        pieChart.setUsePercentValues(true);

        PieDataSet dataSet = new PieDataSet(values, "");
        PieData data = new PieData(dataSet);

        data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);
        Description description = new Description();
        description.setText(desc + "  ");
        description.setTextSize(12f);
        pieChart.setDescription(description);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(45f);
        pieChart.setHoleRadius(45f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.DKGRAY);
        pieChart.animateX(1000);

    }

    private void createpiechart(String lien, String query, PieChart pc) {
        String json_string = Function.getresult(lien, query);
        final ArrayList<PieEntry> val = new ArrayList<>();
        float res = 0f;
        if (json_string != null) {
            try {
                JSONObject json = new JSONObject(json_string);
                JSONArray tabjson = json.getJSONArray("res");

                for (int i = 0; i < tabjson.length(); i++) {

                    JSONObject j = tabjson.getJSONObject(i);
                    String nom = j.getString("nom");
                    float nb = Float.parseFloat(j.getString("nb"));
                    val.add(new PieEntry(nb, nom, i));
                    res += nb;
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        final String desc = "Nombre total de consultations : " + Math.round(res);


        adddrawChart(val, pc, desc);


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("UseCompatLoadingForDrawables")
    public PieChart createpie(ArrayList<PieEntry> entries, PieChart pieChart, String desc) {
        pieChart.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1300));
        pieChart.setBackground(getDrawable(R.drawable.roundlist));
        pieChart.setUsePercentValues(true);

        PieDataSet dataSet = new PieDataSet(entries, "");
        PieData data = new PieData(dataSet);
        Description description = new Description();
        description.setText(desc + "  ");
        description.setTextSize(12f);
        pieChart.setDescription(description);
        data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(45f);
        pieChart.setHoleRadius(45f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.DKGRAY);
        pieChart.animateX(1000);
        return pieChart;

    }

    private void createbarchart(String lien, String query, BarChart barChart) {
        String json_string = Function.getresult(lien, query);
        ArrayList<String> labels = new ArrayList<>();
        final ArrayList<BarEntry> val = new ArrayList<>();


        if (json_string != null) {
            try {
                JSONObject json = new JSONObject(json_string);
                JSONArray tabjson = json.getJSONArray("res");

                for (int i = 0; i < tabjson.length(); i++) {

                    JSONObject j = tabjson.getJSONObject(i);
                    String nom = j.getString("nom");
                    float nb = Float.parseFloat(j.getString("nb"));
                    val.add(new BarEntry(i, nb));
                    labels.add(nom);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        BarDataSet bardataset = new BarDataSet(val, "Cells");
        BarData data = new BarData(bardataset);
        barChart.setData(data);

        Description description = new Description();
        description.setText("");
        barChart.setDescription(description);

        bardataset.setColors(ColorTemplate.MATERIAL_COLORS);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        barChart.animateY(5000);
    }
}