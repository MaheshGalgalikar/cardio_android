package md.fusionworks.android.cardio.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.common.collect.Iterators;

import java.io.File;
import java.io.FileOutputStream;
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


/**
 * Created by admin on 18.08.2015.
 */
public class BackgroundDataGeneratorService extends Service {

    private static final String SEPARATOR = ",";

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
                    long oldTime = System.currentTimeMillis() - 60000;
                    StringBuilder stringBuilder = new StringBuilder();

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

                            stringBuilder.append(newData.getTime() + SEPARATOR);
                            stringBuilder.append(newData.getEcgLead1() + SEPARATOR);
                            stringBuilder.append(newData.getEcgLead2() + SEPARATOR);
                            stringBuilder.append(newData.getEcgLead3() + SEPARATOR);
                            stringBuilder.append(newData.getBp1() + SEPARATOR);
                            stringBuilder.append(newData.getHeartBeat() + SEPARATOR);
                            stringBuilder.append(newData.getTemp() + SEPARATOR);
                            stringBuilder.append(System.getProperty("line.separator"));

                        }
                        oldTime = newTime + 2;

                        try {
                            Thread.sleep(60000);
                        } catch (InterruptedException e) {

                            e.printStackTrace();
                            return;
                        }

                        String createdFileName = saveFileOnInternalStorage(stringBuilder);
                        stringBuilder.setLength(0);
                        if (!TextUtils.isEmpty(createdFileName)) {

                            boolean allowSendDataToServer = LocalStorage.from(getApplicationContext()).getSendDataToServer();
                            boolean isWifiEnabled = NetworkUtils.isWifiEnabled(getApplicationContext());
                            if (allowSendDataToServer && isWifiEnabled)
                                ServiceManager.newInstance(getApplicationContext()).startSendFileDataToServerService(createdFileName);
                            else {

                                File file = new File(getFilesDir(), createdFileName);
                                boolean deleted = file.delete();
                                Log.w("cardio", "file deleted " + deleted);
                            }
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

    private String saveFileOnInternalStorage(StringBuilder stringBuilder) {

        String fileName = String.valueOf(System.currentTimeMillis());

        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(stringBuilder.toString().getBytes());
            outputStream.close();

            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopService = true;
    }
}
