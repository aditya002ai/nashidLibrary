package com.kyc.nashidmrz.mrtd2.BitiAndroid;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;

import com.kyc.nashidmrz.R;


public abstract class AbstractNfcActivity extends AppCompatActivity
{
    protected NfcAdapter mNfcAdapter;
    protected PendingIntent pendingIntent;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null || !mNfcAdapter.isEnabled()) {
            System.out.println("failed to get NFC adapter, NFC disabled?");

            new AlertDialog.Builder(AbstractNfcActivity.this)
                    .setTitle(getResources().getString(R.string.error_error))
                            .setMessage(getResources().getString(R.string.error_nfc_is_disabled))
                                    .setCancelable(true)
                                    .setPositiveButton("enable", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
                                            startActivity(intent);
                                        }
                                    }).create().show();
        }
        else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                Intent intent = new Intent(AbstractNfcActivity.this,
                        AbstractNfcActivity.this.getClass()).addFlags(
                        Intent.FLAG_ACTIVITY_SINGLE_TOP);
                pendingIntent = PendingIntent.getActivity(
                        AbstractNfcActivity.this, 0, intent, PendingIntent.FLAG_MUTABLE);

            } else {

                pendingIntent = PendingIntent.getActivity(
                        this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
//
//                pendingIntent = PendingIntent.getActivity(this,
//                        0, new Intent(AbstractNfcActivity.this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_UPDATE_CURRENT);

            }


//
//            pendingIntent = PendingIntent.getActivity(
//                    this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        }
    }


    public void onPause()
    {
        super.onPause();
        mNfcAdapter.disableForegroundDispatch(this);
    }

    public void onResume()
    {
        super.onResume();
        try {
        if(mNfcAdapter != null && pendingIntent != null) {
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
        }catch (Exception e) {
            System.out.println("onResume error");
        }
    }

    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        TagProvider.setTag(IsoDep.get(tagFromIntent));
        System.out.println("Got new intent!");
    }
}
