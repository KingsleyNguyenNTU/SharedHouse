package com.example.mkhoi.sharedhouse

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.ViewTreeObserver
import com.example.mkhoi.sharedhouse.backup.BackupFragment
import com.example.mkhoi.sharedhouse.backup.RestoreFragment
import com.example.mkhoi.sharedhouse.fees_view.FeesFragment
import com.example.mkhoi.sharedhouse.monthly_bill.MonthlyBillFragment
import com.example.mkhoi.sharedhouse.rooms_view.RoomsFragment
import com.example.mkhoi.sharedhouse.settings.SettingsFragment
import com.example.mkhoi.sharedhouse.util.CircleImageTransformation
import com.example.mkhoi.sharedhouse.util.showBasicDialog
import com.example.mkhoi.sharedhouse.util.toBitmapFromBase64
import com.google.android.gms.ads.doubleclick.PublisherAdRequest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*




open class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    internal open lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val adRequest = PublisherAdRequest.Builder().build()
        publisherAdView.loadAd(adRequest)

        viewModel = ViewModelProviders.of(this, MainViewModel.Factory())
                .get(MainViewModel::class.java)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        drawer_layout.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {

                        // only want to do this once
                        drawer_layout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                        viewModel.housePictureSetting.observe(this@MainActivity, Observer {
                            it?.let {
                                val imageBitmap = CircleImageTransformation().transform(it.value.toBitmapFromBase64())
                                drawer_top_picture.setImageBitmap(imageBitmap)
                            }
                        })
                        viewModel.houseNameSetting.observe(this@MainActivity, Observer {
                            it?.let { drawer_name.text = it.value }
                        })
                    }
                })

        nav_view.setNavigationItemSelectedListener(this)
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
            R.id.nav_setting -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.main_content_fragment, SettingsFragment.newInstance())
                        .addToBackStack(SettingsFragment::class.java.canonicalName)
                        .commit()
            }
            R.id.nav_backup -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.main_content_fragment, BackupFragment.newInstance())
                        .addToBackStack(BackupFragment::class.java.canonicalName)
                        .commit()
            }
            R.id.nav_restore -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.main_content_fragment, RestoreFragment.newInstance())
                        .addToBackStack(RestoreFragment::class.java.canonicalName)
                        .commit()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
