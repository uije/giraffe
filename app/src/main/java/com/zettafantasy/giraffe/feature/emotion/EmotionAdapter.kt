package com.zettafantasy.giraffe.feature.emotion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.AppExecutors
import com.zettafantasy.giraffe.common.DataBoundListAdapter
import com.zettafantasy.giraffe.databinding.ItemEmotionBinding

class EmotionAdapter(
    appExecutors: AppExecutors,
    private val viewModel: FindEmotionViewModel
) : DataBoundListAdapter<EmotionItemViewModel>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<EmotionItemViewModel>() {
        override fun areItemsTheSame(
            oldItem: EmotionItemViewModel,
            newItem: EmotionItemViewModel
        ): Boolean {
            return oldItem.emotion.value === newItem.emotion.value
        }

        override fun areContentsTheSame(
            oldItem: EmotionItemViewModel,
            newItem: EmotionItemViewModel
        ): Boolean {
            return oldItem.emotion.value == newItem.emotion.value
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ItemEmotionBinding {
        val binding = DataBindingUtil
            .inflate<ItemEmotionBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_emotion,
                parent,
                false
            )
        binding.viewModel = viewModel
        return binding
    }

    override fun bind(binding: ViewDataBinding, item: EmotionItemViewModel) {
        when (binding) {
            is ItemEmotionBinding -> {
                binding.item = item
            }
        }
    }
}
