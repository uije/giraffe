package com.zettafantasy.giraffe.feature.record

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zettafantasy.giraffe.GiraffeConstant
import com.zettafantasy.giraffe.databinding.RecordViewBinding

class RecordAdapter(
    private val viewModel: RecordViewModel,
    private val highLightColor: Int,
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
        return RecordViewHolder(binding, highLightColor)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        // Note that item may be null, ViewHolder must support binding null item as placeholder
        getItem(position)?.let { item ->
            holder.binding.also {
                it.item = item
                it.root.setOnClickListener {
                    itemClickCallback?.invoke(item)
                }
            }

            if (item.isHighLight) {
                showHighLightAnimation(holder, item)
            }
        }
    }

    private fun showHighLightAnimation(
        holder: RecordViewHolder,
        item: RecordWrapper
    ) {
        holder.highLightAnimation.addUpdateListener { animator ->
            holder.binding.root.setBackgroundColor(
                animator.animatedValue as Int
            )
        }

        holder.highLightAnimation.doOnEnd {
            item.isHighLight = false
            holder.binding.root.setBackgroundColor(Color.TRANSPARENT)
        }

        holder.highLightAnimation.start()
    }

    override fun onViewRecycled(holder: RecordViewHolder) {
        super.onViewRecycled(holder)
        holder.highLightAnimation.cancel()
    }

    override fun onViewDetachedFromWindow(holder: RecordViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.highLightAnimation.cancel()
    }

    class RecordViewHolder(val binding: RecordViewBinding, private val highLightColor: Int) :
        RecyclerView.ViewHolder(binding.root) {
        val highLightAnimation: ValueAnimator by lazy {
            ValueAnimator.ofObject(ArgbEvaluator(), highLightColor, Color.TRANSPARENT).apply {
                duration = GiraffeConstant.HIDE_COACH_MARK_MILLIS // milliseconds
            }
        }
    }
}
