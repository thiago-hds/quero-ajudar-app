package com.br.queroajudar.organizations

import com.br.queroajudar.vacancies.RecommendedOrganizationsAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.AsyncPagedListDiffer
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.*
import com.br.queroajudar.databinding.NetworkStatusItemBinding
import com.br.queroajudar.databinding.RecommendedOrganizationsListBinding
import com.br.queroajudar.databinding.VacancyItemBinding
import com.br.queroajudar.data.Organization
import com.br.queroajudar.data.Vacancy
import com.br.queroajudar.databinding.OrganizationItemBinding
import com.br.queroajudar.network.ResultWrapper


class OrganizationsAdapter(private val clickListener : OrganizationClickListener) : PagedListAdapter<
        Organization, RecyclerView.ViewHolder>(OrganizationDiffCallback()) {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_STATUS = 1

    private var resultWrapper: ResultWrapper<Any?>

    init {
        resultWrapper = ResultWrapper.Loading
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ItemViewHolder -> {
                val item = getItem(position)!!
                holder.bind(clickListener, item)
            }
            is LoadingViewHolder -> {
                holder.bind(resultWrapper)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType){
            VIEW_TYPE_ITEM -> ItemViewHolder.from(
                parent
            )
            VIEW_TYPE_STATUS -> LoadingViewHolder.from(
                parent
            )
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(resultWrapper !is ResultWrapper.Success && position == itemCount){
            VIEW_TYPE_STATUS
        }
        else {
            VIEW_TYPE_ITEM
        }
    }


    fun setResultWrapper(resultWrapper: ResultWrapper<Any?>){
        this.resultWrapper = resultWrapper
    }

    class ItemViewHolder private constructor(private val binding: OrganizationItemBinding)
        : RecyclerView.ViewHolder(binding.root){

        fun bind(clickListener: OrganizationClickListener, item: Organization) {
            binding.organization = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = OrganizationItemBinding.inflate(
                    layoutInflater, parent, false)
                return ItemViewHolder(
                    binding
                )
            }
        }
    }

    class LoadingViewHolder private constructor(private val binding: NetworkStatusItemBinding)
        : RecyclerView.ViewHolder(binding.root){

        fun bind(resultWrapper: ResultWrapper<Any?>) {
            binding.resultWrapper = resultWrapper
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): LoadingViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NetworkStatusItemBinding.inflate(layoutInflater, parent, false)
                return LoadingViewHolder(
                    binding
                )
            }
        }
    }
}

class OrganizationDiffCallback : DiffUtil.ItemCallback<Organization>() {

    override fun areItemsTheSame(oldItem: Organization, newItem: Organization): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: Organization, newItem: Organization): Boolean {
        return oldItem == newItem
    }
}

class OrganizationClickListener(val clickListener: (organizationId: Int) -> Unit) {
    fun onClick(organization: Organization) = clickListener(organization.id)
}