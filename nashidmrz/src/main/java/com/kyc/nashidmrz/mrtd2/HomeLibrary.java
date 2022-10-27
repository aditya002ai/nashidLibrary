package com.kyc.nashidmrz.mrtd2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.kyc.nashidmrz.CameraRectangle;
import com.kyc.nashidmrz.R;
import com.mv.liveness.LivenessMainActivity;
import com.mv.liveness.UtilityLive;

public class HomeLibrary extends AppCompatActivity {
    Button start, button1;
    int REQUEST_CODE_ASK_PERMISSIONS = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_library);
        start = findViewById(R.id.button);
        button1 = findViewById(R.id.button1);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(HomeLibrary.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(HomeLibrary.this,
                            new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS);

                } else {
                    Intent i = new Intent(HomeLibrary.this, CameraRectangle.class);
                    startActivityForResult(i, 503);

//
//                    Intent i = new Intent(HomeLibrary.this, LivenessMainActivity.class);
//                    startActivityForResult(i, 503);
                }

            }
        });


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(HomeLibrary.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(HomeLibrary.this,
                            new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS);

                } else {
                    Intent i = new Intent(HomeLibrary.this, LivenessMainActivity.class);
                    startActivityForResult(i, 504);
                }

            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 503) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    start.setText(Utility.getInstance().getPassportNumber());
                }
            }, 0);
        }

        if (requestCode == 504) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    byte[] liveness = UtilityLive.getInstance().liveImage;
//                    start.setText(Utility.getInstance().getPassportNumber());
                }
            }, 0);
        }
    }

    private void gotoRead() {
//        Intent intent = new Intent(ComparisionSuccessful.this, HomeLibraryMRZ.class);
//        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent i = new Intent(HomeLibrary.this, CameraRectangle.class);
                startActivityForResult(i, 503);
            } else {

                if (ActivityCompat.checkSelfPermission(HomeLibrary.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {

                    ActivityCompat.requestPermissions(HomeLibrary.this,
                            new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS);
                }
            }

//                }
//                Toas/t.makeText(this, "Camera permission not granted", Toast.LENGTH_SHORT).show();


        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
