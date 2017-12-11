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
import java.util.Arrays;

/**
 * Created by anton on 29/11/2017.
 */

public class FixerJsonUtils {

    private static final String FJU_CURRENCY_DATE = "date"; // "date":"2017-11-28"

    private static final String FJU_RATES = "rates"; // "rates"

    private static final String FJU_BASE = "base"; // "base"

    // TODO - Replace this array with res array that is already done
    private static final String[] FJU_COINS = { "EUR",
            "AUD",
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

        /** Makes a copy of the currencies FJU_COINS and exclude the base currency so,
         * this way it will no be parsed on JSON ratesObject avoiding errors
         * because the base currency key doesn't exist on this JSONObject
         */
        ArrayList<String> currencies = new ArrayList<>();
        for (int y = 0; y <= ratesObject.length(); y++) {
            if (!getString(FJU_BASE, baseObject).equals(FJU_COINS[y])) {
                currencies.add(FJU_COINS[y]);
            }



        }
        for (int i = 0; i < currencies.size(); i++) {
            mCrrenciesModelArrayList.add(i, new CurrenciesModel(
                    i,
                    getString(FJU_BASE, baseObject),
                    getString(FJU_CURRENCY_DATE, baseObject),
                    currencies.get(i),
                    getDouble(currencies.get(i), ratesObject)
            ));
        }




        return mCrrenciesModelArrayList;
    }

    public static CurrenciesModel getSingleCurrency (String currency, String resultJson) throws JSONException {

        JSONObject baseObject = new JSONObject(resultJson);

        JSONObject ratesObject = baseObject.getJSONObject(FJU_RATES);

         CurrenciesModel currenciesModel = new CurrenciesModel(
                getString(FJU_BASE, baseObject),
                getString(FJU_CURRENCY_DATE, baseObject),
                currency,
                getDouble(currency, ratesObject)
        );

        return currenciesModel;
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
