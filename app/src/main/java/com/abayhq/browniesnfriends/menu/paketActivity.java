package com.abayhq.browniesnfriends.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.abayhq.browniesnfriends.GlobalVariable;
import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.adapter.identitasPaketGenerate;
import com.abayhq.browniesnfriends.databasesqlite.dbTransaksiHelper;
import com.abayhq.browniesnfriends.respons.dataBarangRespons;
import com.abayhq.browniesnfriends.respons.dataPaketRespons;
import com.abayhq.browniesnfriends.settergetter.dataBarang;
import com.abayhq.browniesnfriends.settergetter.dataPaket;
import com.abayhq.browniesnfriends.settergetter.setgetMenu;
import com.abayhq.browniesnfriends.volley.volleyRequestHandler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class paketActivity extends AppCompatActivity {

    TextView txtNamaPaket, txtDesPaket, txtHargaPaket, txtEdit;
    ImageView imgPaket;
    View garisBawahEdit;
    EditText etQty;
    Button btnPilihKue, btnTambah, btnKurang, btnPesan;
    ListView listKue, listKuenya;
    int isiPaket, qtyPerKue;
    String idPaket, image, nama, harga, URLimage, identitasPaket;
    ArrayList<String> selectedItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paket);
        btnPilihKue = findViewById(R.id.btnPilihKue);
        btnTambah = findViewById(R.id.btnTambahPaket);
        btnKurang = findViewById(R.id.btnKurangPaket);
        btnPesan = findViewById(R.id.btnPesanPaket);
        txtNamaPaket = findViewById(R.id.txtNamaPaket);
        txtDesPaket = findViewById(R.id.txtDeskripsiPaket);
        txtHargaPaket = findViewById(R.id.txtHargaPaket);
        etQty = findViewById(R.id.txtQtyPaket);
        imgPaket = findViewById(R.id.imgPaket);
        listKue = findViewById(R.id.listKue);
        txtEdit = findViewById(R.id.txtEditIsiPaket);
        garisBawahEdit = findViewById(R.id.view18);

        idPaket = getIntent().getStringExtra("id");
        etQty.setText("0");

        getPaket();
        btnPilihKue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPaket();
            }
        });

        txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPaket();
            }
        });

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(etQty.getText().toString());
                qty += 1;
                etQty.setText(String.valueOf(qty));
            }
        });
        btnKurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(etQty.getText().toString());
                if (qty > 0){
                    qty -= 1;
                    etQty.setText(String.valueOf(qty));
                }
            }
        });

        btnPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(paketActivity.this);

                volleyRequestHandler.getIdentitasPkt(new volleyRequestHandler.ResponseListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String idPkt = "";
                        try {
                            idPkt = response.getString("identitas");
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                        dbTransaksiHelper dbHelper = new dbTransaksiHelper(paketActivity.this, dbTransaksiHelper.DB_NAME, null, dbTransaksiHelper.DB_VER);
//                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        int isiPaket = dbHelper.countPaket();
                        if (isiPaket > 0) {
                            String idnya = "";

                            try (SQLiteDatabase dbId = dbHelper.getReadableDatabase();){
                                String query = "SELECT identitas_pkt FROM paket_tr ORDER BY identitas_pkt DESC LIMIT 1";
                                Cursor cursor = dbId.rawQuery(query, null);

                                if (cursor.moveToFirst()) {
                                    int columnIndex = cursor.getColumnIndex("identitas_pkt");
                                    if (columnIndex != -1) {
                                        idnya = cursor.getString(columnIndex);
                                        identitasPaket = identitasPaketGenerate.generateIdPaket(idnya);
                                    }else{
                                        Toast.makeText(paketActivity.this, "tidak ditemukan identitasPkt dalam paket_tr", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                cursor.close();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else{
                            identitasPaket = identitasPaketGenerate.generateIdPaket(idPkt);
                        }

                        String qtyStr = etQty.getText().toString();
                        int qty = Integer.parseInt(qtyStr);

                        ContentValues values = new ContentValues();
                        values.put("identitas_pkt", identitasPaket);
                        values.put("id_paket", idPaket);
                        values.put("img_paket", URLimage);
                        values.put("nama_paket", nama);
                        values.put("harga_paket", harga);
                        values.put("qty_paket", qty);
                        values.put("total_paket", Integer.parseInt(harga) * qty);

                        try (SQLiteDatabase db = dbHelper.getWritableDatabase()){
                            long newRowId = db.insert("paket_tr", null, values);

                            if (newRowId != -1) {

                                for (String selectedItem : selectedItems){
                                    String barang = selectedItem;
                                    Gson gson = new Gson();
                                    volleyRequestHandler.getInfoBarang(barang, new volleyRequestHandler.ResponseListener() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            dataBarangRespons dataRespon = gson.fromJson(response.toString(), dataBarangRespons.class);
                                            if (dataRespon.getCode() == 200){
                                                dataBarang dataBarang = dataRespon.getBarang_list().get(0);
                                                String idBarang = dataBarang.getId_barang();

                                                ContentValues valuesDetail = new ContentValues();
                                                valuesDetail.put("identitas_pkt_fk", identitasPaket);
                                                valuesDetail.put("id_barang_paket", idBarang);
                                                valuesDetail.put("qty_detail_paket", qtyPerKue);

                                                try (SQLiteDatabase dbdt = dbHelper.getWritableDatabase()){
                                                    long newRowId1 = dbdt.insert("detail_paket", null, valuesDetail);

                                                    if (newRowId1 == -1) {
                                                        Toast.makeText(paketActivity.this, "Gagal Memasukkan Detail Data", Toast.LENGTH_SHORT).show();
                                                    }
                                                }catch (SQLException e){
                                                    e.printStackTrace();
                                                }

                                            }else if(dataRespon.getCode() == 404){
                                                Toast.makeText(paketActivity.this, "Data Barang Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onError(String error) {

                                        }
                                    });
                                }
                                Intent resultIntent = new Intent();
                                setResult(Activity.RESULT_OK, resultIntent);
                                finish();
                            } else {
                                Toast.makeText(paketActivity.this, "Gagal Memasukkan Data", Toast.LENGTH_SHORT).show();
                            }
                        }catch (SQLException e){
                            e.printStackTrace();
                            Toast.makeText(paketActivity.this, "err PaketTR: "+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }
        });
    }

    private void getPaket() {
        Gson gson = new Gson();

        volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(paketActivity.this);
        volleyRequestHandler.getDetailPaket(idPaket, new volleyRequestHandler.ResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                dataPaketRespons dataRespon = gson.fromJson(response.toString(), dataPaketRespons.class);
                if (dataRespon.getCode() == 200) {
                    dataPaket dataPaket = dataRespon.getPaket_list().get(0);
                    image = dataPaket.getGambar_paket();
                    nama = dataPaket.getNama_paket();
                    harga = dataPaket.getHarga_jual();
                    String deskripsi = dataPaket.getDeskripsi();
                    URLimage = "http://" + GlobalVariable.IP + "/APIproject/image/" + image;

                    int macam = Integer.parseInt(dataPaket.getMacam());
                    int jmlKue = Integer.parseInt(dataPaket.getJumlah_kue());
                    isiPaket = macam;
                    qtyPerKue = jmlKue/macam;

                    if (image != null){
                        Glide.with(paketActivity.this).load(URLimage).centerCrop().transition(DrawableTransitionOptions.withCrossFade()).into(imgPaket);
                    }
                    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
                    String formattedHarga = numberFormat.format(Integer.valueOf(harga));

                    txtNamaPaket.setText(nama);
                    txtDesPaket.setText(deskripsi);
                    txtHargaPaket.setText(formattedHarga);

                }else if (dataRespon.getCode() == 404) {
                    Toast.makeText(paketActivity.this, "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void showDialogPaket(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_paket);
        TextView txtNamaPaketnya = dialog.findViewById(R.id.txtNamaPaketnya);
        TextView txtIsiPaket = dialog.findViewById(R.id.txtPilihJenisKue);
        listKuenya = dialog.findViewById(R.id.listPilihKue);
        Button backPaket = dialog.findViewById(R.id.btnBackPilihPaket);
        Button konfirmIsiPaket = dialog.findViewById(R.id.btnKonfirmPaket);

        txtNamaPaketnya.setText(txtNamaPaket.getText().toString());
        txtIsiPaket.setText("(Pilih Jenis "+isiPaket+" kue)");

        getListPaket();

        backPaket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        konfirmIsiPaket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedItems.size() == isiPaket){
                    dialog.dismiss();
                    if (selectedItems != null){
                        btnPilihKue.setVisibility(View.GONE);
                        listKue.setVisibility(View.VISIBLE);
                        txtEdit.setVisibility(View.VISIBLE);
                        garisBawahEdit.setVisibility(View.VISIBLE);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(paketActivity.this, android.R.layout.simple_list_item_1, selectedItems);
                        listKue.setAdapter(adapter);
                    }
                }else{
                    Toast.makeText(paketActivity.this, "Isi Paket Belum Penuh, Tambahkan Item", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void getListPaket(){
        Gson gson = new Gson();
        selectedItems = new ArrayList<>();
        volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(this);
        volleyRequestHandler.getBarangPaket(new volleyRequestHandler.ResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                dataBarangRespons dataRespon = gson.fromJson(response.toString(), dataBarangRespons.class);
                if (dataRespon.getCode() == 200) {
                    List<dataBarang> barangList = dataRespon.getBarang_list();

                    ArrayList<String> barangPaketList = new ArrayList<>();
                    for (dataBarang dataBarang : barangList) {
                        String nama = dataBarang.getNama_barang();
                        barangPaketList.add(nama);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(paketActivity.this, android.R.layout.simple_list_item_1, barangPaketList);
                    listKuenya.setAdapter(adapter);

                    listKuenya.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String selectedItem = barangPaketList.get(position);
                            if (selectedItems.size() < isiPaket) {
                                if (selectedItems.contains(selectedItem)) {
                                    selectedItems.remove(selectedItem);
                                    view.setBackgroundColor(Color.WHITE);
                                } else {
                                    selectedItems.add(selectedItem);
                                    view.setBackgroundColor(Color.parseColor("#F7EEEE"));
                                }
                            }else {
                                if (selectedItems.contains(selectedItem)) {
                                    selectedItems.remove(selectedItem);
                                    view.setBackgroundColor(Color.WHITE);
                                }else{
                                    Toast.makeText(paketActivity.this, "Isi paket sudah mencapai batas", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

                }else if (dataRespon.getCode() == 404) {
                    Toast.makeText(paketActivity.this, "Data Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onError(String error) {
                Toast.makeText(paketActivity.this, error , Toast.LENGTH_SHORT).show();
                Log.e("err Menu Fragment", error);
            }
        });
    }

    }
