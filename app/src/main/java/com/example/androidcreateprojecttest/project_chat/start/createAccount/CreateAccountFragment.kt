package com.example.androidcreateprojecttest.project_chat.start.createAccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.androidcreateprojecttest.R
import com.example.androidcreateprojecttest.data.EventObserver
import com.example.androidcreateprojecttest.databinding.FragmentCreateAccountBinding
import com.example.androidcreateprojecttest.project_chat.main.MainActivity
import com.example.androidcreateprojecttest.util.SharedPreferencesUtil
import com.example.androidcreateprojecttest.util.forceHideKeyboard
import com.example.androidcreateprojecttest.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateAccountFragment : Fragment() {

    private val viewModel by viewModels<CreateAccountViewModel>()
    private lateinit var viewDataBinding: FragmentCreateAccountBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewDataBinding = FragmentCreateAccountBinding.inflate(inflater, container, false)
            .apply { viewmodel = viewModel }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupObservers()
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupObservers() {
        viewModel.dataLoading.observe(viewLifecycleOwner,
            EventObserver { (activity as MainActivity).showGlobalProgressBar(it) })

        viewModel.snackBarText.observe(viewLifecycleOwner,
            EventObserver { text ->
                view?.showSnackBar(text)
                view?.forceHideKeyboard()
            })

        viewModel.isCreatedEvent.observe(viewLifecycleOwner, EventObserver {
            SharedPreferencesUtil.saveUserID(requireContext(), it)
            navigateToChats()
            view?.showSnackBar("You already created new account success fully !")
            view?.forceHideKeyboard()
        })
    }

    private fun navigateToChats() {
        findNavController().navigate(R.id.action_createAccountFragment_to_navigation_chats)
    }
}