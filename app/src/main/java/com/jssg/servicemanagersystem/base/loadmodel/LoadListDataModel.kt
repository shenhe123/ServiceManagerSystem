package com.jssg.servicemanagersystem.base.loadmodel

/**
 * 可以携带当前的下啦刷新状态的load指示器
 * @param <T>
</T> */
class LoadListDataModel<T> : LoadingStatus {
    //是否是刷新
    var isPullRefresh: Boolean

    var total: Int = 0

    var rows: T? = null

    constructor(isPullRefresh: Boolean) : super() {
        this.isPullRefresh = isPullRefresh
    }

    constructor(data: T, isPullRefresh: Boolean) : super(DataStatus.SUCCESS) {
        this.isPullRefresh = isPullRefresh
        this.rows = data
    }

    constructor(code: Int, msg: String?, isPullRefresh: Boolean) : super(code, msg) {
        this.isPullRefresh = isPullRefresh
    }
}