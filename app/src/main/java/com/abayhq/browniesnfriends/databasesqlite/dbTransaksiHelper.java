package com.abayhq.browniesnfriends.databasesqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class dbTransaksiHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "transaksi.db";
    public static final int DB_VER = 3;

    public dbTransaksiHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE list_transaksi(no integer primary key, img text null, id_barang text null, nama_kue text null, harga int null, qty int null, total int null);";
        String sql1 = "CREATE TABLE paket_tr(identitas_pkt TEXT PRIMARY KEY, id_paket TEXT,img_paket TEXT,nama_paket TEXT,harga_paket TEXT, qty_paket INTEGER, total_paket INTEGER);";
        String sql2 = "CREATE TABLE detail_paket(identitas_pkt_fk TEXT REFERENCES paket_tr(identitas_pkt) ON DELETE CASCADE, id_barang_paket TEXT, qty_detail_paket INTEGER);";
        Log.d("Data", "onCreate: "+ sql);
        Log.d("Data", "onCreate: "+ sql1);
        Log.d("Data", "onCreate: "+ sql2);
        db.execSQL(sql);
        db.execSQL(sql1);
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS list_transaksi");
            db.execSQL("DROP TABLE IF EXISTS paket_tr");
            db.execSQL("DROP TABLE IF EXISTS detail_paket");
            onCreate(db);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void hapusSemuaData() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete("list_transaksi", null, null);
            db.delete("paket_tr", null, null);
            db.delete("detail_paket", null, null);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public int countIsiTransaksi() {
        int allCount;
        int countDetailbarang = 0;
        int countpaket = 0;

        try (SQLiteDatabase db = this.getReadableDatabase()) {
            String query = "SELECT COUNT(id_barang) FROM list_transaksi";
            try (Cursor cursor = db.rawQuery(query, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    countDetailbarang = cursor.getInt(0);
                }
            }

            String query2 = "SELECT COUNT(identitas_pkt) FROM paket_tr";
            try (Cursor cursor2 = db.rawQuery(query2, null)) {
                if (cursor2 != null && cursor2.moveToFirst()) {
                    countpaket = cursor2.getInt(0);
                }
            }
        }

        allCount = countDetailbarang + countpaket;
        return allCount;
    }

    public int countBarang() {
        int count = 0;

        try (SQLiteDatabase db = this.getReadableDatabase()) {
            String query = "SELECT COUNT(id_barang) FROM list_transaksi";
            try (Cursor cursor = db.rawQuery(query, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    count = cursor.getInt(0);
                }
            }
        }
        return count;
    }public int countPaket() {
        int count = 0;

        try (SQLiteDatabase db = this.getReadableDatabase()) {
            String query = "SELECT COUNT(identitas_pkt) FROM paket_tr";
            try (Cursor cursor = db.rawQuery(query, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    count = cursor.getInt(0);
                }
            }
        }
        return count;
    }
}
