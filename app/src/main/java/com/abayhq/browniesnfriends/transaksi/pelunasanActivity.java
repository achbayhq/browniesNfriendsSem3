package com.abayhq.browniesnfriends.transaksi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.adapter.adapterNota;
import com.abayhq.browniesnfriends.home.DasboardActivity;
import com.abayhq.browniesnfriends.nota.notaActivity;
import com.abayhq.browniesnfriends.respons.barangNotaRespons;
import com.abayhq.browniesnfriends.respons.notaTransaksiRespons;
import com.abayhq.browniesnfriends.settergetter.dataNotaTransaksi;
import com.abayhq.browniesnfriends.settergetter.listBarangNota;
import com.abayhq.browniesnfriends.volley.volleyRequestHandler;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class pelunasanActivity extends AppCompatActivity {

    ImageView btnTransfer, btnTunai, imgStatusBukti;
    TextView txtInfoBayarTf, txtInfoBayarTunai, txtGrandTotal, txtDp, txtKurangBayar;
    CardView bank1, bank2, bank3;
    Button btnSimpanTunai, btnUpload, btnSimpan;
    EditText txtKembalian, txtPembayaran;
    byte[] imgBuktiByteArray;
    String paramStatusBayar, noNota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelunasan);

        btnTransfer = findViewById(R.id.btnTransfer);
        btnTunai = findViewById(R.id.btnTunai);
        imgStatusBukti = findViewById(R.id.imgStatusBukti);
        txtInfoBayarTf = findViewById(R.id.pembayaran_tf);
        txtInfoBayarTunai = findViewById(R.id.pembayaran_tunai);
        txtGrandTotal = findViewById(R.id.txtGrandTotalPelunasan);
        txtDp = findViewById(R.id.txtDp);
        txtKurangBayar = findViewById(R.id.kurang);
        bank1 = findViewById(R.id.cardView14);
        bank2 = findViewById(R.id.cardView15);
        bank3 = findViewById(R.id.cardView16);
        btnSimpanTunai = findViewById(R.id.btnSimpan);
        btnUpload = findViewById(R.id.btnUpload2);
        txtKembalian = findViewById(R.id.txtKembalian);
        txtPembayaran = findViewById(R.id.txtNominalBayar);
        btnSimpan = findViewById(R.id.btnBayar);

        noNota = getIntent().getStringExtra("nota");
        Toast.makeText(pelunasanActivity.this, "nota: "+noNota, Toast.LENGTH_SHORT).show();
        paramStatusBayar = "transfer";
        getData();

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(pelunasanActivity.this)
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
                imgStatusBukti.setVisibility(View.VISIBLE);
                txtInfoBayarTunai.setVisibility(View.GONE);
                btnSimpanTunai.setVisibility(View.GONE);
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
                imgStatusBukti.setVisibility(View.GONE);
                txtInfoBayarTunai.setVisibility(View.VISIBLE);
                btnSimpanTunai.setVisibility(View.VISIBLE);
                txtKembalian.setVisibility(View.VISIBLE);
                paramStatusBayar = "tunai";
                btnTransfer.setImageResource(R.drawable.logo_bank);
                btnTunai.setImageResource(R.drawable.btn_tunai);
            }
        });

        btnSimpanTunai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int kurang = Integer.parseInt(txtKurangBayar.getText().toString().replace(".", ""));
                int kembalian;
                String bayar = txtPembayaran.getText().toString();

                if (!bayar.isEmpty()){
                    int pembayaran = Integer.parseInt(bayar);
                    if (pembayaran >= kurang){
                        kembalian = pembayaran - kurang;
                        txtKembalian.setText(String.valueOf(kembalian));
                    }else{
                        Toast.makeText(pelunasanActivity.this, "Pembayaran tidak boleh kurang dari kurang bayar", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(pelunasanActivity.this, "Isi Nominal Bayar Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kurang = txtKurangBayar.getText().toString();
                String kurangFormat = kurang.replace(".","");
                int minBayar = Integer.parseInt(kurangFormat);

                String bayarStr = txtPembayaran.getText().toString();
                if (!bayarStr.isEmpty()) {
                    int bayar = Integer.parseInt(bayarStr);
                    if (bayar >= minBayar) {

                        if (paramStatusBayar.equals("transfer")) {
                            if (imgBuktiByteArray != null) {
                                pelunasanTransfer();
                            } else {
                                Toast.makeText(pelunasanActivity.this, "Upload Bukti Pembayaran Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                            }
                        }else if(paramStatusBayar.equals("tunai")){
                            pelunasanTunai();
                        }
                    }else{
                        Toast.makeText(pelunasanActivity.this, "Pembayaran harus Lunas", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(pelunasanActivity.this, "Isi Nominal Bayar Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getData(){
        Gson gson = new Gson();
        volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(this);
        volleyRequestHandler.notaTransaksi(noNota, new volleyRequestHandler.ResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                notaTransaksiRespons notaRespons = gson.fromJson(response.toString(), notaTransaksiRespons.class);
                if (notaRespons.getCode() == 200) {
                    dataNotaTransaksi data = notaRespons.getNota_list().get(0);
                    String dibayarkan = data.getDibayarkan();
                    String grandTotal = data.getGrand_total();
                    String kurangBayar = data.getKurang_bayar();

                    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
                    String formattedGrandTotal = numberFormat.format(Integer.parseInt(grandTotal));
                    String formattedDibayarkan = numberFormat.format(Integer.parseInt(dibayarkan));
                    String formattedKurangBayar = numberFormat.format(Integer.parseInt(kurangBayar));

                    txtGrandTotal.setText(formattedGrandTotal);
                    txtDp.setText(formattedDibayarkan);
                    txtKurangBayar.setText(formattedKurangBayar);

                }else if (notaRespons.getCode() == 400){
                    Toast.makeText(pelunasanActivity.this, "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void pelunasanTransfer(){

        String imgBuktiBase64 = Base64.encodeToString(imgBuktiByteArray, Base64.DEFAULT);

        Gson gson = new Gson();
        volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(this);
        volleyRequestHandler.notaTransaksi(noNota, new volleyRequestHandler.ResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                notaTransaksiRespons notaRespons = gson.fromJson(response.toString(), notaTransaksiRespons.class);
                if (notaRespons.getCode() == 200){
                    dataNotaTransaksi data = notaRespons.getNota_list().get(0);
                    String nota = data.getNo_nota();
                    String bayar = data.getDibayarkan();

                    String pembayaran = txtPembayaran.getText().toString();
                    int totalBayar = Integer.parseInt(bayar) + Integer.parseInt(pembayaran);
                    String totalBayarStr = String.valueOf(totalBayar);
                    String kembalian = "0";

                    volleyRequestHandler.pengambilanDP(nota, totalBayarStr, kembalian, imgBuktiBase64, new volleyRequestHandler.ResponseListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try{
                                int code = response.getInt("code");
                                if (code == 200){
                                    Toast.makeText(pelunasanActivity.this, "Pelunasan Berhasil", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(pelunasanActivity.this, DasboardActivity.class);
                                    startActivity(intent);
                                }else if (code == 400){
                                    Toast.makeText(pelunasanActivity.this, "Pelunasan Gagal", Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });

                }else if (notaRespons.getCode() == 400){
                    Toast.makeText(pelunasanActivity.this, "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {

            }
        });


    }

    private void pelunasanTunai(){
        Gson gson = new Gson();
        volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(this);
        volleyRequestHandler.notaTransaksi(noNota, new volleyRequestHandler.ResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                notaTransaksiRespons notaRespons = gson.fromJson(response.toString(), notaTransaksiRespons.class);
                if (notaRespons.getCode() == 200){
                    dataNotaTransaksi data = notaRespons.getNota_list().get(0);
                    String nota = data.getNo_nota();
                    String bayar = data.getDibayarkan();

                    String pembayaran = txtPembayaran.getText().toString();
                    int totalBayar = Integer.parseInt(bayar) + Integer.parseInt(pembayaran);
                    String totalBayarStr = String.valueOf(totalBayar);
                    String imgBuktiBase64 = "";
                    String kembalian = txtKembalian.getText().toString();

                    volleyRequestHandler.pengambilanDP(nota, totalBayarStr, kembalian, imgBuktiBase64, new volleyRequestHandler.ResponseListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try{
                                int code = response.getInt("code");
                                if (code == 200){
                                    Toast.makeText(pelunasanActivity.this, "Pelunasan Berhasil", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(pelunasanActivity.this, DasboardActivity.class);
                                    startActivity(intent);
                                }else if (code == 400){
                                    Toast.makeText(pelunasanActivity.this, "Pelunasan Gagal", Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });

                }else if (notaRespons.getCode() == 400){
                    Toast.makeText(pelunasanActivity.this, "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {

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