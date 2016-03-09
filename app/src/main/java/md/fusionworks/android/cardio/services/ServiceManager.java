package md.fusionworks.android.cardio.services;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import md.fusionworks.android.cardio.models.Data;
import md.fusionworks.android.cardio.utils.CommonConstants;

/**
 * Created by admin on 20.08.2015.
 */
public class ServiceManager {

    Context context;

    public ServiceManager(Context context) {
        this.context = context;
    }

    public static ServiceManager newInstance(Context context) {

        return new ServiceManager(context);
    }

    public void startSendDataToServerService(ArrayList<Data> dataList, long packetId) {

        Intent sendDataToServerServiceIntent = new Intent(context, SendDataToServerService.class);
        sendDataToServerServiceIntent.putParcelableArrayListExtra(CommonConstants.EXTRA_PARAM_DATA, dataList);
        sendDataToServerServiceIntent.putExtra(CommonConstants.EXTRA_PARAM_PACKET_ID, packetId);
        context.startService(sendDataToServerServiceIntent);

    }

    public void startSendFileDataToServerService(String fileName) {

        Intent sendFileDataToServerServiceIntent = new Intent(context, SendFileDataToServerService.class);
        sendFileDataToServerServiceIntent.putExtra(CommonConstants.EXTRA_PARAM_FILE_NAME, fileName);
        context.startService(sendFileDataToServerServiceIntent);
    }

    public void startBackgroundDataGeneratorService() {

        Intent backgroundDataGeneratorServiceIntent = new Intent(context, BackgroundDataGeneratorService.class);
        context.startService(backgroundDataGeneratorServiceIntent);
    }

    public void stopBackgroundDataGeneratorService() {

        Intent backgroundDataGeneratorServiceIntent = new Intent(context, BackgroundDataGeneratorService.class);
        context.stopService(backgroundDataGeneratorServiceIntent);
    }

    public void startRealTimeDataGeneratorService() {

        stopBackgroundDataGeneratorService();
        Intent dataGeneratorServiceIntent = new Intent(context, RealTimeDataGeneratorService.class);
        context.startService(dataGeneratorServiceIntent);
    }

    public void stopRealTimeDataGeneratorService() {

        Intent dataGeneratorServiceIntent = new Intent(context, RealTimeDataGeneratorService.class);
        context.stopService(dataGeneratorServiceIntent);
        startBackgroundDataGeneratorService();
    }
}
