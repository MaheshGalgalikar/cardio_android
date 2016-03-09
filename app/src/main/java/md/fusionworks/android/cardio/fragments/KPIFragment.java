package md.fusionworks.android.cardio.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import butterknife.Bind;
import butterknife.ButterKnife;
import md.fusionworks.android.cardio.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class KPIFragment extends Fragment {

    @Bind(R.id.stressLevelArcView)
    DecoView stressLevelArcView;
    @Bind(R.id.stressLevelValueField)
    TextView stressLevelValueField;
    @Bind(R.id.stressLevelNameField)
    TextView stressLevelNameField;
    @Bind(R.id.fatigueLevelArcView)
    DecoView fatigueLevelArcView;
    @Bind(R.id.fatigueLevelValueField)
    TextView fatigueLevelValueField;
    @Bind(R.id.fatigueLevelNameField)
    TextView fatigueLevelNameField;
    @Bind(R.id.performanceArcView)
    DecoView performanceArcView;
    @Bind(R.id.performanceValueField)
    TextView performanceValueField;
    @Bind(R.id.performanceNameField)
    TextView performanceNameField;

    final float seriesMax = 100f;
    private int mSeries1Index;
    private int mSeries2Index;
    private int mSeries3Index;
    protected boolean mUpdateListeners = true;

    public static final KPIFragment newInstance() {

        return new KPIFragment();
    }

    public KPIFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kpi, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        stressLevelArcView.deleteAll();
        stressLevelArcView.configureAngles(280, 0);

        stressLevelArcView.addSeries(new SeriesItem.Builder(Color.argb(255, 64, 255, 64), Color.argb(255, 255, 0, 0))
                .setRange(0, seriesMax, seriesMax)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(15f))
                .build());

        stressLevelArcView.addSeries(new SeriesItem.Builder(Color.parseColor("#ffffffff"))
                .setRange(0, seriesMax, seriesMax)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(6f))
                .build());

        SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 64, 255, 64), Color.argb(255, 255, 0, 0))
                .setRange(0, seriesMax, 0)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(15f))
                .setCapRounded(true)
                .setShowPointWhenEmpty(true)
                .build();

        mSeries1Index = stressLevelArcView.addSeries(seriesItem1);

        final TextView textPercent = (TextView) view.findViewById(R.id.stressLevelValueField);
        // textPercent.setText("");
        stressLevelValueField.setText("");
        addProgressListener(seriesItem1, stressLevelValueField, "%.0f");

        fatigueLevelArcView.deleteAll();
        fatigueLevelArcView.configureAngles(280, 0);

        fatigueLevelArcView.addSeries(new SeriesItem.Builder(Color.argb(255, 64, 255, 64), Color.argb(255, 255, 0, 0))
                .setRange(0, seriesMax, seriesMax)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(15f))
                .build());

        fatigueLevelArcView.addSeries(new SeriesItem.Builder(Color.parseColor("#ffffffff"))
                .setRange(0, seriesMax, seriesMax)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(6f))
                .build());

        SeriesItem seriesItem2 = new SeriesItem.Builder(Color.argb(255, 64, 255, 64), Color.argb(255, 255, 0, 0))
                .setRange(0, seriesMax, 0)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(15f))
                .setCapRounded(true)
                .setShowPointWhenEmpty(true)
                .build();

        mSeries2Index = fatigueLevelArcView.addSeries(seriesItem2);

        fatigueLevelValueField.setText("");
        addProgressListener(seriesItem2, fatigueLevelValueField, "%.0f");

        performanceArcView.deleteAll();
        performanceArcView.configureAngles(280, 0);

        performanceArcView.addSeries(new SeriesItem.Builder(Color.argb(255, 64, 255, 64), Color.argb(255, 255, 0, 0))
                .setRange(0, seriesMax, seriesMax)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(15f))
                .build());

        performanceArcView.addSeries(new SeriesItem.Builder(Color.parseColor("#ffffffff"))
                .setRange(0, seriesMax, seriesMax)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(6f))
                .build());

        SeriesItem seriesItem3 = new SeriesItem.Builder(Color.argb(255, 64, 255, 64), Color.argb(255, 255, 0, 0))
                .setRange(0, seriesMax, 0)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(15f))
                .setCapRounded(true)
                .setShowPointWhenEmpty(true)
                .build();

        mSeries3Index = performanceArcView.addSeries(seriesItem3);

        performanceValueField.setText("");
        addProgressListener(seriesItem3, performanceValueField, "%.0f");

        setupEvents();
        setupEvents2();
        setupEvents3();
    }

    protected float getDimension(float base) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, base, getResources().getDisplayMetrics());
    }

    protected void addProgressListener(@NonNull final SeriesItem seriesItem, @NonNull final TextView view, @NonNull final String format) {
        if (format.length() <= 0) {
            throw new IllegalArgumentException("String formatter can not be empty");
        }

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                if (mUpdateListeners) {
                    if (format.contains("%%")) {
                        // We found a percentage so we insert a percentage
                        float percentFilled = ((currentPosition - seriesItem.getMinValue()) / (seriesItem.getMaxValue() - seriesItem.getMinValue()));
                        view.setText(String.format(format, percentFilled * 100f));
                    } else {
                        view.setText(String.format(format, currentPosition));
                    }
                }
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });
    }


    protected void setupEvents() {

        if (stressLevelArcView == null || stressLevelArcView.isEmpty()) {
            throw new IllegalStateException("Unable to add events to empty DecoView");
        }

        stressLevelArcView.executeReset();
        stressLevelArcView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                .setDelay(1000)
                .setDuration(1500).setListener(new DecoEvent.ExecuteEventListener() {
                    @Override
                    public void onEventStart(DecoEvent decoEvent) {

                    }

                    @Override
                    public void onEventEnd(DecoEvent decoEvent) {

                        stressLevelValueField.setVisibility(View.VISIBLE);
                        stressLevelNameField.setVisibility(View.VISIBLE);
                    }
                })
                .build());

        stressLevelArcView.addEvent(new DecoEvent.Builder(46).setIndex(mSeries1Index).setDelay(1000).build());
    }

    protected void setupEvents2() {

        if (fatigueLevelArcView == null || fatigueLevelArcView.isEmpty()) {
            throw new IllegalStateException("Unable to add events to empty DecoView");
        }

        fatigueLevelArcView.executeReset();
        fatigueLevelArcView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                .setDelay(1000)
                .setDuration(1500).setListener(new DecoEvent.ExecuteEventListener() {
                    @Override
                    public void onEventStart(DecoEvent decoEvent) {

                    }

                    @Override
                    public void onEventEnd(DecoEvent decoEvent) {

                        fatigueLevelValueField.setVisibility(View.VISIBLE);
                        fatigueLevelNameField.setVisibility(View.VISIBLE);
                    }
                })
                .build());

        fatigueLevelArcView.addEvent(new DecoEvent.Builder(69).setIndex(mSeries2Index).setDelay(1000).build());
    }

    protected void setupEvents3() {

        if (performanceArcView == null || performanceArcView.isEmpty()) {
            throw new IllegalStateException("Unable to add events to empty DecoView");
        }

        performanceArcView.executeReset();
        performanceArcView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                .setDelay(1000)
                .setDuration(1500).setListener(new DecoEvent.ExecuteEventListener() {
                    @Override
                    public void onEventStart(DecoEvent decoEvent) {

                    }

                    @Override
                    public void onEventEnd(DecoEvent decoEvent) {

                        performanceNameField.setVisibility(View.VISIBLE);
                        performanceValueField.setVisibility(View.VISIBLE);
                    }
                })
                .build());

        performanceArcView.addEvent(new DecoEvent.Builder(25).setIndex(mSeries3Index).setDelay(1000).build());
    }
}
