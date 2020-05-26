package com.br.queroajudar.vacancies

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
import com.br.queroajudar.network.ResultWrapper


class VacancyAdapter(private val clickListener : VacancyClickListener) : PagedListAdapter<
        Vacancy, RecyclerView.ViewHolder>(VacancyDiffCallback()) {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_ORGANIZATIONS = 1
    private val VIEW_TYPE_STATUS = 2

    private val ORGANIZATIONS_LIST_POSITION = 5

    private var displayOrganizations = false

    private var organizations = listOf<Organization>()
    private var resultWrapper: ResultWrapper<Any?>

    private var differ : AsyncPagedListDiffer<Vacancy>

    init {
        resultWrapper = ResultWrapper.Loading

        val adapterCallback = AdapterListUpdateCallback(this)
        differ = AsyncPagedListDiffer(
            object : ListUpdateCallback {
                override fun onChanged(position: Int, count: Int, payload: Any?) {
                    if(displayOrganizations)
                        adapterCallback.onChanged(position + 1, count, payload);
                    else
                        adapterCallback.onChanged(position,count,payload)
                }

                override fun onMoved(fromPosition: Int, toPosition: Int) {
                    if(displayOrganizations)
                        adapterCallback.onMoved(fromPosition + 1, toPosition + 1);
                    else
                        adapterCallback.onMoved(fromPosition, toPosition);
                }

                override fun onInserted(position: Int, count: Int) {
                    if(displayOrganizations)
                        adapterCallback.onInserted(position + 1, count);
                    else{
                        adapterCallback.onInserted(position, count);
                    }
                }

                override fun onRemoved(position: Int, count: Int) {
                    if(displayOrganizations) {
                        adapterCallback.onRemoved(position + 1, count);
                    }
                    else{
                        adapterCallback.onRemoved(position, count);
                    }
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
                holder.bind(resultWrapper)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType){
            VIEW_TYPE_ITEM -> ItemViewHolder.from(
                parent
            )
            VIEW_TYPE_ORGANIZATIONS -> OrganizationListViewHolder.from(
                parent
            )
            VIEW_TYPE_STATUS -> LoadingViewHolder.from(
                parent
            )
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(resultWrapper !is ResultWrapper.Success && position == itemCount - 1){
            VIEW_TYPE_STATUS
        }
        else if(getItem(position) == null){
            VIEW_TYPE_ORGANIZATIONS
        }
        else {
            VIEW_TYPE_ITEM
        }
    }

    override fun getItem(position: Int): Vacancy? {
        return if(displayOrganizations && position == ORGANIZATIONS_LIST_POSITION){
            null
        }
        else{
            differ.getItem(toRealPosition(position))
        }
    }

    private fun toRealPosition(position: Int): Int {
        return if (displayOrganizations && position > ORGANIZATIONS_LIST_POSITION) position - 1
        else position
    }

    override fun submitList(pagedList: PagedList<Vacancy>?) {
        differ.submitList(pagedList)
    }

    override fun getCurrentList(): PagedList<Vacancy?>? {
        return differ.getCurrentList()
    }

    override fun getItemCount(): Int {
        return if(displayOrganizations) differ.itemCount + 1 else differ.itemCount
    }

    fun setOrganizations(organizations : List<Organization>){
        this.organizations = organizations
    }

    fun setResultWrapper(resultWrapper: ResultWrapper<Any?>){
        this.resultWrapper = resultWrapper
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
                return ItemViewHolder(
                    binding
                )
            }
        }
    }

    class OrganizationListViewHolder private constructor(
        private val binding : RecommendedOrganizationsListBinding
    ) : RecyclerView.ViewHolder(binding.root){

        fun bind(organizations : List<Organization>) {
            val adapter =
                RecommendedOrganizationsAdapter()
            binding.rvOrganizations.adapter = adapter
            adapter.submitList(organizations)
        }

        companion object {
            fun from(parent: ViewGroup): OrganizationListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecommendedOrganizationsListBinding.inflate(
                    layoutInflater, parent, false)
                return OrganizationListViewHolder(
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