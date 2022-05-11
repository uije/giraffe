package com.zettafantasy.giraffe.feature.wordcloud

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.ColorInt
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.view.drawToBitmap
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.observe
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.zettafantasy.giraffe.GiraffeApplication
import com.zettafantasy.giraffe.MainViewModel
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.*
import com.zettafantasy.giraffe.data.EmotionInventory
import com.zettafantasy.giraffe.data.GiraffeRepository
import com.zettafantasy.giraffe.data.NeedInventory
import com.zettafantasy.giraffe.databinding.WordCloudFragmentBinding
import com.zettafantasy.giraffe.databinding.WordCloudViewBinding
import net.alhazmy13.wordcloud.ColorTemplate
import java.io.File

class WordCloudFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var needInventory: NeedInventory
    private lateinit var emotionInventory: EmotionInventory
    private lateinit var repository: GiraffeRepository
    private lateinit var binding: WordCloudFragmentBinding
    private val viewModel: WordCloudViewModel by viewModels {
        WordCloudViewModelFactory(
            (requireActivity().application as GiraffeApplication).repository,
            resources
        )
    }

    private val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        repository = (requireActivity().application as GiraffeApplication).repository
        emotionInventory = EmotionInventory.getInstance(resources)
        needInventory = NeedInventory.getInstance(resources)
        binding = DataBindingUtil.inflate(inflater, R.layout.word_cloud_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.pager.adapter = WordCloudPagerAdapter(childFragmentManager, lifecycle)
        initTabLayout()
        initSpinner()
        Log.d(javaClass.simpleName, viewModel.toString()) //lazy loading

        if (model.destinationScreen == DestinationScreen.INSIGHT_NEED) {
            binding.pager.post {
                binding.pager.setCurrentItem(POS_NEED, true)
            }
        }

        model.shareClickEvent.observe(viewLifecycleOwner) {
            if (binding.pager.currentItem == POS_EMOTION) {
                model.callShareEmotionEvent()
            } else if (binding.pager.currentItem == POS_NEED) {
                model.callShareNeedEvent()
            }
        }

        return binding.root
    }

    private fun initSpinner() {
        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            WordCloudPeriod.values().map { getString(it.desc) }
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.spinner.adapter = adapter
            binding.spinner.setSelection(Preferences.wordCloudPeriod.ordinal)
        }
        binding.spinner.onItemSelectedListener = this
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Log.d(javaClass.simpleName, String.format("onNothingSelected %s", parent))
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d(
            javaClass.simpleName,
            String.format("onItemSelected %s %s %s %s", parent, view, position, id)
        )

        Preferences.wordCloudPeriod = WordCloudPeriod.values()[position]
        viewModel.load(viewLifecycleOwner)
    }

    private fun initTabLayout() {
        TabLayoutMediator(
            binding.tabLayout, binding.pager
        ) { tab: TabLayout.Tab, position: Int ->
            if (position == 0) {
                tab.text = getString(R.string.emotion)
            } else {
                tab.text = getString(R.string.need)
            }
        }.attach()
    }

    companion object {
        const val POS_EMOTION = 0
        const val POS_NEED = 1
    }
}

class WordCloudPagerAdapter(
    @NonNull fragmentManager: FragmentManager,
    @NonNull lifecycle: Lifecycle
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    @NonNull
    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            EmotionCloudFragment()
        } else {
            NeedCloudFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}

class EmotionCloudFragment : WordCloudItemFragment() {
    override fun init() {
        viewModel.emotions.observe(viewLifecycleOwner) {
            binding.wordCloud.setDataSet(it)
            binding.wordCloud.setColors(ColorTemplate.MATERIAL_COLORS)
            binding.wordCloud.notifyDataSetChanged()
            Log.d(this.javaClass.simpleName, it.toString())
        }

        model.shareEmotionEvent.observe(viewLifecycleOwner) {
            shareWordCloud()
        }
    }
}

class NeedCloudFragment : WordCloudItemFragment() {
    override fun init() {
        viewModel.needs.observe(viewLifecycleOwner) {
            binding.wordCloud.setDataSet(it)
            binding.wordCloud.setColors(ColorTemplate.MATERIAL_COLORS)
            binding.wordCloud.notifyDataSetChanged()
            Log.d(this.javaClass.simpleName, it.toString())
        }

        model.shareNeedEvent.observe(viewLifecycleOwner) {
            shareWordCloud()
        }
    }
}

abstract class WordCloudItemFragment : Fragment() {
    protected lateinit var binding: WordCloudViewBinding

    protected val viewModel: WordCloudViewModel by viewModels({ requireParentFragment() })

    protected val model: MainViewModel by activityViewModels()

    private var shareFile: File? = null

    abstract fun init()

    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.word_cloud_view, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.wordCloud.setBackgroundColor(Color.TRANSPARENT)

        init()

        Log.d(javaClass.simpleName, "onCreateView")
        return binding.root
    }

    protected fun shareWordCloud() {
        shareFile =
            applyBgColor(binding.wordCloud.drawToBitmap(), Color.WHITE)?.save(requireContext())
        shareFile?.getUri(requireContext())?.let {
            startActivityForResult(
                Intent.createChooser(
                    it.getShareIntent(),
                    resources.getText(R.string.share)
                ), REQ_SHARE_IMAGE
            )
        }
    }

    private fun applyBgColor(bitmap: Bitmap, @ColorInt color: Int): Bitmap? {
        return Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config).also {
            with(Canvas(it)) {
                drawColor(color)
                drawBitmap(bitmap, 0f, 0f, null)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(javaClass.simpleName, "onActivityResult($requestCode $resultCode $data)")

        if (requestCode == REQ_SHARE_IMAGE && shareFile != null) {
            shareFile?.delete()
            Log.d(javaClass.simpleName, "File deleted $shareFile")
        }
    }

    companion object {
        const val REQ_SHARE_IMAGE = 1001
    }
}