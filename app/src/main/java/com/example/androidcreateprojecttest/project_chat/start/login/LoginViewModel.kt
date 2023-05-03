package com.example.androidcreateprojecttest.project_chat.start.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.androidcreateprojecttest.data.model.Login
import com.example.androidcreateprojecttest.data.db.repository.AuthRepository
import com.example.androidcreateprojecttest.project_chat.DefaultViewModel
import com.example.androidcreateprojecttest.data.Event
import com.example.androidcreateprojecttest.data.Result
import com.example.androidcreateprojecttest.data.model.CreateNewUser
import com.example.androidcreateprojecttest.util.isEmailValid
import com.example.androidcreateprojecttest.util.isTextValid
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository : AuthRepository) : DefaultViewModel() {

    private val _isLoggedInEvent = MutableLiveData<Event<CreateNewUser>>()

    val isLoggedInEvent: LiveData<Event<CreateNewUser>> = _isLoggedInEvent
    val emailText = MutableLiveData<String>() // Two way
    val passwordText = MutableLiveData<String>() // Two way
    val isLoggingIn = MutableLiveData<Boolean>() // Two way

    private fun login() {
        isLoggingIn.value = true
        val login = Login(emailText.value!!, passwordText.value!!)

//        authRepository.loginUser(login) { result: Result<FirebaseUser> ->
//            onResult(null, result)
//            if (result is Result.Success) _isLoggedInEvent.value = Event(result.data!!)
//            if (result is Result.Success || result is Result.Error) isLoggingIn.value = false
//        }
        authRepository.loginExistUser(login) { result: Result<CreateNewUser> ->
            onResult(null, result)
            if (result is Result.Success) _isLoggedInEvent.value = Event(result.data!!)

            if (result is Result.Success || result is Result.Error) isLoggingIn.value = false
        }
    }

    fun loginPressed() {
        if (!isEmailValid(emailText.value.toString())) {
            mSnackBarText.value = Event("Invalid email format")
            return
        }
        if (!isTextValid(6, passwordText.value)) {
            mSnackBarText.value = Event("Password is too short")
            return
        }

        login()
    }
}