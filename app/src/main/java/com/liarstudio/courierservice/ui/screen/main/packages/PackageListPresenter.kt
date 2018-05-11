package com.liarstudio.courierservice.ui.screen.main.packages

import android.content.Intent
import android.os.Bundle
import com.liarstudio.courierservice.entitiy.person.Coordinates
import com.liarstudio.courierservice.entitiy.user.User
import com.liarstudio.courierservice.injection.scope.PerScreen
import com.liarstudio.courierservice.logic.pack.PackageLoader
import com.liarstudio.courierservice.ui.base.EXTRA_FIRST
import com.liarstudio.courierservice.ui.base.LoadState
import com.liarstudio.courierservice.ui.base.SnackController
import com.liarstudio.courierservice.ui.base.screen.BasePresenter
import com.liarstudio.courierservice.ui.screen.pack.PackageFieldsActivity
import io.reactivex.disposables.Disposables
import retrofit2.HttpException
import javax.inject.Inject

@PerScreen
class PackageListPresenter @Inject constructor(
        private val packageLoader: PackageLoader,
        private val snackController: SnackController,
        view: PackageListFragment
) : BasePresenter<PackageListFragment>(view) {

    private val model = PackageListScreenModel { view.renderRefreshState(it) }
    private var loadPackagesDisposable = Disposables.disposed()

    fun init(args: Bundle) {
        model.tabType = args.getSerializable(EXTRA_FIRST) as PackTabType
        loadPackages()
    }

    fun reloadPackages() {
        model.isRefreshing = true
        loadPackagesDisposable = subscribe(packageLoader.getCourierPackages(User.CURRENT.id.toLong()),
                {
                    model.isRefreshing = false
                    //model.packages = PackageRepository(it)
                    view.render(model)
                    snackController.show("Данные загружены корректно.")
                },
                {
                    model.isRefreshing = false
                    view.render(model)
                    if (it is HttpException) {
                        snackController.show("Произошла ошибка загрузки данных.")
                    }
                })
    }

    fun openPackageScreen(id: Long) {
        view.startActivity(
                Intent(view.activity, PackageFieldsActivity::class.java)
                        .apply { putExtra(EXTRA_FIRST, id) }
        )
    }

    fun openMap(coordinates: Coordinates) {
        view.startActivity(
                Intent(view.activity, PackageFieldsActivity::class.java)
                        .apply { putExtra(EXTRA_FIRST, coordinates) }
        )
    }

    private fun loadPackages() {
        loadPackagesDisposable = subscribe(packageLoader.getCourierPackages(User.CURRENT.id.toLong()),
                {
                    model.loadState = LoadState.NONE
                    //model.packages = PackageRepository(it)
                    view.render(model)
                    snackController.show("Данные загружены корректно.")
                },
                {
                    model.loadState = LoadState.ERROR
                    view.render(model)
                    if (it is HttpException) {
                        snackController.show("Произошла ошибка загрузки данных.")
                    }
                })
    }
}