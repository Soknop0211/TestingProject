package com.example.androidcreateprojecttest.util

import android.util.Log
import com.google.firebase.firestore.*


interface OnCallBackListener {
    fun onLoginSuccess(userList : ArrayList<User>, mess : String)
    fun onLoginFailed(mess : String)
}

data class User(val userName : String?,
                val email : String?,
                val password : String?,
                val phone : String?)

class UserP {
    var userName : String? = null
    val email : String? = null
    val password : String? = null
    val phone : String? = null
}

class Utils {

    companion object {
        fun getAllUser(onCallBackListener: OnCallBackListener) {
            val fireStore = FirebaseFirestore.getInstance()
            fireStore.collection("UserProfile")
                .get().addOnSuccessListener { collection ->
                    if (collection != null) {
                        val items = collection.documents.map {
                            it.data?.values?.first() as? String
                        }.toList()

                        val userList = ArrayList<User>()
                        for (item in collection){
                            val userName: String? = item.getString("userName")
                            val email: String? = item.getString("email")
                            val password: String? = item.getString("password")
                            val phone: String? = item.getString("phone")
                            userList.add(User(userName, email, password, phone))
                        }

                        onCallBackListener.onLoginSuccess(userList, "You got users")
                    } else {
                        onCallBackListener.onLoginFailed("Failed")
                    }
                }.addOnFailureListener {
                    onCallBackListener.onLoginFailed("Failed")
                }
        }

        fun getUserById() {
            val fireStore = FirebaseFirestore.getInstance()
            fireStore.collection("UserProfile").document("FY0GosBXnHqpSIZjssCd")
                .get().addOnSuccessListener { collection ->
                    if (collection != null) {
                        val userName: String? = collection.getString("userName")
                        val email: String? = collection.getString("email")
                        val password: String? = collection.getString("password")
                        val phone: String? = collection.getString("phone")
                    } else {
                        Log.d("Results", "Errrror")
                    }
                }.addOnFailureListener {
                    Log.d("Results", "Errrror")
                }
        }

        fun getUserByUserEmail(email: String) {
            val fireStore = FirebaseFirestore.getInstance()
            val collection: CollectionReference = fireStore.collection("UserProfile")
            val querye = collection.whereEqualTo("email", email)
            querye.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Results", task.result.toString())
                    val userOrder = task.result.toObjects(UserP::class.java)
                    Log.d("Results", task.result.toString())

                } else {
                    Log.d("Results", "Errrror")
                }
            }

        }


        fun createNewUser(user: User, onCallBackListener: OnCallBackListener) {
            val fireStore = FirebaseFirestore.getInstance()
            val hashMap : HashMap<String, Any> = HashMap()
            hashMap["userName"] = user.userName!!
            hashMap["email"] = user.email!!
            hashMap["phone"] = user.phone!!
            hashMap["password"] = user.password!!

            // val user = hashMapOf("name" to "str")
            fireStore.collection("UserProfile").add(hashMap).addOnSuccessListener {
                getAllUser(onCallBackListener)
            }.addOnFailureListener {
                Log.d("Results", "Errrror")
            }
        }

        fun deleteAllUser(onCallBackListener: OnCallBackListener) {
            val fireStore = FirebaseFirestore.getInstance()
            fireStore.collection("UserProfile")
                .get().addOnSuccessListener { collection ->
                    if (collection != null) {
                        collection.documents.forEach {
                            it.reference.delete()
                        }
                        getAllUser(onCallBackListener)
                    } else {
                        println("No such document")
                    }
                }.addOnFailureListener {
                    println(" ${it.localizedMessage}")
                }
        }

        fun deleteByEmail(email : String, onCallBackListener: OnCallBackListener) {
            val fireStore = FirebaseFirestore.getInstance()
            fireStore.collection("UserProfile")
                .get().addOnSuccessListener { collection ->
                    if (collection != null) {
                        collection.documents.forEach {
                            if (it["email"]!! == email) {
                                it.reference.delete()
                            }
                        }
                        getAllUser(onCallBackListener)
                    } else {
                        println("No such document")
                    }
                }.addOnFailureListener {
                    println(" ${it.localizedMessage}")
                }
        }

    }

}