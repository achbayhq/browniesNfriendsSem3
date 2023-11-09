package com.abayhq.browniesnfriends.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.volley.volleyRequestHandler;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText pass, etTelepon, etAlamat, etJawaban, etNama;
    Spinner spinnerPertanyaan;
    private Button visibilityButton;
    private String noTlp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etNama = findViewById(R.id.namaRegister);
        etAlamat = findViewById(R.id.alamatRegister);
        etJawaban = findViewById(R.id.jawabanRegister);
        spinnerPertanyaan = findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item);
        adapter.add("Pilih Pertanyaan");
        adapter.add("Di kota apa Anda lahir?");
        adapter.add("Siapa Nama Cinta Pertama Anda?");
        adapter.add("Siapa Nama Istri Ayah Anda?");
        adapter.add("Siapa Nama Nenek dari Nenek Anda?");
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPertanyaan.setAdapter(adapter);

        pass = findViewById(R.id.passRegister);
        visibilityButton = findViewById(R.id.visibilityButton);

        etTelepon = findViewById(R.id.tlpRegister);
        noTlp = getIntent().getStringExtra("telepon");
        if (noTlp != null){
            etTelepon.setText(noTlp);
            etTelepon.setEnabled(false);
        }
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

    public void register(View view) {
        String nama = etNama.getText().toString();
        String alamat = etAlamat.getText().toString();
        String telepon = etTelepon.getText().toString();
        String jawaban = etJawaban.getText().toString();
        String password = pass.getText().toString();
        String pertanyaan = spinnerPertanyaan.getSelectedItem().toString();

        Gson gson = new Gson();
        volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(this);
        volleyRequestHandler.registerUser(nama, alamat, telepon, pertanyaan, jawaban, password, new volleyRequestHandler.ResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int code = response.getInt("code");

                    if (code == 200){
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (code == 101) {
                        Toast.makeText(RegisterActivity.this, response.getString("status") , Toast.LENGTH_SHORT).show();
                    } else if (code == 205) {
                        Toast.makeText(RegisterActivity.this, response.getString("status") , Toast.LENGTH_SHORT).show();
                    } else if (code == 100) {
                        Toast.makeText(RegisterActivity.this, response.getString("status") , Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(RegisterActivity.this, error , Toast.LENGTH_SHORT).show();
                Log.e("errorPHP", error);
            }
        });
    }
}