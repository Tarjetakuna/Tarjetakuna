package com.github.sdp.tarjetakuna.ui.singlecard

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

import com.github.sdp.tarjetakuna.databinding.FragmentItemBinding
import com.github.sdp.tarjetakuna.model.User

/**
 * [RecyclerView.Adapter] that can display a [User].
 */
class UserRecyclerViewAdapter(
    private val users: List<User>
) : RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.idView.text = user.email
        holder.contentView.text = user.username
    }

    override fun getItemCount(): Int = users.size

    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}
