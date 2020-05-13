package com.br.queroajudar.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.br.queroajudar.network.ResultWrapper
import kotlinx.coroutines.Dispatchers

object Constants {
    const val VIEW_TYPE_ITEM    = 1
    const val VIEW_TYPE_LOADING = 2
    const val PAGE_SIZE         = 10
}

fun convertOrganizationNameToFormatted(name : String) : String{
    //TODO implementar funcao
    return name
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
    position:Int, newItem : T) {
    val value = this.value ?: mutableListOf()
    if(position >= 0 && position < value.size){
        value[position] = newItem
        this.value = value
    }
}

fun <T> MutableLiveData<MutableList<T>>.append(newItens : List<T>) {
    val value = this.value ?: mutableListOf()
    value.addAll(newItens)
    this.value = value
}

fun SelectionTracker<Long>.enable(){
    this.select(-1)
}

fun SelectionTracker<Long>.disable(){
    this.clearSelection()
}

fun <T> resultLiveData(networkCall: suspend () -> ResultWrapper<T>) : LiveData<ResultWrapper<T>> =
    liveData(Dispatchers.IO) {

        emit(ResultWrapper.Loading)

        val responseStatus = networkCall.invoke()
        emit(responseStatus)
    }

