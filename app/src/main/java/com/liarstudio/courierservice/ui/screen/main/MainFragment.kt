package com.liarstudio.courierservice.ui.screen.main


import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v13.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.google.gson.Gson
import com.liarstudio.courierservice.R
import com.liarstudio.courierservice.entitiy.pack.Package
import com.liarstudio.courierservice.logic.ServerUtils
import com.liarstudio.courierservice.logic.pack.PackageApi
import com.liarstudio.courierservice.logic.pack.PackageRepository
import com.liarstudio.courierservice.ui.screen.auth.AuthActivity
import com.liarstudio.courierservice.ui.screen.main.my_packages.PagerAdapterMyPackages
import com.liarstudio.courierservice.ui.screen.main.new_packages.PagerAdapterNewPackages
import com.liarstudio.courierservice.ui.screen.pack.PackageFieldsActivity

import java.net.HttpURLConnection

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import android.app.Activity.RESULT_OK
import com.liarstudio.courierservice.logic.ServerUtils.CURRENT_USER
import com.liarstudio.courierservice.logic.ServerUtils.IS_ADMIN
import com.liarstudio.courierservice.ui.base.EXTRA_FIRST
import kotlinx.android.synthetic.main.fragment_home.*

class MainFragment : Fragment() {

    private val REQUEST_ADD_OR_EDIT = 1
    lateinit var tabType: MainTabType

    lateinit var adapter: FragmentStatePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)


        val viewPager = view.findViewById<View>(R.id.viewPager) as ViewPager
        val tabLayout = view.findViewById<View>(R.id.tabLayout) as TabLayout

        tabType = arguments.get(EXTRA_FIRST) as MainTabType

        when (tabType) {
            MainTabType.MY -> adapter = PagerAdapterMyPackages(activity.fragmentManager)
            MainTabType.NEW -> {
                adapter = PagerAdapterNewPackages(activity.fragmentManager)
                tabLayout.visibility = View.GONE
            }
        }

        viewPager.adapter = adapter

        tabLayout.setupWithViewPager(viewPager)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //loadListFromServer()
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {

        //Скрываем кнопку добавления для администратора
        val item = menu!!.findItem(R.id.item_add)
        item.isVisible = !IS_ADMIN
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        activity.menuInflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.item_add -> {
                val addIntent = Intent(activity, PackageFieldsActivity::class.java)
                activity.startActivityForResult(addIntent, REQUEST_ADD_OR_EDIT)
            }

            R.id.item_refresh -> {}//loadListFromServer()
            R.id.item_logout -> {
                CURRENT_USER = null
                startActivity(Intent(activity, AuthActivity::class.java))
                activity.finish()
            }
        }
        return true

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_ADD_OR_EDIT) {
            if (data!!.hasExtra("packageToAdd")) {
                val jsonPackage = data.getStringExtra("packageToAdd")
                val pack = Gson().fromJson(jsonPackage, Package::class.java)
        //        addToServer(pack)
            } else {
                if (data.hasExtra("packageToDelete")) {
                    val jsonPackage = data.getStringExtra("packageToDelete")
                    val pack = Gson().fromJson(jsonPackage, Package::class.java)
      //              deleteFromServer(pack.id!!.toInt())
                }
            }
        }
    }

    /*private fun addToServer(pkg: Package) {
        main_pb.visibility = View.VISIBLE

        val retrofit = Retrofit.Builder()
                .baseUrl(ServerUtils.BASE_SERVER_URL)
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
                .baseUrl(ServerUtils.BASE_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api = retrofit.create(PackageApi::class.java)
        val apiCall: Call<List<Package>>

        // Если пользователь - администратор, то загружаем "Новые"/"Отмененные"/"Завершенные"
        // Если курьер - то в зависимости от статуса(типа adapter'а) загружаем либо только новые,
        // либо "Назначенные"/"В процессе"/"Завершенные"

        if (IS_ADMIN)
            apiCall = api.getAdminPackages()
        else
            apiCall = api.getPackagesFromCourier(ServerUtils.CURRENT_USER!!.id, 1 - tabType!!.ordinal)


        apiCall.enqueue(
                object : Callback<List<Package>> {
                    override fun onResponse(call: Call<List<Package>>, response: Response<List<Package>>) {
                        when (response.code()) {
                            HttpURLConnection.HTTP_OK ->

                                // Если тело запроса не пусто,
                                // загружаем список посылок и перезагружаем локальные данные,

                                if (response.body() != null) {
                                    val pkgList = PackageRepository(response.body()!!)
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

                    override fun onFailure(call: Call<List<Package>>, t: Throwable) {
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
                .baseUrl(ServerUtils.BASE_SERVER_URL)
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

}
