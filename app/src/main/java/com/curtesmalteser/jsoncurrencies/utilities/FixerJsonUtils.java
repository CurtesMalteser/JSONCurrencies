package com.curtesmalteser.jsoncurrencies.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.curtesmalteser.jsoncurrencies.R;
import com.curtesmalteser.jsoncurrencies.model.CurrenciesModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by anton on 29/11/2017.
 */

public class FixerJsonUtils {

    private static final String FJU_CURRENCY_DATE = "date"; // "date":"2017-11-28"

    private static final String FJU_RATES = "rates"; // "rates"

    private static final String FJU_BASE = "base"; // "base"

    //"EUR"

    private static final String[] FJU_COINS = { "AUD",
            "BGN",
            "BRL",
            "CAD",
            "CHF",
            "CNY",
            "CZK",
            "DKK",
            "GBP",
            "HKD",
            "HRK",
            "HUF",
            "IDR",
            "ILS",
            "INR",
            "JPY",
            "KRW",
            "MXN",
            "MYR",
            "NOK",
            "NZD",
            "PHP",
            "PLN",
            "RON",
            "RUB",
            "SEK",
            "SGD",
            "THB",
            "TRY",
            "USD",
            "ZAR"};

        public static ArrayList<CurrenciesModel> getCurrencies ( String resultJson) throws JSONException {

        ArrayList<CurrenciesModel> mCrrenciesModelArrayList= new ArrayList<>();

        JSONObject baseObject = new JSONObject(resultJson);

        JSONObject ratesObject = baseObject.getJSONObject(FJU_RATES);


        for (int i = 0; i < ratesObject.length(); i++) {
            // Model params: base, date, coin, currency
            mCrrenciesModelArrayList.add(i, new CurrenciesModel(
                    i,
                    getString(FJU_BASE, baseObject),
                    getString(FJU_CURRENCY_DATE, baseObject),
                    FJU_COINS[i],
                    getDouble(FJU_COINS[i], ratesObject)
            ));
        }

        return mCrrenciesModelArrayList;
    }

    //this return all JSONObject to be parsed
    private static JSONObject getJsonObject(String tagName, JSONObject jsonObject) throws JSONException {
        return jsonObject.getJSONObject(tagName);
    }

    // these methods getString, getInt or geFloat are used to get strings, or ID's, or the prices of the products

    private static String getString(String tagName, JSONObject jsonObject) throws JSONException {

        return jsonObject.getString(tagName);
    }

    private static double getDouble(String tagName, JSONObject jsonObject) throws JSONException {

        //the cast for float is because the double consumes more memory due to more precision digits
        return jsonObject.getDouble(tagName);
    }

}
