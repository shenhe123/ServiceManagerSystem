package com.jssg.servicemanagersystem.ui.onsite.selectorpicture

import androidx.lifecycle.MutableLiveData
import com.jssg.servicemanagersystem.base.loadmodel.AutoDisposViewModel
import com.luck.picture.lib.entity.LocalMedia

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


}