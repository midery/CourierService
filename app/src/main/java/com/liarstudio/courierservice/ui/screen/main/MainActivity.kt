package com.liarstudio.courierservice.ui.screen.main

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.TextView
import com.liarstudio.courierservice.R
import com.liarstudio.courierservice.entitiy.user.User
import com.liarstudio.courierservice.ui.base.EXTRA_FIRST
import com.liarstudio.courierservice.ui.base.LoadState
import com.liarstudio.courierservice.ui.base.screen.BaseActivity
import com.liarstudio.courierservice.ui.screen.auth.AuthActivity
import com.liarstudio.courierservice.ui.screen.main.settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity<MainScreenModel>() {

    @Inject
    lateinit var presenter: MainActivityPresenter

    lateinit var fragment: Fragment

    override fun requestPresenter() = presenter

    override fun getLayout() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Создаем новый MainFragment с менеджером PagerAdapterMyPackages
        fragment = MainFragment().apply { arguments = Bundle().apply { putSerializable(EXTRA_FIRST, MainTabType.MY) } }
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.content_main, fragment).commit()

        initViews()

        presenter.loadCoeffs()
    }


    private fun setNavigationButtons(isAdmin: Boolean) {
        val navMy = nav_view.menu.findItem(R.id.nav_my)
        val navNew = nav_view.menu.findItem(R.id.nav_new)

        if (isAdmin) {
            navMy.setTitle(R.string.nav_my_admin)
            navNew.isVisible = false
        } else {
            navMy.setTitle(R.string.nav_my)
            navNew.isVisible = true
        }

    }
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        fragment.onActivityResult(requestCode, resultCode, data)

    }

    override fun renderData(screenModel: MainScreenModel) {
        screenModel.user?.let {
            setHeader(it.name)
            setNavigationButtons(it.isAdmin)
        }
        screenModel.currentFragment?.let { replaceFragment(it) }
    }

    override fun renderState(loadState: LoadState) { /*без реализации*/ }

    private fun replaceFragment(fragment: Fragment) {
        val ft = fragmentManager.beginTransaction()
        ft.replace(R.id.content_main, fragment).commit()
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    private fun initViews() {
        initToolbar()
        initDrawerButton()
        initNavigationView()
    }

    private fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    private fun initDrawerButton() {
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun initNavigationView() {
        nav_view.setNavigationItemSelectedListener { presenter.openScreen(it.itemId)}
    }

    private  fun setHeader(userName: String) {
        val header = nav_view.getHeaderView(0)
        val textViewNavHeader = header.findViewById<TextView>(R.id.textViewNavHeader)
        textViewNavHeader.text = userName
    }
}
