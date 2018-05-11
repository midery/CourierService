package com.liarstudio.courierservice.ui.screen.main.packages

import com.liarstudio.courierservice.logic.pack.PackageRepository
import com.liarstudio.courierservice.ui.base.screen.BaseScreenModel

class PackageListScreenModel(
        private val refreshingCallBack: (Boolean) -> Unit
): BaseScreenModel() {
    var isRefreshing = false
        set(value) = refreshingCallBack(isRefreshing)
    lateinit var tabType: PackTabType
    var packages = PackageRepository()
}