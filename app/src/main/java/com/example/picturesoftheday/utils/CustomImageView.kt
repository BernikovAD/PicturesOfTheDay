package com.example.picturesoftheday.utils


import androidx.appcompat.widget.AppCompatImageView
import android.content.Context
import android.util.AttributeSet


class CustomImageView @JvmOverloads constructor(
    context: Context,attributeSet: AttributeSet?=null, defStyleAttr: Int = 0)
    :AppCompatImageView(context,attributeSet,defStyleAttr){
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}



