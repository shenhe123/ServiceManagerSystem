package com.jssg.servicemanagersystem.ui.workorder.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/9/12.
 */
@Parcelize
data class AudioRecordEntity(
    var audioFilePath: String,
    var recordTime: Long,
) : Parcelable {
    fun getTag(): String {
        return "audio.${audioFilePath}"
    }

}
