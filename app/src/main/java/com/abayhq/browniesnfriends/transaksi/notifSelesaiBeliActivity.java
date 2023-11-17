package com.abayhq.browniesnfriends.transaksi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.home.DasboardActivity;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class notifSelesaiBeliActivity extends AppCompatActivity {

    TextView tanggalNow, jamNow, txtGrandTotal, txtStatus;
    Integer grandTotal;
    String status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif_selesai_beli);

        tanggalNow = findViewById(R.id.txtTanggalNow);
        jamNow = findViewById(R.id.txtJamNow);
        txtGrandTotal = findViewById(R.id.txtGrandTotalNotif);
        txtStatus = findViewById(R.id.statusTr);

        grandTotal = getIntent().getIntExtra("grand_total", 0);
        status = getIntent().getStringExtra("status");

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        String formattedGrandTotal = numberFormat.format(grandTotal);
        txtGrandTotal.setText(String.valueOf(formattedGrandTotal));
        txtStatus.setText(status);

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH:mm");
        String tanggal = dateFormat.format(currentDate);
        String jam = dateFormat1.format(currentDate);
        tanggalNow.setText(tanggal);
        jamNow.setText(jam);

    }

    public void keListPesanan(View view) {
        Intent intent = new Intent(notifSelesaiBeliActivity.this, DasboardActivity.class);
        startActivity(intent);
    }
}