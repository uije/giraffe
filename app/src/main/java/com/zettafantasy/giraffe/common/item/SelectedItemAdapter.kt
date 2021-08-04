package com.zettafantasy.giraffe.common.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import com.zettafantasy.giraffe.BR
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.AppExecutors
import com.zettafantasy.giraffe.common.DataBoundListAdapter

class SelectedItemAdapter(
    appExecutors: AppExecutors,
    private val viewModel: ViewModel,
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

    override fun createBinding(parent: ViewGroup): ViewDataBinding {
        val binding = DataBindingUtil
            .inflate<ViewDataBinding>(
                LayoutInflater.from(parent.context),
                R.layout.selected_item_view,
                parent,
                false
            )
        binding.setVariable(BR.viewModel, viewModel)
        return binding
    }

    override fun bind(binding: ViewDataBinding, item: Item) {
        binding.setVariable(BR.item, item);
        binding.root.setOnClickListener {
            item?.let {
                itemClickCallback?.invoke(it)
            }
        }
    }
}
