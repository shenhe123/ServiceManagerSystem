package com.jssg.servicemanagersystem.ui.workorder.adapter

import android.util.TypedValue
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.LinearLayoutCompat
import com.jssg.servicemanagersystem.R
import com.jssg.servicemanagersystem.base.BaseBindingAdapter
import com.jssg.servicemanagersystem.base.VBViewHolder
import com.jssg.servicemanagersystem.databinding.ItemAudioRecordLayoutBinding
import com.jssg.servicemanagersystem.ui.workorder.entity.AudioRecordEntity
import com.jssg.servicemanagersystem.utils.DpPxUtils

/**
 * ServiceManagerSystem
 * Created by he.shen on 2023/8/24.
 */
class AudioRecordAdapter :
    BaseBindingAdapter<AudioRecordEntity, ItemAudioRecordLayoutBinding>(
        ItemAudioRecordLayoutBinding::inflate
    ) {

    private var voiceResId: Int = -1

    init {
        addChildClickViewIds(R.id.layout_audio_record_msg)
        addChildLongClickViewIds(R.id.layout_audio_record_msg)
    }

    override fun convert(
        holder: VBViewHolder<ItemAudioRecordLayoutBinding>,
        item: AudioRecordEntity
    ) {
        holder.binding.tvAudioTime.text = "${item.recordTime / 1000}''"

        if (voiceResId != -1) {
            holder.binding.ivAudioSound.setImageResource(voiceResId)
        }

        //设置语音条宽度
        setAudioLayoutWidth(holder.binding.layoutAudioRecordMsg, item.recordTime)
    }

    fun setVoiceImageRes(@DrawableRes resId: Int, pos: Int) {
        voiceResId = resId
        notifyItemChanged(pos)
    }

    private fun setAudioLayoutWidth(layout: LinearLayoutCompat, duration: Long) {
        val perSecondWidth = 5.0f
        val second = duration / 1000.0f
        var width = second * perSecondWidth
        if (width < 60) {
            width = 60.0f
        } else if (width > 240) {
            width = 240.0f
        }

        width += 20f
        val newWidth = DpPxUtils.dip2px(context, width)
        val params = layout.layoutParams
        params.width = newWidth
        layout.layoutParams = params
    }
}