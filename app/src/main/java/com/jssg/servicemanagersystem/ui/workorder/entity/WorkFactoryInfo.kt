package com.jssg.servicemanagersystem.ui.workorder.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/9/3.
 */
@Parcelize
data class WorkFactoryInfo(
    val hrmOrgId: String,
    val orgId: String,
    val orgName: String,
    val orgNameEn: String?,
    val orgShortName: String,
    var isChecked: Boolean = false
) : Parcelable