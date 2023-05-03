package com.example.androidcreateprojecttest.data.model

data class CreateUser(
    var displayName: String = "",
    var email: String = "",
    var password: String = ""
)

data class CreateNewUser(
    var userId : String = "",
    var userName: String = "",
    var email: String = "",
    var phone : String = "",
    var password: String = ""
)

data class Users(
    var userId: String = "",
    var name: String? = "",
    var email: String? = "",
    var password: String? = "",
    var image: String? = "",
    var token: String? = ""
) : java.io.Serializable