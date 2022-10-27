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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Camera_Result extends AppCompatActivity {
    SVGImageView logo, back;
    TextView DocumnetNumber;
    TextView Dob;
    TextView ExpriDate;
    public String dateOfBirth = "000000";
    ImageView imageview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_result);
        logo = findViewById(R.id.logo);
        back = findViewById(R.id.backbutton);
        DocumnetNumber = findViewById(R.id.documnetNumber);
        Dob = findViewById(R.id.dob);
        ExpriDate = findViewById(R.id.ExpriDate);
        try {
            SVG svg = SVG.getFromResource(getResources(), R.raw.logo);
            logo.setSVG(svg);
            SVG back1 = SVG.getFromResource(getResources(), R.raw.arowback);
            back.setSVG(back1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                Intent i = new Intent(Camera_Result.this, ComparisionSuccessful.class);
//                startActivity(i);

            }
        });


        imageview = findViewById(R.id.imageview);

        try {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap bmp = BitmapFactory.decodeByteArray(Utility.getInstance().scannedImage, 0, Utility.getInstance().scannedImage.length);

            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmp, 2048, 2048, true);
            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
            ImageView image = (ImageView) findViewById(R.id.imageview);
            image.setImageBitmap(rotatedBitmap);


//            Bitmap imagemOriginal = BitmapFactory.decodeByteArray(Utility.getInstance().scannedImage, 0, Utility.getInstance().scannedImage.length); // 2560×1440
//            float scale = 1280/1000F;
//            int left = (int) scale*(imagemOriginal.getWidth()-400)/2;
//            int top = (int) scale*(imagemOriginal.getHeight()-616)/2;
//            int width = (int) scale*400;
//            int height = (int) scale*616;
//            Matrix rotationMatrix = new Matrix();
//            rotationMatrix.postRotate(90);
//            Bitmap imagemCortada = Bitmap.createBitmap(imagemOriginal, left, top, width, height, rotationMatrix, false);
//                        ImageView image = (ImageView) findViewById(R.id.imageview);
//            image.setImageBitmap(imagemCortada);


        } catch (Exception e) {
            e.printStackTrace();
        }

        String passport = Utility.getInstance().getPassportNumber();
        String dateOfBirth = Utility.getInstance().getDateOfBirth();
        String expiryDate = Utility.getInstance().getExpiryDate();
        DocumnetNumber.setText(Utility.getInstance().getPassportNumber());
        Dob.setText(formateDateFromstring("yymmdd", "d MMM yyyy",Utility.getInstance().getDateOfBirth() ));
        ExpriDate.setText(formateDateFromstring("yymmdd", "d MMM yyyy",Utility.getInstance().getExpiryDate() ));

    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    public String formatMonth(String month) {
        String value  = null;
        try {
            SimpleDateFormat monthParse = new SimpleDateFormat("MM");
            SimpleDateFormat monthDisplay = new SimpleDateFormat("MMM");
            value = monthDisplay.format(monthParse.parse(month));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {
        Date parsed = null;
        String outputDate = "";
        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());
        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);
        } catch (ParseException e) {
        }
        return outputDate;

    }

}