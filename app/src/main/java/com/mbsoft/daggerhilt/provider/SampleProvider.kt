package com.mbsoft.daggerhilt.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.mbsoft.daggerhilt.db.SimpleRoomDB
import com.mbsoft.daggerhilt.db.dao.UserDao
import com.mbsoft.daggerhilt.db.model.User


class SampleProvider: ContentProvider() {
    private val dbName = "simpleDB"
    lateinit var db : SimpleRoomDB
    lateinit var userDao: UserDao
    private val KEY_EMAIL: String = "KEY_EMAIL"
    private val KEY_PASSWORD: String = "KEY_PASSWORD"
    private val KEY_ID: String = "KEY_UID"
    companion object {
        const val AUTHORITY = "com.mbsoft.daggerhilt"
        val TABLE_NAME =User::class.java.simpleName
        val URI_MENU = Uri.parse(
                "content://$AUTHORITY/$TABLE_NAME"
        )

        /**The match code for some items in the companyTM table.  */
        private const val CODE_COMPANYTM_DIR= 1
        /** The match code for an item in the companyTM table.  */
        private const val CODE_COMPANYTM_ITEM = 2
        /** The URI matcher.  */
        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        init {
            MATCHER.addURI(
                    AUTHORITY,
                    "simpleDB",
                    CODE_COMPANYTM_DIR
            )
            MATCHER.addURI(
                    AUTHORITY,
                    "simpleDB/*",
                    CODE_COMPANYTM_ITEM
            )
        }
    }
    override fun onCreate(): Boolean {
        db = SimpleRoomDB.getInstance(context!!)
        userDao = db.userDao()
        return true
    }

    override fun query(
            uri: Uri,
            projection: Array<out String>?,
            selection: String?,
            selectionArgs: Array<out String>?,
            sortOrder: String?
    ): Cursor? {
        val code = MATCHER.match(uri)
        return if (code == CODE_COMPANYTM_DIR || code == CODE_COMPANYTM_ITEM) {
            val context = context ?: return null
            val userDao: UserDao = SimpleRoomDB.getInstance(context).userDao()
            val cursor: Cursor
            cursor = userDao.getAllUser()
            cursor.setNotificationUri(context.contentResolver, uri)
            cursor
        } else {
            throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        Log.e("USER",values.toString())
        return when (MATCHER.match(uri)) {
            CODE_COMPANYTM_DIR -> {
                val context = context ?: return null
                val user = User(1,values?.getAsString(KEY_EMAIL),values?.getAsString(KEY_PASSWORD))
                SimpleRoomDB.getInstance(context).userDao().insertAll(user)
                context.contentResolver.notifyChange(uri, null)
                ContentUris.withAppendedId(uri, 1)
            }
            CODE_COMPANYTM_ITEM -> throw java.lang.IllegalArgumentException("Invalid URI, cannot insert with ID: $uri")
            else -> throw java.lang.IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return when (MATCHER.match(uri)) {
            CODE_COMPANYTM_DIR -> {
                val context = context ?: return 0
                SimpleRoomDB.getInstance(context).clearAllTables()
                context.contentResolver.notifyChange(uri, null)
                0
            }
            else -> throw java.lang.IllegalArgumentException("Unknown URI: $uri")
        }
    }


    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }
}