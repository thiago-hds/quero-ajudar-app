package com.br.queroajudar.view.adapters

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.br.queroajudar.databinding.CauseItemBinding
import com.br.queroajudar.model.Cause


class CauseAdapter(val clickListener : CauseClickListener) : ListAdapter<
        Cause, CauseAdapter.ViewHolder>(CauseDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)!!
        holder.bind(clickListener, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: CauseItemBinding, val context : Context) : RecyclerView.ViewHolder(binding.root){

        fun bind(clickListener: CauseClickListener, item: Cause) {
//            val fontAwesome =
//                Typeface.createFromAsset(context.assets, "font/fontawesome.ttf")
//            item.iconClass = "&#xf01c;"
//            binding.causeItemTvIcon.typeface = fontAwesome
            binding.cause = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CauseItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, parent.context)
            }
        }
    }
}

class CauseDiffCallback : DiffUtil.ItemCallback<Cause>() {

    override fun areItemsTheSame(oldItem: Cause, newItem: Cause): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: Cause, newItem: Cause): Boolean {
        return oldItem == newItem
    }
}

class CauseClickListener(val clickListener: (causeId: Int) -> Unit) {
    fun onClick(cause: Cause) = clickListener(cause.id)
}
