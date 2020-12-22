package com.zettafantasy.giraffe.feature.emotion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.AppExecutors
import com.zettafantasy.giraffe.databinding.FindEmotionFragmentBinding
import com.zettafantasy.giraffe.model.Emotion


class FindEmotionFragment : Fragment() {
    private lateinit var emotionAdapter: EmotionAdapter
    private lateinit var selectedAdapter: SelectedEmotionAdapter
    private lateinit var viewModel: FindEmotionViewModel
    private lateinit var binding: FindEmotionFragmentBinding

    companion object {
        const val TAG = "FindEmotionFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.find_emotion_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel = ViewModelProvider(this).get(FindEmotionViewModel::class.java)
        binding.viewModel = viewModel
        initUI()
        setData()
        return binding.root
    }

    private fun setData() {
        Log.d(TAG, arguments.toString())

        val emotionType = arguments?.get(EmotionType::class.simpleName)
        val resourceId = getResourceId(emotionType)
        val emotions: Array<String> = resources.getStringArray(resourceId)

        val list = emotions.map {
            Emotion(it)
        }.toList()

        emotionAdapter.submitList(list)
    }

    private fun getResourceId(emotionType: Any?): Int {
        var resourceId = R.array.emotion_bad
        if (emotionType is EmotionType) {
            resourceId = when (emotionType) {
                EmotionType.GOOD -> R.array.emotion_good
                EmotionType.BAD -> R.array.emotion_bad
                else -> R.array.emotion_bad
            }
        }
        return resourceId
    }

    private fun initUI() {
        initEmotionRv()
        initSelectedRv()
    }

    private fun initEmotionRv() {
        emotionAdapter = EmotionAdapter(AppExecutors, viewModel) { emotion ->
            val pos = viewModel.toggle(emotion)
            if (pos >= 0) {
                binding.selectedRv.smoothScrollToPosition(pos)
            }
        }

        binding.emotionRv.adapter = emotionAdapter
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
        selectedAdapter = SelectedEmotionAdapter(AppExecutors, viewModel)
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
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        emotionAdapter.setLifecycleDestroyed()
        selectedAdapter.setLifecycleDestroyed()
    }
}
