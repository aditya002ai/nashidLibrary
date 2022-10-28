package com.kyc.nashidmrz.mrtd2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.kyc.nashidmrz.ComparisionSuccessful;
import com.kyc.nashidmrz.LivenessData;
import com.kyc.nashidmrz.MyUtils;
import com.kyc.nashidmrz.R;
import com.kyc.nashidmrz.UtilityNFC;
import com.kyc.nashidmrz.mrtd2.BitiAndroid.AbstractNfcActivity;
import com.kyc.nashidmrz.mrtd2.BitiMRTD.Parser.DG1Parser;
import com.kyc.nashidmrz.mrtd2.BitiMRTD.Parser.DG2Parser;
import com.kyc.nashidmrz.mrtd2.BitiMRTD.Parser.EFSODParser;
import com.kyc.nashidmrz.mrtd2.interfaceClass.RequestResponse;
import com.kyc.nashidmrz.mrtd2.requestResponse.OkHttpRequestResponse;
import com.mv.liveness.LivenessMainActivity;
import com.mv.liveness.UtilityLive;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;


public class ResultDisplayActivity extends AbstractNfcActivity implements Serializable
{
    ImageView view_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_display);
        view_photo = findViewById(R.id.view_photo);

        Button clickLiveness = findViewById(R.id.clickLiveness);
        clickLiveness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(ResultDisplayActivity.this, LivenessMainActivity.class);
