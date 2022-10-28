package com.kyc.nashidmrz;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.kyc.nashidmrz.mrtd2.HomeLibrary;
import com.kyc.nashidmrz.mrtd2.WaitingForNfcActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CameraRectangle extends AppCompatActivity implements SurfaceHolder.Callback {

    private static final int CAMERA_REQUEST_ID = 2122;

    SurfaceView cameraView,transparentView;
    TextView txtRecognized;
    CameraSource cameraSource;
    SurfaceHolder holder,holderTransparent;
    private float RectLeft, RectTop,RectRight,RectBottom ;
    int  deviceHeight,deviceWidth;




    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST_ID:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(CameraRectangle.this,
                            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        try {
                            cameraSource.start(cameraView.getHolder());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capture);

        cameraView = findViewById(R.id.surfaceView);
        txtRecognized = findViewById(R.id.txtRecognized);
        deviceWidth=getScreenWidth();
        deviceHeight=getScreenHeight();
        transparentView = (SurfaceView)findViewById(R.id.TransparentView);
        holderTransparent = transparentView.getHolder();
        holderTransparent.addCallback((SurfaceHolder.Callback) this);
        holderTransparent.setFormat(PixelFormat.TRANSLUCENT);
        transparentView.setZOrderMediaOverlay(true);

        TextRecognizer textRecognizer = new TextRecognizer.Builder(this).build();
        if (textRecognizer.isOperational()) {
            cameraSource = new CameraSource.Builder(this, textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
//                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedPreviewSize(2048, 2048)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();

            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {

                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    try {
                        if (ActivityCompat.checkSelfPermission(CameraRectangle.this,
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CameraRectangle.this, new String[]{Manifest.permission.CAMERA},
                                    CAMERA_REQUEST_ID);
                            return;
                        }
//                        Draw();
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    try {
                        cameraSource.stop();
                        cameraSource.release();
                        cameraSource = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {

                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    txtRecognized.post(new Runnable() {
                        @Override
                        public void run() {
                            if (items.size() == 0) {
//                                txtRecognized.setText("");
//                                txtRecognized.setVisibility(View.INVISIBLE);
                            } else {
                                StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 0; i < items.size(); i++) {
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");
                                }
//                                txtRecognized.setText(stringBuilder.toString());
                                String s = stringBuilder.toString();
                                if (s.contains("<")) {
                                    handleOcrContinuousDecode(s);
                                }
//                                txtRecognized.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            });
        } else {
            Log.d(getClass().getSimpleName(), "Text Recognizer isn't ready yet");
        }

    }
    public static int getScreenWidth() {

        return Resources.getSystem().getDisplayMetrics().widthPixels;

    }



    public static int getScreenHeight() {

        return Resources.getSystem().getDisplayMetrics().heightPixels;

    }


//
//    void handleOcrContinuousDecode(String ocrResulttext) {
//
//
//        String result = ocrResulttext;
//        if (result != null && !"".equals(result)) {
//            result = result.replaceAll(" ", "");
//            String[] textResultTmpArr = result.split("\n");
//            result = "";
//            for (int i = 0; i < textResultTmpArr.length; i++) {
//                if (textResultTmpArr[i].length() > 10) {
//                    result += textResultTmpArr[i] + '\n';
//                }
//            }
//            if (ocrResulttext.getMeanConfidence() >= 50 && textResultTmpArr.length >= 2 && textResultTmpArr.length <= 3) {
//                try {
//                    if (MRZCheckUtil.check(result)) {
//                        MRZInfo mrzInfo = new MRZInfo(result);
//                        Toast.makeText(this, mrzInfo.toString().trim(), Toast.LENGTH_LONG).show();
//                        Intent returnIntent = new Intent();
//                        returnIntent.putExtra(MRZ_RESULT, mrzInfo);
//                        setResult(Activity.RESULT_OK, returnIntent);
//                        finish();
//                    }
//                } catch (IllegalStateException | IllegalArgumentException e) {
//                    Log.w("CACA", "checksum fail", e);
//                }
//            }
//        }
//    }


    public void handleOcrContinuousDecode(String ocrResulttext) {


        String result = ocrResulttext;
        if (result != null && !"".equals(result)) {
            result.replaceAll(" ", "");
            String[] textResultTmpArr = result.split("\n");
            result = "";
            for (int i = 0; i < textResultTmpArr.length; i++) {
                if (textResultTmpArr[i].length() > 10) {
                    result += textResultTmpArr[i] + '\n';
                }
            }
            result = result.replaceAll(" ", "");
//            ocrResult.setText(result);
//            MRZInfo mrzInfo = new MRZInfo(result);
//            String name = mrzInfo.getDocumentNumber();


        }

        String REGEX_OLD_PASSPORT = "[A-Z0-9<]{9}[0-9]{1}[A-Z<]{3}[0-9]{6}[0-9]{1}[FM<]{1}[0-9]{6}[0-9]{1}";
        String REGEX_IP_PASSPORT_LINE_1 = "\\bIP[A-Z<]{3}[A-Z0-9<]{9}[0-9]{1}";
        String REGEX_IP_PASSPORT_LINE_2 = "[0-9]{6}[0-9]{1}[FM<]{1}[0-9]{6}[0-9]{1}[A-Z<]{3}";

        Pattern patternLineOldPassportType = Pattern.compile(REGEX_OLD_PASSPORT);
        Matcher matcherLineOldPassportType = patternLineOldPassportType.matcher(result);

//                    MRZInfo mrzInfo = new MRZInfo(ocrResulttext);
//                   String documentNumber = mrzInfo.getDocumentNumber();
//                   String dateOfBirthDay = mrzInfo.getDateOfBirth();
//                   String expirationDate = mrzInfo.getDateOfExpiry();
//
//        Utility.getInstance().setPassportNumber(documentNumber);
//        Utility.getInstance().setDateOfBirth(dateOfBirthDay);
//        Utility.getInstance().setExpiryDate(expirationDate);
//        takeimage();

        if (matcherLineOldPassportType.find()) {
//            takeimage();
            //Old passport format
            String line2 = matcherLineOldPassportType.group(0);
            String documentNumber = line2.substring(0, 9).replaceAll("<","");
            String dateOfBirthDay = line2.substring(13, 19);
            String expirationDate = line2.substring(21, 27);

            //As O and 0 and really similar most of the countries just removed them from the passport, so for accuracy I am formatting it
            documentNumber = documentNumber.replace("O", "0");
//                        MRZInfo mrzInfo = new MRZInfo(result);
//            String name = mrzInfo.getDocumentNumber();

//            String pass=documentNumber;

            Utility.getInstance().setPassportNumber(documentNumber);
            Utility.getInstance().setDateOfBirth(dateOfBirthDay);
            Utility.getInstance().setExpiryDate(expirationDate);
//            finish();
            takeimage();



        }else {

            //Try with the new IP passport type
            Pattern patternLineIPassportTypeLine1 = Pattern.compile(REGEX_IP_PASSPORT_LINE_1);
            Matcher matcherLineIPassportTypeLine1 = patternLineIPassportTypeLine1.matcher(result);
            Pattern patternLineIPassportTypeLine2 = Pattern.compile(REGEX_IP_PASSPORT_LINE_2);
            Matcher matcherLineIPassportTypeLine2 = patternLineIPassportTypeLine2.matcher(result);
            if (matcherLineIPassportTypeLine1.find() && matcherLineIPassportTypeLine2.find()) {
                String line1 = matcherLineIPassportTypeLine1.group(0);
                String line2 = matcherLineIPassportTypeLine2.group(0);
                String documentNumber = line1.substring(5, 14).replaceAll("<","");;
                String dateOfBirthDay = line2.substring(0, 6);
                String expirationDate = line2.substring(8, 14);

                //As O and 0 and really similar most of the countries just removed them from the passport, so for accuracy I am formatting it
                documentNumber = documentNumber.replace("O", "0");
                Utility.getInstance().setPassportNumber(documentNumber);
                Utility.getInstance().setDateOfBirth(dateOfBirthDay);
                Utility.getInstance().setExpiryDate(expirationDate);
                takeimage();
//                finish();
//                Intent intent = new Intent(CameraRectangle.this, Camera_Result.class);
//                startActivity(intent);


            } else {
                //No success
            }
        }





    }


    private static final String STRING_DOT = ".";
    private static final String REGEX_DOT = "\\.";

    /**
     * Computes the regular expression corresponding to a package or class name,
     * by escaping dots.
     *
     * @param name A string.
     * @return The specified string, with '.' replaced with "\.".
     * @throws NullPointerException if the specified name is null.
     */
    public static String toRegex(String name) {
        // Implicit null check.
        return name.replace(STRING_DOT, REGEX_DOT);
    }

    private void Draw()

    {

        Canvas canvas = holderTransparent.lockCanvas(null);
//
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setStyle(Paint.Style.STROKE);

        paint.setColor(Color.GREEN);

        paint.setStrokeWidth(10);
//
//        RectLeft = 2;
//
//        RectTop = 500 ;
//
//        RectRight = RectLeft+ deviceWidth-100;
//
//        RectBottom =200;
//
//        Rect rec=new Rect((int) RectLeft,(int)RectTop,(int)RectRight,(int)RectBottom);
//
//        canvas.drawRect(rec,paint);


        int canvasW = deviceWidth;
        int canvasH = deviceHeight;
        Point centerOfCanvas = new Point(canvasW / 2, canvasH / 2);
        int rectW = 970;
        int rectH = 700;
        int left = centerOfCanvas.x - (rectW / 2);
        int top = centerOfCanvas.y - (rectH / 2);
        int right = centerOfCanvas.x + (rectW / 2);
        int bottom = centerOfCanvas.y + (rectH / 2);
        Rect rect = new Rect(left, top, right, bottom);
        canvas.drawRect(rect, paint);

        holderTransparent.unlockCanvasAndPost(canvas);



    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

        Draw();

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

        try {
            cameraSource.stop();
            cameraSource.release();
            cameraSource = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    private void takeimage(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    cameraSource.takePicture(null, new CameraSource.PictureCallback() {
                        @Override
                        public void onPictureTaken(byte[] bytes) {
                            Log.d("com.omanid.TAG", "onPictureTaken - jpeg");
                            Utility.getInstance().scannedImage = bytes;
//                            setResult(Activity.RESULT_OK,
//                                    new Intent().putExtra("PassportNumber", Utility.getInstance().getPassportNumber()).putExtra("DateOfBirth", Utility.getInstance().getDateOfBirth()).putExtra("ExpiryDate", Utility.getInstance().getExpiryDate()));
//                            finish();

                            Intent intent = new Intent(CameraRectangle.this, WaitingForNfcActivity.class);
                            startActivity(intent);

                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private void capturePic(byte[] bytes) {
        try {
            String mPath = Environment.getExternalStorageState();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
                mPath= getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/" + getPhotoTime() + ".jpeg";
            }
            else
            {
                mPath= Environment.getExternalStorageDirectory().toString() + "/" + getPhotoTime() + ".jpeg";
            }

            File folder = new File(mPath);
            FileOutputStream stream = new FileOutputStream(folder);
            stream.write(bytes);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getPhotoTime(){
        SimpleDateFormat sdf=new SimpleDateFormat("ddMMyy_hhmmss");
        return sdf.format(new Date());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            cameraSource.stop();
            cameraSource.release();
            cameraSource = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent i = new Intent(CameraRectangle.this, HomeLibrary.class);
        startActivity(i);
    }
}

