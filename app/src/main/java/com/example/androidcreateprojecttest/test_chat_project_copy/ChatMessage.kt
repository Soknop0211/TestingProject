package com.example.androidcreateprojecttest.test_chat_project_copy

import java.util.*

data class ChatMessage(
    val text: String,
    val user: String,
    val timestamp: Date
)


data class ChatMessageModel(
    val senderId: String?,
    val receiverId: String?,
    val message: String?,
    var dateTime: String?,
    var dateObj: Date?
) : java.io.Serializable