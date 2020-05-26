package com.br.queroajudar.vacancies

import com.br.queroajudar.databinding.RecommendedOrganizationItemBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.br.queroajudar.data.Organization

class RecommendedOrganizationsAdapter : ListAdapter<
        Organization, RecommendedOrganizationsAdapter.ViewHolder>(
    RecommendedOrganizationDiffCallback()
) {

    init {
        setHasStableIds(true)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)!!
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id.toLong()
    }

    class ViewHolder private constructor(val binding: RecommendedOrganizationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item:Organization) {
            binding.organization = item
            binding.executePendingBindings()
        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecommendedOrganizationItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }
}

class RecommendedOrganizationDiffCallback : DiffUtil.ItemCallback<Organization>() {

    override fun areItemsTheSame(oldItem: Organization, newItem: Organization): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Organization, newItem: Organization): Boolean {
        return oldItem == newItem
    }
}