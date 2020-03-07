package com.br.queroajudar.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.br.queroajudar.model.Vacancy
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