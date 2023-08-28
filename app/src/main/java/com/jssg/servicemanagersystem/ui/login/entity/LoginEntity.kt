package com.jssg.servicemanagersystem.ui.login.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/28.
 */
@Parcelize
data class LoginEntity(
    val token: String,
) : Parcelable
