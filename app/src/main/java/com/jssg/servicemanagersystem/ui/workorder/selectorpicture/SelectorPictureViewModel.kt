package com.jssg.servicemanagersystem.ui.workorder.selectorpicture

import androidx.lifecycle.MutableLiveData
import com.jssg.servicemanagersystem.base.http.RetrofitService
import com.jssg.servicemanagersystem.base.http.RxSchedulersHelper
import com.jssg.servicemanagersystem.base.http.observer.WQBaseObserver
import com.jssg.servicemanagersystem.base.loadmodel.AutoDisposViewModel
import com.jssg.servicemanagersystem.base.loadmodel.LoadDataModel
import com.jssg.servicemanagersystem.ui.workorder.entity.UploadEntity
import com.jssg.servicemanagersystem.utils.HUtils
import com.luck.picture.lib.entity.LocalMedia
import io.reactivex.disposables.Disposable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/26.
 */
class SelectorPictureViewModel: AutoDisposViewModel() {

    //不良图片
    val badPicturesLiveData = MutableLiveData<ArrayList<LocalMedia?>>()

    //外箱标签图片
    val boxPicturesLiveData = MutableLiveData<ArrayList<LocalMedia?>>()

    //批次信息图片
    val batchInfoPicturesLiveData = MutableLiveData<ArrayList<LocalMedia?>>()

    //返工图片
    val reworkPicturesLiveData = MutableLiveData<ArrayList<LocalMedia?>>()

    //上传结果
    val fileOssUploadLiveData = MutableLiveData<LoadDataModel<UploadEntity?>>()

    fun fileOssUpload(path: String, tag: String) {
        fileOssUploadLiveData.value = LoadDataModel()
        val file = File(path)
        val body = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

        val builder = MultipartBody.Builder()
        builder.addFormDataPart("file", file.name, body)
        builder.setType(MultipartBody.FORM)
        val multipartBody = builder.build()//设置上传的类型 文件(图片)

        RetrofitService.apiService
            .fileOssUpload(multipartBody)
            .compose(RxSchedulersHelper.io_main())
            .compose(RxSchedulersHelper.ObsHandHttpResult())
            .subscribe(object :WQBaseObserver<UploadEntity?>(){
                override fun onSuccess(result: UploadEntity?) {
                    result?.let {
                        it.tag = tag
                        fileOssUploadLiveData.value = LoadDataModel(it)
                    }
                }

                override fun onFailure(code: Int, message: String?) {
                    fileOssUploadLiveData.value = LoadDataModel(code, message)
                }

                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    addDispos(d)
                }
            })
    }
}