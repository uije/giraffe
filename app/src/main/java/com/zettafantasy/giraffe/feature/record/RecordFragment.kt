package com.zettafantasy.giraffe.feature.record

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.zettafantasy.giraffe.GiraffeApplication
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.data.EmotionInventory
import com.zettafantasy.giraffe.data.GiraffeRepository
import com.zettafantasy.giraffe.data.NeedInventory
import com.zettafantasy.giraffe.data.Record
import com.zettafantasy.giraffe.databinding.SingleRecordFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.lang3.StringUtils

class RecordFragment : Fragment() {
    private val emotionInventory: EmotionInventory by lazy { EmotionInventory.getInstance(resources) }
    private val needInventory: NeedInventory by lazy { NeedInventory.getInstance(resources) }
    private lateinit var binding: SingleRecordFragmentBinding
    private val args by navArgs<RecordFragmentArgs>()
    private val repository: GiraffeRepository by lazy { (requireActivity().application as GiraffeApplication).repository }
    private val record: RecordWrapper by lazy {
        RecordWrapper(
            args.record,
            emotionInventory,
            needInventory
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.single_record_fragment, container, false)

        binding.emotions.text = StringUtils.join(record.emotions, ",")
        binding.needs.text = StringUtils.join(record.needs, ",")
        binding.date.text = getString(R.string.format_datetime, record.date)
        binding.stimulus.text = record.stimulus

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.record, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_delete -> {
                AlertDialog.Builder(requireContext())
                    .setMessage("해당 기록을 지우시겠어요?")
                    .setPositiveButton("네") { _, _ ->
                        delete(record.record) {
                            Log.d(
                                javaClass.simpleName,
                                String.format("record deleted %s", record.record)
                            )
                            Navigation.findNavController(binding.root)
                                .navigate(RecordFragmentDirections.actionRecordToRecords())
                        }
                    }
                    .show()
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun delete(record: Record, onSuccess: () -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                repository.deleteRecord(record)
            }
            if (this@RecordFragment.isVisible) {
                onSuccess()
            }
        }
    }
}