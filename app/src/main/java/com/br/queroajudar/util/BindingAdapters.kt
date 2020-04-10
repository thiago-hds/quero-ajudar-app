package com.br.queroajudar.util

//import com.br.queroajudar.R

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import androidx.databinding.BindingAdapter
import com.br.queroajudar.R
import com.br.queroajudar.network.QueroAjudarApiStatus
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import timber.log.Timber


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

//@BindingAdapter("ivApiStatus")
//fun bindApiStatusImageView(statusImageView: ImageView, status: QueroAjudarApiStatus?) {
//    when (status) {
//        QueroAjudarApiStatus.LOADING -> {
//            statusImageView.visibility = View.VISIBLE
//            statusImageView.setImageResource(R.drawable.ic_navigate_next_black_24dp)
//        }
//        QueroAjudarApiStatus.NETWORK_ERROR -> {
//            statusImageView.visibility = View.VISIBLE
//            statusImageView.setImageResource(R.drawable.ic_person_black_24dp)
//        }
//        QueroAjudarApiStatus.DONE -> {
//            statusImageView.visibility = View.GONE
//        }
//    }
//}

@BindingAdapter("pbApiStatus")
fun bindApiStatusProgressBar(progressBar : ProgressBar, status:QueroAjudarApiStatus?){
    if(status == QueroAjudarApiStatus.LOADING){
        progressBar.visibility = View.VISIBLE
    }
    else{
        progressBar.visibility = View.GONE
    }
}

@BindingAdapter("apiStatusLoading")
fun bindApiStatusLoading(view : View, status:QueroAjudarApiStatus){
    Timber.tag("QA.BindingAdapters").i("bindApiStatusLoading $status")
    if (status == QueroAjudarApiStatus.LOADING) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}

@BindingAdapter("apiStatusNetworkError")
fun bindApiStatusNetworkError(view : View, status:QueroAjudarApiStatus){
    Timber.tag("QA.BindingAdapters").i("bindApiStatusNetworkError $status")
    if (status == QueroAjudarApiStatus.NETWORK_ERROR) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}

//@BindingAdapter("loadingViewApiStatus")
//fun bindApiStatusViewGroup(view: FrameLayout, status: QueroAjudarApiStatus){
//    if(status == QueroAjudarApiStatus.LOADING){
//        view.visibility = View.VISIBLE
//    }
//    else{
//        view.visibility = View.GONE
//    }
//}

@Suppress("DEPRECATION")
@BindingAdapter("htmlText")
fun setHtmlTextValue(textView: TextView, htmlText: String?) {
    if (htmlText == null) return
    val result: Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(htmlText)
    }

    Timber.tag("QA.html").i("$result")
    textView.text = result
}

@BindingAdapter("filterItemSelected")
fun setFilterItemSelected(textView: TextView, isSelected : Boolean){
    Timber.tag("QA.BindingAdapters").i("filterItemSelected $isSelected")
    if(isSelected){
        textView.setTextColor(getColor(textView.context, R.color.colorWhite))
        textView.setBackgroundResource(R.drawable.appcolor_round_shape)
    }
}