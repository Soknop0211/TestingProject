package com.example.androidcreateprojecttest.test_chat_firebase

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

open class BaseActivity : AppCompatActivity() {

    private var documentReference : DocumentReference? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        val fireStore = FirebaseFirestore.getInstance()
        documentReference = fireStore.collection(Constant.KEY_COLLECTION_USERS)
            .document(PreferenceManager.getString(Constant.KEY_USER_ID, this))


    }

    override fun onPause() {
        super.onPause()
        // documentReference!!.update(Constant.KEY_AVAILABILITY, 0)
    }

    override fun onResume() {
        super.onResume()
        // documentReference!!.update(Constant.KEY_AVAILABILITY, 1)
    }
}