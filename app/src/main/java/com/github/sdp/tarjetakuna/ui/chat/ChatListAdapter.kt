package com.github.sdp.tarjetakuna.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.model.Chat
import com.github.sdp.tarjetakuna.model.User

class ChatListAdapter(val chats: ArrayList<Chat>, private val currentUser: User) :
    RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {


    /**
     * Interface for managing the click on a chat-item of the recycler view
     */
    interface OnChatClickListener {
        fun onClick(position: Int)
    }

    /**
     * Listener for the click on a chat-item of the recycler view
     */
    var onChatClickListener: OnChatClickListener? = null


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userIcon: ImageView = itemView.findViewById(R.id.chat_item_user_logo)
        val user: TextView = itemView.findViewById(R.id.chat_item_user)
        val lastMsgTime: TextView = itemView.findViewById(R.id.chat_item_time)
        val lastMsg: TextView = itemView.findViewById(R.id.chat_item_last_msg)
        val notifIcon: ImageView = itemView.findViewById(R.id.chat_item_notif)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // set the user name to other user
        if (chats[position].user1.username == currentUser.username) {
            holder.user.text = chats[position].user2.username
        } else {
            holder.user.text = chats[position].user1.username
        }

        // look for the last message and display it
        val lastMsg = chats[position].messages.maxByOrNull { it.timestamp }
        if (lastMsg != null) {
            holder.lastMsgTime.text = lastMsg.timestamp.toString()
            holder.lastMsg.text = lastMsg.content
        } else {
            holder.lastMsgTime.text = "no messages"
            holder.lastMsg.text = ""
        }

        // TODO set the user icon and notification icon
//        holder.notifIcon.setImageResource(R.drawable.ic_baseline_notifications_24)
//        holder.userIcon.setImageResource(R.drawable.ic_baseline_person_24)

        holder.itemView.setOnClickListener {
            onChatClickListener?.onClick(position)
        }
    }

}
