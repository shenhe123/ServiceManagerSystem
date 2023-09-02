package com.jssg.servicemanagersystem.ui.workorder.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/9/2.
 */
@Parcelize
data class UploadEntity(
    val fileName: String,
    val ossId: String,
    val url: String,
    var tag: String
) : Parcelable