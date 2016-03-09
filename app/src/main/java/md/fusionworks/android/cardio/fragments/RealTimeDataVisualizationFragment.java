package md.fusionworks.android.cardio.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.AsyncHttpHead;
import com.koushikdutta.async.http.WebSocket;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import md.fusionworks.android.cardio.DownSampleImpl;
import md.fusionworks.android.cardio.R;
import md.fusionworks.android.cardio.api.services.CardioService;
import md.fusionworks.android.cardio.models.Data;
import md.fusionworks.android.cardio.models.HRV;
import md.fusionworks.android.cardio.services.RealTimeDataGeneratorService;
import md.fusionworks.android.cardio.storage.LocalStorage;
import md.fusionworks.android.cardio.utils.CommonConstants;
import md.fusionworks.android.cardio.views.BaseLineChart;

/**
 * Created by admin on 22.08.2015.
 */
public class RealTimeDataVisualizationFragment extends BaseFragment {

    private static final String WS_ADDRESS = "http://54.149.201.31:9000/data/ws";
    private static final int MAX_POINTS_ALLOWED_TO_KEEP = CommonConstants.VISIBLE_X_RANGE_MAXIMUM;
    private static final int THRESHOLD_DIVIDE_TO_NUMBER = 4;

    @Bind(R.id.tempGraph)
    LineChart tempGraph;
    @Bind(R.id.bp1Graph)
    LineChart bp1Graph;
    @Bind(R.id.heartBeatGraph)
    LineChart heartBeatGraph;
    @Bind(R.id.ecgGraph)
    LineChart ecgGraph;
    @Bind(R.id.cvGraph)
    LineChart cvGraph;
    @Bind(R.id.hrvGraph)
    LineChart hrvGraph;

    private DataToDrawReceiver dataToDrawReceiver;
    private WebSocket hrvWebSocket;

    public static RealTimeDataVisualizationFragment newInstance() {

        return new RealTimeDataVisualizationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_real_time_data_visualization, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
    }

    private void getHeartRateVariability() {

        AsyncHttpGet get = new AsyncHttpGet(WS_ADDRESS);
        get.addHeader("authToken", LocalStorage.from(getActivity()).getAuthToken());
        AsyncHttpClient.getDefaultInstance().websocket(get, "http", new AsyncHttpClient.WebSocketConnectCallback() {
            @Override
            public void onCompleted(Exception ex, final WebSocket webSocket) {

                if (ex != null) {
                    ex.printStackTrace();
                    return;
                }

                hrvWebSocket = webSocket;

                webSocket.setStringCallback(new WebSocket.StringCallback() {

                    @Override
                    public void onStringAvailable(String s) {

                        Log.w("socket", s);
                        if (s.contains("hrv")) {

                            s = s.replace("{\"hrv\":", "");
                            s = s.replace("}}", "}");
                            HRV hrv = new HRV();
                            hrv = new Gson().fromJson(s, HRV.class);
                            addEntryForCVGraph(hrv);
                            addEntryForHRVGraph(hrv);
                        }
                    }
                });
            }
        });
    }

    private void addEntryForCVGraph(HRV hrv) {

        LineData data = cvGraph.getData();
        if (data != null) {

            LineDataSet set = data.getDataSetByIndex(0);

            if (set == null) {
                set = BaseLineChart.buildBaseLineDataSet(null, Color.BLUE, "CV");
                data.addDataSet(set);
            }
            Timestamp stamp = new Timestamp((long) hrv.getTime());
            Date date = new Date(stamp.getTime());

            data.addXValue(String.valueOf(date.getSeconds()));
            data.addEntry(new Entry((float) hrv.getCv(), set.getEntryCount()), 0);

            shrinkDataSet(data, set);

            cvGraph.notifyDataSetChanged();
            cvGraph.setVisibleXRangeMaximum(CommonConstants.VISIBLE_X_RANGE_MAXIMUM);
            cvGraph.moveViewToX(data.getXValCount() - CommonConstants.VISIBLE_X_RANGE_MAXIMUM + 1);
        }
    }

