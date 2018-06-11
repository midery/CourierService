package com.liarstudio.courierservice.ui.screen.main

import android.app.Fragment
import com.liarstudio.courierservice.entitiy.user.User
import com.liarstudio.courierservice.ui.base.screen.model.BaseScreenModel

class MainScreenModel : BaseScreenModel() {
    var user: User? = null
    var currentFragment: Fragment? = null
}