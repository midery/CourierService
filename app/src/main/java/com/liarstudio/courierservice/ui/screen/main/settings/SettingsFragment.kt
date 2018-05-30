package com.liarstudio.courierservice.ui.screen.main.settings

import android.app.Fragment
import android.os.Bundle
import android.preference.*
import android.support.v7.preference.PreferenceGroup
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.liarstudio.courierservice.entitiy.pack.Pack
import com.liarstudio.courierservice.R
import com.liarstudio.courierservice.ui.base.LoadState
import com.liarstudio.courierservice.ui.base.screen.BaseFragment
import com.liarstudio.courierservice.ui.base.screen.BasePresenter
import java.util.prefs.PreferenceChangeListener
import java.util.prefs.PreferencesFactory

class SettingsFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.settings)
        //Меняем WEIGHT_PROGRAM_STATE при изменении значения у weightDimensions
        val weightDimensions = findPreference("weight_dimensions") as ListPreference
        weightDimensions.setOnPreferenceChangeListener { _, newValue ->
            Pack.WEIGHT_PROGRAM_STATE = Integer.parseInt(newValue as String)
            true
        }

        //Аналогично WEIGHT
        val sizeDimensions = findPreference("size_dimensions")
        sizeDimensions.setOnPreferenceChangeListener { _, newValue ->
            Pack.SIZE_PROGRAM_STATE = Integer.parseInt(newValue as String)
            true
        }
    }

/*    override fun requestPresenter(): BasePresenter<*> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLayout() = R.layout.fragment_preferences

    override fun renderData(screenModel: SettingsModel) { }

    override fun renderState(loadState: LoadState) { }*/
}
