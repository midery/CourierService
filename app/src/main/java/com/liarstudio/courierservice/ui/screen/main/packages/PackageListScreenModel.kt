package com.liarstudio.courierservice.ui.screen.main.packages

import com.liarstudio.courierservice.logic.pack.PackageRepository
import com.liarstudio.courierservice.ui.base.BaseScreenModel

class PackageListScreenModel: BaseScreenModel() {
    var packages = PackageRepository()
}