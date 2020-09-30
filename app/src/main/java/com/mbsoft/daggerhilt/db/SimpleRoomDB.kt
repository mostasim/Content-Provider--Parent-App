package com.mbsoft.daggerhilt.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mbsoft.daggerhilt.db.dao.UserDao
import com.mbsoft.daggerhilt.db.model.User

@Database(entities = arrayOf(User::class), version = 1)
abstract class SimpleRoomDB : RoomDatabase(){
    abstract fun userDao(): UserDao

    companion object{
        private var instance : SimpleRoomDB? =null
        fun getInstance(context: Context): SimpleRoomDB {
            instance = instance ?: Room.databaseBuilder(context,SimpleRoomDB::class.java,"simpleDB").build()
            return instance!!
        }
    }
}