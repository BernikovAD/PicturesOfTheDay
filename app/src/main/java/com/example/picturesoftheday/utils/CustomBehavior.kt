package com.example.picturesoftheday.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import kotlin.math.max

/**
 * Кастомный Behavior, позволяющий View занимать ровно то место, которое осталось между AppBarLayout и нижнем краем экрана
 */
class CustomBehavior @JvmOverloads constructor(
    context: Context? = null, attrs: AttributeSet? = null) : CoordinatorLayout.Behavior<View>(context, attrs) {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return dependency is Snackbar.SnackbarLayout
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        if (parent == null || child == null || dependency == null)
            return false

        val distanceY = getViewOffsetForSnackbar(parent, child)
        val fractionComplete = distanceY / dependency.height
        val scaleFactor = 1 - fractionComplete

        child.scaleX = scaleFactor
        child.scaleY = scaleFactor
        return true
    }

    private fun getViewOffsetForSnackbar(parent: CoordinatorLayout, view: View): Float{
        var maxOffset = 0f
        val dependencies = parent.getDependencies(view)

        dependencies.forEach { dependency ->
            if (dependency is Snackbar.SnackbarLayout && parent.doViewsOverlap(view, dependency)){
                maxOffset = max(maxOffset, (dependency.translationY - dependency.height) * -1)
            }
        }
        return maxOffset
    }
}


