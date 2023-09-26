package com.jssg.servicemanagersystem.base

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import com.jssg.servicemanagersystem.R

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/24.
 */
class LoadingDialog: Dialog {

    constructor(context: Context, themeResId: Int) : super(context, themeResId)

    constructor(context: Context): super(context)

    class Builder(private val context: Context) {
        private var message: String? = null
        private var isShowMessage = true
        private var isCancelable = false
        private var isCancelOutside = false

        /**
         * 设置提示信息
         * @param message
         * @return
         */
        fun setMessage(message: String?): Builder {
            this.message = message
            return this
        }

        /**
         * 设置是否显示提示信息
         * @param isShowMessage
         * @return
         */
        fun setShowMessage(isShowMessage: Boolean): Builder {
            this.isShowMessage = isShowMessage
            return this
        }

        /**
         * 设置是否可以按返回键取消
         * @param isCancelable
         * @return
         */
        fun setCancelable(isCancelable: Boolean): Builder {
            this.isCancelable = isCancelable
            return this
        }

        /**
         * 设置是否可以取消
         * @param isCancelOutside
         * @return
         */
        fun setCancelOutside(isCancelOutside: Boolean): Builder {
            this.isCancelOutside = isCancelOutside
            return this
        }

        fun create(): LoadingDialog {
            val inflater = LayoutInflater.from(context)
            val view: View = inflater.inflate(R.layout.dialog_progress_loading, null)
            val loadingDailog = LoadingDialog(context, R.style.MyDialogStyle)
            val msgText = view.findViewById<TextView>(R.id.tipTextView)
            if(isShowMessage){
                msgText.text = message
                msgText.isVisible = !message.isNullOrEmpty()
            }else{
                msgText.isVisible = false
            }

            loadingDailog.setContentView(view)
            loadingDailog.setCancelable(isCancelable)
            loadingDailog.setCanceledOnTouchOutside(isCancelOutside)
            return loadingDailog
        }
    }
}