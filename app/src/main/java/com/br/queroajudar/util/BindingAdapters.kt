package com.br.queroajudar.util

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.databinding.BindingAdapter
import com.br.queroajudar.R
import com.br.queroajudar.network.ResultWrapper
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


@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    Glide.with(imgView.context)
        .load(imgUrl)
        .centerCrop()
        .placeholder(R.color.colorBackgroundGrey)
        .error(R.drawable.ic_deal_orange)
        .into(imgView)
}

@BindingAdapter("phones")
fun bindPhones(txtView: TextView, phones: List<String>?){
    phones?.let {phones->
        var text = ""
        for (phone in phones) {
            text += formatPhoneNumber(phone) + "\n"
        }
        txtView.text = text
    }
}

@BindingAdapter("resultWrapperLoading")
fun bindResultWrapperLoading(view : View, wrapper: ResultWrapper<Any>?){
    Timber.i("bindResultWrapperLoading $wrapper")
    wrapper?.let {
        if (it is ResultWrapper.Loading) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }
}

@BindingAdapter("resultWrapperNetworkError", "isUserAction")
fun bindResultWrapperNetworkError(view : View, wrapper: ResultWrapper<Any>?, isUserAction: Boolean){
    Timber.i("resultWrapperNetworkError $wrapper")
    wrapper?.let {
        if (it is ResultWrapper.NetworkError && !isUserAction) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }
}

@BindingAdapter("resultWrapperNetworkStatusOverlay", "isUserAction")
fun bindResultWrapperNetworkOverlay(view : View, wrapper: ResultWrapper<Any>?, isUserAction: Boolean){
    Timber.i("resultWrapperNetworkStatusOverlay $wrapper")

    wrapper?.let {
        if ((it is ResultWrapper.NetworkError && !isUserAction) || it is ResultWrapper.Loading) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }
}

@BindingAdapter("resultWrapperSizeZero")
fun bindResultWrapperSizeZero(tv : TextView, wrapper: ResultWrapper<Any>?){
    Timber.i("resultWrapperSizeZero $wrapper")

    wrapper?.let {
        if(it is ResultWrapper.Success && it.value is List<*> && it.value.size == 0){
            tv.visibility = View.VISIBLE
        }
        else {
            tv.visibility = View.GONE
        }
    }
}

@Suppress("DEPRECATION")
@BindingAdapter("htmlText")
fun setHtmlTextValue(textView: TextView, htmlText: String?) {
    if (htmlText == null) return
    val result: Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(htmlText)
    }
    textView.text = result
}

@BindingAdapter("filterItemSelected")
fun setFilterItemSelected(textView: TextView, isSelected : Boolean){
    Timber.i("filterItemSelected $isSelected")
    if(isSelected){
        textView.setTextColor(getColor(textView.context, R.color.colorWhite))
        textView.setBackgroundResource(R.drawable.appcolor_round_shape)
    }
}

