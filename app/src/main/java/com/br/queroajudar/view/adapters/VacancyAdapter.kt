package com.br.queroajudar.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.br.queroajudar.R
import com.br.queroajudar.model.Vacancy
import com.google.android.material.button.MaterialButton

class VacancyAdapter : RecyclerView.Adapter<VacancyAdapter.ViewHolder>() {
    var data = listOf<Vacancy>()
    set(value){
        field = value
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        val res = holder.itemView.context.resources

        holder.tvName.text = item.name
        //holder.btnOrganization.text = item.
        holder.tvType.text = item.type
        //holder. //TODO
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.vacancy_item, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val ivImage : ImageView = itemView.findViewById(R.id.vacancyItem_iv_image)
        val tvName : TextView = itemView.findViewById(R.id.vacancyItem_tv_name)
        val btnOrganization : MaterialButton = itemView.findViewById(R.id.vacancyItem_btn_organization)
        val tvLocation : TextView = itemView.findViewById(R.id.vacancyItem_tv_location)
        val tvType : TextView = itemView.findViewById(R.id.vacancyItem_tv_type)
    }
}

