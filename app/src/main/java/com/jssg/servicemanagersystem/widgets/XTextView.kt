package com.jssg.servicemanagersystem.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.databinding.ItemXTextLayoutBinding

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/9/2.
 */
class XTextView: LinearLayoutCompat {

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

    private var myGravity: Int = 1
    var title: String? = null
    var titleText: String? = null

    lateinit var binding: ItemXTextLayoutBinding
    private fun init(context: Context, attrs: AttributeSet?) {
        View.inflate(context, R.layout.item_x_text_layout, this)
        binding = ItemXTextLayoutBinding.bind(this)

        val array = context.obtainStyledAttributes(attrs, R.styleable.XTextView)
        title = array.getString(R.styleable.XTextView_x_textview_title)
        titleText = array.getString(R.styleable.XTextView_x_textview_content)
        myGravity = array.getInt(R.styleable.XTextView_x_textview_gravity, 1)
        array.recycle()

        binding.tvTextTitle.text = title
        binding.tvText.text = titleText

        when(myGravity) {
            1 -> gravity = Gravity.START
            2 -> gravity = Gravity.CENTER_HORIZONTAL
            3 -> gravity = Gravity.END
        }
    }

    fun setContent(content: String) {
        binding.tvText.text = content
    }
}