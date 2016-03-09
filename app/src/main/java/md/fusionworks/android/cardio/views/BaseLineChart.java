package md.fusionworks.android.cardio.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 16.08.2015.
 */
public class BaseLineChart extends LineChart {

    public BaseLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);

        // no description text
        this.setDescription("");
        this.setNoDataTextDescription("You need to provide data for the chart.");

        // enable value highlighting
        this.setHighlightEnabled(true);

        // enable touch gestures
        this.setTouchEnabled(true);

        // enable scaling and dragging
        this.setDragEnabled(true);
        this.setScaleEnabled(true);
        this.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        this.setPinchZoom(true);

        // set an alternative background color
        this.setBackgroundColor(Color.WHITE);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty data
        this.setData(data);

        // get the legend (only possible after setting data)
        Legend l = this.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.LTGRAY);

        XAxis xl = this.getXAxis();
        xl.setTextColor(Color.BLACK);
        xl.setDrawGridLines(false);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setAvoidFirstLastClipping(true);
        xl.setSpaceBetweenLabels(20);
        xl.setEnabled(true);

        YAxis leftAxis = this.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        // leftAxis.setAxisMaxValue(100f);
        //leftAxis.setAxisMinValue(0f);
        leftAxis.setStartAtZero(false);
        leftAxis.setDrawGridLines(false);

        YAxis rightAxis = this.getAxisRight();
        rightAxis.setEnabled(false);
    }

    public static LineDataSet buildBaseLineDataSet(List<Entry> yAxisDataList, int lineColor, String label) {

        LineDataSet set = new LineDataSet(yAxisDataList, label);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(lineColor);
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(1f);
        set.setCircleSize(0f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);

        return set;
    }
}
