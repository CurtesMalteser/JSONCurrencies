package com.curtesmalteser.jsoncurrencies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by anton on 29/11/2017.
 */

public class CurrenciesModel implements Parcelable{

    int id;
    private String base;
    private String date;
    private String coin;
    private Double currency;

    /**
     * Constructs a CurrenciesModel from values
     */
    public CurrenciesModel(int id, String base, String date, String coin, Double currency) {
        this.id = id;
        this.base = base;
        this.date = date;
        this.coin = coin;
        this.currency = currency;
    }

    /**
     * Constructs a CurrenciesModel from a Parcel
     * @param parcel Source Parcel
     */
    public CurrenciesModel (Parcel parcel) {
        this.id = parcel.readInt();
        this.base = parcel.readString();
        this.date = parcel.readString();
        this.coin = parcel.readString();
        this.currency = parcel.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Required method to write to Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(base);
        dest.writeString(date);
        dest.writeString(coin);
        dest.writeDouble(currency);

    }

    public int getId() {
        return id;
    }

    // Required method to recreate CurrenciesModel from Parcel
    public static Creator<CurrenciesModel> CREATOR = new Creator<CurrenciesModel>() {
        @Override
        public CurrenciesModel createFromParcel(Parcel source) {
            return new CurrenciesModel(source);
        }

        @Override
        public CurrenciesModel[] newArray(int size) {
            return new CurrenciesModel[size];
        }
    };


    public void setId(int id) {
        this.id = id;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public Double getCurrency() {
        return currency;
    }

    public void setCurrency(Double currency) {
        this.currency = currency;
    }


}
