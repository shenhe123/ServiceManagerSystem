package com.jssg.servicemanagersystem.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/24.
 */
class VBViewHolder<VB : ViewBinding>(val binding: VB) : BaseViewHolder(binding.root)


abstract class BaseBindingAdapter<T, VB : ViewBinding>(
    private val inflate: (LayoutInflater, ViewGroup, Boolean) -> VB,
    layoutResId: Int = 0
) : BaseQuickAdapter<T, VBViewHolder<VB>>(layoutResId) {

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): VBViewHolder<VB> {
        val viewBinding = inflate(LayoutInflater.from(parent.context), parent,false)
        return VBViewHolder(viewBinding)
    }
}