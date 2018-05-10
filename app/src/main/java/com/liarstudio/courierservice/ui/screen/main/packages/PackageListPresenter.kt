package com.liarstudio.courierservice.ui.screen.main.packages

import com.liarstudio.courierservice.entitiy.coordinates.Coordinates
import com.liarstudio.courierservice.injection.scope.PerScreen
import com.liarstudio.courierservice.logic.pack.PackageLoader
import com.liarstudio.courierservice.logic.pack.PackageRepository
import com.liarstudio.courierservice.ui.base.BasePresenter
import io.reactivex.disposables.Disposables
import javax.inject.Inject

@PerScreen
class PackageListPresenter @Inject constructor(
        private val packageLoader: PackageLoader,
        view: PackageListFragment
) : BasePresenter<PackageListFragment>(view) {

    private val model = PackageListScreenModel()
    private var loadPackagesDisposable = Disposables.disposed()
    init {

    }

    fun loadPackages() {
        loadPackagesDisposable = subscribe(packageLoader.getPackages(),
                {
                    model.packages = PackageRepository(it)
                    view.render(model)
                },
                {

                })
    }

    fun openPackageScreen(id: Long) {

    }

    fun openMap(coordinates: Coordinates) {

    }
}