package com.abayhq.browniesnfriends.transaksi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.home.DasboardActivity;

public class notifSelesaiBeliActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif_selesai_beli);
    }

    public void keListPesanan(View view) {
        Intent intent = new Intent(notifSelesaiBeliActivity.this, DasboardActivity.class);
        startActivity(intent);
    }
}