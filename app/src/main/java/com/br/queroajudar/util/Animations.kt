package com.br.queroajudar.util

import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.core.animation.doOnEnd


fun toggleViewRotation0to180(view : View, isRotated : Boolean){
    if(isRotated){
        view.animate().setDuration(200).rotation(0F)
    } else{
        view.animate().setDuration(200).rotation(180F)
    }
}

fun toggleViewExpansion2(view:View, isExpanded: Boolean){
    view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    val maxHeight = view.measuredHeight + view.paddingTop + view.paddingBottom
    val startHeight = if (isExpanded) maxHeight else 0
    val targetHeight = if (isExpanded) 0 else maxHeight

    val expandAnimator = ValueAnimator
        .ofInt(startHeight, targetHeight)
        .setDuration(200)

    expandAnimator.addUpdateListener {
        val value = it.animatedValue as Int
        view.layoutParams.height = value
        view.requestLayout()
    }

    expandAnimator.start()
}

fun toggleViewExpansion(view:View, isExpanded:Boolean){
    if(isExpanded){
        collapseView(view)
    } else{
        expandView(view)
    }
}



private fun expandView(view : View){

    view.measure(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
    val viewHeight = view.measuredHeight

    view.layoutParams.height = 0
    view.visibility = View.VISIBLE

    val animation = object: Animation(){
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            view.layoutParams.height = if(interpolatedTime == 1.0f){
                ViewGroup.LayoutParams.WRAP_CONTENT
            } else {
                (viewHeight * interpolatedTime).toInt()
            }
            view.requestLayout()
        }
    }

    animation.duration = (viewHeight / view.context.resources.displayMetrics.density).toLong()

    view.startAnimation(animation)

}

private fun collapseView(view:View){
    val viewHeight = view.measuredHeight

    val animation = object: Animation(){
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            if(interpolatedTime == 1.0f){
                view.visibility = View.GONE
            } else {
                view.layoutParams.height = (viewHeight - (viewHeight * interpolatedTime)).toInt()
                view.requestLayout()
            }
        }
    }

    animation.duration = (viewHeight/view.context.resources.displayMetrics.density).toLong()
    view.startAnimation(animation)

}
