package com.jssg.servicemanagersystem.ui.account.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/9/4.
 */
@Parcelize
data class UserRoles(
    val roles: List<Role>
) : Parcelable
