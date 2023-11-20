package com.abayhq.browniesnfriends.transaksi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.abayhq.browniesnfriends.GlobalVariable;
import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.respons.userLoginRespons;
import com.abayhq.browniesnfriends.settergetter.dataUserLogin;
import com.abayhq.browniesnfriends.volley.volleyRequestHandler;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONObject;

public class rincianBeliAdminActivity extends AppCompatActivity {

    Button btnCekAkun;
    EditText txtTelepon, txtNama, txtAlamat;
    String telepon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rincian_beli_admin);

        txtTelepon = findViewById(R.id.txtTeleponCekAkun);
        txtNama = findViewById(R.id.namaCust);
        txtAlamat = findViewById(R.id.alamatCust);

        telepon = txtTelepon.getText().toString();

        btnCekAkun = findViewById(R.id.btnCekAkun);
        btnCekAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(rincianBeliAdminActivity.this);
                volleyRequestHandler.loginUser(telepon, new volleyRequestHandler.ResponseListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        userLoginRespons userRespon = gson.fromJson(response.toString(), userLoginRespons.class);
                        if (userRespon.getCode() == 200) {
                            dataUserLogin loggedUser = userRespon.getUser_list().get(0);
                            String userName = loggedUser.getNama();
                            String alamat = loggedUser.getAlamat();
                            txtNama.setText(userName);
                            txtAlamat.setText(alamat);
                        }else if (userRespon.getCode() == 404) {
                            Toast.makeText(rincianBeliAdminActivity.this, "Silahkan Registrasikan User", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(String error) {

                    }
                });
            }
        });
    }
}