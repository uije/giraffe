package com.zettafantasy.giraffe.common.item

import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.*
import com.zettafantasy.giraffe.databinding.FindItemFragmentBinding
import kotlin.math.max
import kotlin.math.roundToInt


abstract class FindItemFragment : BaseBindingFragment<FindItemFragmentBinding>() {
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var selectedAdapter: SelectedItemAdapter
    protected lateinit var viewModel: FindItemViewModel
    private var doneMenu: MenuItem? = null

    abstract fun getItems(): List<Item>

    abstract fun getDoneMenu(inflater: MenuInflater, menu: Menu): MenuItem

    override fun init(inflater: LayoutInflater, container: ViewGroup?): FindItemFragmentBinding {
        setHasOptionsMenu(true)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.find_item_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel = ViewModelProvider(this).get(FindItemViewModel::class.java)
        binding.viewModel = viewModel
        initUI()
        setData(getItems())
        return binding
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        doneMenu = getDoneMenu(inflater, menu)
        doneMenu?.isVisible = viewModel.hasItem()
    }

    private fun setData(items: List<Item>) {
        itemAdapter.submitList(items)
        binding.progressbar.max = max(items.size - 1, 0)
    }

    private fun initUI() {
        initItemRv()
        initSelectedRv()
    }

    private fun initItemRv() {
        itemAdapter = ItemAdapter(AppExecutors, viewModel, R.layout.emotion_view) { emotion ->
            val pos = viewModel.toggle(emotion)
            if (pos >= 0) {
                binding.selectedRv.smoothScrollToPosition(pos)
            }
        }

        binding.itemRv.adapter = itemAdapter
        val spanCount = getSpanCount()
        binding.itemRv.layoutManager =
            GridLayoutManager(context, spanCount, RecyclerView.HORIZONTAL, false)

        binding.itemRv.addItemDecoration(
            LayoutMarginDecoration(
                spanCount,
                resources.getDimensionPixelSize(R.dimen.emotion_margin)
            )
        )

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.itemRv)
        binding.itemRv.addOnScrollListener(SnapPagerScrollListener(snapHelper,
            SnapPagerScrollListener.ON_SETTLED,
            true,
            object : SnapPagerScrollListener.OnChangeListener {
                override fun onSnapped(position: Int) {
                    Log.d(javaClass.simpleName, String.format("onSnapped(%s)", position))
                    binding.progressbar.progress = position + spanCount - 1
                }
            }
        ))
    }

    private fun getSpanCount(): Int {
        return (resources.displayMetrics.heightPixels / resources.getDimension(R.dimen.card_height)).roundToInt()
    }

    private fun initSelectedRv() {
        selectedAdapter = SelectedItemAdapter(AppExecutors, viewModel) { item ->
            val pos = selectedAdapter.currentList.indexOf(item)
            if (pos >= 0) {
                binding.itemRv.scrollToPosition(pos)
                binding.progressbar.progress = pos
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
