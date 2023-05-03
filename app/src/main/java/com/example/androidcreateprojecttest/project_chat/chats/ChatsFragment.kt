package com.example.androidcreateprojecttest.project_chat.chats

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.androidcreateprojecttest.R
import com.example.androidcreateprojecttest.App
import com.example.androidcreateprojecttest.data.model.ChatWithUserInfo
import com.example.androidcreateprojecttest.data.EventObserver
import com.example.androidcreateprojecttest.databinding.FragmentChatsBinding
import com.example.androidcreateprojecttest.project_chat.chat.ChatsViewModelFactory
import com.example.androidcreateprojecttest.ui.MainActivity
import com.newapp.test_firebase_app.ui.chat.ChatFragment
import com.newapp.test_firebase_app.util.convertTwoUserIDs

class ChatsFragment : Fragment() {

    private val viewModel: ChatsViewModel by viewModels { ChatsViewModelFactory(App.myUserID) }
    private lateinit var viewDataBinding: FragmentChatsBinding
    private lateinit var listAdapter: ChatsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewDataBinding =
            FragmentChatsBinding.inflate(inflater, container, false).apply { viewmodel = viewModel }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupListAdapter()
        setupObservers()

        startActivity(Intent(context, MainActivity::class.java))

    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            listAdapter = ChatsListAdapter(viewModel)
            viewDataBinding.chatsRecyclerView.adapter = listAdapter
        } else {
            throw Exception("The viewmodel is not initialized")
        }
    }

    private fun setupObservers() {
        viewModel.selectedChat.observe(viewLifecycleOwner,
            EventObserver { navigateToChat(it) })
    }

    private fun navigateToChat(chatWithUserInfo: ChatWithUserInfo) {
        val bundle = bundleOf(
            ChatFragment.ARGS_KEY_USER_ID to App.myUserID,
            ChatFragment.ARGS_KEY_OTHER_USER_ID to chatWithUserInfo.mUserInfo.id,
            ChatFragment.ARGS_KEY_CHAT_ID to convertTwoUserIDs(App.myUserID, chatWithUserInfo.mUserInfo.id)
        )
        findNavController().navigate(R.id.action_navigation_chats_to_chatFragment, bundle)
    }
}