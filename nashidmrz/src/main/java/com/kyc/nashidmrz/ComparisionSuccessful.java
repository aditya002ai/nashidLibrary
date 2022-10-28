package com.kyc.nashidmrz;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;

import org.jmrtd.lds.icao.MRZInfo;

public class ComparisionSuccessful extends AppCompatActivity {
    SVGImageView logo, dropdown1, dropdown2, dropdown3, certificate;
    Button verification;
    RelativeLayout Camera, NFC, Facing;
    static final public String MRZ_RESULT = "MRZ_RESULT";
    int REQUEST_CODE_ASK_PERMISSIONS = 100;
    ImageView success,fail;
    TextView facevalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparision_successful);
        logo = findViewById(R.id.logo);
        dropdown1 = findViewById(R.id.next);
        dropdown2 = findViewById(R.id.next1);
        dropdown3 = findViewById(R.id.next2);
//        certificate = findViewById(R.id.certificate);
        verification = findViewById(R.id.complte);
        Camera = findViewById(R.id.camera);
        NFC = findViewById(R.id.nfcresult);
        Facing = findViewById(R.id.Faceresult);
        success = findViewById(R.id.success);
        fail = findViewById(R.id.fail);
        facevalue = findViewById(R.id.facevalue);
        try {
            if(UtilityNFC.getInstance().confidenceValue<7){
                success.setVisibility(View.VISIBLE);
            }else {
                fail.setVisibility(View.GONE);
            }
            facevalue.setText("Face result : "+UtilityNFC.getInstance().matchPercentage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            SVG svg = SVG.getFromResource(getResources(), R.raw.logo);
            logo.setSVG(svg);
            SVG one = SVG.getFromResource(getResources(), R.raw.next);
            dropdown1.setSVG(one);
            SVG two = SVG.getFromResource(getResources(), R.raw.next);
            dropdown2.setSVG(two);
            SVG three = SVG.getFromResource(getResources(), R.raw.next);
            dropdown3.setSVG(three);
            SVG correct = SVG.getFromResource(getResources(), R.raw.success_logo);
            certificate.setSVG(correct);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(ComparisionSuccessful.this, Camera_Result.class);
                startActivity(i);


            }
        });
        NFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(ComparisionSuccessful.this, NFC_result_Activity.class);
                startActivity(i);
//                Bitmap icon = BitmapFactory.decodeResource(getResources(),
//                        R.drawable.arun);
//                LivenessData.getInstance().setNfcBitmap(icon);
//                Intent i=new Intent(ComparisionSuccessful.this, LivenessActivity.class);
//                startActivity(i);

            }
        });
        Facing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(ComparisionSuccessful.this, FaceialMatchingResult.class);
                startActivity(i);
//                Bitmap icon = BitmapFactory.decodeResource(ComparisionSuccessful.this.getResources(),
//                        R.drawable.arun);
//                LivenessData.getInstance().setNfcBitmap(icon);
//                Intent i=new Intent(ComparisionSuccessful.this,LivenessActivity.class);
//                startActivity(i);

            }
        });

    }




}