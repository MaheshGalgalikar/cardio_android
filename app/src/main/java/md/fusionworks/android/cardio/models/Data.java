package md.fusionworks.android.cardio.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 18.08.2015.
 */
public class Data implements Parcelable, Comparable<Data>, Cloneable {

    private long time;
    private double ecgLead1;
    private double ecgLead2;
    private double ecgLead3;
    private double bp1;
    private double heartBeat;
    private double temp;

    public Data() {
    }

    public Data(long time, double ecgLead1, double ecgLead2, double ecgLead3, double bp1, double heartBeat, double temp) {
        this.time = time;
        this.ecgLead1 = ecgLead1;
        this.ecgLead2 = ecgLead2;
        this.ecgLead3 = ecgLead3;
        this.bp1 = bp1;
        this.heartBeat = heartBeat;
        this.temp = temp;
    }

    public Data(Parcel source) {

        this.time = source.readLong();
        this.ecgLead1 = source.readDouble();
        this.ecgLead2 = source.readDouble();
        this.ecgLead3 = source.readDouble();
        this.bp1 = source.readDouble();
        this.heartBeat = source.readDouble();
        this.temp = source.readDouble();
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getEcgLead1() {
        return ecgLead1;
    }

    public void setEcgLead1(double ecgLead1) {
        this.ecgLead1 = ecgLead1;
    }

    public double getEcgLead2() {
        return ecgLead2;
    }

    public void setEcgLead2(double ecgLead2) {
        this.ecgLead2 = ecgLead2;
    }

    public double getEcgLead3() {
        return ecgLead3;
    }

    public void setEcgLead3(double ecgLead3) {
        this.ecgLead3 = ecgLead3;
    }

    public double getBp1() {
        return bp1;
    }

    public void setBp1(double bp1) {
        this.bp1 = bp1;
    }

    public double getHeartBeat() {
        return heartBeat;
    }

    public void setHeartBeat(double heartBeat) {
        this.heartBeat = heartBeat;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(time);
        dest.writeDouble(ecgLead1);
        dest.writeDouble(ecgLead2);
        dest.writeDouble(ecgLead3);
        dest.writeDouble(bp1);
        dest.writeDouble(heartBeat);
        dest.writeDouble(temp);
    }

    public static final Parcelable.Creator<Data> CREATOR
            = new Parcelable.Creator<Data>() {
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    @Override
    public int compareTo(Data another) {
        return (int) (time - another.time);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {

        Data data = new Data(time, ecgLead1, ecgLead2, ecgLead3, bp1, heartBeat, temp);
        return data;
    }
}
