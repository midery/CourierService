package com.liarstudio.courierservice.ui.base

import android.view.View

import android.widget.AdapterView
import android.widget.Spinner
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
fun Spinner.positionChanges() : Observable<Int> {
    val listener = object : AdapterView.OnItemSelectedListener {
        val itemSelectedSubject = PublishSubject.create<Int>()
        override fun onNothingSelected(parent: AdapterView<*>?) { /*без реализации*/ }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            itemSelectedSubject.onNext(position)
        }
    }
    onItemSelectedListener = listener
    return listener.itemSelectedSubject
}

var View.isVisible: Boolean
get() = visibility == View.VISIBLE
set(value) { visibility = if (value) View.VISIBLE else View.GONE }
