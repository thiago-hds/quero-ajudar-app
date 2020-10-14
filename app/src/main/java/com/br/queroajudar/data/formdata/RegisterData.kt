package com.br.queroajudar.data.formdata

import androidx.databinding.ObservableField
import com.squareup.moshi.Json
import javax.inject.Inject

class RegisterData @Inject constructor() : BaseFormData() {

    @Json(name="first_name")
    var firstName: String = ""
    @Json(name="last_name")
    var lastName: String = ""
    var email: String = ""
    var password: String? = ""

    @Transient
    var firstNameError = ObservableField<String>()
    @Transient
    var lastNameError = ObservableField<String>()
    @Transient
    var emailError = ObservableField<String>()
    @Transient
    var passwordError = ObservableField<String>()

    override fun resetErrorFields() {
        firstNameError.set("")
        lastNameError.set("")
        emailError.set("")
        passwordError.set("")
    }

    override fun getErrorFieldByName(name: String): ObservableField<String>? {
        return when(name){
            "first_name"        -> firstNameError
            "last_name"         -> lastNameError
            "email"             -> emailError
            "password"          -> passwordError
            else                -> null
        }
    }
}
