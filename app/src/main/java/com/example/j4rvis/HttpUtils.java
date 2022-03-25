package com.example.j4rvis;

import android.widget.TextView;
import com.loopj.android.http.*;
import org.json.JSONException;
import java.io.UnsupportedEncodingException;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class HttpUtils {
    private static final String BASE_URL = "http://j4rvis.kickaps.xyz/"; // localhost : "http://10.0.2.2:8000/";

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

    public static void post(String url, StringEntity entity) throws JSONException, UnsupportedEncodingException {
        client.post(null, getAbsoluteUrl(url), entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {}

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {}
        });
    }

    public static void put(String url, RequestParams params) {
        client.put(getAbsoluteUrl(url), params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {}

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {}
        });
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
