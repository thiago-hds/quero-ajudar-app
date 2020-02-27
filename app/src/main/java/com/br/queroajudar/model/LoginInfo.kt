package com.br.queroajudar.model


import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableField

import com.br.queroajudar.R

class LoginInfo () : BaseObservable(){
    var email : String = ""
    var password : String = ""
    var emailError = ObservableField<String>()
    var passwordError = ObservableField<String>()


//    fun setEmail(newEmail:String){
//        if(email != newEmail){
//            email = newEmail
//            //notifyPropertyChanged(BR.email)
//            Log.i("QueroAjudar", "Email changed: $email")
//        }
//    }
//
//    fun getEmail() : String{
//        return email
//    }
//
//    fun setPassword(newPassword:String){
//        if(password != newPassword){
//            password = newPassword
//            //notifyPropertyChanged(BR.valid)
//        }
//    }
//
//    fun getPassword() : String{
//        return password
//    }

    @Bindable
    fun isValid(): Boolean {
        var valid = isEmailValid(true)
        //valid = isPasswordValid(false) && valid
        return valid
    }

    private fun isEmailValid(setMessage : Boolean) : Boolean{
        if (email.length > 5) {
            val indexOfAt: Int = email.indexOf("@")
            val indexOfDot: Int = email.lastIndexOf(".")
            return if (indexOfAt in 1 until indexOfDot && indexOfDot < email.length - 1) {
                emailError.set(null)
                true
            } else {
                if (setMessage) emailError.set("1")
                false
            }
        }
        if (setMessage) emailError.set("2")
        return false
    }

    private fun isPasswordValid(){

    }

}