    private void addEntryForHRVGraph(HRV hrv) {

        LineData data = hrvGraph.getData();
        if (data != null) {

            LineDataSet minLineDataSet = data.getDataSetByIndex(0);

            if (minLineDataSet == null) {
                minLineDataSet = BaseLineChart.buildBaseLineDataSet(null, Color.BLUE, "Min");
                data.addDataSet(minLineDataSet);
            }

            LineDataSet maxLineDataSet = data.getDataSetByIndex(1);

            if (maxLineDataSet == null) {
                maxLineDataSet = BaseLineChart.buildBaseLineDataSet(null, Color.RED, "Max");
                data.addDataSet(maxLineDataSet);
            }

            LineDataSet meanLineDataSet = data.getDataSetByIndex(2);

            if (meanLineDataSet == null) {
                meanLineDataSet = BaseLineChart.buildBaseLineDataSet(null, Color.GREEN, "Mean");
                data.addDataSet(meanLineDataSet);
            }

            LineDataSet stDevLineDataSet = data.getDataSetByIndex(3);

            if (stDevLineDataSet == null) {
                stDevLineDataSet = BaseLineChart.buildBaseLineDataSet(null, Color.GRAY, "StDev");
                data.addDataSet(stDevLineDataSet);
            }

            Timestamp stamp = new Timestamp((long) hrv.getTime());
            Date date = new Date(stamp.getTime());

            data.addXValue(String.valueOf(date.getSeconds()));
            data.addEntry(new Entry((float) hrv.getMin(), minLineDataSet.getEntryCount()), 0);
            data.addEntry(new Entry((float) hrv.getMax(), maxLineDataSet.getEntryCount()), 1);
            data.addEntry(new Entry((float) hrv.getMean(), meanLineDataSet.getEntryCount()), 2);
            data.addEntry(new Entry((float) hrv.getStdDev(), stDevLineDataSet.getEntryCount()), 3);

            shrinkDataSet(data, minLineDataSet, maxLineDataSet, meanLineDataSet, stDevLineDataSet);

            hrvGraph.notifyDataSetChanged();
            hrvGraph.setVisibleXRangeMaximum(CommonConstants.VISIBLE_X_RANGE_MAXIMUM);
            hrvGraph.moveViewToX(data.getXValCount() - CommonConstants.VISIBLE_X_RANGE_MAXIMUM + 1);
        }
    }

    private void addEntryForTempGraph(List<Data> dataList) {

        LineData data = tempGraph.getData();
        if (data != null) {

            LineDataSet set = data.getDataSetByIndex(0);

            if (set == null) {
                set = BaseLineChart.buildBaseLineDataSet(null, Color.BLUE, "Temp");
                data.addDataSet(set);
            }

            for (int i = 0; i < dataList.size(); i++) {

                Timestamp stamp = new Timestamp((long) dataList.get(i).getTime());
                Date date = new Date(stamp.getTime());

                data.addXValue(String.valueOf(date.getSeconds()));
                data.addEntry(new Entry((float) dataList.get(i).getTemp(), set.getEntryCount()), 0);
            }

            shrinkDataSet(data, set);

            tempGraph.notifyDataSetChanged();
            tempGraph.setVisibleXRangeMaximum(CommonConstants.VISIBLE_X_RANGE_MAXIMUM);
            tempGraph.moveViewToX(data.getXValCount() - CommonConstants.VISIBLE_X_RANGE_MAXIMUM + dataList.size() / THRESHOLD_DIVIDE_TO_NUMBER);
        }
    }

    private void shrinkDataSet(LineData data, LineDataSet... sets) {
        int size = sets[0].getEntryCount();
        int delta = size - MAX_POINTS_ALLOWED_TO_KEEP;
        if (delta > 0) {
            for (int i = 0; i < delta; i++) {
                data.removeXValue(i);
                for (LineDataSet set : sets) {
                    set.removeEntry(i);
                }
            }
            for (LineDataSet set : sets) {
                for (Entry entry : set.getYVals()) {
                    entry.setXIndex(entry.getXIndex() - delta);
                }
            }
        }
    }

