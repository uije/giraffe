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

class FindNeedFragment : FindItemFragment() {
    private val args by navArgs<FindNeedFragmentArgs>()

    override fun provideItems(): List<Item> {
        return NeedInventory.getInstance(resources).getList()
    }

    override fun provideDoneMenu(inflater: MenuInflater, menu: Menu): MenuItem {
        inflater.inflate(R.menu.find_need, menu)
        return menu.findItem(R.id.menu_complete)
    }

    override fun provideViewModel(): FindItemViewModel {
        return ViewModelProvider(this).get(FindItemViewModel::class.java)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_complete -> {
                val record = Record(
                    args.record.emotionIds,
                    viewModel.selectedItems.value!!.map { it.getId() }.toList(),
                    args.record.stimulus
                )

                if (record.needIds.size > GiraffeConstant.ITEM_COUNT) {
                    Navigation.findNavController(binding.root)
                        .navigate(FindNeedFragmentDirections.actionGoReduceNeedIntro(record))
                } else {
                    Navigation.findNavController(binding.root)
                        .navigate(FindNeedFragmentDirections.actionFindNeedToConfirm(record))
                }
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}