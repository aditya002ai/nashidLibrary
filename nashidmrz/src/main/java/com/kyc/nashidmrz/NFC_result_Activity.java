package com.kyc.nashidmrz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;
import com.kyc.nashidmrz.mrtd2.BitiMRTD.Parser.DG1Parser;
import com.kyc.nashidmrz.mrtd2.BitiMRTD.Parser.EFSODParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class NFC_result_Activity extends AppCompatActivity {
    SVGImageView logo,back;
    ImageView NFCimage;
    TextView DocumnetNumber,FirstName,LastName,Dob,Gender,ExprieDate,Nationlity,DocumnetType,IssuedBy,IssuerCountry,CeroficationAuthority,IssuerOrganization,OrganizationalUnit,SignatureAlgorithm,LDsHAshAlgorithm,CertificateVaildFrom,CertificateVaildUntil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_result);
        logo = findViewById(R.id.logo);
        back = findViewById(R.id.backbutton);
        NFCimage = findViewById(R.id.nfcimg);
        DocumnetNumber = findViewById(R.id.documnetNumber);
        FirstName = findViewById(R.id.Name);
        LastName = findViewById(R.id.Lastname);
        Dob = findViewById(R.id.DateOfBirth);
        Gender = findViewById(R.id.gender);
        ExprieDate = findViewById(R.id.ExpriDate);
        Nationlity = findViewById(R.id.nationality);
        DocumnetType = findViewById(R.id.documnetType);
        IssuedBy = findViewById(R.id.issuedby);
        IssuerCountry = findViewById(R.id.issuedCountry);
        CeroficationAuthority = findViewById(R.id.cerificationAuthority);
        IssuerOrganization = findViewById(R.id.issuerOrganization);
        OrganizationalUnit = findViewById(R.id.organizationalUnit);
        SignatureAlgorithm = findViewById(R.id.signatureAlgorithm);
        LDsHAshAlgorithm = findViewById(R.id.ledHashAlgorithm);
        CertificateVaildFrom = findViewById(R.id.certificateValidFrom);
        CertificateVaildUntil = findViewById(R.id.certificateValidUntil);
        try {
            SVG svg = SVG.getFromResource(getResources(), R.raw.logo);
            logo.setSVG(svg);
            SVG back1 = SVG.getFromResource(getResources(), R.raw.arowback);
            back.setSVG(back1);
    }catch (Exception e) {
            e.printStackTrace();
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                Intent i=new Intent(NFC_result_Activity.this,ComparisionSuccessful.class);
//                startActivity(i);

            }
        });

        try {
            NFCimage.setImageBitmap(LivenessData.getInstance().getNfcImage());
        } catch (Exception e) {
            e.printStackTrace();
        }


        byte[] dg1 = UtilityNFC.getInstance().dg1;
        byte[] dg2 = UtilityNFC.getInstance().dg2;
        byte[] sod = UtilityNFC.getInstance().sod;


        try {
            NFCimage.setImageBitmap(LivenessData.getInstance().getNfcImage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        createDG1List(dg1);

        createSodDataList(sod);
    }

    private void createDG1List(byte[] dg1)
    {
        DG1Parser dg1Parser = new DG1Parser(dg1);
        DocumnetType.setText(dg1Parser.getDocumentCode());
        IssuedBy.setText(dg1Parser.getIssuingStateCode());
        DocumnetNumber.setText(dg1Parser.getDocumentNumber());
        ExprieDate.setText(format6digitDate(dg1Parser.getDateOfExpiry(), 2000));
        String genderValue = dg1Parser.getGender();
        if(dg1Parser.getGender().equals("M")) {
            genderValue = getResources().getString(R.string.information_gender_male);
        }
        if(dg1Parser.getGender().equals("F")) {
            genderValue = getResources().getString(R.string.information_gender_female);
        }
        Gender.setText(genderValue);
        Nationlity.setText(dg1Parser.getNationalityCode());
        LastName.setText(dg1Parser.getSurname());
        FirstName.setText(dg1Parser.getGivenNames());
        Dob.setText(format6digitDate(dg1Parser.getDateOfBirth(), 1915));
    }


    private void createSodDataList(byte[] sod)
    {
        try {
            EFSODParser sodParser = new EFSODParser(sod);
            IssuerCountry.setText(sodParser.getIssuerCountry());
            CeroficationAuthority.setText(sodParser.getIssuerCertificationAuthority());
            IssuerOrganization.setText(sodParser.getIssuerOrganization());
            OrganizationalUnit.setText(sodParser.getIssuerOrganizationalUnit());
            SignatureAlgorithm.setText(sodParser.getSignatureAlgorithm());
            LDsHAshAlgorithm.setText(sodParser.getLdsHashAlgorithm());
            CertificateVaildFrom.setText(sodParser.getValidFromString());
            CertificateVaildFrom.setText(sodParser.getValidUntilString());
        } catch (Exception e) {
            e.printStackTrace();
        }

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






}