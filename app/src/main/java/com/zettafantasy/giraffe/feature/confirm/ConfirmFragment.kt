package com.zettafantasy.giraffe.feature.confirm

import android.os.Bundle
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.zettafantasy.giraffe.GiraffeApplication
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.Preferences
import com.zettafantasy.giraffe.data.EmotionInventory
import com.zettafantasy.giraffe.data.GiraffeRepository
import com.zettafantasy.giraffe.data.NeedInventory
import com.zettafantasy.giraffe.databinding.ConfirmFragmentBinding
import com.zettafantasy.giraffe.feature.need.FindNeedFragmentArgs
import com.zettafantasy.giraffe.model.Emotion
import com.zettafantasy.giraffe.model.Need
import kotlinx.coroutines.*

class ConfirmFragment : Fragment() {
    private lateinit var binding: ConfirmFragmentBinding
    private lateinit var emotions: List<Emotion?>
    private lateinit var needs: List<Need?>
    private lateinit var repository: GiraffeRepository
    private val args by navArgs<FindNeedFragmentArgs>()
    private lateinit var emotionInventory: EmotionInventory
    private lateinit var needInventory: NeedInventory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        repository = (activity?.application as GiraffeApplication).repository

        binding = DataBindingUtil.inflate(inflater, R.layout.confirm_fragment, container, false)

        binding.btnSave.setOnClickListener {
            save { id -> navigateNextScreen(binding.root, id) }
        }

        emotionInventory = EmotionInventory.getInstance(resources)
        needInventory = NeedInventory.getInstance(resources)

        binding.emotions.movementMethod = ScrollingMovementMethod()
        binding.needs.movementMethod = ScrollingMovementMethod()

        emotions = emotionInventory.getListByIds(args.record.emotionIds)
        emotions?.let {
            binding.emotions.text = TextUtils.join(", ", it)
        }

        needs = needInventory.getListByIds(args.record.needIds)
        needs?.let {
            binding.needs.text = TextUtils.join(", ", it)
        }

        binding.stimulus.text = args.record.stimulus

        return binding.root
    }

    private fun save(onSuccess: (recordId: Long) -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            val recordId = repository.insert(args.record)

            if (this@ConfirmFragment.isVisible) {
                withContext(Dispatchers.Main) {
                    onSuccess(recordId)
                }
            }
        }
    }

    private fun navigateNextScreen(view: View, highLightItemId: Long) {
        if (Preferences.shownCelebrateScreen) {
            Navigation.findNavController(view)
                .navigate(ConfirmFragmentDirections.actionConfirmToRecord(highLightItemId))
        } else {
            Navigation.findNavController(view)
                .navigate(ConfirmFragmentDirections.actionConfirmToCelebrate())
        }
    }
}