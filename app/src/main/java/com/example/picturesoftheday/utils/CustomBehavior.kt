package com.example.picturesoftheday.utils

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.NestedScrollView
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
    )= dependency is NestedScrollView



    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: View,
        ev: MotionEvent
    ): Boolean {

        Log.i("MyTag",
            " \nparent ${parent.alpha} \nchild ${child.alpha}")
        child.alpha = 1f
        parent.alpha = 0f

        return super.onInterceptTouchEvent(parent, child, ev)
    }
}

