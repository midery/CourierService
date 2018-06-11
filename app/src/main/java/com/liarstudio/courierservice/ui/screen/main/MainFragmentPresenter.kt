package com.liarstudio.courierservice.ui.screen.main

import android.content.Intent
import com.liarstudio.courierservice.entitiy.user.User
import com.liarstudio.courierservice.injection.scope.PerScreen
import com.liarstudio.courierservice.logic.user.UserLoader
import com.liarstudio.courierservice.ui.base.EXTRA_FIRST
import com.liarstudio.courierservice.ui.base.screen.presenter.BasePresenter
import com.liarstudio.courierservice.ui.screen.auth.AuthActivity
import com.liarstudio.courierservice.ui.screen.pack.PackageAction
import com.liarstudio.courierservice.ui.screen.pack.PackageActivity
import javax.inject.Inject

@PerScreen
class MainFragmentPresenter @Inject constructor(
        private val userLoader: UserLoader,
        view: MainFragment
): BasePresenter<MainFragment>(view) {

    val screenModel = MainScreenModel()

    override fun viewCreated() {
        screenModel.user = userLoader.getCurrentUser()
        view.renderData(screenModel)
    }
    fun openPackageScreen() {
        val addIntent = Intent(view.activity, PackageActivity::class.java)
                .putExtra(EXTRA_FIRST, PackageAction.ADD)
        view.startActivity(addIntent)
    }

    fun logout() {
        userLoader.putCurrentUser(User())
        view.startActivity(Intent(view.activity, AuthActivity::class.java))
        view.activity.finish()
    }
}