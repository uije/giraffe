package com.zettafantasy.giraffe

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.adapters.Converters
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.zettafantasy.giraffe.data.Record


object BindingAdapter {

    @BindingAdapter("android:tint")
    @JvmStatic
    fun setTintCompat(imageView: ImageView, @ColorInt color: Int) {
        Log.d(this.javaClass.simpleName, "setTintCompat")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setImageTintList(
                Converters.convertColorToColorStateList(
                    color
                )
            )
        } else {
            val originalDrawable: Drawable = imageView.getDrawable()
            if (originalDrawable is VectorDrawableCompat) {
                originalDrawable.setTint(color)
            } else {
                val tintedDrawable = tintDrawable(originalDrawable, color)
                imageView.setImageDrawable(tintedDrawable)
            }
        }
    }

    private fun tintDrawable(drawable: Drawable, @ColorInt tintInt: Int): Drawable {
        var drawable = drawable
        drawable = DrawableCompat.wrap(DrawableCompat.unwrap<Drawable>(drawable).mutate())
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
        DrawableCompat.setTint(drawable, tintInt)
        return drawable
    }

    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        Log.d(javaClass.simpleName, String.format("%s %s", view, show))
        view.visibility = if (show) View.VISIBLE else View.GONE
    }
}