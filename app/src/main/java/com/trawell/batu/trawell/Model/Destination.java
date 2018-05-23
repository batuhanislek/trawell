package com.trawell.batu.trawell.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Batuhan Islek on 31.03.2018.
 */

public class Destination {
    private int destination_Id;
    private String countryName, cityName, longitude, altitude, transportType;
    private String checkInDate, checkOutDate;
    private double transportExpense, accomodationExpense, currentExpense, budget;
    private long daysSpent;
    private int countryFlag;
    private String ownerId;
    private ArrayList<String> imageUrl;

    public Destination() {}

    public Destination(String cityName, String checkInDate, String checkOutDate) {
        this.cityName = cityName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }



    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public ArrayList<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(ArrayList<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getDaysSpent() {
        return daysSpent;
    }

    public void setDaysSpent(long daysSpent) {
        this.daysSpent = daysSpent;
    }

    public double getTransportExpense() {
        return transportExpense;
    }

    public void setTransportExpense(double transportExpense) {
        this.transportExpense = transportExpense;
    }

    public double getAccomodationExpense() {
        return accomodationExpense;
    }

    public void setAccomodationExpense(double accomodationExpense) {
        this.accomodationExpense = accomodationExpense;
    }

    public double getCurrentExpense() {
        return currentExpense;
    }

    public void setCurrentExpense(double currentExpense) {
        this.currentExpense = currentExpense;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    //return hashcode of object
    public int describeContents() { return hashCode(); }

    public int getDestination_Id() {
        return destination_Id;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getCityName() {
        return cityName;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setDestination_Id(int destination_Id) {
        this.destination_Id = destination_Id;
    }



    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }



    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public int getCountryFlag() {
        return countryFlag;
    }

    public void setCountryFlag(int countryFlag) {
        this.countryFlag = countryFlag;
    }
}
