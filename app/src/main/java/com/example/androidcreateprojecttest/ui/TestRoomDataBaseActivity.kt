package com.example.androidcreateprojecttest.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.example.androidcreateprojecttest.databinding.ActivityTestRoomDataBaseBinding
import com.example.androidcreateprojecttest.repostory.test_room_database.model.Book
import com.example.androidcreateprojecttest.repostory.test_room_database.model.view_model.TestDataBaseVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class TestRoomDataBaseActivity : AppCompatActivity() {

    private lateinit var mBind : ActivityTestRoomDataBaseBinding

    private val mViewModel : TestDataBaseVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBind = ActivityTestRoomDataBaseBinding.inflate(layoutInflater).apply {
            viewmodel = mViewModel
        }
        setContentView(mBind.root)

        mBind.progress.visibility = View.GONE

//        mViewModel.isAddEvent.observe(this) {
//            display(it)
//        }
//
        mViewModel.isReadEvent.observe(this) {
            display(it)
        }
//
//        mViewModel.isUpdateEvent.observe(this) {
//            display(it)
//        }
//
//        mViewModel.isDeleteEvent.observe(this) {
//            display(it)
//        }
//
//        mViewModel.isFilterNameEvent.observe(this) {
//            display(it)
//        }

        mViewModel.isProgressEvent.observe(this) {
            if (it) {
                mBind.progress.visibility = View.VISIBLE
            } else {
                mBind.progress.visibility = View.GONE
            }
        }

    }

    private fun display(book : List<Book>) {
        mBind.result.text = ""
        for(item in book){
            mBind.result.append("${item.id} name: ${item.name} author: ${item.author}\n")
        }
    }
}
