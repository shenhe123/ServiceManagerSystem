package com.jssg.servicemanagersystem.ui.onsite

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.jakewharton.rxbinding2.view.RxView
import com.jssg.servicemanagersystem.base.BaseActivity
import com.jssg.servicemanagersystem.databinding.ActivityOrderCheckBinding
import com.jssg.servicemanagersystem.utils.MyLocationClient
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers

class OrderCheckActivity : BaseActivity() {
    private lateinit var binding: ActivityOrderCheckBinding

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val rxPermissions = RxPermissions(this)
        rxPermissions.ensure<Any>(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

        RxView.attaches(binding.tvLocationAddress)
            .compose(rxPermissions.ensure(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { granted: Boolean? ->
                if (granted == true) {
                    val locationClient = MyLocationClient().init(this)
                    locationClient.registerLocationListener(object : BDAbstractLocationListener() {
                        override fun onReceiveLocation(location: BDLocation?) {
                            location?.let {
                                binding.tvLocationAddress.text = it.addrStr
                            }

                            if (location != null) locationClient.stop()
                        }

                    })
                    locationClient.start()
                }
            }
    }

    companion object {
        fun goActivity(context: Context) {
            context.startActivity(Intent(context, OrderCheckActivity::class.java))
        }
    }

}