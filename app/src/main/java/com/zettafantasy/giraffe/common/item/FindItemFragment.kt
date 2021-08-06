package com.zettafantasy.giraffe.common.item

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.rizafu.coachmark.CoachMark
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.AppExecutors
import com.zettafantasy.giraffe.common.BaseBindingFragment
import com.zettafantasy.giraffe.common.SnapPagerScrollListener
import com.zettafantasy.giraffe.databinding.FindItemFragmentBinding
import com.zettafantasy.giraffe.databinding.TooltipFindItemBinding
import kotlin.math.max
import kotlin.math.roundToInt


abstract class FindItemFragment : BaseBindingFragment<FindItemFragmentBinding>() {
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var selectedAdapter: SelectedItemAdapter
    protected lateinit var viewModel: FindItemViewModel
    private var doneMenu: MenuItem? = null
    private var coachMark: CoachMark? = null

    companion object {
        private const val HIDE_COACH_MARK_MILLIS: Long = 2000
    }

    abstract fun provideItems(): List<Item>

    abstract fun provideDoneMenu(inflater: MenuInflater, menu: Menu): MenuItem

    abstract fun provideViewModel(): FindItemViewModel

    override fun init(inflater: LayoutInflater, container: ViewGroup?): FindItemFragmentBinding {
        setHasOptionsMenu(true)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.find_item_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel = provideViewModel()
        binding.viewModel = viewModel
        initUI()
        setData(provideItems())

        if (viewModel.showCoachMark) {
            showCoachMark(viewModel.coachMarkText)
        }

        return binding
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        doneMenu = provideDoneMenu(inflater, menu)
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
        coachMark?.dismiss()
    }

    private fun showCoachMark(desc: String) {
        val tooltip = DataBindingUtil.inflate<TooltipFindItemBinding>(
            LayoutInflater.from(context),
            R.layout.tooltip_find_item,
            null,
            false
        )

        tooltip.desc.text = desc
        coachMark = CoachMark.Builder(requireActivity())
            .addTooltipChild(tooltip.root)
            .setTooltipAlignment(CoachMark.ROOT_CENTER)
            .setTooltipBackgroundColor(android.R.color.transparent)
            .setBackgroundAlpha(0.6f)
            .setDismissible()
            .setTooltipPointer(CoachMark.POINTER_GONE)
            .show()

        Handler(Looper.getMainLooper()).postDelayed({
            coachMark?.dismiss()
        }, HIDE_COACH_MARK_MILLIS)
    }
}
