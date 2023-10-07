package com.abayhq.browniesnfriends.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.abayhq.browniesnfriends.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText pass;
    private Button visibilityButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item);
        adapter.add("Pilih Pertanyaan");
        adapter.add("Di kota apa Anda lahir?");
        adapter.add("Siapa Nama Cinta Pertama Anda?");
        adapter.add("Siapa Nama Istri Ayah Anda?");
        adapter.add("Siapa Nama Nenek dari Nenek Anda?");
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        pass = findViewById(R.id.passLogin);
        visibilityButton = findViewById(R.id.visibilityButton);
    }

    public void toggleVisibility(View view) {

        if (pass.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            // Ubah ke tipe password yang tersembunyi
            pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            visibilityButton.setBackgroundResource(R.drawable.tutup_pass);
        } else {
            // Ubah ke tipe teks biasa
            pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            visibilityButton.setBackgroundResource(R.drawable.buka_pass);
        }
        pass.setSelection(pass.getText().length());
    }
}