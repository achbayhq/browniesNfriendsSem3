package com.abayhq.browniesnfriends.databasesqlite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class dbTransaksiHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "transaksi.db";
    public static final int DB_VER = 1;

    public dbTransaksiHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE list_transaksi(no integer primary key, img text null, id_barang text null, nama_kue text null, harga int null, qty int null, total int null);";
        Log.d("Data", "onCreate: "+ sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS list_transaksi");
            onCreate(db);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void hapusSemuaData() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete("list_transaksi", null, null);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }
}
