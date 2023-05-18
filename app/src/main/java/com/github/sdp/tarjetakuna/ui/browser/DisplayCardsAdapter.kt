package com.github.sdp.tarjetakuna.ui.browser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.model.MagicCard
import com.github.sdp.tarjetakuna.utils.CustomGlide

/**
 * Adapter for the recycler view that displays the cards.
 */
class DisplayCardsAdapter(
    private val contextFragment: Fragment,
    val cardsWithQuantities: ArrayList<Pair<MagicCard, Int>>
) :
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
        val cardQuantity: TextView = itemView.findViewById(R.id.quantityRecyclerViewTextView)
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
        holder.cardName.text = cardsWithQuantities[position].first.name
        // TODO change the image directly from the url (when web API available)
        //holder.cardImage.setImageResource(R.drawable.card)
        CustomGlide.loadDrawable(
            contextFragment,
            cardsWithQuantities[position].first.imageUrl
        ) {
            holder.cardImage.setImageDrawable(it)
        }
        holder.setInfo.text = cardsWithQuantities[position].first.set.name
        if (cardsWithQuantities[position].second > 1) {
            holder.cardQuantity.visibility = View.VISIBLE
            val quantityText = cardsWithQuantities[position].second.toString() + "X"
            holder.cardQuantity.text = quantityText
        } else {
            holder.cardQuantity.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            onCardClickListener?.onCardClick(position)
        }
    }

    /**
     * The number of items in the recycler view
     */
    override fun getItemCount(): Int {
        return cardsWithQuantities.size
    }
}
