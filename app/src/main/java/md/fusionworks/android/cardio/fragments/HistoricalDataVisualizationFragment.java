package md.fusionworks.android.cardio.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import md.fusionworks.android.cardio.R;
import md.fusionworks.android.cardio.api.ServiceGenerator;
import md.fusionworks.android.cardio.api.services.CardioService;
import md.fusionworks.android.cardio.models.Data;
import md.fusionworks.android.cardio.storage.LocalStorage;
import md.fusionworks.android.cardio.utils.CommonConstants;
import md.fusionworks.android.cardio.utils.DateUtils;
import md.fusionworks.android.cardio.views.BaseLineChart;
import md.fusionworks.android.cardio.views.DateTimePickerDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoricalDataVisualizationFragment extends BaseFragment implements DateTimePickerDialog.OnDatePickedListener, View.OnClickListener {

    @Bind(R.id.pickDateTimeButton)
    Button pickDateTimeButton;
    @Bind(R.id.showPreviousMinuteDataButton)
    Button showPreviousMinuteDataButton;
    @Bind(R.id.showNextMinuteDataButton)
    Button showNextMinuteDataButton;
    @Bind(R.id.tempGraph)
    LineChart tempGraph;
    @Bind(R.id.bp1Graph)
    LineChart bp1Graph;
    @Bind(R.id.heartBeatGraph)
    LineChart heartBeatGraph;
    @Bind(R.id.ecgGraph)
    LineChart ecgGraph;

    private Calendar startCalendar;
    private Calendar endCalendar;

    public static HistoricalDataVisualizationFragment newInstance() {

        return new HistoricalDataVisualizationFragment();
    }

    public HistoricalDataVisualizationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_historical_data_visualization, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        pickDateTimeButton.setOnClickListener(this);
        showPreviousMinuteDataButton.setOnClickListener(this);
        showNextMinuteDataButton.setOnClickListener(this);
    }

    private void addEntryForTempGraph(List<Data> dataList) {

        Collections.sort(dataList);

        List<String> xEntry = new ArrayList<>();
        List<Entry> yEntry = new ArrayList<Entry>();

        for (int i = 0; i < dataList.size(); i++) {

            Timestamp stamp = new Timestamp((long) dataList.get(i).getTime());
            Date date = new Date(stamp.getTime());

            xEntry.add(String.valueOf(date.getSeconds()));
            yEntry.add(new Entry((float) dataList.get(i).getTemp(), i));
        }

        LineDataSet tempLineDataSet = BaseLineChart.buildBaseLineDataSet(yEntry, Color.BLUE, "Temp");
        List<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(tempLineDataSet);
        LineData lineData = new LineData(xEntry, sets);
        tempGraph.setData(lineData);
        tempGraph.setVisibleXRangeMaximum(CommonConstants.VISIBLE_X_RANGE_MAXIMUM);
        tempGraph.invalidate();
    }

    private void addEntryForBP1Graph(List<Data> dataList) {

        List<String> xEntry = new ArrayList<>();
        List<Entry> yEntry = new ArrayList<Entry>();

        for (int i = 0; i < dataList.size(); i++) {

            Timestamp stamp = new Timestamp((long) dataList.get(i).getTime());
            Date date = new Date(stamp.getTime());

            xEntry.add(String.valueOf(date.getSeconds()));
            yEntry.add(new Entry((float) dataList.get(i).getBp1(), i));
        }

        LineDataSet tempLineDataSet = BaseLineChart.buildBaseLineDataSet(yEntry, Color.BLUE, "BP1");
        List<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(tempLineDataSet);
        LineData lineData = new LineData(xEntry, sets);
        bp1Graph.setData(lineData);
        bp1Graph.setVisibleXRangeMaximum(CommonConstants.VISIBLE_X_RANGE_MAXIMUM);
        bp1Graph.invalidate();
    }

    private void addEntryForHeartBeatGraph(List<Data> dataList) {

        List<String> xEntry = new ArrayList<>();
        List<Entry> yEntry = new ArrayList<Entry>();

        for (int i = 0; i < dataList.size(); i++) {

            Timestamp stamp = new Timestamp((long) dataList.get(i).getTime());
            Date date = new Date(stamp.getTime());

            xEntry.add(String.valueOf(date.getSeconds()));
            yEntry.add(new Entry((float) dataList.get(i).getHeartBeat(), i));
        }

        LineDataSet tempLineDataSet = BaseLineChart.buildBaseLineDataSet(yEntry, Color.BLUE, "Heart Beat");
        List<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(tempLineDataSet);
        LineData lineData = new LineData(xEntry, sets);
        heartBeatGraph.setData(lineData);
        heartBeatGraph.setVisibleXRangeMaximum(CommonConstants.VISIBLE_X_RANGE_MAXIMUM);
        heartBeatGraph.invalidate();
    }

    private void addEntryForECGGraph(List<Data> dataList) {

        List<String> xEntry = new ArrayList<>();
        List<Entry> yEntryLead1 = new ArrayList<Entry>();
        List<Entry> yEntryLead2 = new ArrayList<Entry>();
        List<Entry> yEntryLead3 = new ArrayList<Entry>();

        for (int i = 0; i < dataList.size(); i++) {

            Timestamp stamp = new Timestamp((long) dataList.get(i).getTime());
            Date date = new Date(stamp.getTime());

            xEntry.add(String.valueOf(date.getSeconds()));
            yEntryLead1.add(new Entry((float) dataList.get(i).getEcgLead1(), i));
            yEntryLead2.add(new Entry((float) dataList.get(i).getEcgLead2(), i));
            yEntryLead3.add(new Entry((float) dataList.get(i).getEcgLead3(), i));
        }

        LineDataSet lead1LineDataSet = BaseLineChart.buildBaseLineDataSet(yEntryLead1, Color.BLUE, "Lead 1");
        LineDataSet lead2LineDataSet = BaseLineChart.buildBaseLineDataSet(yEntryLead2, Color.RED, "Lead 2");
        LineDataSet lead3LineDataSet = BaseLineChart.buildBaseLineDataSet(yEntryLead3, Color.GREEN, "Lead 3");
        List<LineDataSet> sets = new ArrayList<LineDataSet>();
        sets.add(lead1LineDataSet);
        sets.add(lead2LineDataSet);
        sets.add(lead3LineDataSet);
        LineData lineData = new LineData(xEntry, sets);
        ecgGraph.setData(lineData);
        ecgGraph.setVisibleXRangeMaximum(CommonConstants.VISIBLE_X_RANGE_MAXIMUM);
        ecgGraph.invalidate();
    }

    private void getHistoricalData(long timeInMillis, long nextMinuteTimeInMillis) {

        CardioService cardioService = ServiceGenerator.createService(CardioService.class, CardioService.URL_CARDIO, LocalStorage.from(getActivity()).getAuthToken());
        cardioService.getData(timeInMillis, nextMinuteTimeInMillis, new Callback<ArrayList<Data>>() {
            @Override
            public void success(ArrayList<Data> dataList, Response response) {

                addEntryForTempGraph(dataList);
                addEntryForBP1Graph(dataList);
                addEntryForHeartBeatGraph(dataList);
                addEntryForECGGraph(dataList);

                hideLoadingDialog();
            }

            @Override
            public void failure(RetrofitError error) {

                hideLoadingDialog();
            }
        });
    }

    private void onDateTimeChanged(int minutes) {

        showLoadingDialog();

        DateUtils.addNMinutesToCalendar(startCalendar, minutes);
        DateUtils.addNMinutesToCalendar(endCalendar, minutes);

        String startDateTime = DateUtils.getHistoricalDataDateFormat().format(startCalendar.getTime());
        String endTime = DateUtils.getHistoricalDataTimeFormat().format(endCalendar.getTime());

        pickDateTimeButton.setText(String.format("%s - %s", startDateTime, endTime));
        getHistoricalData(startCalendar.getTimeInMillis(), endCalendar.getTimeInMillis());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.pickDateTimeButton:

                DateTimePickerDialog.show(getFragmentManager(), this);
                break;
            case R.id.showPreviousMinuteDataButton:

                onDateTimeChanged(-1);
                break;
            case R.id.showNextMinuteDataButton:

                onDateTimeChanged(1);
                break;
        }
    }

    @Override
    public void onDatePicked(Calendar calendar) {

        showPreviousMinuteDataButton.setEnabled(true);
        showNextMinuteDataButton.setEnabled(true);

        Calendar startCalendarClone = (Calendar) calendar.clone();
        Calendar endCalendarClone = (Calendar) calendar.clone();

        startCalendar = DateUtils.addNMinutesToCalendar(startCalendarClone, -1);
        endCalendar = DateUtils.addNMinutesToCalendar(endCalendarClone, 0);
        onDateTimeChanged(1);
    }
}
