package com.example.myassssmentapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView adapter for displaying a list of entities as summaries.
 *
 * Each entity is a Map<String, Any>. We show up to two fields per item,
 * joined by “ - ”. Clicking an item invokes the provided callback
 * with the full entity map.
 */
class EntityAdapter(
    private val data: List<Map<String, Any>>,
    private val onItemClick: (Map<String, Any>) -> Unit
) : RecyclerView.Adapter<EntityAdapter.EntityViewHolder>() {

    /**
     * ViewHolder holding the summary TextView.
     */
    inner class EntityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val summaryTextView: TextView = itemView.findViewById(R.id.summaryTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityViewHolder {
        // Inflate the item layout
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_entity, parent, false)
        return EntityViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntityViewHolder, position: Int) {
        val entity = data[position]

        // Build a summary string from up to two entries
        val summary = if (entity.isNotEmpty()) {
            entity.entries
                .take(2)
                .joinToString(" - ") { (key, value) ->
                    "$key: ${value.toString()}"
                }
        } else {
            // Fallback when the map has no entries
            "No data available"
        }

        holder.summaryTextView.text = summary

        // Notify listener when clicked
        holder.itemView.setOnClickListener {
            onItemClick(entity)
        }
    }

    override fun getItemCount(): Int = data.size
}
