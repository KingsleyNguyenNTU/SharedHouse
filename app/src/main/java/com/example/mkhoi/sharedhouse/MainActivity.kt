package com.example.mkhoi.sharedhouse

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.mkhoi.sharedhouse.fees_view.FeesFragment
import com.example.mkhoi.sharedhouse.monthly_bill.MonthlyBillFragment
import com.example.mkhoi.sharedhouse.room_edit.EditRoomFragment
import com.example.mkhoi.sharedhouse.rooms_view.RoomsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)


        supportFragmentManager.beginTransaction()
                .replace(R.id.main_content_fragment, MonthlyBillFragment.newInstance())
                .commitNow()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_bill ->{
                supportFragmentManager.beginTransaction()
                        .replace(R.id.main_content_fragment, MonthlyBillFragment.newInstance())
                        .addToBackStack(MonthlyBillFragment::class.java.canonicalName)
                        .commit()
            }
            R.id.nav_room -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.main_content_fragment, RoomsFragment.newInstance())
                        .addToBackStack(RoomsFragment::class.java.canonicalName)
                        .commit()
            }
            R.id.nav_fee -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.main_content_fragment, FeesFragment.newInstance())
                        .addToBackStack(FeesFragment::class.java.canonicalName)
                        .commit()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
