package com.kyc.nashidmrz.mrtd2.requestResponse;

import android.content.Context;
import android.util.Log;

import com.kyc.nashidmrz.MyUtils;
import com.kyc.nashidmrz.mrtd2.interfaceClass.RequestResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpRequestResponse {
    private static final OkHttpRequestResponse ourInstance = new OkHttpRequestResponse();

    public OkHttpRequestResponse() {
    }

    public static OkHttpRequestResponse getInstance() {
        return ourInstance;
    }



    public static Boolean uploadFile(final Context context, byte[] f1, byte[] f2, final RequestResponse requestResponse) {
        try {
//            String URL = "http://192.168.1.20:5001/face_match";
            String URL = "http://185.64.25.107:5001/face_match";

            HttpUrl.Builder urlBuilder = HttpUrl.parse(URL).newBuilder();
            String url = urlBuilder.build().toString();

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(360, TimeUnit.SECONDS)
                    .writeTimeout(360, TimeUnit.SECONDS)
                    .readTimeout(360, TimeUnit.SECONDS)
                    .build();


            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
//                    .addFormDataPart("file1", file1.getName(), RequestBody.create(MediaType.parse("image/jpg"), file1))
//                    .addFormDataPart("file1", file1.getName(), RequestBody.create(MediaType.parse("image/*"), file1))
//                    .addFormDataPart("file1", file1.getName(), RequestBody.create(MediaType.parse("rb"), file1))
//                    .addFormDataPart("file2", file2.getName(), RequestBody.create(MediaType.parse("rb"), file2))
                    .addFormDataPart("file1", "filename1.jpg",
                            RequestBody.create(MediaType.parse("image/*jpg"), f1))
                    .addFormDataPart("file2", "filename2.jpg",
                            RequestBody.create(MediaType.parse("image/*jpg"), f2))
                    .build();


            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    MyUtils.getInstance().dismissDialog();
                    Log.d("error",e.toString());

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String res = response.body().string();
//                        MyUtils.getInstance().dismissDialog();
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(res);
                            requestResponse.myResponse(obj);
//                        showMessage(context,"Match : "+ obj.getDouble("match"));
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            MyUtils.getInstance().dismissDialog();

                        }

                        // Handle the error
                    } else {
//                        showMessage(context, "Face Not Found in the Image");
//                        MyUtils.getInstance().dismissDialog();

                    }
                }
            });

            return true;
        } catch (Exception ex) {
//            MyUtils.getInstance().dismissDialog();
            // Handle the error
        }
        return false;
    }
}
