package com.br.queroajudar.model.formdata

import androidx.databinding.BaseObservable
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import com.google.android.material.textfield.TextInputLayout

abstract class BaseFormData : BaseObservable(){

    fun setApiValidationErrors(errorsMap : Map<String,List<String>>){
        for((field, errors) in errorsMap){
            if(errors.isNotEmpty()) {
                var errorField = getErrorFieldByName(field);
                errorField?.set(errors[0])
            }
        }
    }

    abstract fun getErrorFieldByName(name: String) : ObservableField<String>?
}