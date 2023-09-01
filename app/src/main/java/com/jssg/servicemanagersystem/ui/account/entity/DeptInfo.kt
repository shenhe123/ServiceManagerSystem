package com.jssg.servicemanagersystem.ui.account.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/9/1.
 */
@Parcelize
data class DeptInfo(
    val id: String,
    val label: String,
    val parentId: Long,
    val weight: Int
) : Parcelable