package com.example.androidcreateprojecttest.project_chat.main

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.androidcreateprojecttest.R
import com.example.androidcreateprojecttest.data.db.remote.FirebaseDataSource
import com.example.androidcreateprojecttest.util.forceHideKeyboard
import com.google.android.gms.tasks.*
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.annotations.SerializedName
import com.newapp.test_firebase_app.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Type


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navView: BottomNavigationView
    private lateinit var mainProgressBar: ProgressBar
    private lateinit var mainToolbar: Toolbar
    private lateinit var notificationsBadge: BadgeDrawable
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_2)

        mainToolbar = findViewById(R.id.main_toolbar)
        navView = findViewById(R.id.nav_view)
        mainProgressBar = findViewById(R.id.main_progressBar)

        setSupportActionBar(mainToolbar)

        notificationsBadge = navView.getOrCreateBadge(R.id.navigation_notifications).apply { isVisible = false }


        val navController = findNavController(R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.profileFragment -> navView.visibility = View.GONE
                R.id.chatFragment -> navView.visibility = View.GONE
                R.id.startFragment -> navView.visibility = View.GONE
                R.id.loginFragment -> navView.visibility = View.GONE
                R.id.createAccountFragment -> navView.visibility = View.GONE
                else -> navView.visibility = View.VISIBLE
            }
            showGlobalProgressBar(false)
            currentFocus?.rootView?.forceHideKeyboard()
        }

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_chats,
                R.id.navigation_notifications,
                R.id.navigation_users,
                R.id.navigation_settings,
                R.id.startFragment
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        getListItems()

//        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
//        db.collection("UserProfile").get()
//            .addOnSuccessListener(OnSuccessListener<QuerySnapshot> { queryDocumentSnapshots ->
//                // after getting the data we are calling on success method
//                // and inside this method we are checking if the received
//                // query snapshot is empty or not.
//                if (!queryDocumentSnapshots.isEmpty) {
//                    Toast.makeText(this@MainActivity, "Fail to get the data.", Toast.LENGTH_SHORT)
//                        .show()
//                } else {
//                    Toast.makeText(this@MainActivity, "Fail to get the data.", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            })
//            .addOnFailureListener(OnFailureListener { // if we do not get any data or any error we are displaying
//                // a toast message that we do not get any data
//                Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_SHORT)
//                    .show()
//            })

//        db.collection("users")
//            .get().addOnSuccessListener { collection ->
//                if (collection != null) {
//                    val items = collection.documents.map {
//                        it.data?.values?.first() as? String
//                    }.toList()
//
//                } else {
//                    Toast.makeText(this@MainActivity, "it.message", Toast.LENGTH_SHORT)
//                    .show()
//                }
//            }.addOnFailureListener {
//                Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_SHORT)
//                    .show()
//            }


//        db.collection("users")
//            .get()
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    for (document in task.result) {
//                        s(document.id + " => " + document.data)
//                    }
//                } else {
//                    s("Error getting documents." + task.exception)
//                }
//            }

        FirebaseMessaging.getInstance().token.addOnSuccessListener { token: String ->
            if (!TextUtils.isEmpty(token)) {
                Log.d("TAG", "retrieve token successful : $token")
            } else {
                Log.w("TAG", "token should not be null...")
            }
        }.addOnFailureListener { e: Exception? -> }.addOnCanceledListener {}
            .addOnCompleteListener { task: Task<String> ->
                Log.v(
                    "TAG",
                    "This is the token : " + task.result
                )
            }

    }

    private fun getListItems() : Users {
        val mArrayList: ArrayList<Type> = ArrayList()
        var users = Users()
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
                            val userList: Users = documentSnapshot.toObject(Users::class.java)
                            Log.d("jeeeeeeeeeeee", userList.phone.toString())
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
            }).addOnFailureListener(OnFailureListener {
                Toast.makeText(
                    applicationContext,
                    "Error getting data!!!",
                    Toast.LENGTH_LONG
                ).show()
            })

        return users
    }

    data class Users(
        @SerializedName("email") var email: String? = null,
        @SerializedName("phone") var phone: String? = null,
        @SerializedName("userName") var userName: String? = null,
    )

    override fun onPause() {
        super.onPause()
        FirebaseDataSource.dbInstance.goOffline()
    }

    override fun onResume() {
        FirebaseDataSource.dbInstance.goOnline()
        setupViewModelObservers()
        super.onResume()
    }

    private fun setupViewModelObservers() {
        viewModel.userNotificationsList.observe(this, {
            if (it.size > 0) {
                notificationsBadge.number = it.size
                notificationsBadge.isVisible = true
            } else {
                notificationsBadge.isVisible = false
            }
        })
    }

    fun showGlobalProgressBar(show: Boolean) {
        if (show) mainProgressBar.visibility = View.VISIBLE
        else mainProgressBar.visibility = View.GONE
    }
}