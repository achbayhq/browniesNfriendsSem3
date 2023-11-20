package com.abayhq.browniesnfriends.transaksi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.abayhq.browniesnfriends.login.RegisterActivity;
import com.abayhq.browniesnfriends.respons.responsNota;
import com.abayhq.browniesnfriends.settergetter.listTransaksiBarang;
import com.abayhq.browniesnfriends.settergetter.setgetRincianBeli;
import com.abayhq.browniesnfriends.volley.volleyRequestHandler;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PembayaranActivity extends AppCompatActivity {

    public final int GALERY_REQ_CODE = 101;
    TextView txtFileImg, txtGrandTotal;
    EditText txtBayar;
    String tanggalTr, jamTr, telepon, bayarStr;
    Integer grandTotal, bayar;
    String no_nota;
    private ArrayList<listTransaksiBarang> listTrBarang;
    byte[] imgBuktiByteArray;
    ImageView imgStatusBuktiBayar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);

        txtGrandTotal = findViewById(R.id.txtGrandTotalBayar);
        txtBayar = findViewById(R.id.txtBayar);
        imgStatusBuktiBayar = findViewById(R.id.imgBuktiConfirm);

        tanggalTr = getIntent().getStringExtra("tanggal_transaksi");
        jamTr = getIntent().getStringExtra("jam_transaksi");
        SharedPreferences sharedPreferences = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        telepon = sharedPreferences.getString("telepon", "");

        getGrandTotal();
        addListBarang();
        txtGrandTotal.setText(String.valueOf(grandTotal));

        Button uploadImg = findViewById(R.id.btnUpload);
        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pilih gambar bukti pembayaran
                ImagePicker.with(PembayaranActivity.this)
                        .crop()
                        .compress(1024)
                        .galleryOnly()
                        .start();
            }
        });

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
                    imgStatusBuktiBayar.setImageResource(R.drawable.checklist);
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

    public void submitTransaksi(View view) {
        String status_bayar;
        int kembalian, kekurangan;
        int minDP = grandTotal/2;

        bayarStr = txtBayar.getText().toString();
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


                if (imgBuktiByteArray != null) {
                    dbTransaksiHelper dbHelper = new dbTransaksiHelper(this, dbTransaksiHelper.DB_NAME, null, dbTransaksiHelper.DB_VER);
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    Gson gson = new Gson();

                    String imgBuktiBase64 = Base64.encodeToString(imgBuktiByteArray, Base64.DEFAULT);

                    volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(this);
                    volleyRequestHandler.transaksi(imgBuktiBase64, grandTotal, bayar, kembalian, kekurangan, status_bayar, telepon, tanggalTr, jamTr, new volleyRequestHandler.ResponseListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            responsNota getResponNota = gson.fromJson(response.toString(), responsNota.class);
                            if (getResponNota.getCode() == 200) {
                                no_nota = getResponNota.getNota();
                                for (listTransaksiBarang listBarang : listTrBarang) {
                                    String idBrng = listBarang.getId();
                                    int qty = listBarang.getQty();
                                    int total = listBarang.getTotal();

                                    volleyRequestHandler.listBarangTr(no_nota, idBrng, qty, total, new volleyRequestHandler.ResponseListener() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Intent intent = new Intent(PembayaranActivity.this, notifSelesaiBeliActivity.class);
                                            intent.putExtra("grand_total", grandTotal);
                                            intent.putExtra("status", status_bayar);
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onError(String error) {
                                            Toast.makeText(PembayaranActivity.this, "nota : " + no_nota, Toast.LENGTH_SHORT).show();
                                            Toast.makeText(PembayaranActivity.this, error, Toast.LENGTH_SHORT).show();
                                            Log.e("errListTransaksi", error);
                                        }
                                    });
                                }
                                Toast.makeText(PembayaranActivity.this, "Transaksi Dibuat", Toast.LENGTH_SHORT).show();

                            } else if (getResponNota.getCode() == 400) {
                                Toast.makeText(PembayaranActivity.this, "Transaksi Gagal", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(PembayaranActivity.this, error, Toast.LENGTH_SHORT).show();
                            Log.e("err Transaksi", error);
                        }
                    });

                }else{
                    Toast.makeText(PembayaranActivity.this, "Upload Bukti Pembayaran Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(PembayaranActivity.this, "Pembayaran DP harus 50% dari Total Harga", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(PembayaranActivity.this, "Isi Nominal Bayar Terlebih Dahulu", Toast.LENGTH_SHORT).show();
        }
        dbTransaksiHelper dbHapus = new dbTransaksiHelper(this, dbTransaksiHelper.DB_NAME, null, dbTransaksiHelper.DB_VER);
        dbHapus.hapusSemuaData();
    }

    private void getGrandTotal(){
        dbTransaksiHelper dbHelper = new dbTransaksiHelper(this, dbTransaksiHelper.DB_NAME, null, dbTransaksiHelper.DB_VER);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT SUM(total) AS grand_total FROM list_transaksi";
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            grandTotal = cursor.getInt(cursor.getColumnIndexOrThrow("grand_total"));
        }
        cursor.close();
        db.close();
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

}