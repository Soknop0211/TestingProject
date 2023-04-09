package com.example.androidcreateprojecttest.repostory

import android.content.Context
import androidx.room.Room
import com.example.androidcreateprojecttest.repostory.test_room_database.model.BookDao
import com.example.androidcreateprojecttest.repostory.test_room_database.model.BookDatabase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    lateinit var database: BookDatabase

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BookDatabase {
        database =  Room.databaseBuilder(
            context,
            BookDatabase::class.java,
            "book_database"
        )
            .allowMainThreadQueries()
            // .fallbackToDestructiveMigration()
            .build()

        return database
    }

    @Provides
    @Singleton
    fun provideUserDao(db: BookDatabase): BookDao {
        return db.bookDao()
    }
}

