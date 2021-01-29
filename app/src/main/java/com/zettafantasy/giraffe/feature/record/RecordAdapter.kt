package com.zettafantasy.giraffe.feature.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import com.zettafantasy.giraffe.BR
import com.zettafantasy.giraffe.common.AppExecutors
import com.zettafantasy.giraffe.common.DataBoundListAdapter

class RecordAdapter(
    appExecutors: AppExecutors,
    private val viewModel: ViewModel,
    private val layoutId: Int,
    private val itemClickCallback: ((RecordWrapper) -> Unit)?
) : DataBoundListAdapter<RecordWrapper>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<RecordWrapper>() {
        override fun areItemsTheSame(
            oldItem: RecordWrapper,
            newItem: RecordWrapper
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: RecordWrapper,
            newItem: RecordWrapper
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ViewDataBinding {
        val binding = DataBindingUtil
            .inflate<ViewDataBinding>(
                LayoutInflater.from(parent.context),
                layoutId,
                parent,
                false
            )
        binding.setVariable(BR.viewModel, viewModel)
        return binding
    }

    override fun bind(binding: ViewDataBinding, record: RecordWrapper) {
        binding.setVariable(BR.item, record)
        binding.root.setOnClickListener {
            record?.let {
                itemClickCallback?.invoke(it)
            }
        }
    }
}
