package com.br.queroajudar.data.formdata

import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField

abstract class BaseFormData : BaseObservable(){

    fun setApiValidationErrors(errorsMap : Map<String,List<String>>){
        resetErrorFields()
        for((field, errors) in errorsMap){
            if(errors.isNotEmpty()) {
                var errorField = getErrorFieldByName(field);
                errorField?.set(errors[0])
            }
        }
    }

    abstract fun resetErrorFields()

    abstract fun getErrorFieldByName(name: String) : ObservableField<String>?
}