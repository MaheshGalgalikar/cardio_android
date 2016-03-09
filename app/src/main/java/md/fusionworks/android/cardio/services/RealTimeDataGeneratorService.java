package md.fusionworks.android.cardio.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.common.collect.Iterators;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import md.fusionworks.android.cardio.models.Data;
import md.fusionworks.android.cardio.storage.LocalStorage;
import md.fusionworks.android.cardio.utils.CommonConstants;
import md.fusionworks.android.cardio.utils.NetworkUtils;

public class RealTimeDataGeneratorService extends Service {

    public final static String PASS_DATA_TO_DRAW_ACTION = "PASS_DATA_TO_DRAW_ACTION";
    public static final int DELAY = CommonConstants.DATA_TO_DRAW_LIST_SIZE * 2;

    private boolean stopService = false;
    private Thread thread = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (thread == null) {
            thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    List<Data> fakeDataList = parseCSVFileWithFakeData();
                    Iterator<Data> circular = Iterators.cycle(fakeDataList);
                    ArrayList<Data> dataToSendList = new ArrayList<>();
                    ArrayList<Data> dataToDrawList = new ArrayList<>();
                    long packetId = 0;
                    long oldTime = System.currentTimeMillis() - DELAY;

                    while (!stopService) {

                        long newTime = System.currentTimeMillis();
                        for (; oldTime < newTime; oldTime += 2) {
                            Data newData = null;
                            try {
                                newData = (Data) circular.next().clone();
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                            newData.setTime(oldTime);
                            dataToDrawList.add(newData);
                            dataToSendList.add(newData);
                        }
                        oldTime = newTime + 2;

                        try {
                            Thread.sleep(DELAY);
                        } catch (InterruptedException e) {

                            e.printStackTrace();
                            return;
                        }

                        //Send data to draw
                        Intent passDataToDrawIntent = new Intent();
                        passDataToDrawIntent.setAction(PASS_DATA_TO_DRAW_ACTION);
                        passDataToDrawIntent.putParcelableArrayListExtra(CommonConstants.EXTRA_PARAM_DATA_TO_DRAW_LIST, dataToDrawList);
                        sendBroadcast(passDataToDrawIntent);
                        dataToDrawList.clear();

                        //Send data to server
                        if (dataToSendList.size() > 500) {

                            Log.w("cardio", "send real data to server ");
                            packetId++;
                            boolean allowSendDataToServer = LocalStorage.from(getApplicationContext()).getSendDataToServer();
                            boolean isWifiEnabled = NetworkUtils.isWifiEnabled(getApplicationContext());
                            if (allowSendDataToServer && isWifiEnabled)
                                ServiceManager.newInstance(getApplicationContext()).startSendDataToServerService(dataToSendList, packetId);

                            dataToSendList.clear();
                        }
                    }
                }
            });
            thread.start();
        }

        return START_STICKY;
    }

    private List<Data> parseCSVFileWithFakeData() {

        String next[] = {};
        List<String[]> list = new ArrayList<String[]>();
        List<Data> dataList = new ArrayList<Data>();
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(getApplicationContext().getAssets().open(CommonConstants.CSV_FILE_WITH_FAKE_DATA_2)));

            for (; ; ) {
                next = reader.readNext();
                if (next != null) {
                    list.add(next);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 1; i < list.size() - 1; i++) {

            double yAxisLead1Value = Double.parseDouble(list.get(i)[CommonConstants.CSV_FILE_COLUMN_ECG_LEAD_1_INDEX]);
            double yAxisLead2Value = Double.parseDouble(list.get(i)[CommonConstants.CSV_FILE_COLUMN_ECG_LEAD_2_INDEX]);
            double yAxisLead3Value = Double.parseDouble(list.get(i)[CommonConstants.CSV_FILE_COLUMN_ECG_LEAD_3_INDEX]);
            double yAxisBP1Value = Double.parseDouble(list.get(i)[CommonConstants.CSV_FILE_COLUMN_BP1_INDEX]);
            double yAxisHeartBeatValue = Double.parseDouble(list.get(i)[CommonConstants.CSV_FILE_COLUMN_HEART_BEAT_INDEX]);
            double yAxisTempValue = Double.parseDouble(list.get(i)[CommonConstants.CSV_FILE_COLUMN_TEMP_INDEX]);

            Data data = new Data(0, yAxisLead1Value, yAxisLead2Value, yAxisLead3Value, yAxisBP1Value, yAxisHeartBeatValue, yAxisTempValue);
            dataList.add(data);
        }

        return dataList;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopService = true;
    }
}
