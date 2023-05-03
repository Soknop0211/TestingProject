package com.example.androidcreateprojecttest.test_chat_firebase.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcreateprojecttest.R
import com.example.androidcreateprojecttest.test_chat_project_copy.ChatMessageModel


class TestChatSendReceiveAdapter(private val chatMessages: List<ChatMessageModel>, val senderId: String): RecyclerView.Adapter<TestChatSendReceiveAdapter.ViewHolder>() {

    val VIEW_TYPE_SEND = 1
    val VIEW_TYPE_RECEIVED = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return chatMessages.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (chatMessages[position].senderId == senderId) {
            VIEW_TYPE_SEND
        } else {
            VIEW_TYPE_RECEIVED
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatMessage = chatMessages[position]
        if (getItemViewType(position) == VIEW_TYPE_SEND) {
            holder.chatTextSent!!.text = chatMessage.message
            holder.textview_chat_sent_date!!.text = chatMessage.dateTime
            holder.chatTextReceived!!.visibility = View.GONE
            holder.textview_chat_received_date!!.visibility = View.GONE
        } else {
            holder.chatTextReceived!!.text = chatMessage.message
            holder.textview_chat_received_date!!.text = chatMessage.dateTime
            holder.chatTextSent!!.visibility = View.GONE
            holder.textview_chat_sent_date!!.visibility = View.GONE

        }
    }


    class ViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder(
        inflater.inflate(R.layout.list_item_chat, parent, false)) {

        var chatTextSent: TextView? = null
        var chatTextReceived: TextView? = null
        var textview_chat_sent_date: TextView? = null
        var textview_chat_received_date: TextView? = null

        init {
            textview_chat_received_date = itemView.findViewById(R.id.textview_chat_received_date)
            textview_chat_sent_date = itemView.findViewById(R.id.textview_chat_sent_date)
            chatTextSent = itemView.findViewById(R.id.textview_chat_sent)
            chatTextReceived = itemView.findViewById(R.id.textview_chat_received)
        }

    }

}