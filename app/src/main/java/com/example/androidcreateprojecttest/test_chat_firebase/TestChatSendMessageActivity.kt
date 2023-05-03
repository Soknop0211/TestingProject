package com.example.androidcreateprojecttest.test_chat_firebase

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.androidcreateprojecttest.data.model.Users
import com.example.androidcreateprojecttest.databinding.ActivityTestChatSendMessageBinding
import com.example.androidcreateprojecttest.test_chat_firebase.adapter.TestChatSendReceiveAdapter
import com.example.androidcreateprojecttest.test_chat_project_copy.ChatMessageModel
import com.example.androidcreateprojecttest.util.displaySnackBar
import com.example.androidcreateprojecttest.util.progressBar
import com.example.androidcreateprojecttest.util.showSnackBar
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class TestChatSendMessageActivity : BaseActivity() {

    private lateinit var binding : ActivityTestChatSendMessageBinding

    private lateinit var testChatSendReceiveAdapter: TestChatSendReceiveAdapter
    private var chatList = ArrayList<ChatMessageModel>()
    private lateinit var fireStore : FirebaseFirestore
    private lateinit var users : Users

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestChatSendMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkUser()

        initList()

        setViewListeners()

        showListMessage()
    }

    private fun setViewListeners() {
        binding.btnBack.setOnClickListener { finish() }

        binding.buttonSend.setOnClickListener { sendChatMessage() }
    }

    private fun initList() {
        binding.listChat.layoutManager = LinearLayoutManager(this)

        testChatSendReceiveAdapter = TestChatSendReceiveAdapter(chatList, PreferenceManager.getString(Constant.KEY_USER_ID, this))
        binding.listChat.adapter = testChatSendReceiveAdapter

        fireStore = FirebaseFirestore.getInstance()
    }

    private fun showListMessage() {
        // Sender
        fireStore.collection(Constant.KEY_COLLECTION_CHAT)
            .whereEqualTo(
                Constant.KEY_SENDER_ID,
                PreferenceManager.getString(Constant.KEY_USER_ID, this)
            )
            .whereEqualTo(Constant.KEY_RECEIVER_ID, users.userId)
            .addSnapshotListener(eventListener)

        // Receiver
        fireStore.collection(Constant.KEY_COLLECTION_CHAT)
            .whereEqualTo(Constant.KEY_SENDER_ID, users.userId)
            .whereEqualTo(
                Constant.KEY_RECEIVER_ID,
                PreferenceManager.getString(Constant.KEY_USER_ID, this)
            )
            .addSnapshotListener(eventListener)
    }

    @SuppressLint("NotifyDataSetChanged")
    private val eventListener = EventListener<QuerySnapshot> { value, error ->
        if (error != null) return@EventListener

        if (value != null) {
            val count = chatList.size
            for (item in value.documentChanges) {
                if (item.type == DocumentChange.Type.ADDED) {
                    val chatMessageModel = ChatMessageModel(
                        senderId = item.document.getString(Constant.KEY_SENDER_ID),
                        receiverId = item.document.getString(Constant.KEY_RECEIVER_ID),
                        message = item.document.getString(Constant.KEY_MESSAGE),
                        dateTime = readDateSt(item.document.getDate(Constant.KEY_TIMESTAMP)),
                        dateObj = item.document.getDate(Constant.KEY_TIMESTAMP)
                    )
                    chatList.add(chatMessageModel)
                }

                chatList.sortBy { item.document.getDate(Constant.KEY_TIMESTAMP) }

                if (count == 0) {
                    testChatSendReceiveAdapter.notifyDataSetChanged()
                } else {
                    testChatSendReceiveAdapter.notifyItemRangeInserted(chatList.size , chatList.size)
                    binding.listChat.smoothScrollToPosition(chatList.size - 1)
                }
                binding.listChat.visibility = View.VISIBLE
            }
            binding.progress.progressBar(false)
        }
    }

    private fun sendNotification() {

    }

    private fun readDateSt (date: Date?) : String{
        return SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date!!)
    }

    private fun sendChatMessage() {
        val message = binding.edittextChat.text.toString()
        if (message.isEmpty())  {
            binding.buttonSend.showSnackBar("Please Enter Message . . .")
            return
        }

        binding.edittextChat.setText("")

        val hashMapSendMsg = HashMap<String, Any>()
        hashMapSendMsg[Constant.KEY_SENDER_ID] =
            PreferenceManager.getString(Constant.KEY_USER_ID, this)
        hashMapSendMsg[Constant.KEY_RECEIVER_ID] = users.userId
        hashMapSendMsg[Constant.KEY_MESSAGE] = message
        hashMapSendMsg[Constant.KEY_TIMESTAMP] = Date()

        fireStore.collection(Constant.KEY_COLLECTION_CHAT)
            .add(hashMapSendMsg)
            .addOnSuccessListener {
                binding.progress.progressBar(false)
                binding.buttonSend.isEnabled = true

            }
            .addOnFailureListener {
                binding.progress.progressBar(false)
                binding.buttonSend.isEnabled = true
                it.message?.let { it1 -> binding.edittextEmail.displaySnackBar(it1) }
            }
    }

    private fun checkUser() {
        users = Users()
        if (intent != null && intent.hasExtra("users")) {
            users = intent.getSerializableExtra("users") as Users
            binding.edittextName.text = users.name
            binding.edittextEmail.text = users.email
            val image = users.image
            if (image != null)  Glide.with(binding.profileImage).load(image).into(binding.profileImage)
        }
    }

}