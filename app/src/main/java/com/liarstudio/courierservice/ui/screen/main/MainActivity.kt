package com.liarstudio.courierservice.ui.screen.main

import android.content.Intent
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.preference.PreferenceManager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.TextView

import com.liarstudio.courierservice.logic.ServerUtils
import com.liarstudio.courierservice.ui.screen.main.settings.SettingsFragment
import com.liarstudio.courierservice.R
import com.liarstudio.courierservice.entitiy.pack.Package
import com.liarstudio.courierservice.ui.screen.auth.AuthActivity

import com.liarstudio.courierservice.logic.ServerUtils.CURRENT_USER
import com.liarstudio.courierservice.ui.base.EXTRA_FIRST

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val VOL_COEFFICIENT = "size_dimensions"
    val WEIGHT_COEFFICIENT = "weight_dimensions"

    var tabType = MainTabType.MY

    lateinit var drawer: DrawerLayout
    lateinit var fragment: Fragment

    /*
    ****** METHODS AREA ******
    */


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        loadCoefficients()


        //Создаем новый MainFragment с менеджером PagerAdapterMyPackages
        fragment = MainFragment().apply { arguments = Bundle().apply { putSerializable(EXTRA_FIRST, MainTabType.MY) } }
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.content_main, fragment).commit()


        //Назначаем toolBar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //Создаем navigationDrawer
        drawer = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        //Меняем textView у заголовка drawer'a в соответствии с имененем пользователя
        val header = navigationView.getHeaderView(0)
        val textViewNavHeader = header.findViewById<TextView>(R.id.textViewNavHeader)
        textViewNavHeader.text = CURRENT_USER.name


        //Меняем названия и доступность пунктов меню drawer'а в зависимости от того, является ли
        //пользователь администратором
        val nav_my = navigationView.menu.findItem(R.id.nav_my)
        val nav_new = navigationView.menu.findItem(R.id.nav_new)

        if (ServerUtils.IS_ADMIN) {
            nav_my.setTitle(R.string.nav_my_admin)
            nav_new.isVisible = false
        } else {
            nav_my.setTitle(R.string.nav_my)
            nav_new.isVisible = true
        }
    }


    /*
    * Функция, вызываемая после завершения activity, запущенной для получения результата
    * Вызываем onActivityResult с теми же параметрами у фрагментов
     */
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        //super.onActivityResult(requestCode,resultCode, data);
        fragment.onActivityResult(requestCode, resultCode, data)

    }

    /*
    * Функция, вызываемая при выборе одного из элементов меню drawer'а
    */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        fragment = when (item.itemId) {
            R.id.nav_my -> MainFragment().apply { arguments = Bundle().apply { putSerializable(EXTRA_FIRST, MainTabType.MY) } }
            R.id.nav_new -> MainFragment().apply { arguments = Bundle().apply { putSerializable(EXTRA_FIRST, MainTabType.NEW) } }
            R.id.nav_settings -> SettingsFragment()
            else -> {
                CURRENT_USER = null
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
                return true
            }
        }
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.content_main, fragment).commit()
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    /*
    * Функция считывания коэффициентов из Preferences
     */
    private fun loadCoefficients() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        Package.WEIGHT_PROGRAM_STATE = Integer.parseInt(sharedPref.getString(WEIGHT_COEFFICIENT, "0"))
        Package.SIZE_PROGRAM_STATE = Integer.parseInt(sharedPref.getString(VOL_COEFFICIENT, "0"))
    }
}
