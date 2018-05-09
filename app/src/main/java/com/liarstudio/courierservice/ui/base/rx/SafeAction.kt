package com.liarstudio.courierservice.ui.base.rx

import io.reactivex.functions.Action

interface SafeAction: Action{
    override fun run()
}