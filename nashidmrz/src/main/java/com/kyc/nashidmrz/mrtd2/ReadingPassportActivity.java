package com.kyc.nashidmrz.mrtd2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.kyc.nashidmrz.ComparisionSuccessful;
import com.kyc.nashidmrz.LivenessData;
import com.kyc.nashidmrz.MyUtils;
import com.kyc.nashidmrz.R;
import com.kyc.nashidmrz.Utility;
import com.kyc.nashidmrz.UtilityNFC;
import com.kyc.nashidmrz.mrtd2.BitiAndroid.AbstractNfcActivity;
import com.kyc.nashidmrz.mrtd2.BitiAndroid.TagProvider;
import com.kyc.nashidmrz.mrtd2.BitiMRTD.Constants.MrtdConstants;
import com.kyc.nashidmrz.mrtd2.BitiMRTD.Parser.DG1Parser;
import com.kyc.nashidmrz.mrtd2.BitiMRTD.Parser.DG2Parser;
import com.kyc.nashidmrz.mrtd2.BitiMRTD.Reader.AbstractReader;
import com.kyc.nashidmrz.mrtd2.BitiMRTD.Reader.BacInfo;
import com.kyc.nashidmrz.mrtd2.BitiMRTD.Reader.DESedeReader;
import com.kyc.nashidmrz.mrtd2.BitiMRTD.Reader.ProgressListenerInterface;
import com.kyc.nashidmrz.mrtd2.BitiMRTD.Tools.Tools;
import com.kyc.nashidmrz.mrtd2.interfaceClass.RequestResponse;
import com.kyc.nashidmrz.mrtd2.requestResponse.OkHttpRequestResponse;
import com.mv.liveness.LivenessMainActivity;
import com.mv.liveness.UtilityLive;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;


public class ReadingPassportActivity extends AbstractNfcActivity implements Serializable {

    private String passportNumber;
    private String dateOfBirth;
    private String dateOfExpiration;

    private byte[] dg1;
    private byte[] dg2;
    private byte[] sod;

    private AsyncReader asyncReader;
    private boolean isActivityRunning;

    private ProgressBar mrtdProgressBar;
    ImageView view_photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_passport);
        view_photo = findViewById(R.id.view_photo);



        this.isActivityRunning = true;

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch (Exception e) {

        }

