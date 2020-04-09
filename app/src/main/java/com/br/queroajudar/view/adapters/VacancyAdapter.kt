package com.br.queroajudar.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.br.queroajudar.databinding.LoadingItemBinding
import com.br.queroajudar.databinding.VacancyItemBinding
import com.br.queroajudar.model.Vacancy
import com.br.queroajudar.network.QueroAjudarApiStatus

class VacancyAdapter(private val clickListener : VacancyClickListener) : PagedListAdapter<
        Vacancy, RecyclerView.ViewHolder>(VacancyDiffCallback()) {

    private var apiStatus = QueroAjudarApiStatus.LOADING
    private val VIEW_TYPE_PROGRESS = 0
    private val VIEW_TYPE_ITEM = 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ItemViewHolder) {
            val item = getItem(position)!!
            holder.bind(clickListener, item)
        }
        else{
            (holder as LoadingViewHolder).bind()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == VIEW_TYPE_ITEM) {
            ItemViewHolder.from(parent)
        } else{
            LoadingViewHolder.from(parent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(apiStatus != QueroAjudarApiStatus.DONE && position == itemCount - 1){
            VIEW_TYPE_PROGRESS
        } else {
            VIEW_TYPE_ITEM
        }
    }

    fun setApiStatus(apiStatus : QueroAjudarApiStatus){
        this.apiStatus = apiStatus
    }

    class ItemViewHolder private constructor(val binding: VacancyItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(clickListener: VacancyClickListener, item: Vacancy) {
            binding.vacancy = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = VacancyItemBinding.inflate(layoutInflater, parent, false)
                return ItemViewHolder(binding)
            }
        }
    }

    class LoadingViewHolder private constructor(val binding: LoadingItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind() {
            //TODO exibir texto informando de erro de rede ao carregar mais items
            
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): LoadingViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LoadingItemBinding.inflate(layoutInflater, parent, false)
                return LoadingViewHolder(binding)
            }
        }
    }
}

class VacancyDiffCallback : DiffUtil.ItemCallback<Vacancy>() {

    override fun areItemsTheSame(oldItem: Vacancy, newItem: Vacancy): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: Vacancy, newItem: Vacancy): Boolean {
        return oldItem == newItem
    }
}

//class VacancyListScrollListener(
//    val onEndReached : () -> Unit,
//    val layoutManager : LinearLayoutManager
//) : RecyclerView.OnScrollListener() {
//
//    private val visibleThreshold = 8
//
//    private var visibleItemCount = 0
//    private var totalItemCount = 0
//    private var firstVisibleItemPosition = 0
//
//    private var previousTotalItemCount = 0
//
//    private var loading = false
//
//    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//        super.onScrolled(recyclerView, dx, dy)
//
//        //lista foi rolada
//        if(dy > 0){
//            visibleItemCount = recyclerView.childCount
//            totalItemCount = layoutManager.itemCount
//            firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
//        }
//
//        //if(loading){
////            if(totalItemCount > previousTotalItemCount){
////                loading = false
////                previousTotalItemCount = totalItemCount
////            }
//        //}
//        //else{
//        if(loading && (totalItemCount > previousTotalItemCount)){
//            loading = false
//            previousTotalItemCount = totalItemCount
//        }
//
//        if(!loading && (firstVisibleItemPosition + visibleItemCount  + visibleThreshold) >= totalItemCount){
//            loading = true
//            onEndReached()
//        }
////            if(totalItemCount - visibleItemCount <= firstVisibleItemPosition + visibleThreshold ){
////                onEndReached()
////                loading = true
////            }
//        //}
//    }
//
//}

class VacancyClickListener(val clickListener: (vacancyId: Int) -> Unit) {
    fun onClick(vacancy: Vacancy) = clickListener(vacancy.id)
}




