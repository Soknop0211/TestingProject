package com.example.androidcreateprojecttest.data.model

import com.example.androidcreateprojecttest.data.db.entity.Chat
import com.example.androidcreateprojecttest.data.db.entity.UserInfo

data class ChatWithUserInfo(
    var mChat: Chat,
    var mUserInfo: UserInfo
)
