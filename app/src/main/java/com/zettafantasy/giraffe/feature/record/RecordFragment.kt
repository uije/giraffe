package com.zettafantasy.giraffe.feature.record

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.zettafantasy.giraffe.GiraffeApplication
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.data.GiraffeRepository
import com.zettafantasy.giraffe.databinding.RecordFragmentBinding
import com.zettafantasy.giraffe.feature.main.RecordViewModel
import com.zettafantasy.giraffe.feature.main.RecordViewModelFactory

class RecordFragment : Fragment() {
    private lateinit var binding: RecordFragmentBinding
    private val viewModel: RecordViewModel by viewModels {
        RecordViewModelFactory((requireActivity().application as GiraffeApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.record_fragment, container, false)
        binding.fab.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_history_to_emotion_description)
        }

        viewModel.allRecords.observe(viewLifecycleOwner) { records ->
            records.let { Log.d(this.javaClass.simpleName, it.size.toString()) }
        }

        return binding.root
    }
}