package com.example.amiibo.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.amiibo.R
import com.example.amiibo.model.Amiibo
import com.example.amiibo.viewmodel.AmiiboViewModel

class AmiiboAdapter(private var amiiboList: List<Amiibo>,
                    private var favouriteAmiibos: List<Amiibo>,
                    private val amiiboViewModel: AmiiboViewModel,
                    private val onSelectedAmiibosChanged: (List<Amiibo>) -> Unit) : RecyclerView.Adapter<AmiiboAdapter.AmiiboViewHolder>() {

    private val selectedAmiibos  = favouriteAmiibos.toMutableList()

    // ViewHolder para cada ítem en la lista
    inner class AmiiboViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val amiiboImage: ImageView =  itemView.findViewById(R.id.amiiboImage)
        val amiiboName: TextView = itemView.findViewById(R.id.amiiboName)
        val amiiboGameSeries: TextView = itemView.findViewById(R.id.amiiboGameSeries)
        val amiiboSeries: TextView = itemView.findViewById(R.id.amiiboSeries)
        val star : CheckBox = itemView.findViewById(R.id.favorito_checkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmiiboViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_amiibo, parent, false)
        return AmiiboViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AmiiboViewHolder, position: Int) {
        val amiibo = amiiboList[position]

        holder.amiiboName.text = amiibo.name
        holder.amiiboGameSeries.text = amiibo.gameSeries
        holder.amiiboSeries.text = if (amiibo.gameSeries != amiibo.amiiboSeries) amiibo.amiiboSeries else ""
        Glide.with(holder.itemView.context)
            .load(amiibo.image)
            .into(holder.amiiboImage)

        holder.star.setOnCheckedChangeListener(null)
        holder.star.isChecked = selectedAmiibos.contains(amiibo)
        holder.star.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (!selectedAmiibos.contains(amiibo)) selectedAmiibos.add(amiibo)
            } else {
                selectedAmiibos.remove(amiibo)
            }
            onSelectedAmiibosChanged(selectedAmiibos)
            amiiboViewModel.saveSelectedAmiibos(selectedAmiibos)
            notifyItemChanged(position)
        }
    }
    fun updateFavourites(newFavourites: List<Amiibo>) {
        selectedAmiibos.clear()
        selectedAmiibos.addAll(newFavourites)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = amiiboList.size

}
