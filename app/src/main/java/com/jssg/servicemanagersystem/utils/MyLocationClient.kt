package com.jssg.servicemanagersystem.utils

import android.content.Context
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/26.
 */
class MyLocationClient {
    private lateinit var mLocationClient: LocationClient

    //BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口
    //原有BDLocationListener接口暂时同步保留。具体介绍请参考后文第四步的说明
    fun init(context: Context): LocationClient {
        mLocationClient = LocationClient(context)
        //声明LocationClient类

        val option = LocationClientOption()

        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；
        //LocationMode.Fuzzy_Locating, 模糊定位模式；v9.2.8版本开始支持，可以降低API的调用频率，但同时也会降低定位精度；

        option.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认GCJ02
        //GCJ02：国测局坐标；
        //BD09ll：百度经纬度坐标；
        //BD09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

        option.setFirstLocType(LocationClientOption.FirstLocType.ACCURACY_IN_FIRST_LOC)
        //可选，首次定位时可以选择定位的返回是准确性优先还是速度优先，默认为速度优先
        //可以搭配setOnceLocation(Boolean isOnceLocation)单次定位接口使用，当设置为单次定位时，setFirstLocType接口中设置的类型即为单次定位使用的类型
        //FirstLocType.SPEED_IN_FIRST_LOC:速度优先，首次定位时会降低定位准确性，提升定位速度；
        //FirstLocType.ACCUARACY_IN_FIRST_LOC:准确性优先，首次定位时会降低速度，提升定位准确性；

        option.setScanSpan(1000)
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效

        option.isOpenGnss = true
        //可选，设置是否使用卫星定位，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.isLocationNotify = true
        //可选，设置是否当卫星定位有效时按照1S/1次频率输出卫星定位结果，默认false

        option.setIgnoreKillProcess(false)
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false)
        //可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5 * 60 * 1000)
        //可选，V7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

        option.setEnableSimulateGnss(false)
        //可选，设置是否需要过滤卫星定位仿真结果，默认需要，即参数为false

        option.setNeedNewVersionRgc(true)
        //可选，设置是否需要最新版本的地址信息。默认需要，即参数为true

        option.setIsNeedAddress(true)
        //可选，设置是否需要返回地址信息

        mLocationClient.locOption = option

        return mLocationClient
    }

    class MyLocationListener : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation?) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            location?.let {
                val latitude: Double = location.latitude //获取纬度信息

                val longitude: Double = location.longitude //获取经度信息

                val radius: Float = location.radius //获取定位精度，默认值为0.0f


                val coorType: String = location.coorType
                //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准

                //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
                val errorCode: Int = location.locType
                //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
                val addr = location.addrStr //获取详细地址信息

                val country = location.country //获取国家

                val province = location.province //获取省份

                val city = location.city //获取城市

                val district = location.district //获取区县

                val street = location.street //获取街道信息

                val adcode = location.adCode //获取adcode

                val town = location.town //获取乡镇信息

            }
        }

    }
}