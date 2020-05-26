package com.br.queroajudar.data.formdata



import androidx.databinding.Bindable
import androidx.databinding.ObservableField

class LoginData () : BaseFormData(){
    var email : String = ""
    var password : String = ""
    @Transient
    var emailError = ObservableField<String>()
    @Transient
    var passwordError = ObservableField<String>()

    @Bindable
    fun isValid(): Boolean {
        var valid = isEmailValid()
        valid = isPasswordValid() && valid
        return valid
    }

    private fun isEmailValid() : Boolean{
        if (email.length > 5) {
            val indexOfAt: Int = email.indexOf("@")
            val indexOfDot: Int = email.lastIndexOf(".")
            return if (indexOfAt in 1 until indexOfDot && indexOfDot < email.length - 1) {
                emailError.set(null)
                true
            } else {
                emailError.set("1")
                false
            }
        }
        emailError.set("2")
        return false
    }

    private fun isPasswordValid() : Boolean{
        return password.length in 6 until 10
    }

    override fun getErrorFieldByName(name : String) : ObservableField<String>? {
        return when(name){
            "email"     -> emailError
            "password"  -> passwordError
            else        -> null
        }
    }

}