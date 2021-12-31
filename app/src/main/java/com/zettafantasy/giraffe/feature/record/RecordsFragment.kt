package com.zettafantasy.giraffe.feature.record

import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration
import com.zettafantasy.giraffe.GiraffeApplication
import com.zettafantasy.giraffe.MainFragmentDirections
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.BaseBindingFragment
import com.zettafantasy.giraffe.databinding.RecordsFragmentBinding


class RecordsFragment : BaseBindingFragment<RecordsFragmentBinding>() {
    private lateinit var adapter: RecordAdapter
    private val viewModel: RecordViewModel by viewModels {
        RecordViewModelFactory(
            (requireActivity().application as GiraffeApplication).repository,
            resources
        )
    }

    override fun init(inflater: LayoutInflater, container: ViewGroup?): RecordsFragmentBinding {
        var binding: RecordsFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.records_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        initRecordRv(binding)

        viewModel.allRecords.observe(viewLifecycleOwner) { records ->
            Log.d(javaClass.simpleName, "records updated")
            adapter.submitList(records)
        }

        return binding
    }

    private fun initRecordRv(binding: RecordsFragmentBinding) {
        adapter = RecordAdapter(viewModel) { record ->
            //상세화면
            Navigation.findNavController(binding.root)
                .navigate(MainFragmentDirections.actionViewRecord(record.record))
        }

        binding.recordRv.adapter = adapter
        binding.recordRv.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        binding.recordRv.addItemDecoration(
            LayoutMarginDecoration(resources.getDimensionPixelSize(R.dimen.record_margin))
        )
    }
}