package com.liarstudio.courierservice.ui.screen.main.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.liarstudio.courierservice.entitiy.pack.Package;
import com.liarstudio.courierservice.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Меняем WEIGHT_PROGRAM_STATE при изменении значения у weightDimensions
        ListPreference weightDimensions = (ListPreference)findPreference("weight_dimensions");
        weightDimensions.setOnPreferenceChangeListener(((preference, newValue) -> {
            Package.WEIGHT_PROGRAM_STATE = Integer.parseInt((String) newValue);
            return true;
        }));

        //Аналогично WEIGHT
        Preference sizeDimensions = findPreference("size_dimensions");
        sizeDimensions.setOnPreferenceChangeListener(((preference, newValue) -> {
            Package.SIZE_PROGRAM_STATE = Integer.parseInt((String) newValue);
            return true;
        }));
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
    }

}
