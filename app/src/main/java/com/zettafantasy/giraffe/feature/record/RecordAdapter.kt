package com.zettafantasy.giraffe.feature.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zettafantasy.giraffe.databinding.RecordViewBinding

class RecordAdapter(
    private val viewModel: RecordViewModel,
    private val itemClickCallback: ((RecordWrapper) -> Unit)?
) : PagingDataAdapter<RecordWrapper, RecordAdapter.RecordViewHolder>(
    diffCallback = object : DiffUtil.ItemCallback<RecordWrapper>() {
        override fun areItemsTheSame(
            oldItem: RecordWrapper,
            newItem: RecordWrapper
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: RecordWrapper,
            newItem: RecordWrapper
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val binding = RecordViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.viewModel = viewModel
        return RecordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        // Note that item may be null, ViewHolder must support binding null item as placeholder
        getItem(position)?.let {
            holder.binding.item = it
            holder.binding.root.setOnClickListener { _ ->
                itemClickCallback?.invoke(it)
            }
        }
    }

    class RecordViewHolder(val binding: RecordViewBinding) : RecyclerView.ViewHolder(binding.root)
}
