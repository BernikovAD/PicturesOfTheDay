package com.example.picturesoftheday.utils

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.math.MathUtils
import androidx.core.view.marginBottom
import androidx.core.widget.NestedScrollView
import com.example.picturesoftheday.R
import com.google.android.material.appbar.AppBarLayout

class CustomBehavior @JvmOverloads constructor(
    context: Context? = null, attrs: AttributeSet? = null
) : CoordinatorLayout.Behavior<View>(context, attrs) {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ) = dependency is NestedScrollView

    override fun onStartNestedScroll(
        parent: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        dependency: View,
        axes: Int,
        type: Int
    ): Boolean {
        val scroll = dependency as NestedScrollView
        val scrollY = scroll.scrollY
        val param = child.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(40, 0, 40, scrollY)
        if (scrollY == 0) {
            param.setMargins(40, 0, 40, -300)
        }
        child.layoutParams = param
        child.requestLayout()

        return super.onStartNestedScroll(
            parent,
            child,
            directTargetChild,
            dependency,
            axes,
            type
        )
    }
}

