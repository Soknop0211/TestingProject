package com.example.androidcreateprojecttest.ui

import android.app.DownloadManager
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.androidcreateprojecttest.util.OnCallBackListener
import com.example.androidcreateprojecttest.R
import com.example.androidcreateprojecttest.databinding.ActivityMainBinding
import com.example.androidcreateprojecttest.util.User
import com.example.androidcreateprojecttest.util.Utils


class MainActivity : AppCompatActivity() {

    private var getFilePath: String? = null
    private var videoUpload: VideoView? = null

    var manager: DownloadManager? = null

    private lateinit var mBinding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        // Test Video
        getFilePath = "https://sandboxeazy.daikou.asia/storage/4f15665198381e9c25f7c9a7b013754a.mp4"

        videoUpload = findViewById(R.id.videoPlayer)
        val progressBar: ProgressBar = findViewById(R.id.progress)

        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoUpload)
        mediaController.setMediaPlayer(videoUpload)
        videoUpload!!.setMediaController(mediaController)
        val uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.movie)
        videoUpload!!.setVideoURI(uri)
        videoUpload!!.requestFocus()

        videoUpload!!.setOnPreparedListener { mp: MediaPlayer ->
            mp.isLooping = false
            progressBar.visibility = View.GONE
            videoUpload!!.start()
        }

        //Test Drawer
        mBinding.btnDrawable.setOnClickListener {
            startActivity(Intent(this, TestNavigationDrawerActivity::class.java))
        }

        // Test data base
        mBinding.btnDb.setOnClickListener {
            startActivity(Intent(this, TestRoomDataBaseActivity::class.java))
        }

        Utils.getAllUser(onCallBackListener)

        Utils.getUserById()

        Utils.getUserByUserEmail("testing@gmail.com")

        val userName = "Dara"
        val email = "dara12@gmail.com"
        val password = "654321"
        val phone = "0987654321"

        // Utils.createNewUser(User(userName, email, password, phone), onCallBackListener)

        Utils.deleteByEmail("dara12@gmail.com", onCallBackListener)

    }

     private val onCallBackListener = object : OnCallBackListener {
        override fun onLoginSuccess(userList : ArrayList<User>, mess: String) {
            Toast.makeText(this@MainActivity, mess, Toast.LENGTH_LONG).show()
        }

        override fun onLoginFailed(mess: String) {
            Toast.makeText(this@MainActivity, mess, Toast.LENGTH_LONG).show()
        }

    }
}