package com.example.androidcreateprojecttest.data.db.entity

import com.google.firebase.firestore.PropertyName


data class Chat(
    @get:PropertyName("lastMessage") @set:PropertyName("lastMessage") var lastMessage: Message = Message(),
    @get:PropertyName("info") @set:PropertyName("info") var info: ChatInfo = ChatInfo()
)

data class ChatInfo(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = ""
)