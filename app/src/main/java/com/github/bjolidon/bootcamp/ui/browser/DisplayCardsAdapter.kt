package com.github.bjolidon.bootcamp.ui.browser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.bjolidon.bootcamp.R
import com.github.bjolidon.bootcamp.model.MagicCard

/**
 * Adapter for the recycler view that displays the cards.
 */
class DisplayCardsAdapter(private val cards: ArrayList<MagicCard>) :
    RecyclerView.Adapter<DisplayCardsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardName: TextView = itemView.findViewById(R.id.cardNameRecyclerViewTextView)
        val setName: TextView = itemView.findViewById(R.id.setRecyclerViewTextView)
        val cardImage: ImageView = itemView.findViewById(R.id.cardImageView)

    }

    /**
     * Create the format of the items
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cards_recycler_view_row, parent, false)
        return ViewHolder(view)
    }

    /**
     * Set the values of the items
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cardName.text = cards[position].name
        holder.cardImage.setImageResource(R.drawable.card)
        holder.setName.text = cards[position].set.toString()
    }

    /**
     * The number of items in the recycler view
     */
    override fun getItemCount(): Int {
        return cards.size
    }
}
