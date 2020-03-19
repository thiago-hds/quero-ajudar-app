package com.br.queroajudar.view.adapters

import android.content.Context
import android.graphics.Typeface
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.br.queroajudar.R
import com.br.queroajudar.databinding.CauseItemBinding
import com.br.queroajudar.model.Cause
import timber.log.Timber


class CauseAdapter(val clickListener : CauseClickListener) : ListAdapter<
        Cause, CauseAdapter.ViewHolder>(CauseDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)!!
        holder.bind(clickListener, item, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: CauseItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(clickListener: CauseClickListener, item: Cause, position:Int) {
            binding.cause = item
            binding.position = position
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CauseItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class CauseDiffCallback : DiffUtil.ItemCallback<Cause>() {

    override fun areItemsTheSame(oldItem: Cause, newItem: Cause): Boolean {
        Timber.tag("QueroAjudar").i("are items the same ${oldItem.id} ${oldItem.id == newItem.id}")
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: Cause, newItem: Cause): Boolean {
        Timber.tag("QueroAjudar").i("are contents the same ${oldItem.id} ${oldItem.selected} ${newItem.selected} ${oldItem == newItem}")
        return oldItem == newItem
    }
}

class CauseClickListener(val clickListener: (causeId: Int, position: Int) -> Unit) {
    fun onClick(cause: Cause, position : Int) = clickListener(cause.id, position)
}
