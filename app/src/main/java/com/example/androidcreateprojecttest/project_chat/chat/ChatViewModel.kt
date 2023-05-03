package com.example.androidcreateprojecttest.project_chat.chat

import androidx.lifecycle.*
import com.example.androidcreateprojecttest.data.db.entity.Chat
import com.example.androidcreateprojecttest.data.db.entity.Message
import com.example.androidcreateprojecttest.data.db.entity.UserInfo
import com.example.androidcreateprojecttest.data.db.remote.FirebaseReferenceChildObserver
import com.example.androidcreateprojecttest.data.db.remote.FirebaseReferenceValueObserver
import com.example.androidcreateprojecttest.data.db.repository.DatabaseRepository
import com.example.androidcreateprojecttest.data.Result
import com.example.androidcreateprojecttest.project_chat.DefaultViewModel
import com.example.androidcreateprojecttest.project_chat.chats.ChatsViewModel
import com.example.androidcreateprojecttest.util.addNewItem

class ChatsViewModelFactory(private val myUserID: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatsViewModel(myUserID) as T
    }
}

class ChatViewModelFactory(private val myUserID: String, private val otherUserID: String, private val chatID: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatViewModel(myUserID, otherUserID, chatID) as T
    }
}

class ChatViewModel(private val myUserID: String, private val otherUserID: String, private val chatID: String) : DefaultViewModel() {

    private val dbRepository: DatabaseRepository = DatabaseRepository()

    private val _otherUser: MutableLiveData<UserInfo> = MutableLiveData()
    private val _addedMessage = MutableLiveData<Message>()

    private val fbRefMessagesChildObserver = FirebaseReferenceChildObserver()
    private val fbRefUserInfoObserver = FirebaseReferenceValueObserver()

    val messagesList = MediatorLiveData<MutableList<Message>>()
    val newMessageText = MutableLiveData<String>()
    val otherUser: LiveData<UserInfo> = _otherUser

    init {
        setupChat()
        checkAndUpdateLastMessageSeen()
    }

    override fun onCleared() {
        super.onCleared()
        fbRefMessagesChildObserver.clear()
        fbRefUserInfoObserver.clear()
    }

    private fun checkAndUpdateLastMessageSeen() {
        dbRepository.loadChat(chatID) { result: Result<Chat> ->
            if (result is Result.Success && result.data != null) {
                result.data.lastMessage.let {
                    if (!it.seen && it.senderID != myUserID) {
                        it.seen = true
                        dbRepository.updateChatLastMessage(chatID, it)
                    }
                }
            }
        }
    }

    private fun setupChat() {
        dbRepository.loadAndObserveUserInfo(otherUserID, fbRefUserInfoObserver) { result: Result<UserInfo> ->
            onResult(_otherUser, result)
            if (result is Result.Success && !fbRefMessagesChildObserver.isObserving()) {
                loadAndObserveNewMessages()
            }
        }
    }

    private fun loadAndObserveNewMessages() {
        messagesList.addSource(_addedMessage) { messagesList.addNewItem(it) }

        dbRepository.loadAndObserveMessagesAdded(
            chatID,
            fbRefMessagesChildObserver
        ) { result: Result<Message> ->
            onResult(_addedMessage, result)
        }
    }

    fun sendMessagePressed() {
        if (!newMessageText.value.isNullOrBlank()) {
            val newMsg = Message(myUserID, newMessageText.value!!)
            dbRepository.updateNewMessage(chatID, newMsg)
            dbRepository.updateChatLastMessage(chatID, newMsg)
            newMessageText.value = null
        }
    }
}