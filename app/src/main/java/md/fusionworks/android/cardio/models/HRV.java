package md.fusionworks.android.cardio.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 26.08.2015.
 */
public class HRV {

    @SerializedName("time")
    private long time;
    @SerializedName("min")
    private double min;
    @SerializedName("max")
    private double max;
    @SerializedName("range")
    private double range;
    @SerializedName("mean")
    private double mean;
    @SerializedName("variance")
    private double variance;
    @SerializedName("stdDev")
    private double stdDev;
    @SerializedName("cv")
    private double cv;

    public HRV() {
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getVariance() {
        return variance;
    }

    public void setVariance(double variance) {
        this.variance = variance;
    }

    public double getStdDev() {
        return stdDev;
    }

    public void setStdDev(double stdDev) {
        this.stdDev = stdDev;
    }

    public double getCv() {
        return cv;
    }

    public void setCv(double cv) {
        this.cv = cv;
    }
}
