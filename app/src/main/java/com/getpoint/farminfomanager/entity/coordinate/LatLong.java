package com.getpoint.farminfomanager.entity.coordinate;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Stores latitude and longitude in degrees.
 */
public class LatLong implements Parcelable, Serializable, Cloneable{

    /**
     * Stores latitude, and longitude in degrees
     */
    private double latitude;
    private double longitude;

    public LatLong(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LatLong(LatLong copy){
        this(copy.getLatitude(), copy.getLongitude());
    }

    public void set(LatLong update){
        this.latitude = update.latitude;
        this.longitude = update.longitude;
    }

    /**
     * @return the latitude in degrees
     */
    public double getLatitude(){
        return latitude;
    }

    /**
     * @return the longitude in degrees
     */
    public double getLongitude(){
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public LatLong dot(double scalar) {
        return new LatLong(latitude * scalar, longitude * scalar);
    }

    public LatLong negate() {
        return new LatLong(latitude * -1, longitude * -1);
    }

    public LatLong subtract(LatLong coord) {
        return new LatLong(latitude - coord.latitude, longitude - coord.longitude);
    }

    public LatLong sum(LatLong coord) {
        return new LatLong(latitude + coord.latitude, longitude + coord.longitude);
    }

    public static LatLong sum(LatLong... toBeAdded) {
        double latitude = 0;
        double longitude = 0;
        for (LatLong coord : toBeAdded) {
            latitude += coord.latitude;
            longitude += coord.longitude;
        }
        return new LatLong(latitude, longitude);
    }

    /**
     *  实现克隆方法
     * @return 克隆的对象
     */
    public Object clone() {

        Object o = null;

        try {
            o = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return o;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LatLong)) return false;

        LatLong latLong = (LatLong) o;

        if (Double.compare(latLong.latitude, latitude) != 0) return false;
        return Double.compare(latLong.longitude, longitude) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "LatLong{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this);
    }

    public static final Creator<LatLong> CREATOR = new Creator<LatLong>() {
        public LatLong createFromParcel(Parcel source) {
            return (LatLong) source.readSerializable();
        }

        public LatLong[] newArray(int size) {
            return new LatLong[size];
        }
    };
}
