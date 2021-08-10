package com.sage.cabapp.twilio.chat;

import android.content.Context;
import android.content.res.Resources;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sage.cabapp.R;
import com.sage.cabapp.SageApp;
import com.sage.cabapp.utils.Constant;
import com.sage.cabapp.utils.SharedData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AccessTokenFetcher {

    private Context context;

    public AccessTokenFetcher(Context context) {
        this.context = context;
    }

    public void fetch(final TaskCompletionListener<String, String> listener) {
        JSONObject obj = new JSONObject(getTokenRequestParams());
        String requestUrl = getStringResource(R.string.token_url);

        JsonObjectRequest jsonObjReq =
                new JsonObjectRequest(Request.Method.POST, requestUrl, obj, response -> {
                    String token = null;
                    try {
                        token = response.getString("jwt");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onError("Failed to parse token JSON response");
                    }
                    listener.onSuccess(token);
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        listener.onError("Failed to fetch token");
                    }
                });
        jsonObjReq.setShouldCache(false);
        TokenRequest.getInstance().addToRequestQueue(jsonObjReq);
    }

    private Map<String, String> getTokenRequestParams() {
        Map<String, String> params = new HashMap<>();
        params.put("username", SharedData.getString(context, Constant.USERID));
        return params;
    }

    private String getStringResource(int id) {
        Resources resources = SageApp.get().getResources();
        return resources.getString(id);
    }

}
