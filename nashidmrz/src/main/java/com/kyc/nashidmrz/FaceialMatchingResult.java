package com.kyc.nashidmrz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;

public class FaceialMatchingResult extends AppCompatActivity {
    SVGImageView logo,green,back;
    ImageView PassportImage,RealImage;
    TextView MatchingPercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faceial_matching_result);
        logo = findViewById(R.id.logo);
        green = findViewById(R.id.check);
        back = findViewById(R.id.backbutton);
        PassportImage=findViewById(R.id.passportImg);
        RealImage=findViewById(R.id.realimg);
        MatchingPercentage=findViewById(R.id.matching_per);

        MatchingPercentage.setText(UtilityNFC.getInstance().matchPercentage);
        try {
            SVG svg = SVG.getFromResource(getResources(), R.raw.logo);
            logo.setSVG(svg);
            SVG correct = SVG.getFromResource(getResources(), R.raw.correct);
            green.setSVG(correct);
            SVG back1 = SVG.getFromResource(getResources(), R.raw.arowback);
            back.setSVG(back1);
        }catch (Exception e) {
            e.printStackTrace();
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                Intent i=new Intent(FaceialMatchingResult.this,ComparisionSuccessful.class);
//                startActivity(i);

            }
        });

        try {
            PassportImage.setImageBitmap(LivenessData.getInstance().getNfcImage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Matrix matrix = new Matrix();
            matrix.postRotate(270);
            Bitmap bmp = BitmapFactory.decodeByteArray(Utility.getInstance().liveImage, 0, Utility.getInstance().liveImage.length);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmp, 2048, 2048, true);
            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            RealImage.setImageBitmap(rotatedBitmap);
//            MyUtils.getInstance().showProgressDialog(LivenessActivity.this);
//            new UploadFace().execute();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}