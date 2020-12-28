package com.zettafantasy.giraffe.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.zettafantasy.giraffe.BR
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.databinding.SelectedEmotionViewBinding
import com.zettafantasy.giraffe.feature.emotion.FindEmotionViewModel

class SelectedItemAdapter(
    appExecutors: AppExecutors,
    private val viewModel: FindEmotionViewModel,
    private val itemClickCallback: ((Item) -> Unit)?
) : DataBoundListAdapter<Item>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(
            oldItem: Item,
            newItem: Item
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: Item,
            newItem: Item
        ): Boolean {
            return oldItem.getName() == newItem.getName()
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

        binding.root.setOnClickListener {
            binding.item?.let {
                itemClickCallback?.invoke(it)
            }
        }

        return binding
    }

    override fun bind(binding: ViewDataBinding, item: Item) {
        binding.setVariable(BR.item, item);
    }
}
