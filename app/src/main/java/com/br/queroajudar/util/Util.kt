package com.br.queroajudar.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.br.queroajudar.R
import com.br.queroajudar.data.Vacancy
import com.br.queroajudar.network.ResultWrapper
import kotlinx.coroutines.Dispatchers

object Constants {
    const val VIEW_TYPE_ITEM = 1
    const val VIEW_TYPE_LOADING = 2
    const val PAGE_SIZE = 10

    //vacancy_type
    const val RECURRENT = "recurrent"
    const val UNIQUE_EVENT = "unique_event"

    //periodicity
    const val DAILY = "daily"
    const val WEEKLY = "weekly"
    const val MONTHLY = "monthly"

    //unit_per_period
    const val HOURS = "hours"
    const val DAYS = "days"

    //location_type
    const val ORGANIZATION_ADDRESS = "organization_address"
    const val SPECIFIC_ADDRESS = "specific_address"
    const val REMOTE = "remote"
    const val NEGOTIABLE = "negotiable"

    //category_type
    const val CAUSE_TYPE = 1
    const val SKILL_TYPE = 2

    //register viewmodel
    const val REGISTER_MODE = 1
    const val EDIT_MODE = 2
}

    fun convertOrganizationNameToFormatted(name: String): String {
        //TODO implementar funcao
        return name
    }

    fun formatPhoneNumber(phone: String): String{
        return "(${phone.substring(0,2)}) ${phone.substring(2)}"
    }

//operator fun <T> MutableLiveData<ArrayList<T>>.plusAssign(values: List<T>) {
//    val value = this.value ?: arrayListOf()
//    value.addAll(values)
//    this.value = value
//}

    /**
     * The database serves as the single source of truth.
     * Therefore UI can receive data updates from database only.
     * Function notify UI about:
     * [Result.Status.SUCCESS] - with data from database
     * [Result.Status.ERROR] - if error has occurred from any source
     * [Result.Status.LOADING]
     */
//fun <T, A> resultLiveData(databaseQuery: () -> LiveData<T>,
//                          networkCall: suspend () -> Result<A>,
//                          saveCallResult: suspend (A) -> Unit): LiveData<Result<T>> =
//    liveData(Dispatchers.IO) {
//        emit(Result.loading<T>())
//        val source = databaseQuery.invoke().map { Result.success(it) }
//        emitSource(source)
//
//        val responseStatus = networkCall.invoke()
//        if (responseStatus.status == SUCCESS) {
//            saveCallResult(responseStatus.data!!)
//        } else if (responseStatus.status == ERROR) {
//            emit(Result.error<T>(responseStatus.message!!))
//            emitSource(source)
//        }
//    }

    fun <T> MutableLiveData<MutableList<T>>.updateItemOnPosition(
        position: Int, newItem: T
    ) {
        val value = this.value ?: mutableListOf()
        if (position >= 0 && position < value.size) {
            value[position] = newItem
            this.value = value
        }
    }

    fun <T> MutableLiveData<MutableList<T>>.append(newItens: List<T>) {
        val value = this.value ?: mutableListOf()
        value.addAll(newItens)
        this.value = value
    }

    fun SelectionTracker<Long>.enable() {
        this.select(-1)
    }

    fun SelectionTracker<Long>.disable() {
        this.clearSelection()
    }

    fun <T> resultLiveData(networkCall: suspend () -> ResultWrapper<T>): LiveData<ResultWrapper<T>> =
        liveData(Dispatchers.IO) {

            emit(ResultWrapper.Loading)

            val responseStatus = networkCall.invoke()
            emit(responseStatus)
        }


    fun showNetworkErrorMessage(context: Context?){
        Toast.makeText(context, R.string.error_connection, Toast.LENGTH_LONG).show()
    }

    fun saveApiToken(token: String){
        QueroAjudarPreferences.apiToken = token
    }

    fun clearApiToken(){
        QueroAjudarPreferences.apiToken = null
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }