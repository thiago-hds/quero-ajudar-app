package com.br.queroajudar.util

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation


fun toggleViewRotation0to180(view : View, isRotated : Boolean) : Boolean{
    return if(isRotated){
        view.animate().setDuration(200).rotation(0F)
        false
    } else{
        view.animate().setDuration(200).rotation(180F)
        true
    }
}

fun toggleViewExpansion(view:View, isExpanded:Boolean):Boolean{
    return if(isExpanded){
        collapseView(view)
        false
    } else{
        expandView(view)
        true
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