    private void addEntryForBP1Graph(List<Data> dataList) {

        LineData data = bp1Graph.getData();

        if (data != null) {

            LineDataSet set = data.getDataSetByIndex(0);

            if (set == null) {
                set = BaseLineChart.buildBaseLineDataSet(null, Color.BLUE, "BP1");
                data.addDataSet(set);
            }

            for (int i = 0; i < dataList.size(); i++) {

                Timestamp stamp = new Timestamp((long) dataList.get(i).getTime());
                Date date = new Date(stamp.getTime());

                data.addXValue(String.valueOf(date.getSeconds()));
                data.addEntry(new Entry((float) dataList.get(i).getBp1(), set.getEntryCount()), 0);
            }

            shrinkDataSet(data, set);

            bp1Graph.notifyDataSetChanged();
            bp1Graph.setVisibleXRangeMaximum(CommonConstants.VISIBLE_X_RANGE_MAXIMUM);
            bp1Graph.moveViewToX(data.getXValCount() - CommonConstants.VISIBLE_X_RANGE_MAXIMUM + dataList.size() / THRESHOLD_DIVIDE_TO_NUMBER);
        }
    }

    private void addEntryForHeartBeatGraph(List<Data> dataList) {

        LineData data = heartBeatGraph.getData();

        if (data != null) {

            LineDataSet set = data.getDataSetByIndex(0);

            if (set == null) {
                set = BaseLineChart.buildBaseLineDataSet(null, Color.BLUE, "Heart Beat");
                data.addDataSet(set);
            }

            for (int i = 0; i < dataList.size(); i++) {

                Timestamp stamp = new Timestamp((long) dataList.get(i).getTime());
                Date date = new Date(stamp.getTime());

                data.addXValue(String.valueOf(date.getSeconds()));
                data.addEntry(new Entry((float) dataList.get(i).getHeartBeat(), set.getEntryCount()), 0);
            }

            shrinkDataSet(data, set);

            heartBeatGraph.notifyDataSetChanged();
            heartBeatGraph.setVisibleXRangeMaximum(CommonConstants.VISIBLE_X_RANGE_MAXIMUM);
            heartBeatGraph.moveViewToX(data.getXValCount() - CommonConstants.VISIBLE_X_RANGE_MAXIMUM + dataList.size() / THRESHOLD_DIVIDE_TO_NUMBER);
        }
    }

    private void addEntryForECGGraph(List<Data> dataList) {

        LineData data = ecgGraph.getData();

        if (data != null) {

            LineDataSet lead1LineDataSet = data.getDataSetByIndex(0);
            LineDataSet lead2LineDataSet = data.getDataSetByIndex(1);
            LineDataSet lead3LineDataSet = data.getDataSetByIndex(2);

            if (lead1LineDataSet == null) {
                lead1LineDataSet = BaseLineChart.buildBaseLineDataSet(null, Color.BLUE, "Lead 1");
                data.addDataSet(lead1LineDataSet);
            }
            if (lead2LineDataSet == null) {
                lead2LineDataSet = BaseLineChart.buildBaseLineDataSet(null, Color.RED, "Lead 2");
                data.addDataSet(lead2LineDataSet);
            }
            if (lead3LineDataSet == null) {
                lead3LineDataSet = BaseLineChart.buildBaseLineDataSet(null, Color.GREEN, "Lead 3");
                data.addDataSet(lead3LineDataSet);
            }

            for (int i = 0; i < dataList.size(); i++) {

                Timestamp stamp = new Timestamp((long) dataList.get(i).getTime());
                Date date = new Date(stamp.getTime());

                data.addXValue(String.valueOf(date.getSeconds()));
                data.addEntry(new Entry((float) dataList.get(i).getEcgLead1(), lead1LineDataSet.getEntryCount()), 0);
                data.addEntry(new Entry((float) dataList.get(i).getEcgLead2(), lead2LineDataSet.getEntryCount()), 1);
                data.addEntry(new Entry((float) dataList.get(i).getEcgLead3(), lead3LineDataSet.getEntryCount()), 2);
            }

            shrinkDataSet(data, lead1LineDataSet, lead2LineDataSet, lead3LineDataSet);

            ecgGraph.notifyDataSetChanged();
            ecgGraph.setVisibleXRangeMaximum(CommonConstants.VISIBLE_X_RANGE_MAXIMUM);
            ecgGraph.moveViewToX(data.getXValCount() - CommonConstants.VISIBLE_X_RANGE_MAXIMUM + dataList.size() / THRESHOLD_DIVIDE_TO_NUMBER);
        }
    }

