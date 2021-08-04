package com.zettafantasy.giraffe.feature.emotion

import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
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
import com.zettafantasy.giraffe.data.EmotionInventory
import com.zettafantasy.giraffe.data.Record
import com.zettafantasy.giraffe.databinding.FindEmotionFragmentBinding
import com.zettafantasy.giraffe.model.Emotion
import java.util.*
import kotlin.math.max
import kotlin.math.roundToInt


class FindEmotionFragment : BaseBindingFragment<FindEmotionFragmentBinding>() {
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var selectedAdapter: SelectedItemAdapter
    private lateinit var viewModel: FindEmotionViewModel
    private lateinit var emotions: List<Emotion>
    private var doneMenu: MenuItem? = null
    private val args by navArgs<FindEmotionFragmentArgs>()

    companion object {
        const val TAG = "FindEmotionFragment"
    }

    override fun init(inflater: LayoutInflater, container: ViewGroup?): FindEmotionFragmentBinding {
        setHasOptionsMenu(true)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.find_emotion_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel = ViewModelProvider(this).get(FindEmotionViewModel::class.java)
        binding.viewModel = viewModel
        initUI()
        setData()
        return binding
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

                Navigation.findNavController(binding.root)
                    .navigate(
                        FindEmotionFragmentDirections.actionFindEmotionToFindNeed(
                            Record(
                                viewModel.selectedItems.value!!.map { it.id!! }.toList(),
                                Collections.emptyList(), args.record.stimulus
                            )
                        )
                    )

                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setData() {
        Log.d(TAG, arguments.toString())
        this.emotions = EmotionInventory.getInstance(resources).getListByType(args.emotionType)
        itemAdapter.submitList(this.emotions)
        binding.progressbar.max = max(emotions.size - 1, 0)
    }

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
        val spanCount = getSpanCount()
        binding.emotionRv.layoutManager =
            GridLayoutManager(context, spanCount, RecyclerView.HORIZONTAL, false)

        binding.emotionRv.addItemDecoration(
            LayoutMarginDecoration(
                spanCount,
                resources.getDimensionPixelSize(R.dimen.emotion_margin)
            )
        )

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.emotionRv)
        binding.emotionRv.addOnScrollListener(SnapPagerScrollListener(snapHelper,
            SnapPagerScrollListener.ON_SETTLED,
            true,
            object : SnapPagerScrollListener.OnChangeListener {
                override fun onSnapped(position: Int) {
                    Log.d(TAG, String.format("onSnapped(%s)", position))
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
            val pos = emotions.indexOf(item)
            if (pos >= 0) {
                binding.emotionRv.scrollToPosition(pos)
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
