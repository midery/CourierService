package com.liarstudio.courierservice.ui.screen.main


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.*
import com.google.gson.Gson
import com.liarstudio.courierservice.R
import com.liarstudio.courierservice.entitiy.pack.Package
import com.liarstudio.courierservice.entitiy.user.User
import com.liarstudio.courierservice.ui.base.screen.BaseFragment
import com.liarstudio.courierservice.ui.base.EXTRA_FIRST
import com.liarstudio.courierservice.ui.base.LoadState
import com.liarstudio.courierservice.ui.screen.auth.AuthActivity
import com.liarstudio.courierservice.ui.screen.main.my_packages.PagerAdapterMyPackages
import com.liarstudio.courierservice.ui.screen.main.new_packages.PagerAdapterNewPackages
import com.liarstudio.courierservice.ui.screen.pack.PackageFieldsActivity

class MainFragment : BaseFragment<MainScreenModel>() {

    private val REQUEST_ADD_OR_EDIT = 1
    lateinit var tabType: MainTabType

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

        viewPager.adapter = when (tabType) {
            MainTabType.MY -> PagerAdapterMyPackages(activity.fragmentManager)
            MainTabType.NEW -> {
                tabLayout.visibility = View.GONE
                PagerAdapterNewPackages(activity.fragmentManager)
            }
        }

        viewPager.offscreenPageLimit = 3
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
        item.isVisible = User.CURRENT.isAdmin
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

            R.id.item_refresh -> { }//loadListFromServer()
            R.id.item_logout -> {
                User.CURRENT.id = -1
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
        val apiCall: Call<List<Package>>

        // Если пользователь - администратор, то загружаем "Новые"/"Отмененные"/"Завершенные"
        // Если курьер - то в зависимости от статуса(типа adapter'а) загружаем либо только новые,
        // либо "Назначенные"/"В процессе"/"Завершенные"

        if (IS_ADMIN)
            apiCall = api.getAdminPackages()
        else
            apiCall = api.getCourierPackages(UrlServer.CURRENT_USER!!.id, 1 - tabType!!.ordinal)


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
        //
    }

    override fun renderState(loadState: LoadState) {

    }
}
