package com.curtesmalteser.jsoncurrencies.fragment;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.curtesmalteser.jsoncurrencies.R;

/**
 * Created by anton on 10/12/2017.
 */

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_visualizer);
    }
}
