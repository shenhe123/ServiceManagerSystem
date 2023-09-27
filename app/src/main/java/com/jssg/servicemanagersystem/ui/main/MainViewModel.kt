package com.jssg.servicemanagersystem.ui.main

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jssg.servicemanagersystem.base.entity.BaseHttpResult
import com.jssg.servicemanagersystem.base.http.RetrofitService
import com.jssg.servicemanagersystem.base.http.RxSchedulersHelper
import com.jssg.servicemanagersystem.base.http.observer.WQBaseObserver
import com.jssg.servicemanagersystem.base.loadmodel.AndroidViewModel
import com.jssg.servicemanagersystem.base.loadmodel.LoadDataModel
import com.jssg.servicemanagersystem.base.loadmodel.LoadUpdateDataModel
import com.jssg.servicemanagersystem.ui.account.entity.UserInfo
import com.jssg.servicemanagersystem.utils.download.UpdateEntity
import io.reactivex.disposables.Disposable

/**
 * Created by shenhe on 2020/7/6.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    var updateInfoLiveData = MutableLiveData<LoadUpdateDataModel<UpdateEntity?>>()

    /**
     * 版本更新信息
     */
    fun getUpdateInfo(isSelfCheck: Boolean) {
        updateInfoLiveData.value = LoadUpdateDataModel(isSelfCheck)
        RetrofitService.apiService
            .getUpdateInfo()
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : WQBaseObserver<BaseHttpResult<UpdateEntity?>>() {

                override fun onSuccess(result: BaseHttpResult<UpdateEntity?>) {
                    if (result.isSuccess) {
                        updateInfoLiveData.setValue(LoadUpdateDataModel(result.data, isSelfCheck))
                    } else {
                        updateInfoLiveData.setValue(
                            LoadUpdateDataModel(result.code, "已经是最新版本", isSelfCheck)
                        )
                    }
                }

                override fun onFailure(code: Int, message: String?) {
                    updateInfoLiveData.value =
                        LoadUpdateDataModel(code, "已经是最新版本", isSelfCheck)
                }

            })
    }
}