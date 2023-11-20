package com.abayhq.browniesnfriends.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abayhq.browniesnfriends.GlobalVariable;
import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.databasesqlite.dbTransaksiHelper;
import com.abayhq.browniesnfriends.menu.menuFragment;
import com.abayhq.browniesnfriends.pesanan.tabPesananFragment;
import com.abayhq.browniesnfriends.profile.profileFragment;
import com.abayhq.browniesnfriends.respons.userLoginRespons;
import com.abayhq.browniesnfriends.settergetter.dataUserLogin;
import com.abayhq.browniesnfriends.settergetter.setgetMenu;
import com.abayhq.browniesnfriends.transaksi.rincianBeliActivity;
import com.abayhq.browniesnfriends.transaksi.rincianBeliAdminActivity;
import com.abayhq.browniesnfriends.volley.volleyRequestHandler;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class DasboardActivity extends AppCompatActivity {

    AnimatedBottomBar navBar;

    public CardView btnTransaksi;
    public TextView cardItemTr;
    String akses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dasboard);

        SharedPreferences sharedPreferences = this.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String telepon = sharedPreferences.getString("telepon", "");

        if (!telepon.equals("")){
            Gson gson = new Gson();
            volleyRequestHandler volleyRequestHandler = new volleyRequestHandler(this);
            volleyRequestHandler.loginUser(telepon, new volleyRequestHandler.ResponseListener() {
                @Override
                public void onResponse(JSONObject response) {
                    userLoginRespons userRespon = gson.fromJson(response.toString(), userLoginRespons.class);
                    if (userRespon.getCode() == 200) {
                        dataUserLogin loggedUser = userRespon.getUser_list().get(0);
                        akses = loggedUser.getAkses();
                    }else if (userRespon.getCode() == 404) {
                        Toast.makeText(DasboardActivity.this, "User Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(String error) {

                }
            });
        }

        btnTransaksi = findViewById(R.id.cardTransaksi);
        cardItemTr = findViewById(R.id.cardItemTransaksi);
        navBar = findViewById(R.id.navBar);
        replaceFragment(new homeFragment(), "pesanan");
        navBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int i1, @NonNull AnimatedBottomBar.Tab tab1) {
                if (tab1.getId() == R.id.home){
                    replaceFragment(new homeFragment(), "home");
                    btnTransaksi.setVisibility(View.GONE);
                } else if (tab1.getId() == R.id.menu) {
                    replaceFragment(new menuFragment(), "menu");
                } else if (tab1.getId() == R.id.profile) {
                    replaceFragment(new profileFragment(), "profile");
                    btnTransaksi.setVisibility(View.GONE);
                } else if (tab1.getId() == R.id.pesanan) {
                    replaceFragment(new tabPesananFragment(), "pesanan");
                    btnTransaksi.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {

            }
        });

    }

    private void lanjutTransaksi(){
        menuFragment fragment = (menuFragment) getSupportFragmentManager().findFragmentByTag("menu");
        ArrayList<setgetMenu> transaksiList = fragment.transaksiList;

        dbTransaksiHelper dbHelper = new dbTransaksiHelper(this, dbTransaksiHelper.DB_NAME, null, dbTransaksiHelper.DB_VER);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (setgetMenu menu : transaksiList){
            ContentValues values = new ContentValues();
            values.put("img", menu.getImg());
            values.put("id_barang", menu.getId());
            values.put("nama_kue", menu.getNama());
            values.put("harga", menu.getHarga());
            values.put("qty", menu.getQty());
            values.put("total", menu.getQty() * Integer.parseInt(menu.getHarga()));

            try {
                long newRowId = db.insert("list_transaksi", null, values);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        db.close();
    }

    private void replaceFragment(Fragment fragment, String tag){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.forFragment, fragment, tag);
        transaction.commit();
    }

    public void btnTransaksi(View view) {
        lanjutTransaksi();
        if (akses.equals("customer")) {
            Intent intent = new Intent(DasboardActivity.this, rincianBeliActivity.class);
            startActivity(intent);
        } else if (akses.equals("karyawan")) {
            Intent intent = new Intent(DasboardActivity.this, rincianBeliAdminActivity.class);
            startActivity(intent);
        }
    }
}