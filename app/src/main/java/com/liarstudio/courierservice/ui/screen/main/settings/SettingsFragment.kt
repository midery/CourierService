package com.liarstudio.courierservice.ui.screen.main.settings

import android.os.Bundle
import android.preference.*

import com.liarstudio.courierservice.entitiy.pack.Pack
import com.liarstudio.courierservice.R

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
