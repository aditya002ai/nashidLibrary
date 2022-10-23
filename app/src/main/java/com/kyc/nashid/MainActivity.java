package com.kyc.nashid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.kyc.nashidmrz.CameraRectangle;
import com.kyc.nashidmrz.Utility;

public class MainActivity extends AppCompatActivity {
    TextView mrztext;
    int REQUEST_CODE_ASK_PERMISSIONS = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         mrztext = findViewById(R.id.mrztext);


        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS);

        } else {
            Intent i = new Intent(MainActivity.this, CameraRectangle.class);
            startActivityForResult(i, 503);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 503) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mrztext.setText(Utility.getInstance().getPassportNumber());
                }
            }, 0);
        }
    }

    private void gotoRead() {
//        Intent intent = new Intent(ComparisionSuccessful.this, MainActivityMRZ.class);
//        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent i = new Intent(MainActivity.this, CameraRectangle.class);
                startActivityForResult(i, 503);
            } else {

                if (ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {

                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS);
                }
            }

//                }
//                Toas/t.makeText(this, "Camera permission not granted", Toast.LENGTH_SHORT).show();


        }
    }

}
