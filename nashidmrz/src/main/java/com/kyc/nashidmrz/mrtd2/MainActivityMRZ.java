package com.kyc.nashidmrz.mrtd2;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;
import com.kyc.nashidmrz.R;
import com.kyc.nashidmrz.Utility;
import com.kyc.nashidmrz.mrtd2.BitiAndroid.AbstractNfcActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivityMRZ extends AbstractNfcActivity implements DatePickerDialog.OnDateSetListener
{

    SVGImageView logo, back;
    Button Next;
    public int selectedDateField;
    public String dateOfBirth = "000000";
    public int[] dateOfBirthIntArray = {15,6,1980};
    public String dateOfExpiration = "000000";
    public int[] dateOfExpirationIntArray = {15,6,2020};
    ImageView imageview;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mrz);
        ((EditText)findViewById(R.id.DateOfBirth)).setText("");
        ((EditText)findViewById(R.id.DateOfExpiration)).setText("");
        logo = findViewById(R.id.logo);
        back = findViewById(R.id.backbutton);
        Next = findViewById(R.id.ReadNfcBtn);
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

        this.dateOfBirth = dateOfBirth;
        this.dateOfExpiration = expiryDate;

        Date selectedDatedateOfBirth = null;
        try {
            selectedDatedateOfBirth = (new SimpleDateFormat("yyMMdd")).parse(dateOfBirth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String passportDateDisplay = (new SimpleDateFormat("dd MMM yyyy")).format(selectedDatedateOfBirth);
        ((EditText)findViewById(R.id.DateOfBirth)).setText(passportDateDisplay);



        Date selectedDatedateOfExpire = null;
        try {
            selectedDatedateOfExpire = (new SimpleDateFormat("yyMMdd")).parse(expiryDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String passportDateExpiriDisplay = (new SimpleDateFormat("dd MMM yyyy")).format(selectedDatedateOfExpire);
        ((EditText)findViewById(R.id.DateOfExpiration)).setText(passportDateExpiriDisplay);



//
//        String passportDate = (new SimpleDateFormat("yyMMdd")).format(dateOfBirth);
//        String passportexpiryDate = (new SimpleDateFormat("yyMMdd")).format(expiryDate);
//        ((EditText)findViewById(R.id.DateOfExpiration)).setText(passportexpiryDate);
//

        ((EditText) findViewById(R.id.PassportNbr)).setText(passport);


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
//                Intent i = new Intent(MainActivityMRZ.this, ComparisionSuccessful.class);
//                startActivity(i);

            }
        });
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivityMRZ.this, WaitingForNfcActivity.class);
                startActivity(i);

            }
        });


        ((EditText)findViewById(R.id.PassportNbr)).setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        findViewById(R.id.MainLayout).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        findViewById(R.id.PassportNbr).clearFocus();
                        findViewById(R.id.DateOfBirth).clearFocus();
                        findViewById(R.id.DateOfExpiration).clearFocus();
                    }
                }
        );

        ((EditText)findViewById(R.id.DateOfBirth)).setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
                        if (hasFocus) {
                            MainActivityMRZ.this.selectedDateField = 1;
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            MainActivityMRZ.this.displayDatePickerDialog();
                        }
                    }
                }
        );

        ((EditText)findViewById(R.id.DateOfExpiration)).setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
                        if (hasFocus) {
                            MainActivityMRZ.this.selectedDateField = 2;
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            MainActivityMRZ.this.displayDatePickerDialog();
                        }
                    }
                }
        );

        findViewById(R.id.ReadNfcBtn).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        try {

                            Intent intent = new Intent("bondi.nfcPassportReader.jan.mrtd2.WaitingForNfcActivity");

                            String passportNumber = ((EditText) findViewById(R.id.PassportNbr)).getText().toString();

                            intent.putExtra("passportNumber", passportNumber);
                            intent.putExtra("dateOfBirth", MainActivityMRZ.this.dateOfBirth);
                            intent.putExtra("dateOfExpiration", MainActivityMRZ.this.dateOfExpiration);

                            startActivity(intent);
                        }
                        catch(Exception e) {
                            //@TODO
                        }

                    }
                }
        );

    }

    @Override
    public void onResume() {
        super.onResume();

        // if values are not the default values (HACK)
        if(!(this.dateOfBirthIntArray[0] == 15 && this.dateOfBirthIntArray[1] ==  6 && this.dateOfBirthIntArray[2] == 1980)) {
            this.setDateToTextView("dob", this.dateOfBirthIntArray[2], this.dateOfBirthIntArray[1], this.dateOfBirthIntArray[0]);
        }
        if(!(this.dateOfExpirationIntArray[0] == 15 && this.dateOfExpirationIntArray[1] == 6 && this.dateOfExpirationIntArray[2] == 2020)) {
            this.setDateToTextView("doe", this.dateOfExpirationIntArray[2], this.dateOfExpirationIntArray[1], this.dateOfExpirationIntArray[0]);
        }
    }

    public void displayDatePickerDialog()
    {

        findViewById(R.id.DateOfBirth).clearFocus();
        findViewById(R.id.DateOfExpiration).clearFocus();
        findViewById(R.id.PassportNbr).clearFocus();

        Calendar calendar = Calendar.getInstance();

        //date of expiration
        int calendarYear = this.dateOfExpirationIntArray[2];
        int calendarMonth = this.dateOfExpirationIntArray[1];
        int calendarDay = this.dateOfExpirationIntArray[0];

        if(this.selectedDateField == 1) { // date of birth
            calendarYear = this.dateOfBirthIntArray[2];
            calendarMonth = this.dateOfBirthIntArray[1];
            calendarDay = this.dateOfBirthIntArray[0];
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                MainActivityMRZ.this,
                MainActivityMRZ.this,
                calendarYear,
                calendarMonth,
                calendarDay
        );

        datePickerDialog.getDatePicker().setCalendarViewShown(false);
        datePickerDialog.getDatePicker().setSpinnersShown(true);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
    {
        if (this.selectedDateField == 1) {
            this.setDateToTextView("dob", year, monthOfYear, dayOfMonth);
        }
        if (this.selectedDateField == 2) {
            this.setDateToTextView("doe", year, monthOfYear, dayOfMonth);
        }
    }

    public void setDateToTextView(String fieldName, int year, int monthOfYear, int dayOfMonth)
    {
        try {
            monthOfYear += 1;

            String displayDate = String.valueOf(dayOfMonth)
                    .concat("/")
                    .concat(String.valueOf(monthOfYear))
                    .concat("/")
                    .concat(String.valueOf(year));

            Date selectedDate = (new SimpleDateFormat("dd/MM/yyyy")).parse(displayDate);
            String passportDate = (new SimpleDateFormat("yyMMdd")).format(selectedDate);

            displayDate = SimpleDateFormat.getDateInstance().format(selectedDate);

            if (fieldName.equals("dob")) {
                ((EditText) findViewById(R.id.DateOfBirth)).setText(displayDate);
                this.dateOfBirth = passportDate;

                this.dateOfBirthIntArray[2] = year;
                this.dateOfBirthIntArray[1] = monthOfYear - 1;
                this.dateOfBirthIntArray[0] = dayOfMonth;
            }
            if (fieldName.equals("doe")) {
                ((EditText) findViewById(R.id.DateOfExpiration)).setText(displayDate);
                this.dateOfExpiration = passportDate;

                this.dateOfExpirationIntArray[2] = year;
                this.dateOfExpirationIntArray[1] = monthOfYear - 1;
                this.dateOfExpirationIntArray[0] = dayOfMonth;
            }
        }
        catch (Exception e) {

        }
    }
}