    @Override
    public void onResume() {

        dataToDrawReceiver = new DataToDrawReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RealTimeDataGeneratorService.PASS_DATA_TO_DRAW_ACTION);
        getActivity().registerReceiver(dataToDrawReceiver, intentFilter);

        getHeartRateVariability();

        super.onResume();
    }

    @Override
    public void onPause() {

        if (hrvWebSocket != null)
            hrvWebSocket.close();
        getActivity().unregisterReceiver(dataToDrawReceiver);

        super.onPause();
    }

    private class DataToDrawReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            List<Data> dataList = new ArrayList<>();
            dataList = intent.getParcelableArrayListExtra(CommonConstants.EXTRA_PARAM_DATA_TO_DRAW_LIST);

            Number[][] tempNums = new Number[dataList.size()][2];
            Number[][] bp1Nums = new Number[dataList.size()][2];
            Number[][] heartBeatNums = new Number[dataList.size()][2];
            Number[][] ecgLead1Nums = new Number[dataList.size()][2];
            Number[][] ecgLead2Nums = new Number[dataList.size()][2];
            Number[][] ecgLead3Nums = new Number[dataList.size()][2];

            for (int i = 0; i < dataList.size(); i++) {

                tempNums[i][0] = dataList.get(i).getTime();
                tempNums[i][1] = dataList.get(i).getTemp();
                bp1Nums[i][0] = dataList.get(i).getTime();
                bp1Nums[i][1] = dataList.get(i).getBp1();
                heartBeatNums[i][0] = dataList.get(i).getTime();
                heartBeatNums[i][1] = dataList.get(i).getHeartBeat();
                ecgLead1Nums[i][0] = dataList.get(i).getTime();
                ecgLead1Nums[i][1] = dataList.get(i).getEcgLead1();
                ecgLead2Nums[i][0] = dataList.get(i).getTime();
                ecgLead2Nums[i][1] = dataList.get(i).getEcgLead2();
                ecgLead3Nums[i][0] = dataList.get(i).getTime();
                ecgLead3Nums[i][1] = dataList.get(i).getEcgLead3();
            }

            int threshold = dataList.size() / THRESHOLD_DIVIDE_TO_NUMBER;
            tempNums = DownSampleImpl.largestTriangleThreeBuckets(tempNums, threshold);
            bp1Nums = DownSampleImpl.largestTriangleThreeBuckets(bp1Nums, threshold);
            heartBeatNums = DownSampleImpl.largestTriangleThreeBuckets(heartBeatNums, threshold);
            ecgLead1Nums = DownSampleImpl.largestTriangleThreeBuckets(ecgLead1Nums, threshold);
            ecgLead2Nums = DownSampleImpl.largestTriangleThreeBuckets(ecgLead2Nums, threshold);
            ecgLead3Nums = DownSampleImpl.largestTriangleThreeBuckets(ecgLead3Nums, threshold);
            dataList = new ArrayList<>(tempNums.length);

            for (int i = 0; i < tempNums.length; i++) {

                Data data = new Data();
                data.setTime(tempNums[i][0].longValue());
                data.setTemp(tempNums[i][1].doubleValue());
                data.setBp1(bp1Nums[i][1].doubleValue());
                data.setHeartBeat(heartBeatNums[i][1].doubleValue());
                data.setEcgLead1(ecgLead1Nums[i][1].doubleValue());
                data.setEcgLead2(ecgLead2Nums[i][1].doubleValue());
                data.setEcgLead3(ecgLead3Nums[i][1].doubleValue());
                dataList.add(data);
            }

            addEntryForTempGraph(dataList);
            addEntryForBP1Graph(dataList);
            addEntryForHeartBeatGraph(dataList);
            addEntryForECGGraph(dataList);
        }
    }
}
