package com.zettafantasy.giraffe.feature.emotion

import androidx.lifecycle.ViewModel
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.model.Emotion
import tech.thdev.simpleadapter.SimpleDataBindingAdapter
import tech.thdev.simpleadapter.control.SimpleDataBindingAdapterControl
import tech.thdev.simpleadapter.data.SimpleDataBindingAdapterCreateItem
import tech.thdev.simpleadapter.holder.SimpleDataBindingViewHolder
import tech.thdev.simpleadapter.util.createDataBindingHolder

class EmotionAdapterControl : SimpleDataBindingAdapterControl<AdapterDataBindingItemGroup>() {
    override fun SimpleDataBindingAdapter.onCreateItems(
            item: AdapterDataBindingItemGroup,
            viewModel: ViewModel?) {

        val startPosition = getItemSize()
        var newItem = 0
        // AddItem.
        item.itemList.forEach {
            newItem++
            addItem(0, it, viewModel)
        }
        notifyItemRangeInserted(startPosition, newItem)
    }

    override fun SimpleDataBindingAdapterCreateItem.onCreateViewHolder(): SimpleDataBindingViewHolder {
        return when (viewType) {
            else -> createDataBindingHolder(R.layout.item_emotion)
        }
    }

}

data class AdapterDataBindingItemGroup(
        val itemList: List<Emotion>
)
