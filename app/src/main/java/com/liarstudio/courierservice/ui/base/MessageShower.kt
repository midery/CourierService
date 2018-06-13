package com.liarstudio.courierservice.ui.base

import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import com.liarstudio.courierservice.R
import javax.inject.Inject

/**
 * Класс, отвечающий за показ всплывающих сообщений
 * Действует в контексте Activity
 *
 * @param view Activity, в контексте которой будут показаны сообщения
 */
class MessageShower @Inject constructor(val view: AppCompatActivity) {

    /**
     * Функция, показывающая сообщение с заданной продолжительностью
     *
     * @param message сообщение
     * @param duration продолжительность
     */
    fun show(message: String, duration: Int = Toast.LENGTH_LONG) {
        val toastContainer = view.layoutInflater.inflate(R.layout.layout_toast, view.findViewById(R.id.toast_container), false)
        val messageTv = toastContainer.findViewById<TextView>(R.id.message_tv)
        messageTv.text = message

        val toast = Toast(view)
        toast.view = toastContainer
        toast.setGravity(Gravity.BOTTOM or Gravity.FILL_HORIZONTAL, 0, 0)
        toast.duration = duration
        toast.show()
    }

    /**
     * Функция, показывающая сообщение с заданной продолжительностью
     * Вместо строковой переменной на вход поступает ID ресурса, из которого извлекается сообщение
     *
     * @param resId id ресурса
     * @param duration продолжительность
     */
    fun show(@StringRes resId: Int, duration: Int = Toast.LENGTH_LONG) {
        val message = view.getString(resId)
        show(message)
    }
}