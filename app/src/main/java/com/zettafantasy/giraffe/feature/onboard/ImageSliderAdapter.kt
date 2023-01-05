package com.zettafantasy.giraffe.feature.onboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.zettafantasy.giraffe.R


class ImageSliderAdapter(context: Context, @DrawableRes sliderImage: List<Int>) :
    RecyclerView.Adapter<ImageSliderAdapter.MyViewHolder>() {
    private val context: Context
    private val sliderImage: List<Int>

    init {
        this.context = context
        this.sliderImage = sliderImage
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_slider, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(@NonNull holder: MyViewHolder, position: Int) {
        holder.bindSliderImage(sliderImage[position])
    }

    override fun getItemCount(): Int {
        return sliderImage.size
    }

    inner class MyViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mImageView: ImageView

        init {
            mImageView = itemView.findViewById(R.id.imageSlider)
        }

        fun bindSliderImage(@DrawableRes imageRes: Int) {
            mImageView.setImageResource(imageRes)
        }
    }
}