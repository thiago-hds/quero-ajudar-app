package com.br.queroajudar.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.br.queroajudar.databinding.VacancyItemBinding
import com.br.queroajudar.model.Vacancy
import timber.log.Timber

class VacancyAdapter(val clickListener : VacancyClickListener) : ListAdapter<
    Vacancy, VacancyAdapter.ViewHolder>(VacancyDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)!!
        holder.bind(clickListener, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: VacancyItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(clickListener: VacancyClickListener, item: Vacancy) {
            binding.vacancy = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = VacancyItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
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

class VacancyListScrollListener(
    val onEndReached : () -> Unit,
    val layoutManager : LinearLayoutManager
) : RecyclerView.OnScrollListener() {

    private val visibleThreshold = 8

    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var firstVisibleItemPosition = 0

    private var previousTotalItemCount = 0

    private var loading = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        //lista foi rolada
        if(dy > 0){
            visibleItemCount = recyclerView.childCount
            totalItemCount = layoutManager.itemCount
            firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        }

        //if(loading){
//            if(totalItemCount > previousTotalItemCount){
//                loading = false
//                previousTotalItemCount = totalItemCount
//            }
        //}
        //else{
        if(loading && (totalItemCount > previousTotalItemCount)){
            loading = false
            previousTotalItemCount = totalItemCount
        }

        if(!loading && (firstVisibleItemPosition + visibleItemCount  + visibleThreshold) >= totalItemCount){
            loading = true
            onEndReached()
        }
//            if(totalItemCount - visibleItemCount <= firstVisibleItemPosition + visibleThreshold ){
//                onEndReached()
//                loading = true
//            }
        //}
    }

}

class VacancyClickListener(val clickListener: (vacancyId: Int) -> Unit) {
    fun onClick(vacancy: Vacancy) = clickListener(vacancy.id)
}



