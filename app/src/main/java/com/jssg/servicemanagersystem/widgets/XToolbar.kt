package com.jssg.servicemanagersystem.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.databinding.XToolbarLayoutBinding

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/25.
 */
open class XToolBar : FrameLayout {
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    var title: String? = null
    var subtitle: String? = null
    var onclick: OnClickListener? = null

    lateinit var binding: XToolbarLayoutBinding
    private fun init(context: Context, attrs: AttributeSet?) {
        View.inflate(context, R.layout.x_toolbar_layout, this)
        binding = XToolbarLayoutBinding.bind(this)

        val array = context.obtainStyledAttributes(attrs, R.styleable.XToolBar)
        title = array.getString(R.styleable.XToolBar_x_toolbar_title)
        subtitle = array.getString(R.styleable.XToolBar_x_toolbar_sub_title)
        array.recycle()

        binding.tvTitle.text = title
//        if (subtitle.isNullOrEmpty()) {
//            binding.tvSubTitle.visibility = View.GONE
//        } else {
//            binding.tvSubTitle.visibility = View.VISIBLE
//            binding.tvSubTitle.text = subtitle
//        }
        binding.ivBack.setOnClickListener {
            onclick?.onClick(it)
        }
    }


    open fun updateTitle(title: String) {
        binding.tvTitle.text = title
    }

//    open fun updateSubTitle(title: String) {
//        binding.tvSubTitle.text = title
//    }


}