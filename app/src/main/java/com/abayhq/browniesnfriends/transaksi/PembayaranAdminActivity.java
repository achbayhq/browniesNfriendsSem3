package com.abayhq.browniesnfriends.transaksi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.databasesqlite.dbTransaksiHelper;
import com.abayhq.browniesnfriends.respons.responsNota;
import com.abayhq.browniesnfriends.settergetter.listTransaksiBarang;
import com.abayhq.browniesnfriends.settergetter.listTransaksiDetailPaket;
import com.abayhq.browniesnfriends.settergetter.listTransaksiPaket;
import com.abayhq.browniesnfriends.volley.volleyRequestHandler;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PembayaranAdminActivity extends AppCompatActivity {

    ImageView btnTransfer, btnTunai, imgStatusBukti;
    TextView txtInfoBayarTf, txtInfoBayarTunai, txtGrandTotal;
    CardView bank1, bank2, bank3;
    Button btnSimpanTunai, btnUpload;
    EditText txtKurang, txtKembalian, txtPembayaran;
    private ArrayList<listTransaksiBarang> listTrBarang;
    private ArrayList<listTransaksiDetailPaket> listDetailPaket;
    private ArrayList<listTransaksiPaket> listPaketTr;
    Integer grandTotal;
    String tanggalTr, jamTr, teleponPembeli, teleponPemesan, bayarStr, status_bayar, no_nota, paramStatusBayar;
    byte[] imgBuktiByteArray;
    int kembalian, kekurangan, bayar, isiBarang, isiPaket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran_admin);

        btnTransfer = findViewById(R.id.btnTransfer);
        btnTunai = findViewById(R.id.btnTunai);
        txtInfoBayarTf = findViewById(R.id.pembayaran_tf);
        txtInfoBayarTunai = findViewById(R.id.pembayaran_tunai);
        bank1 = findViewById(R.id.cardView14);
        bank2 = findViewById(R.id.cardView15);
        bank3 = findViewById(R.id.cardView16);
        btnSimpanTunai = findViewById(R.id.button11);
        txtKurang = findViewById(R.id.txtKurangBayar);
        txtKembalian = findViewById(R.id.txtKembalian);
        btnUpload = findViewById(R.id.btnUpload);
        txtGrandTotal = findViewById(R.id.grandTotal);
        imgStatusBukti = findViewById(R.id.imgStatusBukti);
        txtPembayaran = findViewById(R.id.txtPembayaran);

        tanggalTr = getIntent().getStringExtra("tanggal_transaksi");
        jamTr = getIntent().getStringExtra("jam_transaksi");
        teleponPembeli = getIntent().getStringExtra("telepon");
        SharedPreferences sharedPreferences = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        teleponPemesan = sharedPreferences.getString("telepon", "");
        paramStatusBayar = "transfer";

        getGrandTotal();

        dbTransaksiHelper dbHelper = new dbTransaksiHelper(this, dbTransaksiHelper.DB_NAME, null, dbTransaksiHelper.DB_VER);
        isiBarang = dbHelper.countBarang();
        isiPaket = dbHelper.countPaket();

        if (isiBarang > 0) {
            addListBarang();
        }
        if (isiPaket > 0){
            addListPaketTr();
            addDetailListPaket();
        }
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        String formattedGrandTotal = numberFormat.format(grandTotal);
        txtGrandTotal.setText(formattedGrandTotal);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(PembayaranAdminActivity.this)
                        .crop()
                        .compress(1024)
                        .cameraOnly()
                        .start();
            }
        });
        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtInfoBayarTf.setVisibility(View.VISIBLE);
                bank1.setVisibility(View.VISIBLE);
                bank2.setVisibility(View.VISIBLE);
                bank3.setVisibility(View.VISIBLE);
                btnUpload.setVisibility(View.VISIBLE);
                txtInfoBayarTunai.setVisibility(View.GONE);
                btnSimpanTunai.setVisibility(View.GONE);
                txtKurang.setVisibility(View.GONE);
                txtKembalian.setVisibility(View.GONE);
                paramStatusBayar = "transfer";
                btnTransfer.setImageResource(R.drawable.btn_transfer);
                btnTunai.setImageResource(R.drawable.logo_cash);
            }
        });
        btnTunai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtInfoBayarTf.setVisibility(View.GONE);
                bank1.setVisibility(View.GONE);
                bank2.setVisibility(View.GONE);
                bank3.setVisibility(View.GONE);
                btnUpload.setVisibility(View.GONE);
                txtInfoBayarTunai.setVisibility(View.VISIBLE);
                btnSimpanTunai.setVisibility(View.VISIBLE);
                txtKurang.setVisibility(View.VISIBLE);
                txtKembalian.setVisibility(View.VISIBLE);
                paramStatusBayar = "tunai";
                btnTransfer.setImageResource(R.drawable.logo_bank);
                btnTunai.setImageResource(R.drawable.btn_tunai);
            }
        });
    }

    public void cekKurangKembalian(View view) {
        bayarStr = txtPembayaran.getText().toString();
        int minDP = grandTotal/2;
        if (!bayarStr.isEmpty()) {
            bayar = Integer.parseInt(bayarStr);
            if (bayar >= minDP) {
                if (bayar >= grandTotal) {
                    kembalian = bayar - grandTotal;
                    status_bayar = "Lunas";
                    kekurangan = 0;
                    txtKembalian.setText(String.valueOf(kembalian));
                    txtKurang.setText(String.valueOf(kekurangan));
                } else {
                    kekurangan = grandTotal - bayar;
                    status_bayar = "DP";
                    kembalian = 0;
                    txtKembalian.setText(String.valueOf(kembalian));
                    txtKurang.setText(String.valueOf(kekurangan));
                }
            }else {
                Toast.makeText(this, "Pembayaran Minimal 50% dari Total Harga", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Masukkan Nominal Bayar Terlebih Dahulu", Toast.LENGTH_SHORT).show();
        }
    }
    public void submitTransaksiAdmin(View view) {
        int minDP = grandTotal/2;

        bayarStr = txtPembayaran.getText().toString();
        if (!bayarStr.isEmpty()) {
            bayar = Integer.parseInt(bayarStr);
            if (bayar >=minDP) {
                if (bayar >= grandTotal) {
                    kembalian = bayar - grandTotal;
                    status_bayar = "Lunas";
                    kekurangan = 0;
                } else {
                    kekurangan = grandTotal - bayar;
                    status_bayar = "DP";
                    kembalian = 0;
                }

                if (paramStatusBayar.equals("transfer")) {
                    if (imgBuktiByteArray != null) {
                        transaksiTransfer();
                    } else {
                        Toast.makeText(PembayaranAdminActivity.this, "Upload Bukti Pembayaran Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                    }
                }else if(paramStatusBayar.equals("tunai")){
                    transaksiTunai();
                }
            }else{
                Toast.makeText(PembayaranAdminActivity.this, "Pembayaran DP harus 50% dari Total Harga", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(PembayaranAdminActivity.this, "Isi Nominal Bayar Terlebih Dahulu", Toast.LENGTH_SHORT).show();
        }
        dbTransaksiHelper dbHapus = new dbTransaksiHelper(this, dbTransaksiHelper.DB_NAME, null, dbTransaksiHelper.DB_VER);
        dbHapus.hapusSemuaData();
    }

    private void transaksiTransfer(){
        Gson gson = new Gson();
        String imgBuktiBase64 = Base64.encodeToString(imgBuktiByteArray, Base64.DEFAULT);

        volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(this);
        volleyRequestHandler.transaksi(imgBuktiBase64, grandTotal, bayar, kembalian, kekurangan, status_bayar, teleponPembeli, teleponPemesan, tanggalTr, jamTr, new volleyRequestHandler.ResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                responsNota getResponNota = gson.fromJson(response.toString(), responsNota.class);
                if (getResponNota.getCode() == 200) {
                    no_nota = getResponNota.getNota();

                    if (isiBarang > 0){
                        for (listTransaksiBarang listBarang : listTrBarang) {
                            String idBrng = listBarang.getId();
                            int qty = listBarang.getQty();
                            int total = listBarang.getTotal();

                            volleyRequestHandler.listBarangTr(no_nota, idBrng, qty, total, new volleyRequestHandler.ResponseListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Intent intent = new Intent(PembayaranAdminActivity.this, notifSelesaiBeliActivity.class);
                                    intent.putExtra("grand_total", grandTotal);
                                    intent.putExtra("status", status_bayar);
                                    intent.putExtra("nota", no_nota);
                                    startActivity(intent);
                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(PembayaranAdminActivity.this, "nota : " + no_nota, Toast.LENGTH_SHORT).show();
                                    Toast.makeText(PembayaranAdminActivity.this, error, Toast.LENGTH_SHORT).show();
                                    Log.e("errListTransaksi", error);
                                }
                            });
                        }
                    }

                    if (isiPaket > 0){
                        for (listTransaksiPaket listBarang : listPaketTr) {
                            String identitasPkt = listBarang.getIdentitasPaket();
                            String idPaket = listBarang.getIdPaket();
                            int qty = listBarang.getQty();
                            int total = listBarang.getTotal();

                            volleyRequestHandler.listPaketTr(identitasPkt,no_nota, idPaket, qty, total, new volleyRequestHandler.ResponseListener() {
                                @Override
                                public void onResponse(JSONObject response) {

                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(PembayaranAdminActivity.this, "nota : " + no_nota, Toast.LENGTH_SHORT).show();
                                    Toast.makeText(PembayaranAdminActivity.this, error, Toast.LENGTH_SHORT).show();
                                    Log.e("errListTransaksi", error);
                                }
                            });
                        }

                        for (listTransaksiDetailPaket listBarang : listDetailPaket) {
                            String identitasPkt = listBarang.getId_paket();
                            String idBarang = listBarang.getId_barang();
                            int qty = listBarang.getQty();

                            volleyRequestHandler.listDetailPaket(identitasPkt,idBarang, qty, new volleyRequestHandler.ResponseListener() {
                                @Override
                                public void onResponse(JSONObject response) {

                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(PembayaranAdminActivity.this, "nota : " + no_nota, Toast.LENGTH_SHORT).show();
                                    Toast.makeText(PembayaranAdminActivity.this, error, Toast.LENGTH_SHORT).show();
                                    Log.e("errListTransaksi", error);
                                }
                            });
                        }

                        Intent intent = new Intent(PembayaranAdminActivity.this, notifSelesaiBeliActivity.class);
                        intent.putExtra("grand_total", grandTotal);
                        intent.putExtra("status", status_bayar);
                        intent.putExtra("nota", no_nota);
                        startActivity(intent);
                    }
                    Toast.makeText(PembayaranAdminActivity.this, "Transaksi Dibuat", Toast.LENGTH_SHORT).show();

                } else if (getResponNota.getCode() == 400) {
                    Toast.makeText(PembayaranAdminActivity.this, "Transaksi Gagal", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(String error) {
                Toast.makeText(PembayaranAdminActivity.this, error, Toast.LENGTH_SHORT).show();
                Log.e("err Transaksi", error);
            }
        });
    }

    private void transaksiTunai(){
        Gson gson = new Gson();
        String imgBuktiBase64 = "";

        volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(this);
        volleyRequestHandler.transaksi(imgBuktiBase64, grandTotal, bayar, kembalian, kekurangan, status_bayar, teleponPembeli, teleponPemesan, tanggalTr, jamTr, new volleyRequestHandler.ResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                responsNota getResponNota = gson.fromJson(response.toString(), responsNota.class);
                if (getResponNota.getCode() == 200) {
                    no_nota = getResponNota.getNota();

                    if (isiBarang > 0){
                        for (listTransaksiBarang listBarang : listTrBarang) {
                            String idBrng = listBarang.getId();
                            int qty = listBarang.getQty();
                            int total = listBarang.getTotal();

                            volleyRequestHandler.listBarangTr(no_nota, idBrng, qty, total, new volleyRequestHandler.ResponseListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Intent intent = new Intent(PembayaranAdminActivity.this, notifSelesaiBeliActivity.class);
                                    intent.putExtra("grand_total", grandTotal);
                                    intent.putExtra("status", status_bayar);
                                    intent.putExtra("nota", no_nota);
                                    startActivity(intent);
                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(PembayaranAdminActivity.this, "nota : " + no_nota, Toast.LENGTH_SHORT).show();
                                    Toast.makeText(PembayaranAdminActivity.this, error, Toast.LENGTH_SHORT).show();
                                    Log.e("errListTransaksi", error);
                                }
                            });
                        }
                    }

                    if (isiPaket > 0){
                        for (listTransaksiPaket listBarang : listPaketTr) {
                            String identitasPkt = listBarang.getIdentitasPaket();
                            String idPaket = listBarang.getIdPaket();
                            int qty = listBarang.getQty();
                            int total = listBarang.getTotal();

                            volleyRequestHandler.listPaketTr(identitasPkt,no_nota, idPaket, qty, total, new volleyRequestHandler.ResponseListener() {
                                @Override
                                public void onResponse(JSONObject response) {

                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(PembayaranAdminActivity.this, "nota : " + no_nota, Toast.LENGTH_SHORT).show();
                                    Toast.makeText(PembayaranAdminActivity.this, error, Toast.LENGTH_SHORT).show();
                                    Log.e("errListTransaksi", error);
                                }
                            });
                        }

                        for (listTransaksiDetailPaket listBarang : listDetailPaket) {
                            String identitasPkt = listBarang.getId_paket();
                            String idBarang = listBarang.getId_barang();
                            int qty = listBarang.getQty();

                            volleyRequestHandler.listDetailPaket(identitasPkt,idBarang, qty, new volleyRequestHandler.ResponseListener() {
                                @Override
                                public void onResponse(JSONObject response) {

                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(PembayaranAdminActivity.this, "nota : " + no_nota, Toast.LENGTH_SHORT).show();
                                    Toast.makeText(PembayaranAdminActivity.this, error, Toast.LENGTH_SHORT).show();
                                    Log.e("errListTransaksi", error);
                                }
                            });
                        }

                        Intent intent = new Intent(PembayaranAdminActivity.this, notifSelesaiBeliActivity.class);
                        intent.putExtra("grand_total", grandTotal);
                        intent.putExtra("status", status_bayar);
                        intent.putExtra("nota", no_nota);
                        startActivity(intent);
                    }
                    Toast.makeText(PembayaranAdminActivity.this, "Transaksi Dibuat", Toast.LENGTH_SHORT).show();

                } else if (getResponNota.getCode() == 400) {
                    Toast.makeText(PembayaranAdminActivity.this, "Transaksi Gagal", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(String error) {
                Toast.makeText(PembayaranAdminActivity.this, error, Toast.LENGTH_SHORT).show();
                Log.e("err Transaksi", error);
            }
        });
    }

    private void getGrandTotal(){
        int totalHargaBarang = 0;
        int totalHargaPaket = 0;
        dbTransaksiHelper dbHelper = new dbTransaksiHelper(this, dbTransaksiHelper.DB_NAME, null, dbTransaksiHelper.DB_VER);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT COALESCE(SUM(total), 0) AS grand_total FROM list_transaksi";
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            totalHargaBarang = cursor.getInt(cursor.getColumnIndexOrThrow("grand_total"));
        }
        cursor.close();

        String query1 = "SELECT COALESCE(SUM(total_paket), 0) AS grand_total FROM paket_tr";
        Cursor cursor1 = db.rawQuery(query1,null);
        if (cursor1.moveToFirst()) {
            totalHargaPaket = cursor1.getInt(cursor1.getColumnIndexOrThrow("grand_total"));
        }
        cursor1.close();
        db.close();

        grandTotal = totalHargaBarang + totalHargaPaket;
    }

    private void addListBarang(){
        listTrBarang = new ArrayList<>();
        dbTransaksiHelper dbHelper = new dbTransaksiHelper(this, dbTransaksiHelper.DB_NAME, null, dbTransaksiHelper.DB_VER);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                "list_transaksi",
                null,
                null,
                null,
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow("id_barang"));
            Integer qty = cursor.getInt(cursor.getColumnIndexOrThrow("qty"));
            Integer total = cursor.getInt(cursor.getColumnIndexOrThrow("total"));

            listTrBarang.add(new listTransaksiBarang(id,qty,total));
        }
        cursor.close();
    }

    private void addListPaketTr(){
        listPaketTr = new ArrayList<>();
        dbTransaksiHelper dbHelper = new dbTransaksiHelper(this, dbTransaksiHelper.DB_NAME, null, dbTransaksiHelper.DB_VER);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                "paket_tr",
                null,
                null,
                null,
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            String identitasPkt = cursor.getString(cursor.getColumnIndexOrThrow("identitas_pkt"));
            String idPaket = cursor.getString(cursor.getColumnIndexOrThrow("id_paket"));
            int qty = cursor.getInt(cursor.getColumnIndexOrThrow("qty_paket"));
            int total = cursor.getInt(cursor.getColumnIndexOrThrow("total_paket"));

            listPaketTr.add(new listTransaksiPaket(identitasPkt,idPaket,qty,total));
        }
        cursor.close();
    }

    private void addDetailListPaket(){
        listDetailPaket = new ArrayList<>();
        dbTransaksiHelper dbHelper = new dbTransaksiHelper(this, dbTransaksiHelper.DB_NAME, null, dbTransaksiHelper.DB_VER);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                "detail_paket",
                null,
                null,
                null,
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            String idPaket = cursor.getString(cursor.getColumnIndexOrThrow("identitas_pkt_fk"));
            String idBarang = cursor.getString(cursor.getColumnIndexOrThrow("id_barang_paket"));
            int qty = cursor.getInt(cursor.getColumnIndexOrThrow("qty_detail_paket"));

            listDetailPaket.add(new listTransaksiDetailPaket(idPaket,idBarang,qty));
        }
        cursor.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    imgBuktiByteArray = stream.toByteArray();
                    imgStatusBukti.setImageResource(R.drawable.checklist);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(this, "Failed to retrieve image", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }


}