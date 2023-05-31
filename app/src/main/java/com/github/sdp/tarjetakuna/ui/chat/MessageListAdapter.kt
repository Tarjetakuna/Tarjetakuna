package com.github.sdp.tarjetakuna.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.sdp.tarjetakuna.R
import com.github.sdp.tarjetakuna.model.Chat
import com.github.sdp.tarjetakuna.model.User
import java.text.DateFormat

class MessageListAdapter(val chat: Chat, private val currentUser: User) :
    RecyclerView.Adapter<MessageListAdapter.ViewHolder>() {
    
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val message: TextView = itemView.findViewById(R.id.msg_content_text)
        val time: TextView = itemView.findViewById(R.id.msg_timestamp_text)
        val date: TextView = itemView.findViewById(R.id.msg_date_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chat.messages.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.message.text = chat.messages[position].content
        holder.time.text =
            DateFormat.getTimeInstance(DateFormat.SHORT).format(chat.messages[position].timestamp)
        holder.date.text = DateFormat.getDateInstance().format(chat.messages[position].timestamp)
    }

    override fun getItemViewType(position: Int): Int {
        return if (chat.messages[position].sender.uid == currentUser.uid) {
            R.layout.message_sent
        } else {
            R.layout.message_received
        }
    }


}
