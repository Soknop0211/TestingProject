package com.example.androidcreateprojecttest.test_chat_project_copy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcreateprojecttest.R

class ChatAdapter(val chatMessages: List<ChatMessage>, val uid: String): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return chatMessages.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatMessage = chatMessages[position]

        if (chatMessage.user == uid) {
            holder.chatTextSent!!.text = String.format("%s \n %s", chatMessage.text, chatMessage.timestamp)
            holder.chatTextReceived!!.visibility = View.GONE
        } else {
            holder.chatTextReceived!!.text = String.format("%s \n %s", chatMessage.text, chatMessage.timestamp)
            holder.chatTextSent!!.visibility = View.GONE
        }
    }


    class ViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder(
        inflater.inflate(R.layout.list_item_chat, parent, false)) {

        var chatTextSent: TextView? = null
        var chatTextReceived: TextView? = null

        init {
            chatTextSent = itemView.findViewById(R.id.textview_chat_sent)
            chatTextReceived = itemView.findViewById(R.id.textview_chat_received)
        }

    }

}