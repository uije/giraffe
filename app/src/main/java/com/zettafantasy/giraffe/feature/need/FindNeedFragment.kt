package com.zettafantasy.giraffe.feature.need

import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.*
import com.zettafantasy.giraffe.data.NeedInventory
import com.zettafantasy.giraffe.data.Record
import com.zettafantasy.giraffe.databinding.FindNeedFragmentBinding
import com.zettafantasy.giraffe.feature.emotion.FindEmotionFragment
import com.zettafantasy.giraffe.model.Need
import kotlin.math.max
import kotlin.math.roundToInt

class FindNeedFragment : BaseBindingFragment<FindNeedFragmentBinding>() {
    private var doneMenu: MenuItem? = null
    private lateinit var needs: List<Need>
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var selectedAdapter: SelectedItemAdapter
    private lateinit var viewModel: FindItemViewModel
    private val args by navArgs<FindNeedFragmentArgs>()

    override fun init(inflater: LayoutInflater, container: ViewGroup?): FindNeedFragmentBinding {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.find_need_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel = ViewModelProvider(this).get(FindItemViewModel::class.java)
        binding.viewModel = viewModel
        initUI()
        setData()

        return binding
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.find_need, menu)
        doneMenu = menu.findItem(R.id.menu_done)
        doneMenu?.isVisible = viewModel.hasItem()
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

    private fun setData() {
        needs = NeedInventory.getInstance(resources).getList()
        itemAdapter.submitList(needs)
        binding.progressbar.max = max(needs.size - 1, 0)
    }

    private fun initUI() {
        initItemRv()
        initSelectedRv()
    }

    private fun initItemRv() {
        itemAdapter = ItemAdapter(AppExecutors, viewModel, R.layout.need_view) { item ->
            val pos = viewModel.toggle(item)
            if (pos >= 0) {
                binding.selectedRv.smoothScrollToPosition(pos)
            }
        }

        binding.needRv.adapter = itemAdapter
        val spanCount = getSpanCount()
        binding.needRv.layoutManager =
            GridLayoutManager(context, spanCount, RecyclerView.HORIZONTAL, false)

        binding.needRv.addItemDecoration(
            LayoutMarginDecoration(
                spanCount,
                resources.getDimensionPixelSize(R.dimen.emotion_margin)
            )
        )

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.needRv)
        binding.needRv.addOnScrollListener(SnapPagerScrollListener(snapHelper,
            SnapPagerScrollListener.ON_SETTLED,
            true,
            object : SnapPagerScrollListener.OnChangeListener {
                override fun onSnapped(position: Int) {
                    Log.d(FindEmotionFragment.TAG, String.format("onSnapped(%s)", position))
                    binding.progressbar.progress = position + spanCount - 1
                }
            }
        ))
    }

    private fun getSpanCount(): Int {
        return (resources.displayMetrics.heightPixels / resources.getDimension(R.dimen.card_height)).roundToInt()
    }

    private fun initSelectedRv() {
        selectedAdapter = SelectedItemAdapter(AppExecutors, viewModel) { emotion ->
            val pos = needs.indexOf(emotion)
            if (pos >= 0) {
                binding.needRv.scrollToPosition(pos)
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

        viewModel.selectedItems.observe(viewLifecycleOwner, Observer {
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