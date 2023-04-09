package com.example.androidcreateprojecttest.repostory.test_room_database.model.view_model

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcreateprojecttest.repostory.test_room_database.model.Book
import com.example.androidcreateprojecttest.repostory.test_room_database.model.BookDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TestDataBaseVM @Inject constructor (private val bookDao: BookDao) : ViewModel() {

//    private var _isAdd = MutableLiveData< List<Book>>()
//    val isAddEvent: LiveData< List<Book>> = _isAdd

    private val _isRead = MutableLiveData< List<Book>>()
    val isReadEvent: LiveData< List<Book>> = _isRead

//    private val _isUpdate = MutableLiveData< List<Book>>()
//    val isUpdateEvent: LiveData< List<Book>> = _isUpdate
//
//    private val _isDelete = MutableLiveData< List<Book>>()
//    val isDeleteEvent: LiveData< List<Book>> = _isDelete
//
//    private val _filterName = MutableLiveData< List<Book>>()
//    val isFilterNameEvent: LiveData< List<Book>> = _filterName

    val isProgressEvent = MutableLiveData<Boolean>()

    fun addPressed() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                isProgressEvent.value = true
                //Insert
                Log.i("MyTAG","*****     Inserting 3 Books     **********")
                bookDao.insertBook(Book(0,"Java","Alex"))
                bookDao.insertBook(Book(0,"PHP","Mike"))
                bookDao.insertBook(Book(0,"Kotlin","Amelia"))
                Log.i("MyTAG","*****     Inserted 3 Books       **********")

                readPressed()
            }
        }
    }


    fun readPressed() {
        viewModelScope.launch(Dispatchers.IO) {
            //Query
            val books = bookDao.getAllBooks()
            withContext(Dispatchers.Main) {

                Log.i("MyTAG","*****   ${books.size} books there *****")
                for(book in books){
                    Log.i("MyTAG","id: ${book.id} name: ${book.name} author: ${book.author}")
                }
                isProgressEvent.value = false
                _isRead.value = bookDao.getAllBooks()
            }
        }
    }

    fun updatePressed() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                isProgressEvent.value = true

                //Update
                Log.i("MyTAG","*****      Updating a book      **********")
                bookDao.updateBook(Book(1,"PHPUpdated","Mike"))
                //Query
                val books2 = bookDao.getAllBooks()
                Log.i("MyTAG","*****   ${books2.size} books there *****")
                for(book in books2){
                    Log.i("MyTAG","id: ${book.id} name: ${book.name} author: ${book.author}")
                }
                // _isUpdate.value = bookDao.getAllBooks()
                readPressed()
            }
        }
    }

    fun removePressed() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                isProgressEvent.value = true
                //delete
                Log.i("MyTAG","*****       Deleting a book      **********")
                // bookDao.deleteBook(Book(2,"Kotlin","Amelia"))
                bookDao.deleteAllBook()
                //Query
                val books3 = bookDao.getAllBooks()
                Log.i("MyTAG","*****   ${books3.size} books there *****")
                for(book in books3){
                    Log.i("MyTAG","id: ${book.id} name: ${book.name} author: ${book.author}")
                }
                // _isDelete.value = bookDao.getAllBooks()
                readPressed()
            }
        }
    }

    fun removeAllPressed() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                isProgressEvent.value = true
                //delete
                Log.i("MyTAG","*****       Deleting a book      **********")
                // bookDao.deleteBook(Book(2,"Kotlin","Amelia"))
                bookDao.deleteAllBook()
                //Query
                val books3 = bookDao.getAllBooks()
                Log.i("MyTAG","*****   ${books3.size} books there *****")
                for(book in books3){
                    Log.i("MyTAG","id: ${book.id} name: ${book.name} author: ${book.author}")
                }
                // _isDelete.value = bookDao.getAllBooks()
                readPressed()
            }
        }
    }

    fun filterPressed() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                isProgressEvent.value = true
                //delete
                Log.i("MyTAG","*****       Deleting a book      **********")

                val books3 = bookDao.getNamedDebt("Kotlin")
//                Log.i("MyTAG","*****   ${books3.size} books there *****")
//                for(book in books3){
//                    Log.i("MyTAG","id: ${book.id} name: ${book.name} author: ${book.author}")
//                }
                // _filterName.value = listOf(books3)
                readPressed()
            }
        }
    }

}