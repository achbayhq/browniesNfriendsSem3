package com.abayhq.browniesnfriends.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.respons.userLoginRespons;
import com.abayhq.browniesnfriends.settergetter.dataUserLogin;
import com.abayhq.browniesnfriends.volley.volleyRequestHandler;
import com.google.gson.Gson;

import org.json.JSONObject;

public class lupaPassActivity extends AppCompatActivity {

    EditText etTelepon, etJawaban;
    CardView cardPertanyaan, cardJawaban, cardPass;
    TextView subTextPass, txtPass, etPertanyaan;
    Button btnSubmit;
    private String noTlp, pertanyaan, jawaban, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_pass);

        etTelepon = findViewById(R.id.tlpLupaPass);
        etPertanyaan = findViewById(R.id.txtPertanyaan);
        txtPass = findViewById(R.id.txtPassLupaPass);
        etJawaban = findViewById(R.id.jawabanLupaPass);
        cardPertanyaan = findViewById(R.id.cardView4);
        cardJawaban = findViewById(R.id.cardView5);
        cardPass = findViewById(R.id.cardView6);
        subTextPass = findViewById(R.id.textView40);
        btnSubmit = findViewById(R.id.button10);

        noTlp = getIntent().getStringExtra("telepon");

        if (noTlp != null){
            etTelepon.setText(noTlp);
            etTelepon.setEnabled(false);
        }
    }

    public void lupaPass(View view) {
        String txtTelepon = etTelepon.getText().toString();
        String txtJawaban = etJawaban.getText().toString();
        String isiPass = txtPass.getText().toString();

        Gson gson = new Gson();
        volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(this);
        volleyRequestHandler.loginUser(txtTelepon, new volleyRequestHandler.ResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                userLoginRespons userRespon = gson.fromJson(response.toString(), userLoginRespons.class);
                if (userRespon.getCode() == 200) {
                    dataUserLogin loggedUser = userRespon.getUser_list().get(0);
                    String userTelepon = loggedUser.getNo_telepon();
                    String userPass = loggedUser.getPassword();
                    String userPertanyaan = loggedUser.getPertanyaan();
                    String userJawaban = loggedUser.getJawaban();

                    if (isiPass.equals("")){
                        cardPertanyaan.setVisibility(View.VISIBLE);
                        cardJawaban.setVisibility(View.VISIBLE);
                        btnSubmit.setText(R.string.tampilkan_password);
                        etPertanyaan.setText(userPertanyaan);

                        if (!txtJawaban.equals("")) {
                            if (txtJawaban.equals(userJawaban)) {
                                cardPass.setVisibility(View.VISIBLE);
                                subTextPass.setVisibility(View.VISIBLE);
                                txtPass.setText(userPass);
                                btnSubmit.setText(R.string.kembali_ke_login);
                            } else {
                                Toast.makeText(lupaPassActivity.this, "Jawaban Anda Salah", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else{
                        Intent intent = new Intent(lupaPassActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else if (userRespon.getCode() == 404) {
                    Toast.makeText(lupaPassActivity.this, "User Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onError(String error) {

            }
        });

    }
}