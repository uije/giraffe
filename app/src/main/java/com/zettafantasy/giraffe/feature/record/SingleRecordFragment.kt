package com.zettafantasy.giraffe.feature.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.databinding.SingleRecordFragmentBinding

class SingleRecordFragment : Fragment() {
    private lateinit var binding: SingleRecordFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.single_record_fragment, container, false)
        return binding.root
    }
}