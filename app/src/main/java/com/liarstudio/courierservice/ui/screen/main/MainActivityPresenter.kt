package com.liarstudio.courierservice.ui.screen.main

import android.support.v7.preference.PreferenceManager
import com.liarstudio.courierservice.entitiy.pack.Package
import com.liarstudio.courierservice.ui.base.BasePresenter
import com.liarstudio.courierservice.ui.base.VOL_COEFFICIENT
import com.liarstudio.courierservice.ui.base.WEIGHT_COEFFICIENT
import javax.inject.Inject

class MainActivityPresenter @Inject constructor(
        view: MainActivity
): BasePresenter<MainActivity>(view) {

    /**
     * Функция считывания коэффициентов из Preferences
     */
    fun loadCoeffs() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(view)
        Package.WEIGHT_PROGRAM_STATE = Integer.parseInt(sharedPref.getString(WEIGHT_COEFFICIENT, "0"))
        Package.SIZE_PROGRAM_STATE = Integer.parseInt(sharedPref.getString(VOL_COEFFICIENT, "0"))
    }

}