package com.br.queroajudar.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.br.queroajudar.databinding.LoadingItemBinding
import com.br.queroajudar.databinding.RecommendedOrganizationsListBinding
import com.br.queroajudar.databinding.VacancyItemBinding
import com.br.queroajudar.model.Vacancy
import com.br.queroajudar.network.QueroAjudarApiStatus

class VacancyAdapter(private val clickListener : VacancyClickListener) : PagedListAdapter<
        Vacancy, RecyclerView.ViewHolder>(VacancyDiffCallback()) {

    private var apiStatus = QueroAjudarApiStatus.LOADING

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_ORGANIZATIONS = 1
    private val VIEW_TYPE_PROGRESS = 2

    //private val adapterScope = CoroutineScope(Dispatchers.Default)



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ItemViewHolder -> {
                val item = getItem(position)!!
                holder.bind(clickListener, item)
            }
            is OrganizationListViewHolder -> {
                holder.bind()
            }
            is LoadingViewHolder -> {
                holder.bind(apiStatus)
            }

        }
//        if(holder is ItemViewHolder) {
//            val item = getItem(position)!!
//            holder.bind(clickListener, item)
//        }
//        else{
//            (holder as LoadingViewHolder).bind(apiStatus)
//        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType){
            VIEW_TYPE_ITEM -> ItemViewHolder.from(parent)
            VIEW_TYPE_ORGANIZATIONS -> OrganizationListViewHolder.from(parent)
            VIEW_TYPE_PROGRESS -> LoadingViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(apiStatus != QueroAjudarApiStatus.DONE && position == itemCount - 1){
            VIEW_TYPE_PROGRESS
        }
        else if(getItem(position) == null){
            VIEW_TYPE_ORGANIZATIONS
        }
        else {
            VIEW_TYPE_ITEM
        }
    }

    override fun getItem(position: Int): Vacancy? {

        return if(position == 5){
            null
        }
        else if(position < 5){
            super.getItem(position)
        }
        else{
            super.getItem(position - 1)
        }

//        return if(position == 0){
//            null
//        }
//        else{
//            super.getItem(position - 1)
//        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

//    override fun submitList(pagedList: PagedList<Vacancy>?) {
////        pagedList?.addWeakCallback(pagedList.snapshot(), object : PagedList.Callback(){
////            override fun onChanged(position: Int, count: Int) {
////                Timber.tag("QA.VacancyAdapter").i("onChanged")
////            }
////
////            override fun onInserted(position: Int, count: Int) {
////                Timber.tag("QA.VacancyAdapter").i("onInserted $count")
////            }
////
////            override fun onRemoved(position: Int, count: Int) {
////                Timber.tag("QA.VacancyAdapter").i("onRemoved")
////            }
////        })
////        Timber.tag("QA.VacancyAdapter").i("submitted ${pagedList?.size}" )
//        super.submitList(pagedList)
//    }

    fun setApiStatus(apiStatus : QueroAjudarApiStatus){
        this.apiStatus = apiStatus
    }

    class ItemViewHolder private constructor(private val binding: VacancyItemBinding)
        : RecyclerView.ViewHolder(binding.root){

        fun bind(clickListener: VacancyClickListener, item: Vacancy) {
            binding.vacancy = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = VacancyItemBinding.inflate(
                    layoutInflater, parent, false)
                return ItemViewHolder(binding)
            }
        }
    }

    class OrganizationListViewHolder private constructor(
        private val binding : RecommendedOrganizationsListBinding
    ) : RecyclerView.ViewHolder(binding.root){

        fun bind() {
            //TODO
        }

        companion object {
            fun from(parent: ViewGroup): OrganizationListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecommendedOrganizationsListBinding.inflate(
                    layoutInflater, parent, false)
                return OrganizationListViewHolder(binding)
            }
        }
    }

    class LoadingViewHolder private constructor(private val binding: LoadingItemBinding)
        : RecyclerView.ViewHolder(binding.root){

        fun bind(apiStatus: QueroAjudarApiStatus) {
            binding.apiStatus = apiStatus
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

class VacancyClickListener(val clickListener: (vacancyId: Int) -> Unit) {
    fun onClick(vacancy: Vacancy) = clickListener(vacancy.id)
}

//sealed class VacancyDataItem{
//    data class VacancyItem(val vacancy : Vacancy) : VacancyDataItem(){
//        override val id: Int
//            get() = vacancy.id
//    }
//    data class OrganizationsItem(val organizations : List<Organization>) : VacancyDataItem(){
//        override val id: Int
//            get() = Int.MIN_VALUE
//    }
//    abstract val id : Int
//}




