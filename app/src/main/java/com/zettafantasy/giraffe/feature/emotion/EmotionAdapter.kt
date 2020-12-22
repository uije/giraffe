package com.zettafantasy.giraffe.feature.emotion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.AppExecutors
import com.zettafantasy.giraffe.common.DataBoundListAdapter
import com.zettafantasy.giraffe.databinding.EmotionViewBinding
import com.zettafantasy.giraffe.model.Emotion

class EmotionAdapter(
    appExecutors: AppExecutors,
    private val viewModel: FindEmotionViewModel,
    private val emotionClickCallback: ((Emotion) -> Unit)?
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

    override fun createBinding(parent: ViewGroup): EmotionViewBinding {
        val binding = DataBindingUtil
            .inflate<EmotionViewBinding>(
                LayoutInflater.from(parent.context),
                R.layout.emotion_view,
                parent,
                false
            )
        binding.viewModel = viewModel

        binding.root.setOnClickListener {
            binding.item?.let {
                emotionClickCallback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: ViewDataBinding, item: Emotion) {
        when (binding) {
            is EmotionViewBinding -> {
                binding.item = item
            }
        }
    }
}
