package com.abayhq.browniesnfriends.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.home.DasboardActivity;
import com.abayhq.browniesnfriends.respons.userLoginRespons;
import com.abayhq.browniesnfriends.settergetter.dataUserLogin;
import com.abayhq.browniesnfriends.volley.volleyRequestHandler;
import com.google.gson.Gson;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText etTlp, etPass;
    private Button visibilityButton, btnLogin;
    private CardView cardTxtPass;
    private int attempPass = 3;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTlp = findViewById(R.id.tlpLogin);
        etPass = findViewById(R.id.passLogin);
        visibilityButton = findViewById(R.id.visibilityButton);
        btnLogin = findViewById(R.id.login);
        cardTxtPass = findViewById(R.id.cardViewPassLogin);

        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String telepon = sharedPreferences.getString("telepon", "");
        if (!telepon.equals("")){
            Intent intent = new Intent(MainActivity.this, DasboardActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void toggleVisibility(View view) {
        if (etPass.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            // Ubah ke tipe password yang tersembunyi
            etPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            visibilityButton.setBackgroundResource(R.drawable.tutup_pass);
        } else {
            // Ubah ke tipe teks biasa
            etPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            visibilityButton.setBackgroundResource(R.drawable.buka_pass);
        }
        etPass.setSelection(etPass.getText().length());
    }


    public void keRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void keLupaPassword(View view) {
        Intent intent = new Intent(this, lupaPassActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        String noTlp = etTlp.getText().toString();
        String pass = etPass.getText().toString();

        Gson gson = new Gson();
        volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(this);
        volleyRequestHandler.loginUser(noTlp, new volleyRequestHandler.ResponseListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        userLoginRespons userRespon = gson.fromJson(response.toString(), userLoginRespons.class);
                        if (userRespon.getCode() == 200){
                            dataUserLogin loggedUser = userRespon.getUser_list().get(0);
                            String userTelepon = loggedUser.getNo_telepon();
                            String userNama = loggedUser.getNama();
                            String userAlamat = loggedUser.getAlamat();
                            String userPass = loggedUser.getPassword();

                            if (pass.equals("")){
                                if (userPass == null){
                                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                                    intent.putExtra("telepon", userTelepon);
                                    intent.putExtra("nama", userNama);
                                    intent.putExtra("alamat", userAlamat);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    cardTxtPass.setVisibility(View.VISIBLE);
                                    btnLogin.setText(R.string.login);
                                }
                            }else{
                                if (attempPass > 0) {
                                    if (noTlp.equals(userTelepon) && pass.equals(userPass)) {
                                        Intent intent = new Intent(MainActivity.this, DasboardActivity.class);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("telepon", userTelepon);
                                        editor.apply();
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        attempPass--;
                                        Toast.makeText(MainActivity.this, "Password Salah Sisa Percobaan: "+attempPass, Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Intent intent = new Intent(MainActivity.this, lupaPassActivity.class);
                                    intent.putExtra("telepon", userTelepon);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                        } else if (userRespon.getCode() == 404) {
                            Toast.makeText(MainActivity.this, "User Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(String error) {

                    }
                });
    }
}