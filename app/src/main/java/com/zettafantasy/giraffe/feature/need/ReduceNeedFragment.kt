package com.zettafantasy.giraffe.feature.need

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.zettafantasy.giraffe.GiraffeConstant
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.item.FindItemFragment
import com.zettafantasy.giraffe.common.item.FindItemViewModel
import com.zettafantasy.giraffe.common.item.Item
import com.zettafantasy.giraffe.data.NeedInventory
import com.zettafantasy.giraffe.data.Record


class ReduceNeedFragment : FindItemFragment() {
    private val args by navArgs<ReduceNeedFragmentArgs>()

    override fun provideItems(): List<Item> {
        return NeedInventory.getInstance(resources).getListByIds(args.record.needIds)
    }

    override fun provideDoneMenu(inflater: MenuInflater, menu: Menu): MenuItem {
        inflater.inflate(R.menu.find_emotion, menu)
        return menu.findItem(R.id.menu_complete)
    }

    override fun provideViewModel(): FindItemViewModel {
        return ViewModelProvider(this).get(FindItemViewModel::class.java).apply {
            maxItemCount = GiraffeConstant.ITEM_COUNT
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_complete -> {

                Navigation.findNavController(binding.root)
                    .navigate(
                        ReduceNeedFragmentDirections.actionReduceNeedToConfirm(
                            Record(
                                args.record.emotionIds,
                                viewModel.selectedItems.value!!.map { it.getId() }.toList(),
                                args.record.stimulus
                            )
                        )
                    )

                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
