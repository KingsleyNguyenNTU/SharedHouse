package com.example.mkhoi.sharedhouse

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.example.mkhoi.sharedhouse.fees_view.FeesFragment
import com.example.mkhoi.sharedhouse.monthly_bill.MonthlyBillFragment
import com.example.mkhoi.sharedhouse.rooms_view.RoomsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.io.ByteArrayOutputStream


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
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    0)
        }

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

    fun sendWhatsAppMsg() {
        val b = Bitmap.createBitmap(nav_view.getWidth(), nav_view.getHeight(), Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        nav_view.draw(c)

        val bytes = ByteArrayOutputStream()
        b.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = Images.Media.insertImage(contentResolver, b, "Title", null)
        val imageUri =  Uri.parse(path)
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.`package` = "com.whatsapp"
        sendIntent.putExtra(Intent.EXTRA_TEXT, "My sample image text");
        sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        sendIntent.setType("image/jpeg");
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sendIntent.putExtra("jid", "6594572814@s.whatsapp.net")
        startActivity(sendIntent)
    }
}
