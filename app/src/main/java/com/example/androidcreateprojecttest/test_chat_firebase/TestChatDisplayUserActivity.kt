package com.example.androidcreateprojecttest.test_chat_firebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.androidcreateprojecttest.data.model.Users
import com.example.androidcreateprojecttest.databinding.ActivityTestChatDisplayUserBinding
import com.example.androidcreateprojecttest.test_chat_firebase.adapter.TestChatAdapter
import com.example.androidcreateprojecttest.util.displaySnackBar
import com.example.androidcreateprojecttest.util.progressBar
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

class TestChatDisplayUserActivity : BaseActivity() {

    var email: String? = null
    var password: String? = null
    var name: String? = null

    private lateinit var binding : ActivityTestChatDisplayUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestChatDisplayUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showDetailInfo()

    }

    private fun showDetailInfo() {
        binding.edittextName.text = PreferenceManager.getString(Constant.KEY_NAME, this)
        binding.edittextEmail.text = PreferenceManager.getString(Constant.KEY_EMAIL, this)
        val image = PreferenceManager.getString(Constant.KEY_IMAGE, this)
        if (image != null)  Glide.with(binding.profileImage).load(image).into(binding.profileImage)

        getToken()

        binding.btnLogout.setOnClickListener  {
            logout()
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@TestChatDisplayUserActivity)
        }

        getAllUsers()
    }

    private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            this.updateToken(it)
        }
    }

    private fun updateToken(token : String) {
        val fireStore = FirebaseFirestore.getInstance()
        val documentReference = fireStore.collection(Constant.KEY_COLLECTION_USERS)
            .document(PreferenceManager.getString(Constant.KEY_USER_ID, this))

        documentReference.update(Constant.KEY_FCM_TOKEN, token)
            .addOnSuccessListener {
                Log.d("tokenfirebase","Token updated successfully .")
            }
            .addOnFailureListener {
                binding.progress.progressBar(false)
                it.message?.let { it1 -> binding.progress.displaySnackBar("Unable to update token") }
            }
    }

    private fun logout() {
        binding.progress.progressBar(true)
        binding.btnLogout.isEnabled = false

        val fireStore = FirebaseFirestore.getInstance()
        val documentReference = fireStore.collection(Constant.KEY_COLLECTION_USERS)
            .document(PreferenceManager.getString(Constant.KEY_USER_ID, this))

        val hashMapUpdate = HashMap<String, Any>()
        hashMapUpdate[Constant.KEY_FCM_TOKEN] = FieldValue.delete()
        documentReference.update(hashMapUpdate)
            .addOnSuccessListener {
                PreferenceManager.clear(this)
                this.startBackLoginScreen()
                binding.progress.displaySnackBar("Token updated successfully .")
            }
            .addOnFailureListener {
                binding.progress.progressBar(false)
                it.message?.let { it1 -> binding.progress.displaySnackBar("Unable to to sign out . ") }
            }
    }

    private fun getAllUsers() {
        binding.progress.progressBar(true)
        val fireStore = FirebaseFirestore.getInstance()
        val documentReference = fireStore.collection(Constant.KEY_COLLECTION_USERS)
        documentReference
            .get()
            .addOnCompleteListener {
                binding.progress.progressBar(false)

                val currentUserId = PreferenceManager.getString(
                    Constant.KEY_USER_ID,
                    this
                ) // user id of current user logined

                if (it.isSuccessful && it.result != null) {
                  val userList = ArrayList<Users>()
                  for (item in it.result) {
                      if (currentUserId.equals(item.id)){
                          continue
                      }
                      userList.add(Users(
                          userId = item.id,
                          name = item.getString(Constant.KEY_NAME),
                          email = item.getString(Constant.KEY_EMAIL),
                          image = item.getString(Constant.KEY_IMAGE),
                          token = item.getString(Constant.KEY_FCM_TOKEN)
                      ))
                  }
                    binding.recyclerView.apply {
                        adapter = TestChatAdapter(userList, object : TestChatAdapter.CallBackItemClickListener {
                            override fun clickItemCallBack(usersItem: Users) {
                                usersItem.name?.let { it1 -> binding.progress.displaySnackBar(it1)
                                }
                                intent = Intent(this@TestChatDisplayUserActivity, TestChatSendMessageActivity::class.java).apply {
                                    putExtra("users", usersItem)
                                }
                                startActivity(intent)
                            }

                        })
                    }
                    binding.noItemFound.visibility = if (userList.isEmpty())    View.VISIBLE else View.GONE
                } else {
                    binding.noItemFound.visibility = View.VISIBLE
                }

            }
            .addOnFailureListener {
                binding.progress.progressBar(false)
                it.message?.let { it1 -> binding.progress.displaySnackBar("Unable to to sign out . ") }
            }
    }

}