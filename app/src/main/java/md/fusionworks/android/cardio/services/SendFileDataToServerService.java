package md.fusionworks.android.cardio.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import md.fusionworks.android.cardio.api.ServiceGenerator;
import md.fusionworks.android.cardio.api.services.CardioService;
import md.fusionworks.android.cardio.models.Data;
import md.fusionworks.android.cardio.storage.LocalStorage;
import md.fusionworks.android.cardio.utils.CommonConstants;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class SendFileDataToServerService extends IntentService {

    public SendFileDataToServerService() {
        super(SendFileDataToServerService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.w("cardio", "send file data to server");
        String fileName = intent.getStringExtra(CommonConstants.EXTRA_PARAM_FILE_NAME);
        final File file = new File(getFilesDir(), fileName);

        TypedFile typedFile = new TypedFile("multipart/form-data", file);
        CardioService cardioService = ServiceGenerator.createService(CardioService.class, CardioService.URL_CARDIO, LocalStorage.from(getApplicationContext()).getAuthToken());
        cardioService.uploadFileData(typedFile, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.w("cardio", "send file data to server s");

                boolean deleted = file.delete();
                Log.w("cardio", "file deleted " + deleted);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.w("cardio", "send file data to server e");

                boolean deleted = file.delete();
                Log.w("cardio", "file deleted " + deleted);
            }
        });
    }
}
