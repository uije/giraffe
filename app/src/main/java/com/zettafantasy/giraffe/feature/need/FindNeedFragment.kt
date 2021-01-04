package com.zettafantasy.giraffe.feature.need

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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
    private var doneMenu: MenuItem? = null
    private lateinit var needs: List<Need>
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var selectedAdapter: SelectedItemAdapter
    private lateinit var binding: FindNeedFragmentBinding
    private lateinit var viewModel: FindNeedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.find_need_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel = ViewModelProvider(this).get(FindNeedViewModel::class.java)
        binding.viewModel = viewModel
        initUI()
        setData()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.find_need, menu)
        doneMenu = menu.findItem(R.id.menu_done)
        doneMenu?.isVisible = viewModel.hasItem()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_done -> {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_find_need_to_confirm)
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setData() {
        needs = NeedInventory.getInstance(resources).getList()
        itemAdapter.submitList(needs)
    }

    private fun initUI() {
        initItemRv()
        initSelectedRv()
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

    private fun initSelectedRv() {
        selectedAdapter = SelectedItemAdapter(AppExecutors, viewModel) { emotion ->
            val pos = needs.indexOf(emotion)
            if (pos >= 0) {
                binding.needRv.smoothScrollToPosition(pos)
            }
        }
        binding.selectedRv.adapter = selectedAdapter
        binding.selectedRv.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.selectedRv.addItemDecoration(
            LayoutMarginDecoration(
                1, resources.getDimensionPixelSize(R.dimen.selected_emotion_margin)
            )
        )

        viewModel.selectedItems.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                it.toList().let(selectedAdapter::submitList)
            }
            doneMenu?.isVisible = it.isNotEmpty()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        itemAdapter.setLifecycleDestroyed()
        selectedAdapter.setLifecycleDestroyed()
    }
}