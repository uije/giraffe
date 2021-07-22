package com.zettafantasy.giraffe.feature.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.navArgs
import com.zettafantasy.giraffe.GiraffeApplication
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.data.EmotionInventory
import com.zettafantasy.giraffe.data.GiraffeRepository
import com.zettafantasy.giraffe.data.NeedInventory
import com.zettafantasy.giraffe.databinding.SingleRecordFragmentBinding
import org.apache.commons.lang3.StringUtils

class SingleRecordFragment : Fragment() {
    private lateinit var repository: GiraffeRepository
    private lateinit var emotionInventory: EmotionInventory
    private lateinit var needInventory: NeedInventory
    private lateinit var binding: SingleRecordFragmentBinding

    private val args by navArgs<SingleRecordFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.single_record_fragment, container, false)
        emotionInventory = EmotionInventory.getInstance(resources)
        needInventory = NeedInventory.getInstance(resources)
        repository = (activity?.application as GiraffeApplication).repository

        repository.findRecord(args.recordId).asLiveData().observe(
            viewLifecycleOwner, {
                val record = RecordWrapper(it, emotionInventory, needInventory)
                binding.emotions.text = StringUtils.join(record.emotions, ",")
                binding.needs.text = StringUtils.join(record.needs, ",")
                binding.date.text = getString(R.string.format_datetime, record.date)
            }
        )

        return binding.root
    }
}