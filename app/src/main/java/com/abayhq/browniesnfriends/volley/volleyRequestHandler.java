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
        String url = "http://"+ GlobalVariable.IP +"/APIproject/APImobile.php?function=cekTelephoneLogin&telepon=" + tlp; // Ganti dengan URL server PHP Anda.

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
        String url = "http://"+ GlobalVariable.IP +"/APIproject/APImobile.php?function=register";

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
        String url = "http://"+ GlobalVariable.IP +"/APIproject/APImobile.php?function=updateProfile";

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
        String url = "http://"+ GlobalVariable.IP +"/APIproject/APImobile.php?function=gantiPassword&password="+ password +"&telepon="+ tlp;

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

    public void getBarang( final ResponseListener listener) {
        String url = "http://"+ GlobalVariable.IP +"/APIproject/APImobile.php?function=getBarang";

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

    public void transaksi(final String imgBukti,final Integer grandTotal, final Integer bayar, final Integer kembalian, final Integer kurangBayar, final String statusBayar, final String tlpPembeli, final String tlpPemesan, final String tanggalAmbil, final String jam, final ResponseListener listener) {
        String url = "http://"+ GlobalVariable.IP +"/APIproject/APImobile.php?function=transaksi";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("image_bukti", imgBukti);
            jsonObject.put("grand_total", grandTotal);
            jsonObject.put("dibayarkan", bayar);
            jsonObject.put("kembalian", kembalian);
            jsonObject.put("kurang_bayar", kurangBayar);
            jsonObject.put("status_bayar", statusBayar);
            jsonObject.put("tlpPembeli", tlpPembeli);
            jsonObject.put("tlpPemesan", tlpPemesan);
            jsonObject.put("tanggal_ambil", tanggalAmbil);
            jsonObject.put("jam", jam);
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

    public void listBarangTr(final String no_nota, final String id_barang, final Integer qty, final Integer total, final ResponseListener listener) {
        String url = "http://"+ GlobalVariable.IP +"/APIproject/APImobile.php?function=barangTransaksi";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("no_nota", no_nota);
            jsonObject.put("id_barang", id_barang);
            jsonObject.put("qty", qty);
            jsonObject.put("total", total);
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

    public void pesananTerjadwal(final String tlp, final ResponseListener listener) {
        String url = "http://"+ GlobalVariable.IP +"/APIproject/APImobile.php?function=riwayatPesanTerjadwal&telepon=" + tlp;

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

    public void pesananProses(final String tlp, final ResponseListener listener) {
        String url = "http://"+ GlobalVariable.IP +"/APIproject/APImobile.php?function=riwayatPesanProses&telepon=" + tlp;

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

    public void pesananRiwayat(final String tlp, final ResponseListener listener) {
        String url = "http://"+ GlobalVariable.IP +"/APIproject/APImobile.php?function=riwayatPesanRiwayat&telepon=" + tlp;

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

    public void addCust(final String nama, final String alamat, final String telepon, final ResponseListener listener) {
        String url = "http://"+ GlobalVariable.IP +"/APIproject/APImobile.php?function=addCustomer";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nama", nama);
            jsonObject.put("alamat", alamat);
            jsonObject.put("telepon", telepon);
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

    public void pesananTerjadwalAdmin(final ResponseListener listener) {
        String url = "http://"+ GlobalVariable.IP +"/APIproject/APImobile.php?function=riwayatPesanTerjadwalAdmin";

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

    public void pesananProsesAdmin(final ResponseListener listener) {
        String url = "http://"+ GlobalVariable.IP +"/APIproject/APImobile.php?function=riwayatPesanProsesAdmin";

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

    public void pesananRiwayatAdmin(final ResponseListener listener) {
        String url = "http://"+ GlobalVariable.IP +"/APIproject/APImobile.php?function=riwayatPesanRiwayatAdmin";

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

    public void notaTransaksi(final String nota, final ResponseListener listener) {
        String url = "http://"+ GlobalVariable.IP +"/APIproject/APImobile.php?function=getNotaTransaksi&no_nota=" + nota;

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

    public void notaBarang(final String nota, final ResponseListener listener) {
        String url = "http://"+ GlobalVariable.IP +"/APIproject/APImobile.php?function=getNotaBarang&no_nota=" + nota;

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

    public void reqPembatalan(final String nota, final String alasan, final ResponseListener listener) {
        String url = "http://"+ GlobalVariable.IP +"/APIproject/APImobile.php?function=reqPembatalan";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("no_nota", nota);
            jsonObject.put("alasan_batal", alasan);
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

    public void pengambilanDP(final String nota, final String dibayarkan, final String kembalian, final String buktiBayar, final ResponseListener listener) {
        String url = "http://"+ GlobalVariable.IP +"/APIproject/APImobile.php?function=pengambilanDP";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("no_nota", nota);
            jsonObject.put("dibayarkan", dibayarkan);
            jsonObject.put("kembalian", kembalian);
            jsonObject.put("buktiBayar", buktiBayar);
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

    public void pengambilanLunas(final String nota, final ResponseListener listener) {
        String url = "http://"+ GlobalVariable.IP +"/APIproject/APImobile.php?function=pengambilanLunas&no_nota="+nota;

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
