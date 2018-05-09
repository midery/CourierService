package com.liarstudio.courierservice.ui.base

import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.liarstudio.courierservice.injection.scope.PerScreen
import com.liarstudio.courierservice.ui.screen.auth.AuthActivity
import javax.inject.Inject

@PerScreen
class SnackController @Inject constructor(val view: AppCompatActivity) {

    fun show(message: String, duration: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(view.findViewById(android.R.id.content), message, duration)
    }

    fun show(@StringRes resId: Int, duration: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(view.findViewById(android.R.id.content), resId, duration).show()
    }
}