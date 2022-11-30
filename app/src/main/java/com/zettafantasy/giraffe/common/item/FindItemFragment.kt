package com.zettafantasy.giraffe.common.item

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.rizafu.coachmark.CoachMark
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.AppExecutors
import com.zettafantasy.giraffe.common.Preferences
import com.zettafantasy.giraffe.common.SnapPagerScrollListener
import com.zettafantasy.giraffe.common.ext.hideDelayed
import com.zettafantasy.giraffe.databinding.FindItemFragmentBinding
import com.zettafantasy.giraffe.databinding.TooltipFindItemBinding
import kotlin.math.max
import kotlin.math.roundToInt


abstract class FindItemFragment : Fragment() {
    protected lateinit var binding: FindItemFragmentBinding
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var selectedAdapter: SelectedItemAdapter
    protected lateinit var viewModel: FindItemViewModel
    private var doneMenu: MenuItem? = null
    private var coachMark: CoachMark? = null
    private val handler by lazy {
        Handler(Looper.getMainLooper())
    }

    abstract fun provideItems(): List<Item>

    abstract fun provideDoneMenu(inflater: MenuInflater, menu: Menu): MenuItem

    abstract fun provideViewModel(): FindItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.find_item_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel = provideViewModel()
        binding.viewModel = viewModel
        initUI()
        setData(provideItems())

        if (viewModel.showCoachMark) {
            showSwipeCoachMark(viewModel.coachMarkText, container)
        }

        return binding.root
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
            try {
                val pos = viewModel.toggle(emotion)
                if (pos >= 0) {
                    binding.selectedRv.smoothScrollToPosition(pos)
                }
            } catch (e: IllegalStateException) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.alert_over_max_item_count, viewModel.maxItemCount),
                    Toast.LENGTH_SHORT
                ).show()
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
                    bindCoachMark(position)
                }

                private fun bindCoachMark(position: Int) {
                    //상태바 코치마크
                    if (!Preferences.shownCoachMarkProgressBar && position > 0) {
                        handler.postDelayed({
                            showProgressBarCoachMark().hideDelayed {
                                Preferences.shownCoachMarkProgressBar = true
                            }
                        }, 100)
                    }

                    //완료버튼 코치마크
                    if (isEndOfProgress() && !Preferences.shownCoachMarkComplete) {
                        doneMenu?.let { item ->
                            activity?.findViewById<View>(item.itemId)?.let {
                                handler.postDelayed({
                                    showCompleteCoachMark(it).hideDelayed {
                                        Preferences.shownCoachMarkComplete = true
                                    }
                                }, 100)
                            }
                        }
                    }
                }

                private fun isEndOfProgress() =
                    binding.progressbar.max == binding.progressbar.progress

                private fun showProgressBarCoachMark(): CoachMark =
                    CoachMark.Builder(requireActivity())
                        .setTarget(binding.progressbar)
                        .addTooltipChildText(
                            requireActivity(),
                            getString(R.string.tooltip_progress_bar),
                            android.R.color.black
                        )
                        .setTooltipAlignment(CoachMark.TARGET_TOP)
                        .setTooltipPointer(CoachMark.POINTER_MIDDLE)
                        .setTooltipBackgroundColor(R.color.accent)
                        .show()

                private fun showCompleteCoachMark(view: View): CoachMark =
                    CoachMark.Builder(requireActivity())
                        .setTarget(view)
                        .addTooltipChildText(
                            requireActivity(),
                            getString(R.string.tooltip_complete),
                            android.R.color.black
                        )
                        .setTooltipAlignment(CoachMark.TARGET_BOTTOM_LEFT)
                        .setTooltipPointer(CoachMark.POINTER_RIGHT)
                        .setTooltipBackgroundColor(R.color.accent)
                        .show()
            }
        ))
    }

    private fun getSpanCount(): Int {
        return (resources.displayMetrics.heightPixels / resources.getDimension(R.dimen.card_height)).roundToInt()
    }

    private fun initSelectedRv() {
        selectedAdapter = SelectedItemAdapter(AppExecutors, viewModel) { item ->
            val pos = itemAdapter.currentList.indexOf(item)
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

        viewModel.selectedItems.observe(viewLifecycleOwner) {
            selectedAdapter.submitList(it.toList())
            doneMenu?.isVisible = it.isNotEmpty()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        itemAdapter.setLifecycleDestroyed()
        selectedAdapter.setLifecycleDestroyed()
        coachMark?.dismiss()
    }

    private fun showSwipeCoachMark(desc: String, container: ViewGroup?) {
        val tooltip = DataBindingUtil.inflate<TooltipFindItemBinding>(
            LayoutInflater.from(context),
            R.layout.tooltip_find_item,
            container,
            false
        )

        //이상하게(?) match_parent 적용이 안되서 코드로 재작업
        stretchWidthMatchParent(tooltip, container)

        tooltip.desc.text = desc
        coachMark = CoachMark.Builder(requireActivity())
            .addTooltipChild(tooltip.root)
            .setTooltipAlignment(CoachMark.ROOT_CENTER)
            .setTooltipBackgroundColor(android.R.color.transparent)
            .setBackgroundAlpha(0.6f)
            .setTooltipPointer(CoachMark.POINTER_GONE)
            .show()

        coachMark?.hideDelayed {
            Preferences.shownCoachMarkSwipeCard = true
        }
    }

    private fun stretchWidthMatchParent(
        tooltip: TooltipFindItemBinding,
        container: ViewGroup?
    ) {
        val layoutParams = tooltip.root.layoutParams
        layoutParams.width = container?.width ?: layoutParams.width
        tooltip.root.layoutParams = layoutParams
    }
}
