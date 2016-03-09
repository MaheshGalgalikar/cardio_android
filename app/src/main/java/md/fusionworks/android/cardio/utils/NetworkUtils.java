package md.fusionworks.android.cardio.utils;

import android.content.Context;
import android.net.wifi.WifiManager;

/**
 * Created by admin on 05.08.2015.
 */
public class NetworkUtils {

    public static boolean isWifiEnabled(Context context) {

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

}
