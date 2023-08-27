package com.jssg.servicemanagersystem.ui.accountcenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jssg.servicemanagersystem.base.loadmodel.AutoDisposViewModel

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/27.
 */
class AccountViewModel: AutoDisposViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
}