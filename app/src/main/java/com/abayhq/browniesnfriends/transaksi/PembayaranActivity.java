package com.abayhq.browniesnfriends.transaksi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.abayhq.browniesnfriends.R;

public class PembayaranActivity extends AppCompatActivity {

    public final int GALERY_REQ_CODE = 101;
    TextView txtFileImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);

        Button uploadImg = findViewById(R.id.btnUpload);
        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGalery = new Intent(Intent.ACTION_PICK);
                iGalery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGalery, GALERY_REQ_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == GALERY_REQ_CODE) {
                if (data != null) {
                    Uri selectedImageUri = data.getData();
                    String[] projection = {MediaStore.Images.Media.DISPLAY_NAME};
                    Cursor cursor = getContentResolver().query(selectedImageUri, projection, null, null, null);

                    if (cursor != null && cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                        String fileName = cursor.getString(columnIndex);
                        cursor.close();

                        // Set the file name to your TextView
                        txtFileImg.setText(fileName);
                    }
                }
            }
        }
    }
}