//        this.passportNumber = (String) getIntent().getSerializableExtra("passportNumber");
//        this.dateOfBirth = (String) getIntent().getSerializableExtra("dateOfBirth");
//        this.dateOfExpiration = (String) getIntent().getSerializableExtra("dateOfExpiration");

        this.passportNumber = Utility.getInstance().getPassportNumber();
        this.dateOfBirth =Utility.getInstance().getDateOfBirth();
        this.dateOfExpiration = Utility.getInstance().getExpiryDate();

        this.mrtdProgressBar = (ProgressBar) this.findViewById(R.id.mrtdProgressBar);

        this.readNfc();
    }

    protected void readNfc() {
        this.asyncReader = new AsyncReader(
                this,
                this.passportNumber,
                this.dateOfBirth,
                this.dateOfExpiration
        );
        asyncReader.execute();
    }

    public void showResult() {
        if (this.dg1 != null && this.dg2 != null) {
            LivenessData.getInstance().setPassportNumber(this.passportNumber);

//            UtilityNFC.getInstance().dg1 = this.dg1;
//            UtilityNFC.getInstance().dg2 = this.dg2;
//            UtilityNFC.getInstance().sod = this.sod;

//
//            DG2Parser dg2Parser;
//            dg2Parser = new DG2Parser(UtilityNFC.getInstance().dg2);
//            Bitmap faceImage = dg2Parser.getBitmap();
//            if(faceImage != null) {
//                LivenessData.getInstance().setNfcBitmap(faceImage);
////                LivenessData.getInstance().setNfcBitmap(icon);
//
//            }else {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(ReadingPassportActivity.this,"dg2 file issue",Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }

//
//            Bitmap icon = BitmapFactory.decodeResource(ReadingPassportActivity.this.getResources(),
//                        R.drawable.arun);
//                LivenessData.getInstance().setNfcBitmap(icon);

//            Intent i = new Intent(ReadingPassportActivity.this, LivenessMainActivity.class);
//            startActivityForResult(i, 504);

//            Intent intent = new Intent(ReadingPassportActivity.this, ComparisionSuccessful.class);
            Intent intent = new Intent(ReadingPassportActivity.this, ResultDisplayActivity.class);
            intent.putExtra("dg1", this.dg1);
            intent.putExtra("dg2", this.dg2);
            intent.putExtra("sod", this.sod);
            UtilityNFC.getInstance().dg1 = this.dg1;
            UtilityNFC.getInstance().dg2 = this.dg2;
            UtilityNFC.getInstance().sod = this.sod;
            this.setMrtdProgressBarPercentage(96);
            startActivity(intent);
            this.setMrtdProgressBarPercentage(100);
        } else {
            System.out.println("dg1 or/and dg2 is/are null");
        }
    }

    public void showError(final String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(ReadingPassportActivity.this.isActivityRunning) {
                    new AlertDialog.Builder(ReadingPassportActivity.this)
                            .setTitle(getResources().getString(R.string.error_error))
                            .setMessage(message)
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    ReadingPassportActivity.this.finish();
//                                    Intent i = new Intent(ReadingPassportActivity.this, HomeLibrary.class);
//                                    startActivity(i);

//                                    Intent i = new Intent(ReadingPassportActivity.this, LivenessMainActivity.class);
//                                    startActivityForResult(i, 504);
                                }
                            }).create().show();
                }
            }
        });

    }

    public void setMrtdProgressBarPercentage(int progress) {
        this.mrtdProgressBar.setProgress(progress);
    }

    public void setDg1(byte[] dg1) {
        this.dg1 = dg1;
    }

    public void setDg2(byte[] dg2) {
        this.dg2 = dg2;
    }

    public void setSOD(byte[] sod) {
        this.sod = sod;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.isActivityRunning = false;
                this.asyncReader.cancel();
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.isFinishing()) {
            this.isActivityRunning = false;
            this.asyncReader.cancel();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.isActivityRunning = false;
        this.asyncReader.cancel();
    }

    private class AsyncReader extends AsyncTask<Void, Integer, Boolean> implements ProgressListenerInterface {

        private boolean success = false;
        private WeakReference<ReadingPassportActivity> readingPassportActivity;
        private String passportNumber;
        private String dateOfBirth;
        private String dateOfExpiration;
        private boolean isCanceled = false;
        private int currentStep = 0;

        public AsyncReader(
                ReadingPassportActivity readingPassportActivity,
                String passportNumber,
                String dateOfBirth,
                String dateOfExpiration
        ) {

            this.passportNumber = passportNumber;
            this.dateOfBirth = dateOfBirth;
            this.dateOfExpiration = dateOfExpiration;
            this.isCanceled = false;

            this.link(readingPassportActivity);
        }

        @Override
        protected Boolean doInBackground(Void... arg0) {


            try {
                if (TagProvider.getTag() != null) {

                    System.out.println("GOT TAG");

                    BacInfo bacInfo = new BacInfo();

                    bacInfo.setPassportNbr(this.passportNumber);
                    bacInfo.setDateOfBirth(this.dateOfBirth);
                    bacInfo.setDateOfExpiry(this.dateOfExpiration);

                    AbstractReader mrtd = new DESedeReader();
                    mrtd.setBacInfo(bacInfo);


                    if (mrtd.initSession()) {

                        mrtd.setProgressListener(new WeakReference<Object>(this));

                        this.readingPassportActivity.get().setMrtdProgressBarPercentage(5);

                        this.currentStep = 1;
                        byte[] dg1 = mrtd.readFile(MrtdConstants.FID_DG1);
                        if (dg1 == null) {
                            this.readingPassportActivity.get().showError(getResources().getString(R.string.error_dg1_is_null));
                        }

                        this.readingPassportActivity.get().setMrtdProgressBarPercentage(10);

                        this.readingPassportActivity.get().setDg1(dg1);
                        DG1Parser dg1Parser = new DG1Parser(dg1);
                        Tools tools = new Tools();

                        /*
                        System.out.println("Document Code : ".concat(dg1Parser.getDocumentCode()));
                        System.out.println("Issuing state : ".concat(dg1Parser.getIssuingStateCode()));
                        System.out.println("Document Number : ".concat(dg1Parser.getDocumentNumber()));
                        System.out.println("Gender : ".concat(dg1Parser.getGender()));
                        System.out.println("Given names : ".concat(dg1Parser.getGivenNames()));
                        System.out.println("Surname : ".concat(dg1Parser.getSurname()));
                        System.out.println("Nationality : ".concat(dg1Parser.getNationalityCode()));
                        System.out.println("Date of birth : ".concat(dg1Parser.getDateOfBirth()));
                        System.out.println("Date of Expiry : ".concat(dg1Parser.getDateOfExpiry()));
                        System.out.println("File content : ".concat(tools.bytesToString(dg1)));*/

                        this.currentStep = 2;
                        byte[] dg2 = mrtd.readFile(MrtdConstants.FID_DG2);
                        if (dg2 == null) {
                            this.readingPassportActivity.get().showError(getResources().getString(R.string.error_dg2_is_null));
                        }
                        this.readingPassportActivity.get().setDg2(dg2);

                        this.currentStep = 3;
                        byte[] efsod = mrtd.readFile(MrtdConstants.FID_EF_SOD);
                        this.readingPassportActivity.get().setSOD(efsod);

                        if (dg1 != null && dg2 != null) {
                            this.success = true;
                        }

                    } else {
                        System.out.println("Failed to init session");
                        this.readingPassportActivity.get().showError(getResources().getString(R.string.error_mutual_authentication_failed));
                        TagProvider.closeTag();
                        return false;
                    }
                } else {
                    System.out.println("Couldn't get Tag from intent");
                    this.readingPassportActivity.get().showError(getResources().getString(R.string.error_lost_connexion));
                    return false;
                }
            } catch (Exception e) {
                System.out.println("Exception");
                this.readingPassportActivity.get().showError(getResources().getString(R.string.error_nfc_exception));
                return false;
            }

            if(!this.success) {
                this.readingPassportActivity.get().showError(getResources().getString(R.string.error_nfc_exception));
            }

            this.readingPassportActivity.get().setMrtdProgressBarPercentage(95);

            return true;
        }

        public void link(ReadingPassportActivity readingPassportActivity) {
            this.readingPassportActivity = new WeakReference<ReadingPassportActivity>(readingPassportActivity);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (this.success) {
                this.readingPassportActivity.get().showResult();
                this.readingPassportActivity.get().finish();
            }
        }

        public void updateProgress(int progress) {
            switch (currentStep) {
                case 1:
                    this.readingPassportActivity.get().setMrtdProgressBarPercentage(Math.round(progress * 10 / 100));
                    break;
                case 2:
                    this.readingPassportActivity.get().setMrtdProgressBarPercentage(Math.round(progress * 75 / 100) + 10);
                    break;
                case 3:
                    this.readingPassportActivity.get().setMrtdProgressBarPercentage(Math.round(progress * 10 / 100) + 85);
                    break;
            }
        }

        public void cancel() {
            this.isCanceled = true;
        }

        @Override
        protected void onCancelled() {
            this.isCanceled = true;
            super.onCancelled();
        }

        @Override
        public boolean isCanceled() {
            return this.isCanceled;
        }
    }


    public void callToComparision(){
        Intent intent = new Intent(ReadingPassportActivity.this, ComparisionSuccessful.class);
        this.setMrtdProgressBarPercentage(96);
        startActivity(intent);
        this.setMrtdProgressBarPercentage(100);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 504) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    try {
                        DG2Parser dg2Parser;
                        dg2Parser = new DG2Parser(UtilityNFC.getInstance().dg2);
                        Bitmap faceImage = dg2Parser.getBitmap();
                        if(faceImage != null) {
                          LivenessData.getInstance().setNfcBitmap(faceImage);
                            try {
                                byte[] liveness = new byte[0];
                                try {
                                    liveness = UtilityLive.getInstance().liveImage;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(ReadingPassportActivity.this,"liveness",Toast.LENGTH_SHORT).show();

                                }
                                view_photo.setImageBitmap(LivenessData.getInstance().getNfcImage());
                                MyUtils.getInstance().showProgressDialog(ReadingPassportActivity.this);

                                OkHttpRequestResponse.getInstance().uploadFile(ReadingPassportActivity.this, byteArray(view_photo), liveness, requestResponseFace);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(ReadingPassportActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ReadingPassportActivity.this,"dg2 file issue",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ReadingPassportActivity.this,"dg2 file issue with Exception"+e.toString(),Toast.LENGTH_SHORT).show();

                    }


//                    Bitmap icon = BitmapFactory.decodeResource(ReadingPassportActivity.this.getResources(),
//                        R.drawable.arun);
//                LivenessData.getInstance().setNfcBitmap(icon);

//                    try {
//                        byte[] liveness = UtilityLive.getInstance().liveImage;
//                        view_photo.setImageBitmap(LivenessData.getInstance().getNfcImage());
//                        MyUtils.getInstance().showProgressDialog(ReadingPassportActivity.this);
//
//                        OkHttpRequestResponse.getInstance().uploadFile(ReadingPassportActivity.this, byteArray(view_photo), liveness, requestResponseFace);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Toast.makeText(ReadingPassportActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
//                    }

                }
            }, 0);
        }
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



}
