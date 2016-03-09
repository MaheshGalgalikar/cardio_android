package md.fusionworks.android.cardio.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import md.fusionworks.android.cardio.services.BackgroundDataGeneratorService;
import md.fusionworks.android.cardio.services.ServiceManager;

/**
 * Created by admin on 20.08.2015.
 */
public class DeviceBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        ServiceManager.newInstance(context).startBackgroundDataGeneratorService();
    }
}
