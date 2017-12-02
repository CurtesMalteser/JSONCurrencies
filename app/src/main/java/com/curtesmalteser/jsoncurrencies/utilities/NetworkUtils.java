package com.curtesmalteser.jsoncurrencies.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by anton on 29/11/2017. Base on
 * Udacity Google Developer Challenge Scholarship: Android Dev ? lessons
 */

/**
 * These utilities will be used to communicate with the network.
 */

public class NetworkUtils {

    final static String FIXER_BASE_URL = "https://api.fixer.io/";

    /*
     * The latest string can be replaced on the future with specific dates.
     * This is a GitHub example project to enrich my portfolio.
     */
    final static String FIXER_LATEST = "latest";

    final static String QUERY_BASE_CUR = "?base=";

    final static String QUERY_SYMBOLS = "?symbols=";

    /**
     * Builds the URL used to query Fixer.io API.
     *
     * @param baseCurrency The selected currency that will be the base reference.
     * @return The URL to use to query the Fixer.io API.
     */

    public static URL buildUrlLatest(String baseCurrency) {
        Uri buildUri = Uri.parse(FIXER_BASE_URL).buildUpon()
                .appendPath(FIXER_LATEST)
                .appendQueryParameter(QUERY_BASE_CUR, baseCurrency)
                .build();

        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */

    public static String getResponseFromHttpUrl(URL url) throws  IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }

        } finally {
            urlConnection.disconnect();
        }
    }

}
