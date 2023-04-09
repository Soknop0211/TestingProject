package com.example.androidcreateprojecttest.project_chat.start.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnSuccessListener
import com.newapp.test_firebase_app.data.model.Login
import com.example.androidcreateprojecttest.data.db.repository.AuthRepository
import com.newapp.test_firebase_app.ui.DefaultViewModel
import com.example.androidcreateprojecttest.data.Event
import com.example.androidcreateprojecttest.data.Result
import com.newapp.test_firebase_app.util.isEmailValid
import com.newapp.test_firebase_app.util.isTextValid
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.example.androidcreateprojecttest.project_chat.main.MainActivity
import java.lang.reflect.Type

class LoginViewModel : DefaultViewModel() {

    private val authRepository = AuthRepository()
    private val _isLoggedInEvent = MutableLiveData<Event<FirebaseUser>>()

    val isLoggedInEvent: LiveData<Event<FirebaseUser>> = _isLoggedInEvent
    val emailText = MutableLiveData<String>() // Two way
    val passwordText = MutableLiveData<String>() // Two way
    val isLoggingIn = MutableLiveData<Boolean>() // Two way

    private fun getListItems() : MainActivity.Users {
        val mArrayList: ArrayList<Type> = ArrayList()
        var users = MainActivity.Users()
        val TAG = "hjkllkjhghjkljhghjk"
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        db.collection("UserProfile").get()
            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { documentSnapshots ->
                if (documentSnapshots.isEmpty) {
                    Log.d(TAG, "onSuccess: LIST EMPTY")
                    return@OnSuccessListener
                } else {
                    for (documentSnapshot in documentSnapshots) {
                        if (documentSnapshot.exists()) {
                            Log.d(TAG, "onSuccess: DOCUMENT" + documentSnapshot.id + " ; " + documentSnapshot.data)
                            val userList: MainActivity.Users = documentSnapshot.toObject(
                                MainActivity.Users::class.java)
                            Log.d("jeeeeeeeeeeee", userList.phone.toString())
                            if (emailText.value == userList.email){
                                Log.d("jeeeeeeeeeeee", "Success")
                            } else {
                                Log.d("jeeeeeeeeeeee", "Failed")
                            }
//                            val documentReference1 =
//                                FirebaseFirestore.getInstance().document("email")
//                            documentReference1.get().addOnSuccessListener { documentSnapshot ->
//                                val type: Type? = documentSnapshot.toObject(Type::class.java)
//                                Log.d(
//                                    TAG,
//                                    "onSuccess: " + type.toString()
//                                )
//                                mArrayList.add(type!!)
//                                Log.d(TAG, "onSuccess: $mArrayList")
//                                /* these logs here display correct data but when
//                                                             I log it in onCreate() method it's empty*/
//                            }
                        }
                    }
                }
            }).addOnFailureListener {
                Log.d(TAG, "Failed: Login")
            }

        return users
    }

    private fun login() {
        isLoggingIn.value = true
        val login = Login(emailText.value!!, passwordText.value!!)
        getListItems()

        authRepository.loginUser(login) { result: Result<FirebaseUser> ->
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