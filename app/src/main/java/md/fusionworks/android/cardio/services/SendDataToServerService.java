package md.fusionworks.android.cardio.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import md.fusionworks.android.cardio.api.ServiceGenerator;
import md.fusionworks.android.cardio.api.models.DataRequest;
import md.fusionworks.android.cardio.models.Data;
import md.fusionworks.android.cardio.api.services.CardioService;
import md.fusionworks.android.cardio.storage.LocalStorage;
import md.fusionworks.android.cardio.utils.CommonConstants;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by admin on 18.08.2015.
 */
public class SendDataToServerService extends IntentService {

    public SendDataToServerService() {
        super(SendDataToServerService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        List<Data> dataList = new ArrayList<>();
        long packetId;
        dataList = intent.getParcelableArrayListExtra(CommonConstants.EXTRA_PARAM_DATA);
        packetId = intent.getLongExtra(CommonConstants.EXTRA_PARAM_PACKET_ID, 0);

        Log.w("cardio", "send real data to server " + packetId);

        CardioService cardioService = ServiceGenerator.createService(CardioService.class, CardioService.URL_CARDIO, LocalStorage.from(getApplicationContext()).getAuthToken());
        cardioService.data(new DataRequest(dataList, packetId), new Callback<String>() {
            @Override
            public void success(String s, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
