package com.abayhq.browniesnfriends.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abayhq.browniesnfriends.R;
import com.abayhq.browniesnfriends.menu.menuFragment;
import com.abayhq.browniesnfriends.profile.profileFragment;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class DasboardActivity extends AppCompatActivity {

    AnimatedBottomBar navBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dasboard);

        navBar = findViewById(R.id.navBar);
        replaceFragment(new homeFragment());

        navBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int i1, @NonNull AnimatedBottomBar.Tab tab1) {
                if (tab1.getId() == R.id.home){

                    replaceFragment(new homeFragment());
                } else if (tab1.getId() == R.id.menu) {
                    replaceFragment(new menuFragment());
                } else if (tab1.getId() == R.id.profile) {
                    replaceFragment(new profileFragment());
                } else if (tab1.getId() == R.id.pesanan) {
                    //replaceFragment(new pesananFragment());
                }
            }

            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {

            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.forFragment, fragment);
        transaction.commit();
    }
}