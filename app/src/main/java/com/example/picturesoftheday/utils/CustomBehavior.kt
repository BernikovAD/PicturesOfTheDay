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
import com.example.picturesoftheday.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import kotlin.math.max

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
        Log.i("MyTag", "parent ${parent.alpha}  \nchild ${child.alpha}")
        if(child.alpha == 0f && parent.findViewById<NestedScrollView>(R.id.scroll).alpha == 1f){
            child.alpha = 1f
            parent.findViewById<NestedScrollView>(R.id.scroll).alpha = 0f
        }else{
            child.alpha = 0f
            parent.findViewById<NestedScrollView>(R.id.scroll).alpha = 1f
        }
        return super.onInterceptTouchEvent(parent, child, ev)
    }


}

