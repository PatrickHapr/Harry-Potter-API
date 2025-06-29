package com.example.harrypotterapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SpellsAdapter(
    private val onSpellClick: (Spell) -> Unit
) : RecyclerView.Adapter<SpellsAdapter.SpellViewHolder>() {

    private var spells = listOf<Spell>()

    fun updateSpells(newSpells: List<Spell>) {
        spells = newSpells
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpellViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_spell, parent, false)
        return SpellViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpellViewHolder, position: Int) {
        holder.bind(spells[position])
    }

    override fun getItemCount() = spells.size

    inner class SpellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvSpellName: TextView = itemView.findViewById(R.id.tvSpellName)

        fun bind(spell: Spell) {
            tvSpellName.text = spell.name
            itemView.setOnClickListener { onSpellClick(spell) }
        }
    }
}