package com.example.androidcreateprojecttest.test_chat_firebase

class Constant {
    companion object {
        val KEY_COLLECTION_USERS = "users_chat"
        val KEY_NAME = "name"
        val KEY_EMAIL = "email"
        val KEY_PASSWORD = "password"
        val KEY_PREFERENCE = "chatapppreference"
        val KEY_IS_SIGNED_IN = "isSignedIn"
        val KEY_USER_ID = "userId"
        val KEY_IMAGE = "image"
        val KEY_FCM_TOKEN = "fcmToken"

        val KEY_COLLECTION_CHAT = "chat_messenger"
        val KEY_SENDER_ID = "senderId"
        val KEY_RECEIVER_ID = "receiverId"
        val KEY_MESSAGE = "message"
        val KEY_TIMESTAMP = "timestamp"

        val KEY_AVAILABILITY = "availability"

        // Notification
        val REMOTE_MSG_AUTHRIZATION = "Authorization"
        val REMOTE_MSG_CONTENT_TYPE = "Content-Type"
        val REMOTE_MSG_DATA = "data"
        val REMOTE_MSG_REGIESTRATION_IDS = "registration_ids"

        var remoteMsgHeader : HashMap<String, Any>? = null

        @JvmName("getRemoteMsgHeader1")
        fun getRemoteMsgHeader() : HashMap<String, Any>? {
            if (remoteMsgHeader == null) {
                remoteMsgHeader = HashMap()
                remoteMsgHeader!![REMOTE_MSG_AUTHRIZATION] = "key=AAAAgbfciSE:APA91bHTfX2zWgYYgYoYaB6RzNwp77jLAt07BC7f8ENlGbo0M9UBSk6-X-O6bhNLTe8BVV9UnRONn-bjMQaH3EOHeLY83GEp3zDJ2Q0Imz_SFH6qsR0HnMYtAneG-H1iELwLTl9vfmIf"

                remoteMsgHeader!![REMOTE_MSG_CONTENT_TYPE] = "application/json"
            }

            return remoteMsgHeader
        }

    }
}