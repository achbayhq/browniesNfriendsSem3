package com.abayhq.browniesnfriends.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.abayhq.browniesnfriends.R;

public class paketActivity extends AppCompatActivity {

    Button bottomsheetpaket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paket);
        bottomsheetpaket = findViewById(R.id.bottom_sheet_paket);
//        bottomsheetpaket.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                showDialog();
//
//            }
//
//            private void showDialog() {
//                final Dialog dialog = new Dialog(this);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.bottom_sheet_paket);
//            }
//        });
//
//    }

    }}
