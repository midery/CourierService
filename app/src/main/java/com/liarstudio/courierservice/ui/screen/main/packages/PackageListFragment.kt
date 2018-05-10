package com.liarstudio.courierservice.ui.screen.main.packages

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.google.gson.GsonBuilder
import com.liarstudio.courierservice.logic.pack.PackageApi
import com.liarstudio.courierservice.logic.ServerUtils
import com.liarstudio.courierservice.ui.screen.pack.PackageFieldsActivity
import com.liarstudio.courierservice.entitiy.pack.Package
import com.liarstudio.courierservice.logic.pack.PackageRepository
import com.liarstudio.courierservice.R
import com.liarstudio.courierservice.ui.base.BaseFragment
import com.liarstudio.courierservice.ui.screen.main.packages.controller.PackageListElementController
import kotlinx.android.synthetic.main.list_packages.*

import java.net.HttpURLConnection

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.surfstudio.easyadapter.recycler.EasyAdapter
import ru.surfstudio.easyadapter.recycler.ItemList
import javax.inject.Inject


class PackageListFragment : BaseFragment<PackageListScreenModel>() {
    private val REQUEST_ADD_OR_EDIT = 1

    @Inject
    lateinit var presenter: PackageListPresenter

    //Количество посылок в текущем фрагменте
    lateinit var packages: PackageRepository
    lateinit var adapter: EasyAdapter

    val packageController = PackageListElementController(
            { presenter.openPackageScreen(it) },
            { presenter.openMap(it) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) = inflater!!.inflate(R.layout.list_packages, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        presenter.loadPackages()
    }

    fun initRecycler() {
        adapter = EasyAdapter()
        recycler.adapter = adapter
    }

    override fun render(screenModel: PackageListScreenModel) {
        adapter.setItems(
                ItemList.create()
                        .addAll(screenModel.packages, packageController)
        )
    }

}