package com.zettafantasy.giraffe.feature.record

import android.util.Log
import android.view.*
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration
import com.zettafantasy.giraffe.GiraffeApplication
import com.zettafantasy.giraffe.MainFragmentDirections
import com.zettafantasy.giraffe.MainViewModel
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.BaseBindingFragment
import com.zettafantasy.giraffe.common.navigateRecord
import com.zettafantasy.giraffe.databinding.RecordsFragmentBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class RecordsFragment : BaseBindingFragment<RecordsFragmentBinding>() {
    private lateinit var adapter: RecordAdapter
    private val model: MainViewModel by activityViewModels()
    private val viewModel: RecordViewModel by viewModels {
        RecordViewModelFactory(
            (requireActivity().application as GiraffeApplication).repository,
            resources,
            model.highLightItemId
        )
    }
    private val navController by lazy {
        Navigation.findNavController(binding.root)
    }

    override fun init(inflater: LayoutInflater, container: ViewGroup?): RecordsFragmentBinding {
        var binding: RecordsFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.records_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        initRecordRv(binding)

        lifecycleScope.launch {
            viewModel.allRecords.collectLatest {
                Log.d(javaClass.simpleName, "records updated")
                adapter.submitData(it)
            }
        }

        return binding
    }

    private fun initRecordRv(binding: RecordsFragmentBinding) {
        adapter = RecordAdapter(viewModel, resources.getColor(R.color.accent, null)) { record ->
            //상세화면
            navController.navigate(MainFragmentDirections.actionViewRecord(record.record))
        }

        adapter.addLoadStateListener { loadState ->
            if (isDataEmpty(loadState)) {
                binding.content?.isVisible = false
                binding.emptyView?.isVisible = true
            } else {
                binding.content?.isVisible = true
                binding.emptyView?.isVisible = false
            }
        }

        binding.recordRv.adapter = adapter
        binding.recordRv.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        binding.recordRv.addItemDecoration(
            LayoutMarginDecoration(resources.getDimensionPixelSize(R.dimen.record_margin))
        )
    }

    private fun isDataEmpty(loadState: CombinedLoadStates) =
        loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && adapter.itemCount < 1
}