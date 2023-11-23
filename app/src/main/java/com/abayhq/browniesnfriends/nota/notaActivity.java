package com.abayhq.browniesnfriends.nota;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.adapter.adapterMenuUtama;
import com.abayhq.browniesnfriends.adapter.adapterNota;
import com.abayhq.browniesnfriends.respons.barangNotaRespons;
import com.abayhq.browniesnfriends.respons.notaTransaksiRespons;
import com.abayhq.browniesnfriends.settergetter.dataNotaTransaksi;
import com.abayhq.browniesnfriends.settergetter.listBarangNota;
import com.abayhq.browniesnfriends.volley.volleyRequestHandler;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class notaActivity extends AppCompatActivity {

    TextView tglTr, jamTr, namaCust, noNota, tglAmbil, jamAmbil, txtGrandTotal, txtStatus, textKurangbayar, txtKurangbayar;
    ImageView imgQr;
    String nota, notaTerjadwal, notaProsesAdmin;
    Button btnAmbil, btnBatal, btnKembali;
    private RecyclerView recyclerView;
    private adapterNota adapter;
    private ArrayList<listBarangNota> menuArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);

        tglTr = findViewById(R.id.date);
        jamTr = findViewById(R.id.hour);
        namaCust = findViewById(R.id.nama_nota);
        noNota = findViewById(R.id.order_id);
        tglAmbil = findViewById(R.id.tanggalAmbil);
        jamAmbil = findViewById(R.id.jamAmbil);
        txtGrandTotal = findViewById(R.id.txtGrandTotalNota);
        txtStatus = findViewById(R.id.txtStatusNota);
        imgQr = findViewById(R.id.imgQr);
        textKurangbayar = findViewById(R.id.textKurangBayar);
        txtKurangbayar = findViewById(R.id.txtKurangBayarNota);
        btnAmbil = findViewById(R.id.btnAmbilNota);
        btnBatal = findViewById(R.id.btnBatalNota);
        btnKembali = findViewById(R.id.btnKembaliNota);

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH:mm z");
        dateFormat1.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta")); //format WIB nya
        String tanggal = dateFormat.format(currentDate);
        String jam = dateFormat1.format(currentDate);
        tglTr.setText(tanggal);
        jamTr.setText(jam);

        nota = getIntent().getStringExtra("nota"); //nota abis transaksi
        notaTerjadwal = getIntent().getStringExtra("notaTerjadwal");
        notaProsesAdmin = getIntent().getStringExtra("notaProsesAdmin");

