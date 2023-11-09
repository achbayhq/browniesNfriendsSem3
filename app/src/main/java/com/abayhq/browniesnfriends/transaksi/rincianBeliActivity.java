package com.abayhq.browniesnfriends.transaksi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.adapter.adapterRincianBeli;
import com.abayhq.browniesnfriends.settergetter.setgetRincianBeli;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class rincianBeliActivity extends AppCompatActivity {

    Calendar myCalendar;
    EditText txtTgl;
    private RecyclerView recyclerView;
    private adapterRincianBeli adapterRecycle;
    private ArrayList<setgetRincianBeli> beliArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rincian_beli);

        myCalendar = Calendar.getInstance();
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

        Spinner spinner = findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item);
        adapter.add("Jam Ambil");
        adapter.add("08.00 - 10.00");
        adapter.add("10.00 - 12.00");
        adapter.add("12.00 - 14.00");
        adapter.add("14.00 - 16.00");
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        dibeli();
        recyclerView = findViewById(R.id.recyclerView);
        adapterRecycle = new adapterRincianBeli(beliArrayList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterRecycle);
    }

    void dibeli(){
        beliArrayList = new ArrayList<>();
        beliArrayList.add(new setgetRincianBeli(R.drawable.menu_a, "Menu A", "10.000", 50));
        beliArrayList.add(new setgetRincianBeli(R.drawable.menu_b, "Menu B", "12.000", 35));
        beliArrayList.add(new setgetRincianBeli(R.drawable.menu_c, "Menu C", "15.000", 20));
        beliArrayList.add(new setgetRincianBeli(R.drawable.menu_d, "Menu D", "11.000", 55));
        beliArrayList.add(new setgetRincianBeli(R.drawable.menu_b, "Menu B", "13.000", 45));
        beliArrayList.add(new setgetRincianBeli(R.drawable.menu_c, "Menu C", "9.000", 65));
        beliArrayList.add(new setgetRincianBeli(R.drawable.menu_a, "Menu A", "16.000", 30));
        beliArrayList.add(new setgetRincianBeli(R.drawable.menu_d, "Menu D", "10.000", 55));
        beliArrayList.add(new setgetRincianBeli(R.drawable.menu_a, "Menu A", "14.000", 60));
    }
    private void updateLabel(){
        String myFormat = "dd MMMM yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        txtTgl.setText(dateFormat.format(myCalendar.getTime()));
    }

}