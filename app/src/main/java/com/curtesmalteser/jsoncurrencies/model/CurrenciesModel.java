package com.curtesmalteser.jsoncurrencies.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by anton on 29/11/2017.
 */

@Entity(tableName = "currencies_table")
public class CurrenciesModel implements Parcelable{

    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "selected_currency")
    private String base;

    private String date;

    private String currency;

    private Double rate;

    @Ignore
    public CurrenciesModel(String base, String date, String currency, Double rate) {
        this.base = base;
        this.date = date;
        this.currency = currency;
        this.rate = rate;
    }

    // TODO: 14/12/2017 remove the int from the constructor?
    /**
     * Constructs a CurrenciesModel to use with ArrayList
     */
    public CurrenciesModel(int id, String base, String date, String currency, Double rate) {
        this.id = id;
        this.base = base;
        this.date = date;
        this.currency = currency;
        this.rate = rate;
    }

    /**
     * Constructs a CurrenciesModel from a Parcel
     * @param parcel Source Parcel
     */
    public CurrenciesModel (Parcel parcel) {
        this.id = parcel.readInt();
        this.base = parcel.readString();
        this.date = parcel.readString();
        this.currency = parcel.readString();
        this.rate = parcel.readDouble();
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
        dest.writeString(currency);
        dest.writeDouble(rate);

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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }


}
