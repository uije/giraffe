package com.zettafantasy.giraffe.feature.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration
import com.zettafantasy.giraffe.GiraffeApplication
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.AppExecutors
import com.zettafantasy.giraffe.data.EmotionInventory
import com.zettafantasy.giraffe.data.NeedInventory
import com.zettafantasy.giraffe.databinding.RecordFragmentBinding

class RecordFragment : Fragment() {
    private lateinit var binding: RecordFragmentBinding
    private lateinit var adapter: RecordAdapter
    private lateinit var emotionInventory: EmotionInventory
    private lateinit var needInventory: NeedInventory
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
                .navigate(R.id.action_create_record)
        }

        initRecordRv()

        emotionInventory = EmotionInventory.getInstance(resources)
        needInventory = NeedInventory.getInstance(resources)

        viewModel.allRecords.observe(viewLifecycleOwner) { records ->
            records.let { it ->
                adapter.submitList(it.map {
                    RecordWrapper(
                        it,
                        emotionInventory,
                        needInventory
                    )
                }.toList())
            }
        }

        return binding.root
    }

    private fun initRecordRv() {
        adapter = RecordAdapter(AppExecutors, viewModel, R.layout.record_view) { record ->
            //상세화면
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_view_record)
        }

        binding.recordRv.adapter = adapter
        binding.recordRv.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        binding.recordRv.addItemDecoration(
            LayoutMarginDecoration(resources.getDimensionPixelSize(R.dimen.record_margin))
        )
    }
}