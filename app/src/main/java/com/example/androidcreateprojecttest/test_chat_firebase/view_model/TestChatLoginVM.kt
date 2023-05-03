package com.example.androidcreateprojecttest.test_chat_firebase.view_model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidcreateprojecttest.data.Event
import com.example.androidcreateprojecttest.data.model.Login
import com.example.androidcreateprojecttest.test_chat_firebase.Constant
import com.example.androidcreateprojecttest.test_chat_firebase.PreferenceManager
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TestChatLoginVM @Inject constructor(
    private val fireStore : FirebaseFirestore,
    private val mContext : Context) :   // can use private val mContext : Application
    ViewModel() {
    private val _isLoggedInEvent = MutableLiveData<Event<String>>()

    fun login(login : Login) {
        val fireStore = FirebaseFirestore.getInstance()
        fireStore.collection(Constant.KEY_COLLECTION_USERS)
            .whereEqualTo(Constant.KEY_EMAIL, login.email)
            .whereEqualTo(Constant.KEY_PASSWORD, login.password)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && it.result != null && it.result.documentChanges.size > 0) {
                    val documentSnapshot = it.result.documents[0]

                    PreferenceManager.saveBoolean(Constant.KEY_IS_SIGNED_IN, true, mContext)
                    PreferenceManager.saveString(Constant.KEY_USER_ID, documentSnapshot.id, mContext)
                    PreferenceManager.saveString(Constant.KEY_NAME, documentSnapshot.get(Constant.KEY_NAME) as String?, mContext)
                    PreferenceManager.saveString(Constant.KEY_EMAIL, documentSnapshot.get(Constant.KEY_EMAIL) as String?, mContext)
                    PreferenceManager.saveString(
                        Constant.KEY_PASSWORD, documentSnapshot.get(
                            Constant.KEY_PASSWORD) as String?, mContext)
                    PreferenceManager.saveString(Constant.KEY_IMAGE, documentSnapshot.get(Constant.KEY_IMAGE) as String?, mContext)
                    _isLoggedInEvent.value = Event("You have already sign in your account.")
                } else {
                    _isLoggedInEvent.value = Event("You have already sign in your account.")
                }
            }
            .addOnFailureListener {
                _isLoggedInEvent.value = Event(it.message.toString())
            }
    }
}