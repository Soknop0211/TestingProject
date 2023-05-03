package com.example.androidcreateprojecttest.repostory

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.androidcreateprojecttest.data.db.repository.AuthRepository
import com.example.androidcreateprojecttest.test_room_database.BookDao
import com.example.androidcreateprojecttest.test_room_database.BookDatabase
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

    @Provides
    fun provideFireStore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    fun provideAuthRepository(): AuthRepository  = AuthRepository()

    @Singleton
    @Provides
    fun provideContext(application: Application): Context {
        return application
    }
}

