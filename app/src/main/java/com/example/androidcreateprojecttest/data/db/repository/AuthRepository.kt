package com.example.androidcreateprojecttest.data.db.repository

import android.util.Log
import com.example.androidcreateprojecttest.data.Result
import com.example.androidcreateprojecttest.data.db.remote.FirebaseAuthSource
import com.example.androidcreateprojecttest.data.db.remote.FirebaseAuthStateObserver
import com.example.androidcreateprojecttest.data.model.CreateNewUser
import com.example.androidcreateprojecttest.data.model.CreateUser
import com.example.androidcreateprojecttest.data.model.Login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository {
    private val firebaseAuthService = FirebaseAuthSource()

    fun observeAuthState(stateObserver: FirebaseAuthStateObserver, b: ((Result<FirebaseUser>) -> Unit)){
        firebaseAuthService.attachAuthStateObserver(stateObserver,b)
    }

    fun loginUser(login: Login, b: ((Result<FirebaseUser>) -> Unit)) {
        b.invoke(Result.Loading)
        firebaseAuthService.loginWithEmailAndPassword(login).addOnSuccessListener {
            b.invoke(Result.Success(it.user))
        }.addOnFailureListener {
            b.invoke(Result.Error(msg = it.message))
        }
    }

    fun createUser(createUser: CreateUser, b: ((Result<FirebaseUser>) -> Unit)) {
        b.invoke(Result.Loading)
        firebaseAuthService.createUser(createUser).addOnSuccessListener {
            b.invoke(Result.Success(it.user))
        }.addOnFailureListener {
            b.invoke(Result.Error(msg = it.message))
        }
    }

    fun logoutUser() {
        firebaseAuthService.logout()
    }

    // Use FirebaseFireStore
    private val fireStore = FirebaseFirestore.getInstance()
    fun createNewUser(createUser: CreateNewUser, b: ((Result<String>) -> Unit)) {
        b.invoke(Result.Loading)
        fireStore.collection("UserProfile").add(createUser).addOnSuccessListener {
            b.invoke(Result.Success(it.id))
        }.addOnFailureListener {
            b.invoke(Result.Error(msg = it.message))
        }
    }
    fun loginExistUser(login: Login, b: ((Result<CreateNewUser>) -> Unit)) {
        val collection: CollectionReference = fireStore.collection("UserProfile")
        val query = collection.whereEqualTo("email", login.email)

        val testUser = FirebaseAuth.getInstance().currentUser //getting the current logged in users id

        if (testUser != null){
            val userUid = testUser.uid
            val uidInput = userUid
        }


        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Results", task.result.toString())
                val user = task.result.toObjects(CreateNewUser::class.java)
                if (user.isNotEmpty()){
                    for (mUser in user){
                        if (mUser.password == login.password){
                            b.invoke(Result.Success(mUser, msg = "Congratulations"))
                        } else {
                            b.invoke(Result.Error(msg = "Invalid Password"))
                        }
                    }
                } else {
                    b.invoke(Result.Error(msg = "Invalid Email"))
                }
            } else {
                b.invoke(Result.Error(msg = "Firebase Error"))
            }
        }
    }

}