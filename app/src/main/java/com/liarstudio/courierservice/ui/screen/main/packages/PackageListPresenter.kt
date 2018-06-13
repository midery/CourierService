package com.liarstudio.courierservice.ui.screen.main.packages

import android.content.Intent
import android.os.Bundle
import com.liarstudio.courierservice.entitiy.pack.Coordinates
import com.liarstudio.courierservice.injection.scope.PerScreen
import com.liarstudio.courierservice.logic.pack.PackageLoader
import com.liarstudio.courierservice.logic.user.UserLoader
import com.liarstudio.courierservice.ui.base.EXTRA_FIRST
import com.liarstudio.courierservice.ui.base.EXTRA_SECOND
import com.liarstudio.courierservice.ui.base.screen.LoadState
import com.liarstudio.courierservice.ui.base.MessageShower
import com.liarstudio.courierservice.ui.base.screen.presenter.BasePresenter
import com.liarstudio.courierservice.ui.screen.maps.MapActivity
import com.liarstudio.courierservice.ui.screen.pack.PackageAction
import com.liarstudio.courierservice.ui.screen.pack.PackageActivity
import io.reactivex.disposables.Disposables
import retrofit2.HttpException
import javax.inject.Inject

@PerScreen
class PackageListPresenter @Inject constructor(
        private val packageLoader: PackageLoader,
        private val userLoader: UserLoader,
        private val messageShower: MessageShower,
        view: PackageListFragment
) : BasePresenter<PackageListFragment>(view) {

    private val model = PackageListScreenModel { view.renderRefreshState(it) }
    private var loadPackagesDisposable = Disposables.disposed()

    fun init(args: Bundle) {
        model.isAdminMode = userLoader.getCurrentUser().isAdmin
        model.tabType = args.getSerializable(EXTRA_FIRST) as PackTabType
        initStatuses()
        loadUserPackages()
    }


    fun initStatuses() {
        model.statuses = when (model.tabType) {
            PackTabType.ACTIVE -> if (model.isAdminMode) listOf(0, 2, 3) else listOf(1, 2)
            PackTabType.DELIVERED -> listOf(4)
            PackTabType.ALL -> if (model.isAdminMode) listOf(0, 2, 3, 4) else listOf(1, 2, 4)
            null -> listOf(0)
        }
    }

    fun reloadPackages() {
        loadUserPackages()
    }

    fun openPackageScreen(id: Long) {
        view.startActivity(
                Intent(view.activity, PackageActivity::class.java)
                        .apply {
                            putExtra(EXTRA_FIRST, PackageAction.EDIT)
                            putExtra(EXTRA_SECOND, id)
                        }
        )
    }

    fun openMap(coordinates: Coordinates) {
        view.startActivity(
                Intent(view.activity, MapActivity::class.java)
                        .apply { putExtra(EXTRA_FIRST, coordinates) }
        )
    }

    private fun loadUserPackages() {
        model.loadState = LoadState.LOADING
        loadPackagesDisposable = subscribe(packageLoader.getUserPackages(
                userLoader.getCurrentUser().id,
                *model.statuses.toIntArray()),
                {
                    model.loadState = LoadState.NONE
                    model.packages = it
                    view.render(model)
                },
                {
                    model.loadState = LoadState.ERROR
                    view.render(model)
                    if (it is HttpException) {
                        messageShower.show("Произошла ошибка загрузки данных.")
                    }
                })
    }

}