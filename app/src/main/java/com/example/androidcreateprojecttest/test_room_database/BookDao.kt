package com.example.androidcreateprojecttest.test_room_database

import androidx.room.*

@Dao
interface BookDao {

    @Insert
    suspend fun insertBook(book: Book)

    @Query("SELECT * FROM books_table")
    fun getAllBooks(): List<Book>

    @Query("SELECT * FROM books_table WHERE name LIKE :name")
    fun getNamedDebt(name : String) : Book

    @Update
    suspend fun updateBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

    @Query("DELETE FROM books_table")
    suspend fun deleteAllBook()

}