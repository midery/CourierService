package com.liarstudio.courierservice.ui.screen.main


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.google.gson.Gson
import com.liarstudio.courierservice.R
import com.liarstudio.courierservice.entitiy.pack.Pack
import com.liarstudio.courierservice.ui.base.EXTRA_FIRST
import com.liarstudio.courierservice.ui.base.screen.LoadState
import com.liarstudio.courierservice.ui.base.screen.view.BaseFragment
import com.liarstudio.courierservice.ui.screen.main.my_packages.PagerAdapterMyPackages
import com.liarstudio.courierservice.ui.screen.main.new_packages.PagerAdapterNewPackages
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class MainFragment : BaseFragment<MainScreenModel>() {


    @Inject
    lateinit var presenter: MainFragmentPresenter

    var isAdmin = false

    private val REQUEST_ADD_OR_EDIT = 1

    override fun requestPresenter() = presenter

    override fun getLayout() = R.layout.fragment_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initPager()
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        //Скрываем кнопку добавления для администратора
        val item = menu!!.findItem(R.id.item_add)
        item.isVisible = !isAdmin
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        activity.menuInflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.item_add -> {
                presenter.openPackageScreen()
            }
            R.id.item_refresh -> {

            }
            R.id.item_logout -> {
                presenter.logout()
            }
        }
        return true
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_ADD_OR_EDIT) {
            if (data!!.hasExtra("packageToAdd")) {
                val jsonPackage = data.getStringExtra("packageToAdd")
                val pack = Gson().fromJson(jsonPackage, Pack::class.java)
                //        addToServer(pack)
            } else {
                if (data.hasExtra("packageToDelete")) {
                    val jsonPackage = data.getStringExtra("packageToDelete")
                    val pack = Gson().fromJson(jsonPackage, Pack::class.java)
                    //              deleteFromServer(pack.id!!.toInt())
                }
            }
        }
    }

    private fun initPager() {
        val tabType = arguments[EXTRA_FIRST] as MainTabType
        viewPager.adapter = createPagerAdapter(tabType)
        viewPager.offscreenPageLimit = 3
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun createPagerAdapter(tabType: MainTabType) = when (tabType) {
        MainTabType.MY ->
            PagerAdapterMyPackages(childFragmentManager)
        MainTabType.NEW -> {
            tabLayout.visibility = View.GONE
            PagerAdapterNewPackages(childFragmentManager)
        }
    }
    /*private fun addToServer(pkg: Pack) {
        main_pb.visibility = View.VISIBLE

        val retrofit = Retrofit.Builder()
                .baseUrl(UrlServer.BASE_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api = retrofit.create(PackageApi::class.java)

        api.add(pkg).enqueue(
                object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        when (response.code()) {
                            HttpURLConnection.HTTP_OK ->

                                //Если все ОК и посылка добавлена - загружаем список посылок
                                loadListFromServer()
                            HttpURLConnection.HTTP_NOT_FOUND ->

                                Toast.makeText(activity, R.string.error_add_http_not_found,
                                        Toast.LENGTH_LONG).show()
                            else ->

                                Toast.makeText(activity, R.string.error_db,
                                        Toast.LENGTH_LONG).show()
                        }
                        main_pb.visibility = View.GONE

                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(activity, R.string.error_could_not_connect_to_server,
                                Toast.LENGTH_LONG).show()
                        main_pb.visibility = View.GONE

                    }
                }
        )
    }*/

    /*fun loadListFromServer() {
        main_pb.visibility = View.VISIBLE

        val retrofit = Retrofit.Builder()
                .baseUrl(UrlServer.BASE_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api = retrofit.create(PackageApi::class.java)
        val apiCall: Call<List<Pack>>

        // Если пользователь - администратор, то загружаем "Новые"/"Отмененные"/"Завершенные"
        // Если курьер - то в зависимости от статуса(типа adapter'а) загружаем либо только новые,
        // либо "Назначенные"/"В процессе"/"Завершенные"

        if (IS_ADMIN)
            apiCall = api.getAdminPackages()
        else
            apiCall = api.getCourierPackages(UrlServer.CURRENT_USER!!.id, 1 - tabType!!.ordinal)


        apiCall.enqueue(
                object : Callback<List<Pack>> {
                    override fun onResponse(call: Call<List<Pack>>, response: Response<List<Pack>>) {
                        when (response.code()) {
                            HttpURLConnection.HTTP_OK ->

                                // Если тело запроса не пусто,
                                // загружаем список посылок и перезагружаем локальные данные,

                                if (response.body() != null) {
                                    val pkgList = PackageRepo(response.body()!!)
                                    pkgList.reloadAll()
                                    adapter.notifyDataSetChanged()
                                }
                            HttpURLConnection.HTTP_NOT_FOUND -> Toast.makeText(activity, R.string.error_http_not_found,
                                    Toast.LENGTH_LONG).show()
                            else -> Toast.makeText(activity, R.string.error_db,
                                    Toast.LENGTH_LONG).show()
                        }
                        main_pb.visibility = View.GONE
                    }

                    override fun onFailure(call: Call<List<Pack>>, t: Throwable) {
                        Toast.makeText(activity, R.string.error_could_not_connect_to_server,
                                Toast.LENGTH_LONG).show()
                        main_pb.visibility = View.GONE


                    }
                }
        )
    }

    private fun deleteFromServer(id: Int) {
        main_pb.visibility = View.VISIBLE

        val retrofit = Retrofit.Builder()
                .baseUrl(UrlServer.BASE_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api = retrofit.create(PackageApi::class.java)


        api.delete(id).enqueue(
                object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        when (response.code()) {
                            HttpURLConnection.HTTP_OK ->

                                //Если все ОК и посылка удалена - загружаем список посылок
                                loadListFromServer()
                            HttpURLConnection.HTTP_NOT_FOUND ->

                                Toast.makeText(activity, R.string.error_add_http_not_found,
                                        Toast.LENGTH_LONG).show()
                            else ->

                                Toast.makeText(activity, R.string.error_db,
                                        Toast.LENGTH_LONG).show()
                        }
                        main_pb.visibility = View.GONE

                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(activity, R.string.error_could_not_connect_to_server,
                                Toast.LENGTH_LONG).show()
                        main_pb.visibility = View.GONE
                    }
                }
        )
    }*/

    override fun renderData(screenModel: MainScreenModel) {
        isAdmin = screenModel.user?.isAdmin ?: false
    }

    override fun renderState(loadState: LoadState) { /*без реализации*/ }
}
