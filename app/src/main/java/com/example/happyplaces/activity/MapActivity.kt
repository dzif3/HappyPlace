package com.example.happyplaces.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.happyplaces.MainActivity
import com.example.happyplaces.R
import com.example.happyplaces.model.HappyPlaceModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mHappyDetailActivity: HappyPlaceModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mHappyDetailActivity = intent.getParcelableExtra(MainActivity.HAPPY_PLACE_DETAIL)

        mHappyDetailActivity.let {
            if (it != null) {
                setSupportActionBar(toolbar_map)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.title = it?.title

                toolbar_map.setNavigationOnClickListener {
                    onBackPressed()
                }
                val supportMapFragment : SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                supportMapFragment.getMapAsync(this)
            }

        }
    }

    override fun onMapReady(map: GoogleMap?) {
        val position = LatLng(mHappyDetailActivity!!.latitude, mHappyDetailActivity!!.longitude)
        map!!.addMarker(MarkerOptions().position(position).title(mHappyDetailActivity!!.location))
        val newLatngZoom = CameraUpdateFactory.newLatLngZoom(position, 20f)
        map.animateCamera(newLatngZoom)
    }
}