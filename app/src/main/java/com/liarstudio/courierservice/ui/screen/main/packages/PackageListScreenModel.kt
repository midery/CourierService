package com.liarstudio.courierservice.ui.screen.main.packages

import com.liarstudio.courierservice.entitiy.pack.Pack
import com.liarstudio.courierservice.ui.base.screen.model.BaseScreenModel

class PackageListScreenModel(
        private val refreshingCallBack: (Boolean) -> Unit
): BaseScreenModel() {
    var isRefreshing = false
        set(value) = refreshingCallBack(isRefreshing)
    var isAdminMode = false
    var tabType: PackTabType? = null
    var packages = emptyList<Pack>()
    var statuses = emptyList<Int>()
}