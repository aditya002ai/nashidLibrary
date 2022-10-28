package com.kyc.nashidmrz;

public class UtilityNFC {

    public byte[] dg1;
    public byte[] dg2;
    public byte[] sod;

    public String matchPercentage = "0 %";
    public int confidenceValue = 10;
    public String passportNumber;
    public String dateOfBirth;
    public String dateOfExpiration;


    private static final UtilityNFC ourInstance = new UtilityNFC();


    private UtilityNFC() {
    }

    public static UtilityNFC getInstance() {
        return ourInstance;
    }

}

