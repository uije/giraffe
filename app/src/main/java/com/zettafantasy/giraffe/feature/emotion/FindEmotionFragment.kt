package com.zettafantasy.giraffe.feature.emotion

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.AppExecutors
import com.zettafantasy.giraffe.common.ItemAdapter
import com.zettafantasy.giraffe.common.SelectedItemAdapter
import com.zettafantasy.giraffe.data.EmotionInventory
import com.zettafantasy.giraffe.databinding.FindEmotionFragmentBinding
import com.zettafantasy.giraffe.model.Emotion
import com.zettafantasy.giraffe.model.EmotionType


class FindEmotionFragment : Fragment() {
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var selectedAdapter: SelectedItemAdapter
    private lateinit var viewModel: FindEmotionViewModel
    private lateinit var binding: FindEmotionFragmentBinding
    private lateinit var emotions: List<Emotion>
    private var doneMenu: MenuItem? = null

    companion object {
        const val TAG = "FindEmotionFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.find_emotion_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel = ViewModelProvider(this).get(FindEmotionViewModel::class.java)
        binding.viewModel = viewModel
        initUI()
        setData()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.find_emotion, menu)
        doneMenu = menu.findItem(R.id.menu_done)
        doneMenu?.isVisible = viewModel.hasItem()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_done -> {

                val args = Bundle()
                args.putSerializable(EmotionType::class.simpleName, getEmotionType())
                args.putParcelableArrayList(
                    Emotion::class.simpleName,
                    viewModel.selectedItems.value?.let { ArrayList(it.toMutableList()) }
                )
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_find_emotion_to_find_need, args)

                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setData() {
        Log.d(TAG, arguments.toString())
        this.emotions = EmotionInventory.getInstance(resources).getList(getEmotionType())
        itemAdapter.submitList(this.emotions)
    }

    private fun getEmotionType(): EmotionType =
        arguments?.get(EmotionType::class.simpleName) as EmotionType

    private fun initUI() {
        initItemRv()
        initSelectedRv()
    }

    private fun initItemRv() {
        itemAdapter = ItemAdapter(AppExecutors, viewModel, R.layout.emotion_view) { emotion ->
            val pos = viewModel.toggle(emotion as Emotion)
            if (pos >= 0) {
                binding.selectedRv.smoothScrollToPosition(pos)
            }
        }

        binding.emotionRv.adapter = itemAdapter
        val spanCount = 2
        binding.emotionRv.layoutManager =
            GridLayoutManager(context, spanCount, RecyclerView.HORIZONTAL, false)

        binding.emotionRv.addItemDecoration(
            LayoutMarginDecoration(
                spanCount,
                resources.getDimensionPixelSize(R.dimen.emotion_margin)
            )
        )
    }

    private fun initSelectedRv() {
        selectedAdapter = SelectedItemAdapter(AppExecutors, viewModel) { item ->
            val pos = emotions.indexOf(item)
            if (pos >= 0) {
                binding.emotionRv.smoothScrollToPosition(pos)
            }
        }
        binding.selectedRv.adapter = selectedAdapter
        binding.selectedRv.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.selectedRv.addItemDecoration(
            LayoutMarginDecoration(
                1, resources.getDimensionPixelSize(R.dimen.selected_emotion_margin)
            )
        )

        viewModel.selectedItems.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                it.toList().let(selectedAdapter::submitList)
            }
            doneMenu?.isVisible = it.isNotEmpty()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        itemAdapter.setLifecycleDestroyed()
        selectedAdapter.setLifecycleDestroyed()
    }
}
