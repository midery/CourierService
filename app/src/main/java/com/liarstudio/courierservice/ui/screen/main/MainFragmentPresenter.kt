package com.liarstudio.courierservice.ui.screen.main

import com.liarstudio.courierservice.injection.scope.PerScreen
import com.liarstudio.courierservice.ui.base.EXTRA_FIRST
import com.liarstudio.courierservice.ui.base.screen.BasePresenter
import com.liarstudio.courierservice.ui.screen.main.MainFragment
import javax.inject.Inject

@PerScreen
class MainFragmentPresenter @Inject constructor(
        view: MainFragment
): BasePresenter<MainFragment>(view)