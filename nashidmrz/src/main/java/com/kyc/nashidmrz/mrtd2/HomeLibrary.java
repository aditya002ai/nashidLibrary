package com.kyc.nashidmrz.mrtd2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.kyc.nashidmrz.CameraRectangle;
import com.kyc.nashidmrz.ComparisionSuccessful;
import com.kyc.nashidmrz.LivenessData;
import com.kyc.nashidmrz.MyUtils;
import com.kyc.nashidmrz.R;
import com.kyc.nashidmrz.UtilityNFC;
import com.kyc.nashidmrz.mrtd2.interfaceClass.RequestResponse;
import com.kyc.nashidmrz.mrtd2.requestResponse.OkHttpRequestResponse;
import com.mv.liveness.LivenessMainActivity;
import com.mv.liveness.UtilityLive;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class HomeLibrary extends AppCompatActivity {
    Button start, button1;
    int REQUEST_CODE_ASK_PERMISSIONS = 100;
    ImageView view_photo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_library);
        start = findViewById(R.id.button);
        button1 = findViewById(R.id.button1);
        view_photo = findViewById(R.id.view_photo);


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
        if (requestCode == 504) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Bitmap icon = BitmapFactory.decodeResource(HomeLibrary.this.getResources(),
                        R.drawable.arun);
                LivenessData.getInstance().setNfcBitmap(icon);

                    try {
                        byte[] liveness = UtilityLive.getInstance().liveImage;
                        view_photo.setImageBitmap(LivenessData.getInstance().getNfcImage());
                        MyUtils.getInstance().showProgressDialog(HomeLibrary.this);

                        OkHttpRequestResponse.getInstance().uploadFile(HomeLibrary.this, byteArray(view_photo), liveness, requestResponseFace);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(HomeLibrary.this,e.toString(),Toast.LENGTH_SHORT).show();
                    }

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



    private byte[] byteArray(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageInByte = baos.toByteArray();
        return imageInByte;
    }


    RequestResponse requestResponseFace = new RequestResponse() {
        @Override
        public void myResponse(final JSONObject jsonObject) {

            MyUtils.getInstance().dismissDialog();

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {

                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    try {
                        double value = jsonObject.getDouble("match");
                        String matchine = "0 %";

                        if(value ==0){
                            matchine = "100 %";
                        }else if(value<=0.1){
                            matchine = "96 %";
                        }else if(value<=0.2){
                            matchine = "92 %";
                        }else if(value<=0.3){
                            matchine = "88 %";
                        }else if(value<=0.4){
                            matchine = "80 %";
                        } else if(value<=0.5){
                            matchine = "75 %";
                        }

                        UtilityNFC.getInstance().matchPercentage = matchine;
                        callToComparision();

////                        if (value < 0.35) {
//                        if (value < 0.5) {
//                            MyUtils.getInstance().setFacecomparisonStatus("Face Comparison Successful");
//                            myText = "Face Comparison Successful";
//                            faceComparision = true;
//
//                        } else {
//                            MyUtils.getInstance().setFacecomparisonStatus("Face Comparison Failed");
//                            faceComparision = false;
//
//                        }
//
//
//
//                        text.setText(myText);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


        }

        @Override
        public void myJsonArray(JSONArray jsonObject) {

        }
    };


    public void callToComparision(){
        Intent intent = new Intent(HomeLibrary.this, ComparisionSuccessful.class);
        startActivity(intent);
    }

}
