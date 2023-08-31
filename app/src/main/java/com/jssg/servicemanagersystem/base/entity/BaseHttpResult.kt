package com.jssg.servicemanagersystem.base.entity

import java.io.Serializable

class BaseHttpResult<T> : Serializable {
    @JvmField
    var code = 0
    var msg: String? = null
    @JvmField
    var data: T? = null

    var rows: T? = null

    fun setData(data: T) {
        this.data = data
    }

    val isSuccess: Boolean
        get() = code == 0 || code == 200
}