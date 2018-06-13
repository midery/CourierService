package com.liarstudio.courierservice.ui.screen.main

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import android.widget.TextView
import com.liarstudio.courierservice.R
import com.liarstudio.courierservice.ui.base.EXTRA_FIRST
import com.liarstudio.courierservice.ui.base.isVisible
import com.liarstudio.courierservice.ui.base.screen.LoadState
import com.liarstudio.courierservice.ui.base.screen.view.BaseActivity
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


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        fragment.onActivityResult(requestCode, resultCode, data)

    }

    override fun renderData(screenModel: MainScreenModel) {
        screenModel.user?.let {
            setHeader(it.name, it.isAdmin)
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

    private  fun setHeader(userName: String, isAdmin: Boolean) {
        val header = nav_view.getHeaderView(0)
        val usernameTv = header.findViewById<TextView>(R.id.username_tv)
        usernameTv.text = userName
        if (isAdmin) {
            val adminModeTv = header.findViewById<TextView>(R.id.admin_mode_tv)
            adminModeTv.isVisible = true
            val logoIv = header.findViewById<ImageView>(R.id.logo_iv)
            DrawableCompat.setTint(logoIv.background.mutate(), getColor(R.color.colorAccent))
        }

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
}
