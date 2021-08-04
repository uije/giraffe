package com.zettafantasy.giraffe.feature.need

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.item.FindItemFragment
import com.zettafantasy.giraffe.common.item.Item
import com.zettafantasy.giraffe.data.NeedInventory
import com.zettafantasy.giraffe.data.Record

class FindNeedFragment : FindItemFragment() {
    private val args by navArgs<FindNeedFragmentArgs>()

    override fun getItems(): List<Item> {
        return NeedInventory.getInstance(resources).getList()
    }

    override fun getDoneMenu(inflater: MenuInflater, menu: Menu): MenuItem {
        inflater.inflate(R.menu.find_need, menu)
        return menu.findItem(R.id.menu_done)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_done -> {
                navigateConfirmScreen()
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateConfirmScreen() {
        Navigation.findNavController(binding.root)
            .navigate(
                FindNeedFragmentDirections.actionFindNeedToConfirm(
                    Record(
                        args.record.emotionIds,
                        viewModel.selectedItems.value!!.map { it.getId() }.toList(),
                        args.record.stimulus
                    )
                )
            )
    }
}