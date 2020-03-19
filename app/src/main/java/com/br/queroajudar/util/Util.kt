package com.br.queroajudar.util

import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

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

fun <T> MutableLiveData<MutableList<T>>.updateItemOnPosition(
    position:Int, newItem : T) {
    val value = this.value ?: mutableListOf()
    if(position >= 0 && position < value.size){
        value[position] = newItem
        this.value = value
    }
}

//fun <T, VH : RecyclerView.ViewHolder> ListAdapter<T, VH>.updateList(list: List<T>?) {
//    // ListAdapter<>.submitList() contains (stripped):
//    //  if (newList == mList) {
//    //      // nothing to do
//    //      return;
//    //  }
//    this.submitList(if (list == this.currentList) list.toList() else list)
//}