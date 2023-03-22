package com.example.androidcreateprojecttest

import android.app.DownloadManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.ByteArrayBuffer
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection


class MainActivity : AppCompatActivity() {

    private var getFilePath: String? = null
    private var videoUpload: VideoView? = null

    var manager: DownloadManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getFilePath = "https://sandboxeazy.daikou.asia/storage/4f15665198381e9c25f7c9a7b013754a.mp4"

        videoUpload = findViewById(R.id.videoPlayer)
        val progressBar: ProgressBar = findViewById(R.id.progress)

        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoUpload)
        mediaController.setMediaPlayer(videoUpload)
        videoUpload!!.setMediaController(mediaController)
        // val uri = Uri.parse(getFilePath)
        val uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.movie)
        videoUpload!!.setVideoURI(uri)
        videoUpload!!.requestFocus()

        videoUpload!!.setOnPreparedListener { mp: MediaPlayer ->
            mp.isLooping = false
            progressBar.visibility = View.GONE
            videoUpload!!.start()
        }

        DownloadFromUrl("Hlelelelelellelelle.mp4")

    }

    fun DownloadFromUrl(fileName: String) {  //this is the downloader method
        val PATH = "/sdcard/download/" //put the downloaded file here
        try {
            val url = URL("http://www.ericmoyer.com/episode1.mp4") //you can write here any link
            val file = File(fileName)
            val startTime = System.currentTimeMillis()
            Log.d("VideoManager", "download begining")
            Log.d("VideoManager", "download url:$url")
            Log.d("VideoManager", "downloaded file name:$fileName")
            /* Open a connection to that URL. */
            val ucon = url.openConnection()

            /*
                         * Define InputStreams to read from the URLConnection.
                         */
            val `is` = ucon.getInputStream()
            val bis = BufferedInputStream(`is`)

            /*
                         * Read bytes to the Buffer until there is nothing more to read(-1).
                         */
            val baf = ByteArrayBuffer(50)
            var current = 0
            while (bis.read().also { current = it } != -1) {
                baf.append(current.toByte().toInt())
            }

            /* Convert the Bytes read to a String. */
            val fos = FileOutputStream(PATH + file)
            fos.write(baf.toByteArray())
            fos.close()
            Log.d(
                "VideoManager",
                "download ready in" + (System.currentTimeMillis() - startTime) / 1000
                        + " sec"
            )
        } catch (e: IOException) {
            Log.d("VideoManager", "Error: $e")
        }
    }

    private fun downloadFile(fileURL: String, fileName: String) {
        try {
            val rootDir: String = Environment.getExternalStorageDirectory().absolutePath + File.separator.toString() + "Video"
            val rootFile = File(rootDir)
            rootFile.mkdir()
            val url = URL(fileURL)
            val c = url.openConnection() as HttpURLConnection
            c.requestMethod = "GET"
            c.doOutput = true
            c.connect()
            val f = FileOutputStream(
                File(
                    rootFile,
                    fileName
                )
            )
            val `in` = c.inputStream
            val buffer = ByteArray(1024)
            var len1 = 0
            while (`in`.read(buffer).also { len1 = it } > 0) {
                f.write(buffer, 0, len1)
            }
            f.close()
        } catch (e: IOException) {
            Log.d("Error....", e.toString())
        }
    }

    private fun downloadFile(url: String, outputFile: File) {
        try {
            val u = URL(url)
            val conn: URLConnection = u.openConnection()
            val contentLength: Int = conn.contentLength
            val stream = DataInputStream(u.openStream())
            val buffer = ByteArray(contentLength)
            stream.readFully(buffer)
            stream.close()
            val fos = DataOutputStream(FileOutputStream(outputFile))
            fos.write(buffer)
            fos.flush()
            fos.close()
        } catch (e: FileNotFoundException) {
            return  // swallow a 404
        } catch (e: IOException) {
            return  // swallow a 404
        }
    }

    private fun down(string: String) {
        try {
            val url = URL(string)
            val c: HttpURLConnection = url.openConnection() as HttpURLConnection
            c.setRequestMethod("GET")
            c.setDoOutput(true)
            c.connect()
            val PATH: String = (Environment.getExternalStorageDirectory().toString() + "/load")
            Log.v("LOG_TAG", "PATH: $PATH")
            val file = File(PATH)
            file.mkdirs()
            // val outputFile = File(file)
            val fos = FileOutputStream(file)
            val `is`: InputStream = c.inputStream
            val buffer = ByteArray(4096)
            var len1 = 0
            while (`is`.read(buffer).also { len1 = it } != -1) {
                fos.write(buffer, 0, len1)
            }
            fos.close()
            `is`.close()
            Toast.makeText(
                this, " A new file is downloaded successfully",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}