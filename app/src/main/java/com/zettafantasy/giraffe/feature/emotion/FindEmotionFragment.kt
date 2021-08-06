package com.zettafantasy.giraffe.feature.emotion

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.Preferences
import com.zettafantasy.giraffe.common.item.FindItemFragment
import com.zettafantasy.giraffe.common.item.FindItemViewModel
import com.zettafantasy.giraffe.common.item.Item
import com.zettafantasy.giraffe.data.EmotionInventory
import com.zettafantasy.giraffe.data.Record
import java.util.*


class FindEmotionFragment : FindItemFragment() {
    private val args by navArgs<FindEmotionFragmentArgs>()

    override fun provideItems(): List<Item> {
        return EmotionInventory.getInstance(resources).getListByType(args.emotionType)
    }

    override fun provideDoneMenu(inflater: MenuInflater, menu: Menu): MenuItem {
        inflater.inflate(R.menu.find_emotion, menu)
        return menu.findItem(R.id.menu_done)
    }

    override fun provideViewModel(): FindItemViewModel {
        val viewModel = ViewModelProvider(this).get(FindItemViewModel::class.java)
        viewModel.showCoachMark = !Preferences.shownCoachMarkFindEmotion
        viewModel.coachMarkText = getString(R.string.tooltip_find_item)
        return viewModel
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_done -> {

                Navigation.findNavController(binding.root)
                    .navigate(
                        FindEmotionFragmentDirections.actionFindEmotionToFindNeed(
                            Record(
                                viewModel.selectedItems.value!!.map { it.getId() }.toList(),
                                Collections.emptyList(), args.record.stimulus
                            )
                        )
                    )

                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun afterShowCoachMark() {
        Preferences.shownCoachMarkFindEmotion = true
    }
}
