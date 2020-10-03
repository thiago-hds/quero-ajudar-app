package com.br.queroajudar.data.formdata

import androidx.databinding.ObservableField
import com.squareup.moshi.Json
import javax.inject.Inject


class VacancyApplicationData @Inject constructor() : BaseFormData() {

    @Json(name="vacancy_id")
    var vacancyId: Int = 0
    @Json(name="volunteer_phone")
    var phone: String = ""
    @Json(name="volunteer_email")
    var email: String = ""
    @Json(name="volunteer_message")
    var message: String = ""

    @Transient
    var phoneError = ObservableField<String>()
    @Transient
    var emailError = ObservableField<String>()
    @Transient
    var messageError = ObservableField<String>()

    override fun resetErrorFields() {
        phoneError.set("")
        emailError.set("")
        messageError.set("")
    }

    override fun getErrorFieldByName(name: String): ObservableField<String>? {
        return when(name){
            "phone"     -> phoneError
            "email"     -> emailError
            "message"   -> messageError
            else        -> null
        }
    }
}