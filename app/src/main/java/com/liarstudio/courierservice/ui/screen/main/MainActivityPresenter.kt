package com.liarstudio.courierservice.ui.screen.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.preference.PreferenceManager
import com.liarstudio.courierservice.R
import com.liarstudio.courierservice.entitiy.pack.Pack
import com.liarstudio.courierservice.entitiy.user.User
import com.liarstudio.courierservice.injection.scope.PerActivity
import com.liarstudio.courierservice.injection.scope.PerScreen
import com.liarstudio.courierservice.logic.user.UserLoader
import com.liarstudio.courierservice.ui.base.EXTRA_FIRST
import com.liarstudio.courierservice.ui.base.screen.BasePresenter
import com.liarstudio.courierservice.ui.base.VOL_COEFFICIENT
import com.liarstudio.courierservice.ui.base.WEIGHT_COEFFICIENT
import com.liarstudio.courierservice.ui.screen.auth.AuthActivity
import com.liarstudio.courierservice.ui.screen.main.settings.SettingsFragment
import javax.inject.Inject

@PerActivity
class MainActivityPresenter @Inject constructor(
        private val userLoader: UserLoader,
        view: MainActivity
): BasePresenter<MainActivity>(view) {

    val screenModel = MainScreenModel()

    override fun viewCreated() {
        screenModel.user = userLoader.getCurrentUser()
        view.renderData(screenModel)
    }

    fun loadCoeffs() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(view)
        Pack.WEIGHT_PROGRAM_STATE = Integer.parseInt(sharedPref.getString(WEIGHT_COEFFICIENT, "0"))
        Pack.SIZE_PROGRAM_STATE = Integer.parseInt(sharedPref.getString(VOL_COEFFICIENT, "0"))
    }

    fun openScreen(id: Int): Boolean {
        screenModel.currentFragment = when (id) {
            R.id.nav_my ->
                MainFragment().apply {
                    arguments = Bundle().apply { putSerializable(EXTRA_FIRST, MainTabType.MY) }
                }

            R.id.nav_new ->
                MainFragment().apply {
                    arguments = Bundle().apply { putSerializable(EXTRA_FIRST, MainTabType.NEW) }
                }

            R.id.nav_settings -> {
                SettingsFragment()
            }

            else -> {
                User.CURRENT.id = -1
                view.startActivity(Intent(view, AuthActivity::class.java))
                view.finish()
                return true
            }
        }
        return true
    }

}