package com.example.androidcreateprojecttest.project_chat.start.createAccount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidcreateprojecttest.data.Event
import com.example.androidcreateprojecttest.data.Result
import com.example.androidcreateprojecttest.data.db.repository.AuthRepository
import com.example.androidcreateprojecttest.data.model.CreateNewUser
import com.example.androidcreateprojecttest.project_chat.DefaultViewModel
import com.example.androidcreateprojecttest.util.isEmailValid
import com.example.androidcreateprojecttest.util.isTextValid
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    private val authRepository : AuthRepository
) : DefaultViewModel() {

    private val mIsCreatedEvent = MutableLiveData<Event<String>>()

    val isCreatedEvent: LiveData<Event<String>> = mIsCreatedEvent

    val displayNameText = MutableLiveData<String>() // Two way
    val emailText = MutableLiveData<String>() // Two way
    val phoneText = MutableLiveData<String>() // Two way
    val passwordText = MutableLiveData<String>() // Two way

    val isCreatingAccount = MutableLiveData<Boolean>()

    private fun createNewUser() {
        isCreatingAccount.value = true
        val createUser = CreateNewUser(userName = displayNameText.value!!, email = emailText.value!!, phone = phoneText.value!!, password = passwordText.value!!)
        authRepository.createNewUser(createUser) { result: Result<String> ->
            onResult(null, result)
            if (result is Result.Success) {
                mIsCreatedEvent.value = Event(result.data!!)
            }
            if (result is Result.Success || result is Result.Error) isCreatingAccount.value = false
        }

    }

    fun createAccountPressed() {
        if (!isTextValid(2, displayNameText.value)) {
            mSnackBarText.value = Event("Display name is too short")
            return
        }

        if (!isEmailValid(emailText.value.toString())) {
            mSnackBarText.value = Event("Invalid email format")
            return
        }

        if (!isTextValid(9, phoneText.value.toString())) {
            mSnackBarText.value = Event("Invalid phone format")
            return
        }

        if (!isTextValid(6, passwordText.value)) {
            mSnackBarText.value = Event("Password is too short")
            return
        }

        createNewUser()
    }
}