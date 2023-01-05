package com.zettafantasy.giraffe.feature.onboard

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.zettafantasy.giraffe.R
import com.zettafantasy.giraffe.common.Preferences
import com.zettafantasy.giraffe.databinding.OnboardActivityBinding

class OnboardActivity : AppCompatActivity() {

    private lateinit var binding: OnboardActivityBinding
    private lateinit var mViewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.onboard_activity)

        initViewPager()

        binding.textSkip.setOnClickListener {
            finish()
            animateSlideLeft(this)
        }

        binding.btnNextStep.setOnClickListener {
            if (getItem() < pageCount() - 1) {
                mViewPager.setCurrentItem(getItem() + 1, true)
            } else {
                finish()
                animateSlideLeft(this)
            }
        }

        Preferences.shownOnboardScreen = true
    }

    private fun initViewPager() {
        mViewPager = binding.viewPager
        mViewPager.adapter = ImageSliderAdapter(this, onboardImages)
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.btnNextStep.text =
                    if (isEndPage(position)) getString(R.string.start) else getString(
                        R.string.next
                    )
            }

            private fun isEndPage(position: Int) = position == (pageCount() - 1)

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
            override fun onPageScrollStateChanged(arg0: Int) {}
        })

        TabLayoutMediator(binding.pageIndicator, mViewPager) { _, _ -> }.attach()
    }

    private fun pageCount() = onboardImages.size

    private fun getItem(): Int {
        return mViewPager.currentItem
    }

    private fun animateSlideLeft(context: Context) {
        (context as Activity).overridePendingTransition(
            R.anim.animate_slide_left_enter,
            R.anim.animate_slide_left_exit
        )
    }

    companion object {
        private val onboardImages: List<Int> = listOf(
            R.drawable.onboard_1,
            R.drawable.onboard_2,
            R.drawable.onboard_3,
            R.drawable.onboard_4
        )
    }
}