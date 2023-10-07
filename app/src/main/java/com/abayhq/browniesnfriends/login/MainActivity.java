package com.abayhq.browniesnfriends.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.abayhq.browniesnfriends.R;

public class MainActivity extends AppCompatActivity {

    private EditText pass;
    private Button visibilityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


    public void keRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}