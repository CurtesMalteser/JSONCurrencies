package com.curtesmalteser.jsoncurrencies.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import com.curtesmalteser.jsoncurrencies.R;
import com.curtesmalteser.jsoncurrencies.model.CurrenciesModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by António "Curtes Malteser" Bastião on 29/11/2017.
 */

public class FixerJsonUtils {

    // FJU == FixerJsonUtils
    private static final String FJU_CURRENCY_DATE = "date"; // "date":"2017-11-28"

    private static final String FJU_RATES = "rates"; // "rates"

    private static final String FJU_BASE = "base"; // "base"

    private static String[] fju_coins; // Array of currencies to parse the Json response

    public static ArrayList<CurrenciesModel> getCurrencies ( Context context, String resultJson ) throws JSONException {

        fju_coins = context.getResources().getStringArray(R.array.currencies_array);

        ArrayList<CurrenciesModel> mCrrenciesModelArrayList= new ArrayList<>();

        JSONObject baseObject = new JSONObject(resultJson);

        JSONObject ratesObject = baseObject.getJSONObject(FJU_RATES);

        /** Makes a copy of the currencies FJU_COINS and exclude the base currency so,
         * this way it will no be parsed on JSON ratesObject avoiding errors
         * because the base currency key doesn't exist on this JSONObject
         */
        ArrayList<String> currencies = new ArrayList<>();
        for (int y = 0; y <= ratesObject.length(); y++) {
            if (ratesObject.length() >= fju_coins.length) {
                throw new IndexOutOfBoundsException("IndexOutOfBoundsException: " + ratesObject.length());
            } else {
                if (!getString(FJU_BASE, baseObject).equals(fju_coins[y])) {
                    currencies.add(fju_coins[y]);
                }
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

    public static ContentValues[] getCurrenciesContentValues (Context context, String resultJson) throws JSONException {

        fju_coins = context.getResources().getStringArray(R.array.currencies_array);

        JSONObject baseObject = new JSONObject(resultJson);

        JSONObject ratesObject = baseObject.getJSONObject(FJU_RATES);

        /** Makes a copy of the currencies FJU_COINS and exclude the base currency so,
         * this way it will no be parsed on JSON ratesObject avoiding errors
         * because the base currency key doesn't exist on this JSONObject
         */
        ArrayList<String> currencies = new ArrayList<>();
        for (int y = 0; y <= ratesObject.length(); y++) {
            if (!getString(FJU_BASE, baseObject).equals(fju_coins[y])) {
                currencies.add(fju_coins[y]);
            }
        }

        ContentValues[] currenciesContentValues = new ContentValues[currencies.size()];

        for (int i = 0; i < currencies.size(); i++) {

            ContentValues contentValues = new ContentValues();

            contentValues.put( CurrenciesModel.COLUMN_ID, i);
            contentValues.put( CurrenciesModel.COLUMN_BASE, getString(FJU_BASE, baseObject));
            contentValues.put( CurrenciesModel.COLUMN_DATE, getString(FJU_CURRENCY_DATE, baseObject));
            contentValues.put( CurrenciesModel.COLUMN_CURRENCY, currencies.get(i));
            contentValues.put( CurrenciesModel.COLUMN_RATE, getDouble(currencies.get(i), ratesObject));

            currenciesContentValues[i] = contentValues;

        }

        return currenciesContentValues;
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