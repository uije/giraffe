package com.zettafantasy.giraffe.feature.record

import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration
import com.zettafantasy.giraffe.GiraffeApplication
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.AppExecutors
import com.zettafantasy.giraffe.common.BaseBindingFragment
import com.zettafantasy.giraffe.common.Preferences
import com.zettafantasy.giraffe.data.EmotionInventory
import com.zettafantasy.giraffe.data.NeedInventory
import com.zettafantasy.giraffe.data.Record
import com.zettafantasy.giraffe.databinding.RecordFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RecordFragment : BaseBindingFragment<RecordFragmentBinding>() {
    private lateinit var adapter: RecordAdapter
    private lateinit var emotionInventory: EmotionInventory
    private lateinit var needInventory: NeedInventory
    private val viewModel: RecordViewModel by viewModels {
        RecordViewModelFactory((requireActivity().application as GiraffeApplication).repository)
    }

    companion object {
        const val TAG = "RecordFragment"
    }

    override fun init(inflater: LayoutInflater, container: ViewGroup?): RecordFragmentBinding {
        setHasOptionsMenu(true)
        var binding: RecordFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.record_fragment, container, false)
        binding.fab.setOnClickListener {
            if (Preferences.shownRecordIntro) {
                Navigation.findNavController(binding.root)
                    .navigate(RecordFragmentDirections.actionGoGoodOrBad())
            } else {
                Navigation.findNavController(binding.root)
                    .navigate(RecordFragmentDirections.actionGoIntroDesc())
            }
        }

        initRecordRv(binding)

        emotionInventory = EmotionInventory.getInstance(resources)
        needInventory = NeedInventory.getInstance(resources)

        viewModel.allRecords.observe(viewLifecycleOwner) { records ->
            records.let { it ->
                Log.d(TAG, String.format("allRecords %s", it.size))
                adapter.submitList(it.map {
                    RecordWrapper(
                        it,
                        emotionInventory,
                        needInventory
                    )
                }.toList())
            }
        }

        return binding
    }

    private fun initRecordRv(binding: RecordFragmentBinding) {
        adapter = RecordAdapter(AppExecutors, viewModel, R.layout.record_view) { record ->
            //상세화면
            Navigation.findNavController(binding.root)
                .navigate(RecordFragmentDirections.actionViewRecord(record.record))
        }

        binding.recordRv.adapter = adapter
        binding.recordRv.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        binding.recordRv.addItemDecoration(
            LayoutMarginDecoration(resources.getDimensionPixelSize(R.dimen.record_margin))
        )

        val itemTouchHelper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    AlertDialog.Builder(requireContext())
                        .setMessage("해당 기록을 지우시겠어요?")
                        .setPositiveButton("네") { _, _ ->
                            val recordWrapper = adapter.currentList[viewHolder.adapterPosition]
                            delete(recordWrapper.record) {
                                Log.d(TAG, String.format("record deleted %s", recordWrapper.record))
                            }
                        }
                        .setNegativeButton("아니오") { _, _ -> adapter.notifyItemChanged(viewHolder.adapterPosition); }
                        .show()
                }
            })
        itemTouchHelper.attachToRecyclerView(binding.recordRv)
    }

    private fun delete(record: Record, onSuccess: () -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                viewModel.repository.deleteRecord(record)
            }
            if (this@RecordFragment.isVisible) {
                onSuccess()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.record, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_setting -> {
                Navigation.findNavController(binding.root)
                    .navigate(RecordFragmentDirections.actionGoSetting())
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}