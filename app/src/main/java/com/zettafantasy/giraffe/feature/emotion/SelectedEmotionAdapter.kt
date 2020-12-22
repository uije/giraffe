package com.zettafantasy.giraffe.feature.emotion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.AppExecutors
import com.zettafantasy.giraffe.common.DataBoundListAdapter
import com.zettafantasy.giraffe.databinding.SelectedEmotionViewBinding
import com.zettafantasy.giraffe.model.Emotion

class SelectedEmotionAdapter(
    appExecutors: AppExecutors,
    private val viewModel: FindEmotionViewModel
) : DataBoundListAdapter<Emotion>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Emotion>() {
        override fun areItemsTheSame(
            oldItem: Emotion,
            newItem: Emotion
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: Emotion,
            newItem: Emotion
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun createBinding(parent: ViewGroup): SelectedEmotionViewBinding {
        val binding = DataBindingUtil
            .inflate<SelectedEmotionViewBinding>(
                LayoutInflater.from(parent.context),
                R.layout.selected_emotion_view,
                parent,
                false
            )
        binding.viewModel = viewModel
        return binding
    }

    override fun bind(binding: ViewDataBinding, item: Emotion) {
        when (binding) {
            is SelectedEmotionViewBinding -> {
                binding.item = item
            }
        }
    }
}
