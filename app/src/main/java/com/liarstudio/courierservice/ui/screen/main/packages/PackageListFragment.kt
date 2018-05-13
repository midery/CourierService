package com.liarstudio.courierservice.ui.screen.main.packages

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.liarstudio.courierservice.R
import com.liarstudio.courierservice.logic.pack.PackageRepository
import com.liarstudio.courierservice.ui.base.screen.BaseFragment
import com.liarstudio.courierservice.ui.base.EXTRA_FIRST
import com.liarstudio.courierservice.ui.base.LoadState
import com.liarstudio.courierservice.ui.screen.main.packages.controller.PackageListElementController
import kotlinx.android.synthetic.main.list_packages.*
import ru.surfstudio.easyadapter.recycler.EasyAdapter
import ru.surfstudio.easyadapter.recycler.ItemList
import javax.inject.Inject


class PackageListFragment : BaseFragment<PackageListScreenModel>() {

    @Inject
    lateinit var presenter: PackageListPresenter

    //Количество посылок в текущем фрагменте
    lateinit var packages: PackageRepository
    lateinit var adapter: EasyAdapter

    val packageController = PackageListElementController(
            { presenter.openPackageScreen(it) },
            { presenter.openMap(it) }
    )

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?)
            = inflater!!.inflate(R.layout.list_packages, container, false)

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
        packages_swr.isRefreshing = isRefreshing
    }

    companion object {
        fun newInstance(status: PackTabType) =
                PackageListFragment().apply {
                    arguments = Bundle().apply { putSerializable(EXTRA_FIRST, status) }
                }
    }
}