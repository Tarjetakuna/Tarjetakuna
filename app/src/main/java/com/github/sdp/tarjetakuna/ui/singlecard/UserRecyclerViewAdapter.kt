package com.github.sdp.tarjetakuna.ui.singlecard

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.databinding.FragmentUserAdapterBinding
import com.github.sdp.tarjetakuna.model.User

/**
 * [RecyclerView.Adapter] that can display a [User].
 */
class UserRecyclerViewAdapter(
    private val users: List<User>,
    private val currentUser: User?
) : RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder>() {

    /**
     * Interface for managing the click on a user-item of the recycler view
     */
    interface OnUserClickListener {
        fun onUserClick(user: User)
    }

    /**
     * Listener for the click on a user-item of the recycler view
     */
    var onUserClickListener: OnUserClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentUserAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.usernameView.text = user.username
        holder.kmView.text =
            holder.itemView.context.getString(
                R.string.single_card_distance,
                currentUser?.location?.distanceKmTo(user.location)?.toInt()?.toString() ?: "-"
            )
        holder.msgBtn.setOnClickListener {
            Log.w("UserRecyclerViewAdapter", "TODO: Implement message button")
            onUserClickListener?.onUserClick(user)
        }
    }

    override fun getItemCount(): Int = users.size

    inner class ViewHolder(binding: FragmentUserAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val usernameView: TextView = binding.userAdapterUsername
        val kmView: TextView = binding.userAdapterKmText
        val msgBtn: TextView = binding.userAdapterMessageButton
    }

}
