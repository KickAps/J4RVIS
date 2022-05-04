package com.example.j4rvis;

import android.app.AlertDialog;
import android.text.Html;
import android.widget.TextView;

import com.loopj.android.http.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpStatus;
import cz.msebera.android.httpclient.entity.StringEntity;

public class HttpUtils {
    private static final String BASE_URL = "http://j4rvis.kickaps.xyz/";
//    private static final String BASE_URL = "http://10.0.2.2:8000/";

    private static final AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, TextView textView) {
        client.get(getAbsoluteUrl(url), params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                textView.setText(new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                textView.setText("ERROR");
            }
        });
    }

    public static void post(String url, String activity_title, AlertDialog alertDialog) throws JSONException, UnsupportedEncodingException {
        JSONObject jsonParams = new JSONObject();
        jsonParams.put("title", activity_title);

        client.post(null, getAbsoluteUrl(url), new StringEntity(jsonParams.toString()), "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String msg;

                if(statusCode == HttpStatus.SC_CREATED) {
                    msg = "Démarrage de l'activité <b>" + activity_title + "</b>";
                } else if(statusCode == HttpStatus.SC_ACCEPTED) {
                    msg = "Activité <b>" + activity_title + "</b> déjà démarrée";
                } else {
                    msg = "error";
                }

                // TODO : proposer de l'arrêter ?
//                try {
//                    JSONObject response = new JSONObject(new String(responseBody));
//                    if(response.has("id")) {
//
//                    } else {
//
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                alertDialog.setMessage(Html.fromHtml(msg));
                alertDialog.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                alertDialog.setMessage(error.toString());
                alertDialog.show();
            }
        });
    }

    public static void put(String url, String activity_title, AlertDialog alertDialog) {
        RequestParams params = new RequestParams();
        try {
            params.put("title", URLEncoder.encode(activity_title, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        client.put(getAbsoluteUrl(url), params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String msg;

                if(statusCode == HttpStatus.SC_OK) {
                    msg = "Arrêt de l'activité <b>" + activity_title + "</b>";
                } else if(statusCode == HttpStatus.SC_ACCEPTED) {
                    msg = "Aucune activité <b>" + activity_title + "</b> démarrée";
                } else {
                    msg = "error";
                }

                alertDialog.setMessage(Html.fromHtml(msg));
                alertDialog.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                alertDialog.setMessage(error.toString());
                alertDialog.show();
            }
        });
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
