package com.br.queroajudar.util

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.br.queroajudar.R
import com.br.queroajudar.model.Vacancy
import com.br.queroajudar.network.QueroAjudarApiStatus
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("error")
fun setError(textLayout: TextInputLayout, strOrResId: Any?) {
    if (strOrResId is Int) {
        textLayout.error = textLayout.context.getString((strOrResId as Int?)!!)
    } else {
        textLayout.error = strOrResId as String?
    }
}

@BindingAdapter("organizationNameFormatted")
fun TextView.setOrganizationNameFormatted(name : String?){
    name?.let {
        text = convertOrganizationNameToFormatted(name)
    }
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        Glide.with(imgView.context)
            .load(imgUrl)
            .into(imgView)
    }
}

@BindingAdapter("apiStatus")
fun bindApiStatus(statusImageView: ImageView, status: QueroAjudarApiStatus?) {
    when (status) {
        QueroAjudarApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_navigate_next_black_24dp)
        }
        QueroAjudarApiStatus.NETWORK_ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_person_black_24dp)
        }
        QueroAjudarApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}

@BindingAdapter("pbApiStatus")
fun bindApiStatusProgressBar(progressBar : ProgressBar, status:QueroAjudarApiStatus?){
    if(status == QueroAjudarApiStatus.LOADING){
        progressBar.visibility = View.VISIBLE
    }
    else{
        progressBar.visibility = View.GONE
    }
}