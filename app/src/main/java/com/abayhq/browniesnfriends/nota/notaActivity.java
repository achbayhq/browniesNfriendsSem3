package com.abayhq.browniesnfriends.nota;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.adapter.adapterMenuUtama;
import com.abayhq.browniesnfriends.adapter.adapterNota;
import com.abayhq.browniesnfriends.home.DasboardActivity;
import com.abayhq.browniesnfriends.respons.barangNotaRespons;
import com.abayhq.browniesnfriends.respons.notaTransaksiRespons;
import com.abayhq.browniesnfriends.respons.paketNotaRespons;
import com.abayhq.browniesnfriends.settergetter.dataNotaTransaksi;
import com.abayhq.browniesnfriends.settergetter.listBarangNota;
import com.abayhq.browniesnfriends.settergetter.listPaketNota;
import com.abayhq.browniesnfriends.transaksi.notifSelesaiBeliActivity;
import com.abayhq.browniesnfriends.transaksi.pelunasanActivity;
import com.abayhq.browniesnfriends.volley.volleyRequestHandler;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
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
    String nota, notaTerjadwal, notaProsesAdmin, alasanBatal = "";
    Button btnAmbil, btnBatal, btnKembali;
    private RecyclerView recyclerView;
    private adapterNota adapter;
    private ArrayList<listBarangNota> menuArrayList;
    Boolean pickAlasan = false;
    private MultiFormatWriter multi = new MultiFormatWriter();
    int requestsCompleted;
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

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(notaActivity.this, DasboardActivity.class);
                startActivity(intent);
            }
        });
        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogBatal();
            }
        });

        btnAmbil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = txtStatus.getText().toString();
                if (status.equals("DP")) {
                    Intent intent = new Intent(notaActivity.this, pelunasanActivity.class);
                    intent.putExtra("nota", noNota.getText().toString());
                    startActivity(intent);
                }else{
                    String nt = noNota.getText().toString();
                    volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(notaActivity.this);
                    volleyRequestHandler.pengambilanLunas(nt, new volleyRequestHandler.ResponseListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try{
                                int code = response.getInt("code");
                                if (code == 200){
                                    Intent intent = new Intent(notaActivity.this, DasboardActivity.class);
                                    startActivity(intent);
                                } else if (code == 400){
                                    Toast.makeText(notaActivity.this, "Pengambilan Gagal", Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
                }
            }
        });

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

                        //generate QRcode
                        try {
                            BitMatrix bitMatrix = multi.encode(nota, BarcodeFormat.QR_CODE, 300, 300);
                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                            imgQr.setImageBitmap(bitmap);
                        }catch (WriterException e){
                            e.printStackTrace();
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
                                    }
                                    onVolleyRequestComplete(); //untuk memastikan kode get list barang dan paket dieksekusi terlebih dahulu sebelum setAdapter
                                }else if (barangRes.getCode() == 400){
                                    onVolleyRequestComplete();
                                }
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });

                        volleyRequestHandler.notaPaket(nota, new volleyRequestHandler.ResponseListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                paketNotaRespons barangRes = gson.fromJson(response.toString(), paketNotaRespons.class);
                                if (barangRes.getCode() == 200){
                                    List<listPaketNota> barangList = barangRes.getNota_list();
                                    for (listPaketNota list : barangList){
                                        String qty = list.getQty();
                                        String nama = list.getNama_paket();
                                        String harga = list.getHarga_jual();
                                        String total = list.getTotal();

                                        menuArrayList.add(new listBarangNota(nama, qty, harga, total));
                                    }
                                    onVolleyRequestComplete();
                                }else if (barangRes.getCode() == 400){
                                    onVolleyRequestComplete();
                                }
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });

//                        recyclerView = findViewById(R.id.recyclerNota);
//                        adapter = new adapterNota(menuArrayList,notaActivity.this);
//                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(notaActivity.this);
//                        recyclerView.setLayoutManager(layoutManager);
//                        recyclerView.setAdapter(adapter);

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

                        //generate QRcode
                        try {
                            BitMatrix bitMatrix = multi.encode(nota, BarcodeFormat.QR_CODE, 300, 300);
                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                            imgQr.setImageBitmap(bitmap);
                        }catch (WriterException e){
                            e.printStackTrace();
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
                                    }
                                    onVolleyRequestComplete();
                                }else if (barangRes.getCode() == 400){
                                    onVolleyRequestComplete();
                                }
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });

                        volleyRequestHandler.notaPaket(notaTerjadwal, new volleyRequestHandler.ResponseListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                paketNotaRespons barangRes = gson.fromJson(response.toString(), paketNotaRespons.class);
                                if (barangRes.getCode() == 200){
                                    List<listPaketNota> barangList = barangRes.getNota_list();
                                    for (listPaketNota list : barangList){
                                        String qty = list.getQty();
                                        String nama = list.getNama_paket();
                                        String harga = list.getHarga_jual();
                                        String total = list.getTotal();

                                        menuArrayList.add(new listBarangNota(nama, qty, harga, total));
                                    }
                                    onVolleyRequestComplete();
                                }else if (barangRes.getCode() == 400){
                                    onVolleyRequestComplete();
                                }
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });

