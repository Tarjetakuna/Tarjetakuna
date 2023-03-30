package com.github.sdp.tarjetakuna.ui.browser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.model.MagicCard
import com.github.sdp.tarjetakuna.utils.CustomGlide

/**
 * Adapter for the recycler view that displays the cards.
 */
class DisplayCardsAdapter(val cards: ArrayList<MagicCard>) :
    RecyclerView.Adapter<DisplayCardsAdapter.ViewHolder>() {

    /**
     * Interface for managing the click on a card-item of the recycler view
     */
    interface OnCardClickListener {
        fun onCardClick(position: Int)
    }

    /**
     * Listener for the click on a card-item of the recycler view
     */
    var onCardClickListener: OnCardClickListener? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardName: TextView = itemView.findViewById(R.id.cardNameRecyclerViewTextView)
        val setInfo: TextView = itemView.findViewById(R.id.setRecyclerViewTextView)
        val cardImage: ImageView = itemView.findViewById(R.id.cardImageView)
    }

    /**
     * Create the format of the items
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cards_recycler_view_row, parent, false)
        return ViewHolder(view)
    }

    /**
     * Set the items
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cardName.text = cards[position].name
        // TODO change the image directly from the url (when web API available)

        Glide.with(holder.cardImage.context)
            .load(cards[position].imageUrl)
            .into(holder.cardImage)
        holder.setInfo.text = cards[position].set.toString()
        holder.itemView.setOnClickListener {
            onCardClickListener?.onCardClick(position)
        }
    }

    /**
     * The number of items in the recycler view
     */
    override fun getItemCount(): Int {
        return cards.size
    }
}
