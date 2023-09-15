package com.jssg.servicemanagersystem.base.download

import java.io.File

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/9/15.
 */
sealed class DownloadState {
    data class InProgress(val progress: Int) : DownloadState()
    data class Success(val file: File) : DownloadState()
    data class Error(val throwable: Throwable) : DownloadState()
}