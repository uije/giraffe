package com.zettafantasy.giraffe.feature.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.BaseBindingFragment
import com.zettafantasy.giraffe.data.EmotionInventory
import com.zettafantasy.giraffe.data.NeedInventory
import com.zettafantasy.giraffe.databinding.SingleRecordFragmentBinding
import org.apache.commons.lang3.StringUtils

class SingleRecordFragment : BaseBindingFragment<SingleRecordFragmentBinding>() {
    private lateinit var emotionInventory: EmotionInventory
    private lateinit var needInventory: NeedInventory

    private val args by navArgs<SingleRecordFragmentArgs>()

    override fun init(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): SingleRecordFragmentBinding {
        val binding: SingleRecordFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.single_record_fragment, container, false)
        emotionInventory = EmotionInventory.getInstance(resources)
        needInventory = NeedInventory.getInstance(resources)

        val record = RecordWrapper(args.record, emotionInventory, needInventory)
        binding.emotions.text = StringUtils.join(record.emotions, ",")
        binding.needs.text = StringUtils.join(record.needs, ",")
        binding.date.text = getString(R.string.format_datetime, record.date)
        binding.stimulus.text = record.stimulus

        return binding
    }
}