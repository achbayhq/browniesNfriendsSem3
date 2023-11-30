package com.abayhq.browniesnfriends.adapter;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.abayhq.browniesnfriends.volley.volleyRequestHandler;

public class identitasPaketGenerate {
    public static String generateIdPaket(String identitas){
        String prefix = identitas.replaceAll("[^a-zA-Z]", "");
        int angka = Integer.parseInt(identitas.replaceAll("[^0-9]", ""));
        angka++;

        String formattedCount = String.format("%04d", angka);
        String notaNumber = prefix + formattedCount;

        return notaNumber;
    }
}
