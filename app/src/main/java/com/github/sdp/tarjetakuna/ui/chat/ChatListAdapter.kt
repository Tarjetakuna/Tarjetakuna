package com.github.sdp.tarjetakuna.ui.chat

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.model.Chat
import com.github.sdp.tarjetakuna.model.Message
import com.github.sdp.tarjetakuna.model.User
import java.text.DateFormat

class ChatListAdapter(val chats: List<Chat>, private val currentUser: User) :
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
        val userIcon: ImageView = itemView.findViewById(R.id.chat_user_logo)
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
        setUserName(holder, position)

        // look for the last message and display it
        val lastMsg = chats[position].messages.maxByOrNull { it.timestamp }
        setLastMessage(holder, lastMsg)

        // update the notification icon
        setNotificationIcon(holder, position, lastMsg)

        // TODO set the user icon
//        holder.userIcon.setImageResource(R.drawable.ic_baseline_person_24)

        // set the onClick listener
        holder.itemView.setOnClickListener {
            onChatClickListener?.onClick(position)
        }
    }

    private fun setUserName(holder: ViewHolder, position: Int) {
        if (chats[position].user1.username == currentUser.username) {
            holder.user.text = chats[position].user2.username
        } else {
            holder.user.text = chats[position].user1.username
        }
    }

    private fun setLastMessage(holder: ViewHolder, lastMsg: Message?) {
        if (lastMsg != null) {
            holder.lastMsgTime.text = DateFormat.getDateInstance().format(lastMsg.timestamp)
            holder.lastMsg.text = lastMsg.content
        } else {
            holder.lastMsgTime.text = ""
            holder.lastMsg.text = Application().getString(R.string.chat_list_no_message)
        }
    }

    private fun setNotificationIcon(holder: ViewHolder, position: Int, lastMsg: Message?) {
        val lastRead = if (chats[position].user1.username == currentUser.username) {
            chats[position].user1LastRead;
        } else {
            chats[position].user2LastRead;
        }

        if (lastMsg != null && lastMsg.timestamp.after(lastRead)) {
            holder.notifIcon.visibility = View.VISIBLE
        } else {
            holder.notifIcon.visibility = View.INVISIBLE
        }
    }

}
