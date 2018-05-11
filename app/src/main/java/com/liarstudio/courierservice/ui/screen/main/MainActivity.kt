package com.liarstudio.courierservice.ui.screen.main

import android.content.Intent
import android.support.design.widget.NavigationView
import android.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.TextView

import com.liarstudio.courierservice.R
import com.liarstudio.courierservice.entitiy.user.User
import com.liarstudio.courierservice.ui.screen.auth.AuthActivity

import com.liarstudio.courierservice.ui.base.screen.BaseActivity
import com.liarstudio.courierservice.ui.base.EXTRA_FIRST
import com.liarstudio.courierservice.ui.base.LoadState
import javax.inject.Inject

class MainActivity : BaseActivity<MainScreenModel>(), NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var presenter: MainActivityPresenter
    var tabType = MainTabType.MY

    lateinit var drawer: DrawerLayout
    lateinit var fragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Создаем новый MainFragment с менеджером PagerAdapterMyPackages
        fragment = MainFragment().apply { arguments = Bundle().apply { putSerializable(EXTRA_FIRST, MainTabType.MY) } }
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.content_main, fragment).commit()

        initNavigationDrawer()

        presenter.loadCoeffs()
    }

    fun initNavigationDrawer() {
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
        textViewNavHeader.text = User.CURRENT.name

        //Меняем названия и доступность пунктов меню drawer'а в зависимости от того, является ли
        //пользователь администратором
        val nav_my = navigationView.menu.findItem(R.id.nav_my)
        val nav_new = navigationView.menu.findItem(R.id.nav_new)

        if (User.CURRENT.isAdmin) {
            nav_my.setTitle(R.string.nav_my_admin)
            nav_new.isVisible = false
        } else {
            nav_my.setTitle(R.string.nav_my)
            nav_new.isVisible = true
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode,resultCode, data);
        fragment.onActivityResult(requestCode, resultCode, data)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        fragment = when (item.itemId) {
            R.id.nav_my -> MainFragment().apply { arguments = Bundle().apply { putSerializable(EXTRA_FIRST, MainTabType.MY) } }
            R.id.nav_new -> MainFragment().apply { arguments = Bundle().apply { putSerializable(EXTRA_FIRST, MainTabType.NEW) } }
            R.id.nav_settings -> {MainFragment()}//SettingsFragment()
            else -> {
                User.CURRENT.id = -1
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
                return true
            }
        }
        val ft = fragmentManager.beginTransaction()
        ft.replace(R.id.content_main, fragment).commit()
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun renderData(screenModel: MainScreenModel) { /*без реализации*/ }
    override fun renderState(loadState: LoadState) {
        //TODO//("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
