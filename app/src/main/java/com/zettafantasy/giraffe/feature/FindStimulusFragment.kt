package com.zettafantasy.giraffe.feature

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.BaseBindingFragment
import com.zettafantasy.giraffe.databinding.FindStimulusFragmentBinding
import com.zettafantasy.giraffe.model.EmotionType

class FindStimulusFragment : BaseBindingFragment<FindStimulusFragmentBinding>() {

    private var doneMenu: MenuItem? = null

    override fun init(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FindStimulusFragmentBinding {
        Log.d(TAG, "init")
        setHasOptionsMenu(true)
        val binding: FindStimulusFragmentBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.find_stimulus_fragment,
            container,
            false
        )

        binding.editTx.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                doneMenu?.isVisible = (s?.trim()?.isNotEmpty() == true)
            }
        })

        return binding
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.find_stimulus, menu)
        doneMenu = menu.findItem(R.id.menu_done)
        doneMenu?.isVisible = binding.editTx.text.trim().isNotEmpty()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_done -> {

                val args = Bundle()
                args.putSerializable(EmotionType::class.simpleName, getEmotionType())
                args.putString("stimulus", binding.editTx.text.toString().trim())
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_find_stimulus_to_find_emotion, args)

                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getEmotionType(): EmotionType =
        arguments?.get(EmotionType::class.simpleName) as EmotionType

    companion object {
        const val TAG = "FindStimulusFragment"
    }
}
