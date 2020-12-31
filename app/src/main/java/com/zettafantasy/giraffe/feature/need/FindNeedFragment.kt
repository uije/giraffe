package com.zettafantasy.giraffe.feature.need

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.AppExecutors
import com.zettafantasy.giraffe.common.ItemAdapter
import com.zettafantasy.giraffe.common.SelectedItemAdapter
import com.zettafantasy.giraffe.data.NeedInventory
import com.zettafantasy.giraffe.databinding.FindNeedFragmentBinding
import com.zettafantasy.giraffe.model.Need

class FindNeedFragment : Fragment() {
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var selectedAdapter: SelectedItemAdapter
    private lateinit var binding: FindNeedFragmentBinding
    private lateinit var viewModel: FindNeedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.find_need_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel = ViewModelProvider(this).get(FindNeedViewModel::class.java)
        binding.viewModel = viewModel
        initUI()
        itemAdapter.submitList(NeedInventory.getInstance(resources).getList())

        return binding.root
    }

    private fun initUI() {
        initItemRv()
    }

    private fun initItemRv() {
        itemAdapter = ItemAdapter(AppExecutors, viewModel, R.layout.need_view) { item ->
            val pos = viewModel.toggle(item as Need)
            if (pos >= 0) {
                binding.selectedRv.smoothScrollToPosition(pos)
            }
        }

        binding.needRv.adapter = itemAdapter
        val spanCount = 2
        binding.needRv.layoutManager =
            GridLayoutManager(context, spanCount, RecyclerView.HORIZONTAL, false)

        binding.needRv.addItemDecoration(
            LayoutMarginDecoration(
                spanCount,
                resources.getDimensionPixelSize(R.dimen.emotion_margin)
            )
        )
    }
}