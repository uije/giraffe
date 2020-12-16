package com.zettafantasy.giraffe.feature.emotion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.databinding.FindEmotionFragmentBinding
import com.zettafantasy.giraffe.model.Emotion


class FindEmotionFragment : Fragment() {
    private lateinit var vAdapter: EmotionAdapterControl
    private lateinit var viewModel: FindEmotionViewModel
    private lateinit var binding: FindEmotionFragmentBinding

    companion object {
        const val TAG = "EmotionFragment"
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.find_emotion_fragment, container, false)
        viewModel = ViewModelProvider(this).get(FindEmotionViewModel::class.java)
        initUI()
        setData()
        return binding.root
    }

    private fun setData() {
        val emotions: Array<String> = resources.getStringArray(R.array.emotion_good)
        val group = AdapterDataBindingItemGroup(emotions.map { Emotion(it) }.toList())
        vAdapter.setItems(group, viewModel)
    }

    private fun initUI() {
        vAdapter = EmotionAdapterControl()
        binding.emotionRv.adapter = vAdapter.adapter
        binding.emotionRv.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    }
}
