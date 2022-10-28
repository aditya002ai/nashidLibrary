package com.kyc.nashidmrz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;
import com.kyc.nashidmrz.mrtd2.HomeLibrary;

public class SelectDocumentActivity extends AppCompatActivity {
    SVGImageView SplachScreen,dropdown1,dropdown2,dropdown3;
    Button verification;
    int REQUEST_CODE_ASK_PERMISSIONS = 100;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_document);
        SplachScreen=findViewById(R.id.icon);
        dropdown1=findViewById(R.id.dropbox);
        dropdown2=findViewById(R.id.dropbox2);
        dropdown3=findViewById(R.id.dropbox3);
        verification=findViewById(R.id.verification);
        try {
            SVG svg= SVG.getFromResource(getResources(), R.raw.name_icon);
            SplachScreen.setSVG(svg);
            SVG one= SVG.getFromResource(getResources(), R.raw.dropbox);
            dropdown1.setSVG(one);
            SVG two= SVG.getFromResource(getResources(), R.raw.dropbox);
            dropdown2.setSVG(two);
            SVG three= SVG.getFromResource(getResources(), R.raw.dropbox);
            dropdown3.setSVG(three);
        } catch (Exception e) {
            e.printStackTrace();
        }
        verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(SelectDocumentActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(SelectDocumentActivity.this,
                            new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS);

                } else {
                    Intent i = new Intent(SelectDocumentActivity.this, CameraRectangle.class);
                    startActivityForResult(i, 503);

                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent i = new Intent(SelectDocumentActivity.this, CameraRectangle.class);
                startActivityForResult(i, 503);
            } else {

                if (ActivityCompat.checkSelfPermission(SelectDocumentActivity.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {

                    ActivityCompat.requestPermissions(SelectDocumentActivity.this,
                            new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSIONS);
                }
            }

//                }
//                Toas/t.makeText(this, "Camera permission not granted", Toast.LENGTH_SHORT).show();


        }
    }

}