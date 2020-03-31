package com.br.queroajudar.view.adapters

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.br.queroajudar.databinding.SkillItemBinding
import com.br.queroajudar.model.Skill
import timber.log.Timber


class SkillAdapter() : ListAdapter<
        Skill, SkillAdapter.ViewHolder>(SkillDiffCallback()) {
    var tracker: SelectionTracker<Long>? = null

    init{
        setHasStableIds(true)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)!!
        tracker?.let {
            Timber.tag("QA.SkillAdapter").i("onBindViewHolder tracker isSelected ${it.isSelected(position.toLong())}")
            holder.bind(/*clickListener,*/ item, it.isSelected(position.toLong()))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    class ViewHolder private constructor(val binding: SkillItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(/*clickListener: SkillClickListener,*/ item: Skill, isSelected:Boolean = false) {
            binding.skill = item
            //binding.clickListener = clickListener
            itemView.isActivated = false
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
                val binding = SkillItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class SkillDiffCallback : DiffUtil.ItemCallback<Skill>() {

    override fun areItemsTheSame(oldItem: Skill, newItem: Skill): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: Skill, newItem: Skill): Boolean {
        return oldItem == newItem
    }
}

//class SkillClickListener(val clickListener: (skillId: Int) -> Unit) {
//    fun onClick(skill: Skill) = clickListener(skill.id)
//}

class SkillDetailsLookup(private val recyclerView: RecyclerView) :
    ItemDetailsLookup<Long>() {
    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        if (view != null) {
            return (recyclerView.getChildViewHolder(view) as SkillAdapter.ViewHolder)
                .getItemDetails()
        }
        return null
    }
}