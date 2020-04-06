package com.br.queroajudar.view.adapters


import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.br.queroajudar.databinding.CauseItemBinding
import com.br.queroajudar.model.Cause
import timber.log.Timber


class CauseAdapter() : ListAdapter<Cause, CauseAdapter.ViewHolder>(CauseDiffCallback()) {
    var tracker: SelectionTracker<Long>? = null

    init{
        setHasStableIds(true)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)!!
        tracker?.let {
            Timber.tag("QA.CauseAdapter")
                .i("onBindViewHolder tracker isSelected ${it.isSelected(position.toLong())}")
            holder.bind(/*clickListener,*/ item, it.isSelected(item.id.toLong()))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id.toLong()
    }

    class ViewHolder private constructor(val binding: CauseItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(/*clickListener: CauseClickListener, */item: Cause, isSelected:Boolean = false) {
            binding.cause = item
            //binding.clickListener = clickListener
            itemView.isActivated = isSelected

            binding.executePendingBindings()
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long? = itemId
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

class CauseDetailsLookup(private val recyclerView: RecyclerView) :
    ItemDetailsLookup<Long>() {
    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        if (view != null) {
            return (recyclerView.getChildViewHolder(view) as CauseAdapter.ViewHolder)
                .getItemDetails()
        }
        return null
    }
}
