package com.abayhq.browniesnfriends.volley;

import android.content.Context;

import com.abayhq.browniesnfriends.GlobalVariable;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class volleyRequestHandler {
    private static final String TAG = volleyRequestHandler.class.getSimpleName();
    private RequestQueue requestQueue;
    private Context context;

    public volleyRequestHandler(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    public interface ResponseListener {
        void onResponse(JSONObject response);
        void onError(String error);
    }

    public void loginUser(final String tlp, final ResponseListener listener) {
        String url = "http://"+ GlobalVariable.IP +"/APIproject/login.php?function=cekTelephoneLogin&telepon=" + tlp; // Ganti dengan URL server PHP Anda.

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("telepon", tlp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Response sukses dari server
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Response error dari server
                        listener.onError(error.getMessage());
                    }
                });
        requestQueue.add(request);
    }

    public void registerUser(final String nama, final String alamat, final String telepon, final String pertanyaan, final String jawaban, final String password, final ResponseListener listener) {
        String url = "http://"+ GlobalVariable.IP +"/APIproject/login.php?function=register";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nama", nama);
            jsonObject.put("alamat", alamat);
            jsonObject.put("telepon", telepon);
            jsonObject.put("pertanyaan", pertanyaan);
            jsonObject.put("jawaban", jawaban);
            jsonObject.put("password", password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Response sukses dari server
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Response error dari server
                        listener.onError(error.getMessage());
                    }
                });

        requestQueue.add(request);
    }

    public void updateProfile(final String photo, final String nama, final String alamat, final String tlpNew, final String tlpOld, final ResponseListener listener) {
        String url = "http://"+ GlobalVariable.IP +"/APIproject/login.php?function=updateProfile";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("photo_profile", photo);
            jsonObject.put("nama", nama);
            jsonObject.put("alamat", alamat);
            jsonObject.put("telepon_new", tlpNew);
            jsonObject.put("telepon_old", tlpOld);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Response sukses dari server
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Response error dari server
                        listener.onError(error.getMessage());
                    }
                });

        requestQueue.add(request);
    }

    public void gantiPassword(final String password, final String tlp, final ResponseListener listener) {
        String url = "http://"+ GlobalVariable.IP +"/APIproject/login.php?function=gantiPassword&password="+ password +"&telepon="+ tlp;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Response sukses dari server
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Response error dari server
                        listener.onError(error.getMessage());
                    }
                });
        requestQueue.add(request);
    }
}
