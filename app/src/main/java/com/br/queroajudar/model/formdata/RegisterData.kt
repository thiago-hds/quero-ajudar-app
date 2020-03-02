package com.br.queroajudar.model.formdata

import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField
import com.squareup.moshi.Json

class RegisterData () : BaseFormData() {

    @Json(name="first_name")
    var firstName: String = ""
    @Json(name="last_name")
    var lastName: String = ""
    @Json(name="date_of_birth")
    var dateOfBirth: String = ""
    var email: String = ""
    var password: String = ""
    @Json(name="password_confirm")
    var passwordConfirm: String = ""

    @Transient
    var firstNameError = ObservableField<String>()
    @Transient
    var lastNameError = ObservableField<String>()
    @Transient
    var dateOfBirthError = ObservableField<String>()
    @Transient
    var emailError = ObservableField<String>()
    @Transient
    var passwordError = ObservableField<String>()
    @Transient
    var passwordConfirmError = ObservableField<String>()

    override fun getErrorFieldByName(name: String): ObservableField<String>? {
        return when(name){
            "first_name"        -> firstNameError
            "last_name"         -> lastNameError
            "date_of_birth"     -> dateOfBirthError
            "email"             -> emailError
            "password"          -> passwordError
            "password_confirm"  -> passwordConfirmError
            else                -> null
        }
    }
}
