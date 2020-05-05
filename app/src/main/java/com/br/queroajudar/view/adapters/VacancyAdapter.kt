package com.br.queroajudar.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.AsyncPagedListDiffer
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.*
import com.br.queroajudar.databinding.LoadingItemBinding
import com.br.queroajudar.databinding.RecommendedOrganizationsListBinding
import com.br.queroajudar.databinding.VacancyItemBinding
import com.br.queroajudar.model.Organization
import com.br.queroajudar.model.Vacancy
import com.br.queroajudar.network.ApiStatus


class VacancyAdapter(private val clickListener : VacancyClickListener) : PagedListAdapter<
        Vacancy, RecyclerView.ViewHolder>(VacancyDiffCallback()) {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_ORGANIZATIONS = 1
    private val VIEW_TYPE_PROGRESS = 2

    private val ORGANIZATIONS_LIST_POSITION = 5

    private var organizations = listOf<Organization>()
    private var apiStatus = ApiStatus.LOADING

    private var differ : AsyncPagedListDiffer<Vacancy>

    init {

        val adapterCallback = AdapterListUpdateCallback(this)
        differ = AsyncPagedListDiffer(
            object : ListUpdateCallback {
                override fun onChanged(position: Int, count: Int, payload: Any?) {
                    adapterCallback.onChanged(position + 1, count, payload);
                }

                override fun onMoved(fromPosition: Int, toPosition: Int) {
                    adapterCallback.onMoved(fromPosition + 1, toPosition + 1);
                }

                override fun onInserted(position: Int, count: Int) {
                    adapterCallback.onInserted(position + 1, count);
                }

                override fun onRemoved(position: Int, count: Int) {
                    adapterCallback.onRemoved(position + 1, count);
                }
            },
            AsyncDifferConfig.Builder(VacancyDiffCallback()).build()
        )
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ItemViewHolder -> {
                val item = getItem(position)!!
                holder.bind(clickListener, item)
            }
            is OrganizationListViewHolder -> {
                holder.bind(organizations)
            }
            is LoadingViewHolder -> {
                holder.bind(apiStatus)
            }
        }
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
        return if(apiStatus != ApiStatus.DONE && position == itemCount - 1){
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
        return if(position == ORGANIZATIONS_LIST_POSITION){
            null
        }
        else{
            differ.getItem(toRealPosition(position))
        }
    }

    override fun submitList(pagedList: PagedList<Vacancy>?) {
        differ.submitList(pagedList)
    }

    override fun getCurrentList(): PagedList<Vacancy?>? {
        return differ.getCurrentList()
    }

    override fun getItemCount(): Int {
        return differ.itemCount + 1
    }

    fun setOrganizations(organizations : List<Organization>){
        this.organizations = organizations
    }

    fun setApiStatus(apiStatus : ApiStatus){
        this.apiStatus = apiStatus
    }

    private fun toRealPosition(position: Int): Int {
        return if (position < ORGANIZATIONS_LIST_POSITION) position else position - 1
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

        fun bind(organizations : List<Organization>) {
            val adapter = RecommendedOrganizationsAdapter()
            binding.rvOrganizations.adapter = adapter
            adapter.submitList(organizations)
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

        fun bind(apiStatus: ApiStatus) {
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