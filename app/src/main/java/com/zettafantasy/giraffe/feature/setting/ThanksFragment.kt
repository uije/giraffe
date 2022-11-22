package com.zettafantasy.giraffe.feature.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.databinding.ThanksFragmentBinding

class ThanksFragment : Fragment() {

    private lateinit var binding: ThanksFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.thanks_fragment, container, false)
        return binding.root
    }

}