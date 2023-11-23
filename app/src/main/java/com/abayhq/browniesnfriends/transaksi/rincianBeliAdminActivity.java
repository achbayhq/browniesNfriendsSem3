package com.abayhq.browniesnfriends.transaksi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abayhq.browniesnfriends.GlobalVariable;
import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.adapter.adapterRincianBeli;
import com.abayhq.browniesnfriends.databasesqlite.dbTransaksiHelper;
import com.abayhq.browniesnfriends.respons.userLoginRespons;
import com.abayhq.browniesnfriends.settergetter.dataUserLogin;
import com.abayhq.browniesnfriends.settergetter.setgetRincianBeli;
import com.abayhq.browniesnfriends.volley.volleyRequestHandler;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class rincianBeliAdminActivity extends AppCompatActivity {

    Button btnCekAkun, btnAddAkun;
    EditText txtTelepon, txtNama, txtAlamat;
    String telepon;
    Calendar myCalendar;
    EditText txtTgl;
    TextView txtGrandTotal;
    ImageView imgStatus;
    Spinner jamSpinner;
    private RecyclerView recyclerView;
    private adapterRincianBeli adapterRecycle;
    private ArrayList<setgetRincianBeli> beliArrayList;
    Integer grandTotal;
    Boolean statusAkunCust = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rincian_beli_admin);

        txtTelepon = findViewById(R.id.txtTeleponCekAkun);
        txtNama = findViewById(R.id.namaCust);
        txtAlamat = findViewById(R.id.alamatCust);
        txtGrandTotal = findViewById(R.id.txtGrandTotalAdmin);
        imgStatus = findViewById(R.id.imgStatusCekAkun);

        telepon = txtTelepon.getText().toString();

        txtNama.setEnabled(false);
        txtAlamat.setEnabled(false);

        myCalendar = Calendar.getInstance();
        txtTgl = findViewById(R.id.tanggalAmbilAdmin);
        ImageView btnTgl = findViewById(R.id.btnPilihTgl);
        DatePickerDialog.OnDateSetListener date=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR,year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        txtTgl.setOnClickListener(view-> {
            new DatePickerDialog(rincianBeliAdminActivity.this, date,
                    myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        btnTgl.setOnClickListener(view-> {
            new DatePickerDialog(rincianBeliAdminActivity.this, date,
                    myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        jamSpinner = findViewById(R.id.jamAmbilAdmin);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item);
        adapter.add("Jam Ambil");
        adapter.add("08.00 - 10.00");
        adapter.add("10.00 - 12.00");
        adapter.add("12.00 - 14.00");
        adapter.add("14.00 - 16.00");
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jamSpinner.setAdapter(adapter);


        btnCekAkun = findViewById(R.id.btnCekAkun);
        btnCekAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noTelepon = txtTelepon.getText().toString();
                Gson gson = new Gson();
                volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(rincianBeliAdminActivity.this);
                volleyRequestHandler.loginUser(noTelepon, new volleyRequestHandler.ResponseListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        userLoginRespons userRespon = gson.fromJson(response.toString(), userLoginRespons.class);
                        if (userRespon.getCode() == 200) {
                            dataUserLogin loggedUser = userRespon.getUser_list().get(0);
                            String userName = loggedUser.getNama();
                            String alamat = loggedUser.getAlamat();
                            txtNama.setText(userName);
                            txtAlamat.setText(alamat);
                            statusAkunCust = true;
                            imgStatus.setVisibility(View.VISIBLE);
                            btnCekAkun.setVisibility(View.GONE);
                        }else if (userRespon.getCode() == 404) {
                            btnAddAkun.setVisibility(View.VISIBLE);
                            btnCekAkun.setVisibility(View.GONE);
                            txtNama.setEnabled(true);
                            txtAlamat.setEnabled(true);
                            Toast.makeText(rincianBeliAdminActivity.this, "Silahkan Registrasikan User", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(String error) {

                    }
                });
            }
        });

        btnAddAkun = findViewById(R.id.btnAddCust);
        btnAddAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String telepon = txtTelepon.getText().toString();
                String nama = txtNama.getText().toString();
                String alamat = txtAlamat.getText().toString();
                Gson gson = new Gson();
                volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(rincianBeliAdminActivity.this);
                if (!telepon.isEmpty() && !nama.isEmpty() && !alamat.isEmpty()) {
                    volleyRequestHandler.addCust(nama, alamat, telepon, new volleyRequestHandler.ResponseListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int code = response.getInt("code");
                                if (code == 200){
                                    btnAddAkun.setVisibility(View.GONE);
                                    imgStatus.setVisibility(View.VISIBLE);
                                    statusAkunCust = true;
                                } else if (code == 101) {
                                    Toast.makeText(rincianBeliAdminActivity.this, "Akun Telah Terdaftar", Toast.LENGTH_SHORT).show();
                                } else if (code == 205) {
                                    Toast.makeText(rincianBeliAdminActivity.this, "Akun Gagal Terdaftar", Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(String error) {

                        }
                    });
                }else {
                    Toast.makeText(rincianBeliAdminActivity.this, "Lengkapi Data Yang Dibutuhkan", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtTelepon.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_DEL)) {
                    // Jika EditText kosong pada saat tombol "Backspace" ditekan
                    if (txtTelepon.getText().toString().isEmpty()) {
                        btnAddAkun.setVisibility(View.GONE);
                        btnCekAkun.setVisibility(View.VISIBLE);
                        statusAkunCust = false;
                        imgStatus.setVisibility(View.GONE);
                        txtNama.setText("");
                        txtAlamat.setText("");
                        return true;
                    }
                }
                return false;
            }
        });

        listBeli();
        getGrandTotal();
        txtGrandTotal.setText(String.valueOf(grandTotal));
        recyclerView = findViewById(R.id.recyclerViewAdmin);
        adapterRecycle = new adapterRincianBeli(beliArrayList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterRecycle);

        adapterRecycle.setKurangOnClickListener(new adapterRincianBeli.kurangOnClickListener() {
            @Override
            public void kurangOnClick(int position) {
                setgetRincianBeli getData = beliArrayList.get(position);
                dbTransaksiHelper dbHelper = new dbTransaksiHelper(rincianBeliAdminActivity.this, dbTransaksiHelper.DB_NAME, null, dbTransaksiHelper.DB_VER);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                int cekQty = getData.getQty();
                if (cekQty != 0) {
                    ContentValues values = new ContentValues();
                    values.put("qty", getData.getQty());
                    values.put("total", getData.getQty() * Integer.parseInt(getData.getHarga()));

                    String whereClause = "nama_kue = ?";
                    String[] whereArgs = {getData.getNama()};

                    int numRowsUpdated = db.update("list_transaksi", values, whereClause, whereArgs);

                    if (numRowsUpdated > 0) {
                        getGrandTotal();
                        txtGrandTotal.setText(String.valueOf(grandTotal));
                    } else {
                        Toast.makeText(rincianBeliAdminActivity.this, "gagal update qty", Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                }else{
                    String whereClause = "nama_kue = ?";
                    String[] whereArgs = {getData.getNama()};

                    int numRowsDelete = db.delete("list_transaksi", whereClause, whereArgs);

                    if (numRowsDelete > 0) {
                        adapterRecycle.removeItem(position);
                        getGrandTotal();
                        txtGrandTotal.setText(String.valueOf(grandTotal));
                    } else {
                        Toast.makeText(rincianBeliAdminActivity.this, "gagal update list", Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                }
            }
        });
        adapterRecycle.setTambahOnClickListener(new adapterRincianBeli.tambahOnClickListener() {
            @Override
            public void tambahOnClick(int position) {
                setgetRincianBeli getData = beliArrayList.get(position);
                dbTransaksiHelper dbHelper = new dbTransaksiHelper(rincianBeliAdminActivity.this, dbTransaksiHelper.DB_NAME, null, dbTransaksiHelper.DB_VER);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put("qty", getData.getQty());
                values.put("total", getData.getQty()* Integer.parseInt(getData.getHarga()));

                String whereClause = "nama_kue = ?";
                String[] whereArgs = {getData.getNama()};

                int numRowsUpdated = db.update("list_transaksi", values, whereClause, whereArgs);

                if (numRowsUpdated > 0) {
                    getGrandTotal();
                    txtGrandTotal.setText(String.valueOf(grandTotal));
                } else {
                    Toast.makeText(rincianBeliAdminActivity.this, "gagal update qty", Toast.LENGTH_SHORT).show();
                }
                db.close();
            }
        });
    }

    private void updateLabel(){
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        txtTgl.setText(dateFormat.format(myCalendar.getTime()));
    }

    void listBeli(){
        beliArrayList = new ArrayList<>();
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
            String img = cursor.getString(cursor.getColumnIndexOrThrow("img"));
            String nama = cursor.getString(cursor.getColumnIndexOrThrow("nama_kue"));
            Integer harga = cursor.getInt(cursor.getColumnIndexOrThrow("harga"));
            Integer qty = cursor.getInt(cursor.getColumnIndexOrThrow("qty"));
            beliArrayList.add(new setgetRincianBeli(img, nama, String.valueOf(harga), qty));
        }
        cursor.close();
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

    public void lanjutPembayaranAdmin(View view) {
        String tanggal = txtTgl.getText().toString();
        String jam = jamSpinner.getSelectedItem().toString();
        String telepon = txtTelepon.getText().toString();
        if (statusAkunCust) {
            if (!tanggal.isEmpty() && !jam.equals("Jam Ambil")) {
                Intent intent = new Intent(rincianBeliAdminActivity.this, PembayaranAdminActivity.class);
                intent.putExtra("tanggal_transaksi", tanggal);
                intent.putExtra("jam_transaksi", jam);
                intent.putExtra("telepon", telepon);
                startActivity(intent);
            } else {
                Toast.makeText(rincianBeliAdminActivity.this, "Isi Tanggal dan Jam Pengambilan Terlebih Dahulu", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(rincianBeliAdminActivity.this, "Masukkan Akun Customer Terlebih Dahulu", Toast.LENGTH_SHORT).show();
        }
    }
}