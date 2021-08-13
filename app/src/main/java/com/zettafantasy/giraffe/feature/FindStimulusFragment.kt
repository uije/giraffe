package com.zettafantasy.giraffe.feature

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.BaseBindingFragment
import com.zettafantasy.giraffe.data.Record
import com.zettafantasy.giraffe.databinding.FindStimulusFragmentBinding
import java.util.*


class FindStimulusFragment : BaseBindingFragment<FindStimulusFragmentBinding>() {
    private val args by navArgs<FindStimulusFragmentArgs>()
    private var doneMenu: MenuItem? = null

    override fun init(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FindStimulusFragmentBinding {
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
                hideKeyboard()
                Navigation.findNavController(binding.root)
                    .navigate(
                        FindStimulusFragmentDirections.actionFindStimulusToFindEmotion(
                            Record(
                                Collections.emptyList(),
                                Collections.emptyList(),
                                binding.editTx.text.toString().trim()
                            ), args.emotionType
                        )
                    )

                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.editTx.windowToken, 0);
    }

    companion object {
        const val TAG = "FindStimulusFragment"
    }
}
