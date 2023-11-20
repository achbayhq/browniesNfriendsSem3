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
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.adapter.adapterRincianBeli;
import com.abayhq.browniesnfriends.databasesqlite.dbTransaksiHelper;
import com.abayhq.browniesnfriends.settergetter.setgetRincianBeli;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class rincianBeliActivity extends AppCompatActivity {

    Calendar myCalendar;
    EditText txtTgl;
    Spinner jamSpinner;
    TextView txtGrandTotal;
    private RecyclerView recyclerView;
    private adapterRincianBeli adapterRecycle;
    private ArrayList<setgetRincianBeli> beliArrayList;
    Integer grandTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rincian_beli);

        myCalendar = Calendar.getInstance();
        txtGrandTotal = findViewById(R.id.txtGrandTotal);
        txtTgl = findViewById(R.id.tanggalAmbil);
        ImageView btnTgl = findViewById(R.id.imageView13);
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
            new DatePickerDialog(rincianBeliActivity.this, date,
                    myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        btnTgl.setOnClickListener(view-> {
            new DatePickerDialog(rincianBeliActivity.this, date,
                    myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        jamSpinner = findViewById(R.id.jamAmbil);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item);
        adapter.add("Jam Ambil");
        adapter.add("08.00 - 10.00");
        adapter.add("10.00 - 12.00");
        adapter.add("12.00 - 14.00");
        adapter.add("14.00 - 16.00");
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jamSpinner.setAdapter(adapter);

        listBeli();
        getGrandTotal();
        txtGrandTotal.setText(String.valueOf(grandTotal));
        recyclerView = findViewById(R.id.recyclerView);
        adapterRecycle = new adapterRincianBeli(beliArrayList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterRecycle);

        adapterRecycle.setKurangOnClickListener(new adapterRincianBeli.kurangOnClickListener() {
            @Override
            public void kurangOnClick(int position) {
                setgetRincianBeli getData = beliArrayList.get(position);
                dbTransaksiHelper dbHelper = new dbTransaksiHelper(rincianBeliActivity.this, dbTransaksiHelper.DB_NAME, null, dbTransaksiHelper.DB_VER);
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
                        Toast.makeText(rincianBeliActivity.this, "gagal update qty", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(rincianBeliActivity.this, "gagal update list", Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                }
            }
        });
        adapterRecycle.setTambahOnClickListener(new adapterRincianBeli.tambahOnClickListener() {
            @Override
            public void tambahOnClick(int position) {
                setgetRincianBeli getData = beliArrayList.get(position);
                dbTransaksiHelper dbHelper = new dbTransaksiHelper(rincianBeliActivity.this, dbTransaksiHelper.DB_NAME, null, dbTransaksiHelper.DB_VER);
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
                    Toast.makeText(rincianBeliActivity.this, "gagal update qty", Toast.LENGTH_SHORT).show();
                }
                db.close();
            }
        });
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

    private void updateLabel(){
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        txtTgl.setText(dateFormat.format(myCalendar.getTime()));
    }

    public void lanjutPembayaran(View view) {
        String tanggal = txtTgl.getText().toString();
        String jam = jamSpinner.getSelectedItem().toString();
        if (!tanggal.isEmpty() && !jam.equals("Jam Ambil")) {
            Intent intent = new Intent(rincianBeliActivity.this, PembayaranActivity.class);
            intent.putExtra("tanggal_transaksi", tanggal);
            intent.putExtra("jam_transaksi", jam);
            startActivity(intent);
        }else{
            Toast.makeText(rincianBeliActivity.this, "Isi Tanggal dan Jam Pengambilan Terlebih Dahulu", Toast.LENGTH_SHORT).show();
        }
    }

}