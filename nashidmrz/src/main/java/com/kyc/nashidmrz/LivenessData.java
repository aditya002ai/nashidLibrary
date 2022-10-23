package com.kyc.nashidmrz;

import android.graphics.Bitmap;

public class LivenessData {
    public Bitmap getNfcImage() {
        return nfcImage;
    }

    public void setNfcBitmap(Bitmap nfcImage) {
        this.nfcImage = nfcImage;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    private Bitmap nfcImage;
    private String passportNumber;

    private static final LivenessData ourInstance = new LivenessData();

    public static LivenessData getInstance() {
        return ourInstance;
    }

    private LivenessData() {
    }
}
