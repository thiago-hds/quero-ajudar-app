package com.br.queroajudar.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.br.queroajudar.R
import com.br.queroajudar.databinding.VacancyItemBinding
import com.br.queroajudar.model.Vacancy
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton

class VacancyAdapter : ListAdapter<Vacancy, VacancyAdapter.ViewHolder>(VacancyDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: VacancyItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Vacancy) {
            binding.vacancy = item
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


