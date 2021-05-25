package com.example.projetandroid;

import android.app.Activity;
import android.util.Log;
import android.widget.Spinner;

import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Function extends Activity {
    private static String res;
    private static String resjson;


    public static void executeRequest(String link, String request) {
        final String lienbdd = link;
        final String query = request;

        final Thread thread = new Thread(() -> {
            try {

                URL lien = new URL(lienbdd);
                HttpURLConnection http = (HttpURLConnection) lien.openConnection();
                http.setRequestMethod("POST");
                http.setDoOutput(true);
                OutputStream os = http.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
                String data_string = URLEncoder.encode("query", "UTF-8") + "=" + URLEncoder.encode(query, "UTF-8") + "&";

                bw.write(data_string);
                bw.flush();
                bw.close();
                os.close();
                InputStream is = http.getInputStream();
                is.close();


                http.disconnect();


            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static String getresult(String link, String request) {
        res = "";
        resjson = "";
        final String lienbdd = link;
        final String query = request;
        Thread thread = new Thread(() -> {
            try {

                URL lien = new URL(lienbdd);
                HttpURLConnection http = (HttpURLConnection) lien.openConnection();
                http.setRequestMethod("POST");
                http.setDoOutput(true);
                OutputStream os = http.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
                String data_string = URLEncoder.encode("query", "UTF-8") + "=" + URLEncoder.encode(query, "UTF-8") + "&";

                bw.write(data_string);
                bw.flush();
                bw.close();
                os.close();


                InputStream is = http.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();

                while ((res = br.readLine()) != null) {
                    sb.append(res);
                }
                br.close();
                is.close();

                http.disconnect();

                resjson = sb.toString().trim();


            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return resjson;
    }

    public static int getIndex(Spinner spinner, String myString) {

        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(myString)) {
                index = i;
            }
        }
        return index;
    }


    public static void SendPushNotification(String token, String title, String message, android.content.Context c) {
        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", title);
            notifcationBody.put("body", message);

            notification.put("to", token);
            notification.put("notification", notifcationBody);
            Log.d("jsonnnn", notification.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendnotif(notification, c);

    }

    public static void sendnotif(JSONObject notification, android.content.Context c) {
        String FCM_API = "https://fcm.googleapis.com/fcm/send";
        String serverKey = "key=" + "AAAA3A_GpHA:APA91bFgbaylpX2Y5LrelASWKI_Cn6diDCbEP-iUgZJLHpxA0iBWO01zJqboz8Q0k5uWNMOW2qom2BpUUkUso6jlH-0Y9Uir_qU8RgAmQ3bPJnDsWOz_1a6DyIjvz_eyHe1fKwbWivJf";
        String contentType = "application/json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                response -> Log.d("rrrrrrrrrrrrrrrrrrr : ", "onResponse: " + response.toString()),
                error -> Log.d("mmmmmmmmmmmmm", "errrrrrrrrrrrrr")) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(c).addToRequestQueue(jsonObjectRequest);
    }


}






















   /*   final Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    runOnUiThread(new Runnable() {

            @Override
            public void run() {
                try {


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        */




