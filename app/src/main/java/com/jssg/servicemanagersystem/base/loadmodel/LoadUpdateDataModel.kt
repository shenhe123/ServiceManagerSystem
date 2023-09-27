package com.jssg.servicemanagersystem.base.loadmodel

/**
 * Created by shenhe on 2020/7/6.
 */
class LoadUpdateDataModel<T> : LoadDataModel<T> {
    var isSelfCheck: Boolean

    constructor(isSelfCheck: Boolean) : super() {
        this.isSelfCheck = isSelfCheck
    }

    constructor(data: T, isSelfCheck: Boolean) : super(data) {
        this.isSelfCheck = isSelfCheck
    }

    constructor(code: Int, msg: String?, isSelfCheck: Boolean) : super(code, msg) {
        this.isSelfCheck = isSelfCheck
    }
}