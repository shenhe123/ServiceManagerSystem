package com.jssg.servicemanagersystem.ui.travelreport

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TravelReportViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "开发计划中"
    }
    val text: LiveData<String> = _text
}