package com.jssg.servicemanagersystem.ui.workorder.selectorpicture

import android.net.Uri
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.jssg.servicemanagersystem.base.http.RetrofitService
import com.jssg.servicemanagersystem.base.http.RxSchedulersHelper
import com.jssg.servicemanagersystem.base.http.observer.WQBaseObserver
import com.jssg.servicemanagersystem.base.loadmodel.AutoDisposViewModel
import com.jssg.servicemanagersystem.base.loadmodel.LoadDataModel
import com.jssg.servicemanagersystem.core.AppApplication
import com.jssg.servicemanagersystem.ui.workorder.entity.AudioRecordEntity
import com.jssg.servicemanagersystem.ui.workorder.entity.UploadEntity
import com.jssg.servicemanagersystem.utils.FileUtils
import com.luck.picture.lib.entity.LocalMedia
import io.reactivex.disposables.Disposable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import top.zibin.luban.CompressionPredicate
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.util.Locale

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

    //录音附录
    val audioRecordLiveData = MutableLiveData<AudioRecordEntity>()

    //上传结果
    val fileOssUploadLiveData = MutableLiveData<LoadDataModel<UploadEntity?>>()

    val badOssListLiveData = MutableLiveData<LoadDataModel<List<UploadEntity>>>()
    val boxOssListLiveData = MutableLiveData<LoadDataModel<List<UploadEntity>>>()
    val batchOssListLiveData = MutableLiveData<LoadDataModel<List<UploadEntity>>>()
    val reworkOssListLiveData = MutableLiveData<LoadDataModel<List<UploadEntity>>>()

    //content:/media/external_primary/images/media/1000027063
    fun fileOssUpload(file: File, tag: String) {
        fileOssUploadLiveData.value = LoadDataModel()

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


    fun getBadPictures(ossIds: String?) {
        if (ossIds.isNullOrEmpty()) return
        badOssListLiveData.value = LoadDataModel()
        RetrofitService.apiService
            .getOssListByIds(ossIds)
            .compose(RxSchedulersHelper.ObsResultWithMain())
            .subscribe(createObserver(badOssListLiveData))
    }

    fun getBoxPictures(ossIds: String?) {
        if (ossIds.isNullOrEmpty()) return
        boxOssListLiveData.value = LoadDataModel()
        RetrofitService.apiService
            .getOssListByIds(ossIds)
            .compose(RxSchedulersHelper.ObsResultWithMain())
            .subscribe(createObserver(boxOssListLiveData))
    }

    fun getBatchPictures(ossIds: String?) {
        if (ossIds.isNullOrEmpty()) return
        batchOssListLiveData.value = LoadDataModel()
        RetrofitService.apiService
            .getOssListByIds(ossIds)
            .compose(RxSchedulersHelper.ObsResultWithMain())
            .subscribe(createObserver(batchOssListLiveData))
    }

    fun getReworkPictures(ossIds: String?) {
        if (ossIds.isNullOrEmpty()) return
        reworkOssListLiveData.value = LoadDataModel()
        RetrofitService.apiService
            .getOssListByIds(ossIds)
            .compose(RxSchedulersHelper.ObsResultWithMain())
            .subscribe(createObserver(reworkOssListLiveData))
    }
}