//        if (nota == null){
//            nota = notaTerjadwal;
//        }

        if (nota != null) {
            Gson gson = new Gson();
            menuArrayList = new ArrayList<>();
            volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(this);
            volleyRequestHandler.notaTransaksi(nota, new volleyRequestHandler.ResponseListener() {
                @Override
                public void onResponse(JSONObject response) {
                    notaTransaksiRespons notaRespons = gson.fromJson(response.toString(), notaTransaksiRespons.class);
                    if (notaRespons.getCode() == 200){
                        dataNotaTransaksi data = notaRespons.getNota_list().get(0);
                        String nota = data.getNo_nota();
                        String nama = data.getNama();
                        String tgl = data.getTanggal_pengambilan();
                        String jam = data.getJam();
                        String grandTotal = data.getGrand_total();
                        String status = data.getStatus_bayar();
                        String kurangBayar = data.getKurang_bayar();
                        String formattedDate = "";

                        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
                        String formattedGrandTotal = numberFormat.format(Integer.parseInt(grandTotal));
                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        try {
                            Date date = inputFormat.parse(tgl);
                            SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("id"));
                            formattedDate = outputFormat.format(date);
                        }catch (ParseException e){
                            e.printStackTrace();
                        }

                        if (status.equals("DP")){
                            textKurangbayar.setVisibility(View.VISIBLE);
                            txtKurangbayar.setVisibility(View.VISIBLE);
                            txtKurangbayar.setText(kurangBayar);
                        }else if (status.equals("Lunas")){
                            textKurangbayar.setVisibility(View.GONE);
                            txtKurangbayar.setVisibility(View.GONE);
                        }

                        noNota.setText(nota);
                        namaCust.setText(nama);
                        tglAmbil.setText(formattedDate);
                        jamAmbil.setText(jam);
                        txtGrandTotal.setText(formattedGrandTotal);
                        txtStatus.setText(status);

                        volleyRequestHandler.notaBarang(nota, new volleyRequestHandler.ResponseListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                barangNotaRespons barangRes = gson.fromJson(response.toString(), barangNotaRespons.class);
                                if (barangRes.getCode() == 200){
                                    List<listBarangNota> barangList = barangRes.getNota_list();
                                    for (listBarangNota list : barangList){
                                        String qty = list.getQty();
                                        String nama = list.getNama_barang();
                                        String harga = list.getHarga_jual();
                                        String total = list.getTotal();

                                        menuArrayList.add(new listBarangNota(nama, qty, harga, total));

                                        recyclerView = findViewById(R.id.recyclerNota);
                                        adapter = new adapterNota(menuArrayList,notaActivity.this);
                                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(notaActivity.this);
                                        recyclerView.setLayoutManager(layoutManager);
                                        recyclerView.setAdapter(adapter);
                                    }
                                }
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });

                    }else if (notaRespons.getCode() == 400){
                        Toast.makeText(notaActivity.this, "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(String error) {

                }
            });

        }else if (notaTerjadwal != null){
            Gson gson = new Gson();
            menuArrayList = new ArrayList<>();
            volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(this);
            volleyRequestHandler.notaTransaksi(notaTerjadwal, new volleyRequestHandler.ResponseListener() {
                @Override
                public void onResponse(JSONObject response) {
                    notaTransaksiRespons notaRespons = gson.fromJson(response.toString(), notaTransaksiRespons.class);
                    if (notaRespons.getCode() == 200){
                        dataNotaTransaksi data = notaRespons.getNota_list().get(0);
                        String nota = data.getNo_nota();
                        String nama = data.getNama();
                        String tgl = data.getTanggal_pengambilan();
                        String jam = data.getJam();
                        String grandTotal = data.getGrand_total();
                        String status = data.getStatus_bayar();
                        String kurangBayar = data.getKurang_bayar();
                        String statusBatal = data.getStatus_batal();
                        String formattedDate = "";

                        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
                        String formattedGrandTotal = numberFormat.format(Integer.parseInt(grandTotal));
                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        try {
                            Date date = inputFormat.parse(tgl);
                            SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("id"));
                            formattedDate = outputFormat.format(date);
                        }catch (ParseException e){
                            e.printStackTrace();
                        }

                        if (status.equals("DP")){
                            textKurangbayar.setVisibility(View.VISIBLE);
                            txtKurangbayar.setVisibility(View.VISIBLE);
                            txtKurangbayar.setText(kurangBayar);
                        }else if (status.equals("Lunas")){
                            textKurangbayar.setVisibility(View.GONE);
                            txtKurangbayar.setVisibility(View.GONE);
                        }

                        if (statusBatal.equals("1")){
                            btnBatal.setVisibility(View.VISIBLE);
                        }else{
                            btnBatal.setVisibility(View.GONE);
                        }

                        noNota.setText(nota);
                        namaCust.setText(nama);
                        tglAmbil.setText(formattedDate);
                        jamAmbil.setText(jam);
                        txtGrandTotal.setText(formattedGrandTotal);
                        txtStatus.setText(status);

                        volleyRequestHandler.notaBarang(notaTerjadwal, new volleyRequestHandler.ResponseListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                barangNotaRespons barangRes = gson.fromJson(response.toString(), barangNotaRespons.class);
                                if (barangRes.getCode() == 200){
                                    List<listBarangNota> barangList = barangRes.getNota_list();
                                    for (listBarangNota list : barangList){
                                        String qty = list.getQty();
                                        String nama = list.getNama_barang();
                                        String harga = list.getHarga_jual();
                                        String total = list.getTotal();

                                        menuArrayList.add(new listBarangNota(nama, qty, harga, total));

                                        recyclerView = findViewById(R.id.recyclerNota);
                                        adapter = new adapterNota(menuArrayList,notaActivity.this);
                                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(notaActivity.this);
                                        recyclerView.setLayoutManager(layoutManager);
                                        recyclerView.setAdapter(adapter);
                                    }
                                }
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });

                    }else if (notaRespons.getCode() == 400){
                        Toast.makeText(notaActivity.this, "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(String error) {

                }
            });
        }else if (notaProsesAdmin != null){
            Gson gson = new Gson();
            menuArrayList = new ArrayList<>();
            volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(this);
            volleyRequestHandler.notaTransaksi(notaProsesAdmin, new volleyRequestHandler.ResponseListener() {
                @Override
                public void onResponse(JSONObject response) {
                    notaTransaksiRespons notaRespons = gson.fromJson(response.toString(), notaTransaksiRespons.class);
                    if (notaRespons.getCode() == 200){
                        dataNotaTransaksi data = notaRespons.getNota_list().get(0);
                        String nota = data.getNo_nota();
                        String nama = data.getNama();
                        String tgl = data.getTanggal_pengambilan();
                        String jam = data.getJam();
                        String grandTotal = data.getGrand_total();
                        String status = data.getStatus_bayar();
                        String kurangBayar = data.getKurang_bayar();
                        String formattedDate = "";

                        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
                        String formattedGrandTotal = numberFormat.format(Integer.parseInt(grandTotal));
                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        try {
                            Date date = inputFormat.parse(tgl);
                            SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("id"));
                            formattedDate = outputFormat.format(date);
                        }catch (ParseException e){
                            e.printStackTrace();
                        }

                        if (status.equals("DP")){
                            textKurangbayar.setVisibility(View.VISIBLE);
                            txtKurangbayar.setVisibility(View.VISIBLE);
                            txtKurangbayar.setText(kurangBayar);
                        }else if (status.equals("Lunas")){
                            textKurangbayar.setVisibility(View.GONE);
                            txtKurangbayar.setVisibility(View.GONE);
                        }

                        btnKembali.setVisibility(View.GONE);
                        btnBatal.setVisibility(View.GONE);
                        btnAmbil.setVisibility(View.VISIBLE);

                        noNota.setText(nota);
                        namaCust.setText(nama);
                        tglAmbil.setText(formattedDate);
                        jamAmbil.setText(jam);
                        txtGrandTotal.setText(formattedGrandTotal);
                        txtStatus.setText(status);

                        volleyRequestHandler.notaBarang(notaProsesAdmin, new volleyRequestHandler.ResponseListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                barangNotaRespons barangRes = gson.fromJson(response.toString(), barangNotaRespons.class);
                                if (barangRes.getCode() == 200){
                                    List<listBarangNota> barangList = barangRes.getNota_list();
                                    for (listBarangNota list : barangList){
                                        String qty = list.getQty();
                                        String nama = list.getNama_barang();
                                        String harga = list.getHarga_jual();
                                        String total = list.getTotal();

                                        menuArrayList.add(new listBarangNota(nama, qty, harga, total));

                                        recyclerView = findViewById(R.id.recyclerNota);
                                        adapter = new adapterNota(menuArrayList,notaActivity.this);
                                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(notaActivity.this);
                                        recyclerView.setLayoutManager(layoutManager);
                                        recyclerView.setAdapter(adapter);
                                    }
                                }
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });

                    }else if (notaRespons.getCode() == 400){
                        Toast.makeText(notaActivity.this, "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(String error) {

                }
            });
        }
    }
}