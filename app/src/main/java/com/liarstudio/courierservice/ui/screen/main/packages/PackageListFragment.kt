package com.liarstudio.courierservice.ui.screen.main.packages

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.liarstudio.courierservice.R
import com.liarstudio.courierservice.logic.pack.PackageRepo
import com.liarstudio.courierservice.ui.base.screen.view.BaseFragment
import com.liarstudio.courierservice.ui.base.EXTRA_FIRST
import com.liarstudio.courierservice.ui.base.screen.LoadState
import com.liarstudio.courierservice.ui.screen.main.packages.controller.PackageListElementController
import kotlinx.android.synthetic.main.list_packages.*
import ru.surfstudio.easyadapter.recycler.EasyAdapter
import javax.inject.Inject


class PackageListFragment : BaseFragment<PackageListScreenModel>() {

    @Inject
    lateinit var presenter: PackageListPresenter

    //Количество посылок в текущем фрагменте
    lateinit var packages: PackageRepo
    private lateinit var adapter: EasyAdapter

    private val packageController = PackageListElementController(
            { presenter.openPackageScreen(it) },
            { presenter.openMap(it) }
    )

    override fun requestPresenter() = presenter

    override fun getLayout() = R.layout.list_packages

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        initListeners()
        presenter.init(arguments)
    }

    private fun initRecycler() {
        adapter = EasyAdapter()
        recycler.adapter = adapter
        val layoutManager = LinearLayoutManager(activity)
        recycler.layoutManager = layoutManager
        recycler.addItemDecoration(DividerItemDecoration(recycler.context, layoutManager.orientation))
    }

    private fun initListeners() {
        packages_swr.setOnRefreshListener { presenter.reloadPackages() }
    }

    override fun renderState(loadState: LoadState) {
        packages_swr.isRefreshing = false
        when (loadState) {
            LoadState.NONE, LoadState.ERROR -> packages_pb.visibility = View.GONE
            LoadState.LOADING -> packages_pb.visibility = View.VISIBLE
        }
    }

    override fun renderData(screenModel: PackageListScreenModel) {
        packages_swr.isRefreshing = screenModel.isRefreshing

        adapter.setData(screenModel.packages, packageController)
        adapter.notifyDataSetChanged()
    }

    fun renderRefreshState(isRefreshing: Boolean) {
    }

    companion object {
        fun newInstance(status: PackTabType) =
                PackageListFragment().apply {
                    arguments = Bundle().apply { putSerializable(EXTRA_FIRST, status) }
                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            presenter.reloadPackages()
        }
    }
}