//                startActivity(intent);

                            Intent i = new Intent(ResultDisplayActivity.this, LivenessMainActivity.class);
            startActivityForResult(i, 504);

            }
        });


        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch (Exception e) {

        }

        byte[] dg1 = (byte[]) getIntent().getSerializableExtra("dg1");
        byte[] dg2 = (byte[]) getIntent().getSerializableExtra("dg2");
        byte[] sod = (byte[]) getIntent().getSerializableExtra("sod");

        UtilityNFC.getInstance().dg1 = dg1;
        UtilityNFC.getInstance().dg2 = dg2;
        UtilityNFC.getInstance().sod = sod;


        DG2Parser dg2Parser;

        dg2Parser = new DG2Parser(dg2);
        Bitmap faceImage = dg2Parser.getBitmap();

        ResultFragmentPagerAdapter resultFragmentPagerAdapter = new ResultFragmentPagerAdapter(getSupportFragmentManager());

        resultFragmentPagerAdapter.setDg1DataList(this.createDG1List(dg1));
        if(faceImage != null) {
            resultFragmentPagerAdapter.setFaceImage(faceImage);
            LivenessData.getInstance().setNfcBitmap(faceImage);
        }
        resultFragmentPagerAdapter.setSodDataList(this.createSodDataList(sod));

        ViewPager resultContentViewPager = (ViewPager)this.findViewById(R.id.resultContentViewPager);
        resultContentViewPager.setAdapter(
                resultFragmentPagerAdapter
        );
    }

    private ArrayList<String[]> createSodDataList(byte[] sod)
    {
        EFSODParser sodParser = new EFSODParser(sod);
        ArrayList<String[]> sodDataList = new ArrayList<String[]>();

        String[] issuerCountry = {getResources().getString(R.string.information_issuer_country), sodParser.getIssuerCountry()};
        sodDataList.add(issuerCountry);

        String[] issuerCertificationAuthority = {getResources().getString(R.string.information_certification_authority), sodParser.getIssuerCertificationAuthority()};
        sodDataList.add(issuerCertificationAuthority);

        String[] issuerOrganization = {getResources().getString(R.string.information_issuer_organization), sodParser.getIssuerOrganization()};
        sodDataList.add(issuerOrganization);

        String[] issuerOrganizationalUnit = {getResources().getString(R.string.information_issuer_organizational_unit), sodParser.getIssuerOrganizationalUnit()};
        sodDataList.add(issuerOrganizationalUnit);

        String[] signatureAlgorithm = {getResources().getString(R.string.information_signature_algorithm), sodParser.getSignatureAlgorithm()};
        sodDataList.add(signatureAlgorithm);

        String[] ldsHashAlgorithm = {getResources().getString(R.string.information_lds_hash_algorithm), sodParser.getLdsHashAlgorithm()};
        sodDataList.add(ldsHashAlgorithm);

        String[] validFromString = {getResources().getString(R.string.information_valid_from), sodParser.getValidFromString()};
        sodDataList.add(validFromString);

        String[] validUntilString = {getResources().getString(R.string.information_valid_until), sodParser.getValidUntilString()};
        sodDataList.add(validUntilString);

        return sodDataList;
    }

    private ArrayList<String[]> createDG1List(byte[] dg1)
    {
        DG1Parser dg1Parser = new DG1Parser(dg1);

        ArrayList<String[]> dg1DataList = new ArrayList<String[]>();

        String[] documentType = {getResources().getString(R.string.information_document_type), dg1Parser.getDocumentCode()};
        dg1DataList.add(documentType);

        String[] issuingStateCode = {getResources().getString(R.string.information_issued_by), dg1Parser.getIssuingStateCode()};
        dg1DataList.add(issuingStateCode);

        String[] documentNumber = {getResources().getString(R.string.information_document_number), dg1Parser.getDocumentNumber()};
        dg1DataList.add(documentNumber);

        String[] dateOfExpiry = {
                getResources().getString(R.string.information_date_of_expiry),
                this.format6digitDate(dg1Parser.getDateOfExpiry(), 2000)
        };
        dg1DataList.add(dateOfExpiry);

        String genderValue = dg1Parser.getGender();
        if(dg1Parser.getGender().equals("M")) {
            genderValue = getResources().getString(R.string.information_gender_male);
        }
        if(dg1Parser.getGender().equals("F")) {
            genderValue = getResources().getString(R.string.information_gender_female);
        }

        String[] gender = {getResources().getString(R.string.information_gender), genderValue};
        dg1DataList.add(gender);

        String[] nationality = {getResources().getString(R.string.information_nationality), dg1Parser.getNationalityCode()};
        dg1DataList.add(nationality);

        String[] lastName = {getResources().getString(R.string.information_surname), dg1Parser.getSurname()};
        dg1DataList.add(lastName);

        String[] firstName = {getResources().getString(R.string.information_given_names), dg1Parser.getGivenNames()};
        dg1DataList.add(firstName);

        String[] dateOfBirth = {
                getResources().getString(R.string.information_date_of_birth),
                this.format6digitDate(dg1Parser.getDateOfBirth(), 1915)
        };
        dg1DataList.add(dateOfBirth);

        return dg1DataList;
    }

    private String format6digitDate(String mrtdDate, int startingYear)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd");
        simpleDateFormat.set2DigitYearStart(new GregorianCalendar(startingYear,1,1).getTime());
        try {
            Date mrtdDateObject = simpleDateFormat.parse(mrtdDate);
            return SimpleDateFormat.getDateInstance().format(mrtdDateObject);
        }
        catch(Exception e) {
            return "";
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ResultFragmentPagerAdapter extends FragmentPagerAdapter
    {

        private Bitmap faceImage;
        private ArrayList<String[]> dg1DataList;
        private ArrayList<String[]> sodDataList;

        public ResultFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Dg1Fragment dg1Fragment = new Dg1Fragment();
                    dg1Fragment.setDg1List(this.dg1DataList);
                    return dg1Fragment;
                case 1:
                    Dg2Fragment dg2Fragment = new Dg2Fragment();
                    dg2Fragment.setBitmap(this.faceImage);
                    return dg2Fragment;
                case 2:
                    SodFragment sodFragment = new SodFragment();
                    sodFragment.setSodList(this.sodDataList);
                    return sodFragment;
            }
            return null;
        }

        public void setDg1DataList(ArrayList<String[]> dg1DataList)
        {
            this.dg1DataList = dg1DataList;
        }

        public void setFaceImage(Bitmap faceImage)
        {
            this.faceImage = faceImage;
        }

        public void setSodDataList(ArrayList<String[]> sodDataList)
        {
            this.sodDataList = sodDataList;
        }


        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.information_tab_text);
                case 1:
                    return getResources().getString(R.string.image_tab_text);
                case 2:
                    return getResources().getString(R.string.security_tab_text);
            }
            return null;
        }
    }



    public void callToComparision(){
        Intent intent = new Intent(ResultDisplayActivity.this, ComparisionSuccessful.class);
        startActivity(intent);
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
                                    Toast.makeText(ResultDisplayActivity.this,"liveness",Toast.LENGTH_SHORT).show();

                                }
                                view_photo.setImageBitmap(LivenessData.getInstance().getNfcImage());
                                MyUtils.getInstance().showProgressDialog(ResultDisplayActivity.this);

                                OkHttpRequestResponse.getInstance().uploadFile(ResultDisplayActivity.this, byteArray(view_photo), liveness, requestResponseFace);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(ResultDisplayActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ResultDisplayActivity.this,"dg2 file issue",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ResultDisplayActivity.this,"dg2 file issue with Exception"+e.toString(),Toast.LENGTH_SHORT).show();

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