//                        Toast.makeText(notaActivity.this, "Dieksekusi kok", Toast.LENGTH_SHORT).show();
//                        recyclerView = findViewById(R.id.recyclerNota);
//                        adapter = new adapterNota(menuArrayList,notaActivity.this);
//                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(notaActivity.this);
//                        recyclerView.setLayoutManager(layoutManager);
//                        recyclerView.setAdapter(adapter);

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

                        //generate QRcode
                        try {
                            BitMatrix bitMatrix = multi.encode(nota, BarcodeFormat.QR_CODE, 300, 300);
                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                            imgQr.setImageBitmap(bitmap);
                        }catch (WriterException e){
                            e.printStackTrace();
                        }

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
                                    }
                                    onVolleyRequestComplete();
                                }else if (barangRes.getCode() == 400){
                                    onVolleyRequestComplete();
                                }
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });

                        volleyRequestHandler.notaPaket(notaProsesAdmin, new volleyRequestHandler.ResponseListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                paketNotaRespons barangRes = gson.fromJson(response.toString(), paketNotaRespons.class);
                                if (barangRes.getCode() == 200){
                                    List<listPaketNota> barangList = barangRes.getNota_list();
                                    for (listPaketNota list : barangList){
                                        String qty = list.getQty();
                                        String nama = list.getNama_paket();
                                        String harga = list.getHarga_jual();
                                        String total = list.getTotal();

                                        menuArrayList.add(new listBarangNota(nama, qty, harga, total));
                                    }
                                    onVolleyRequestComplete();
                                }else if (barangRes.getCode() == 400){
                                    onVolleyRequestComplete();
                                }
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });

//                        recyclerView = findViewById(R.id.recyclerNota);
//                        adapter = new adapterNota(menuArrayList,notaActivity.this);
//                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(notaActivity.this);
//                        recyclerView.setLayoutManager(layoutManager);
//                        recyclerView.setAdapter(adapter);

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

    private void onVolleyRequestComplete() {
        requestsCompleted++;

        if (requestsCompleted == 2) {
            // Kedua permintaan telah selesai, tampilkan pesan dan inisialisasi RecyclerView
            recyclerView = findViewById(R.id.recyclerNota);
            adapter = new adapterNota(menuArrayList, notaActivity.this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(notaActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }
    }

    private void showDialogBatal(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetnota);
        TextView alasan1 = dialog.findViewById(R.id.alasanBatal1);
        TextView alasan2 = dialog.findViewById(R.id.alasanBatal2);
        TextView alasan3 = dialog.findViewById(R.id.alasanBatal3);
        TextView alasan4 = dialog.findViewById(R.id.alasanBatal4);
        EditText alasanLain = dialog.findViewById(R.id.alasanLainnya);
        Button kirim = dialog.findViewById(R.id.btnKirimPembatalan);

        String nota = noNota.getText().toString();
        String isiAlasanLain = alasanLain.getText().toString();

        if (isiAlasanLain.isEmpty()){
            alasan1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pickAlasan){
                        pickAlasan = false;
                        alasanBatal = "";
                        alasan1.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                        alasanLain.setEnabled(true);
                    }else{
                        pickAlasan = true;
                        alasanBatal = alasan1.getText().toString();
                        alasanLain.setEnabled(false);
                        alasan1.setBackgroundColor(Color.parseColor("#F7EEEE"));
                        alasan2.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                        alasan3.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                        alasan4.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                    }
                }
            });
            alasan2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pickAlasan){
                        pickAlasan = false;
                        alasanBatal = "";
                        alasan2.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                        alasanLain.setEnabled(true);
                    }else {
                        pickAlasan = true;
                        alasanBatal = alasan2.getText().toString();
                        alasanLain.setEnabled(false);
                        alasan2.setBackgroundColor(Color.parseColor("#F7EEEE"));
                        alasan3.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                        alasan4.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                        alasan1.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                    }
                }
            });
            alasan3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pickAlasan){
                        pickAlasan = false;
                        alasanBatal = "";
                        alasan3.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                        alasanLain.setEnabled(true);
                    }else {
                        pickAlasan = true;
                        alasanBatal = alasan3.getText().toString();
                        alasanLain.setEnabled(false);
                        alasan3.setBackgroundColor(Color.parseColor("#F7EEEE"));
                        alasan2.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                        alasan1.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                        alasan4.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                    }
                }
            });
            alasan4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pickAlasan){
                        pickAlasan = false;
                        alasanBatal = "";
                        alasan4.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                        alasanLain.setEnabled(true);
                    }else {
                        pickAlasan = true;
                        alasanBatal = alasan4.getText().toString();
                        alasanLain.setEnabled(false);
                        alasan4.setBackgroundColor(Color.parseColor("#F7EEEE"));
                        alasan2.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                        alasan3.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                        alasan1.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                    }
                }
            });
        }else {
            alasanBatal = isiAlasanLain;
        }

        alasanLain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    alasan1.setEnabled(true);
                    alasan2.setEnabled(true);
                    alasan3.setEnabled(true);
                    alasan4.setEnabled(true);
                } else {
                    alasan1.setEnabled(false);
                    alasan2.setEnabled(false);
                    alasan3.setEnabled(false);
                    alasan4.setEnabled(false);
                    alasan1.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                    alasan2.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                    alasan3.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                    alasan4.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                    alasanBatal = s.toString();
                }
            }
        });

        kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!alasanBatal.equals("")){
                    volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(notaActivity.this);
                    volleyRequestHandler.reqPembatalan(nota, alasanBatal, new volleyRequestHandler.ResponseListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int code = response.getInt("code");
                                if (code == 200){
                                    dialog.dismiss();
                                }else if (code == 400){
                                    Toast.makeText(notaActivity.this, "Gagal Request Pembatalan", Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
                }else{
                    Toast.makeText(notaActivity.this, "Pilih Alasan Batal Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }
                }
            });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}