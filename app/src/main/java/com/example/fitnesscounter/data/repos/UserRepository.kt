package com.example.fitnesscounter.data.repos

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.example.fitnesscounter.data.network.ApiService
import com.example.fitnesscounter.utils.toResultFlow
import javax.inject.Inject
import android.provider.ContactsContract
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception


class UserRepository @Inject constructor(private val apiService: ApiService) {

    fun createUser(username: String, email: String, password: String) = toResultFlow {
        apiService.createUser(username, email, password)
    }

    fun login(username: String, password: String) = toResultFlow {
        apiService.login(username, password)
    }

    @SuppressLint("Range")
    fun getContact(
        activity: Activity
    ): Flow<HashMap<String?, String?>> = flow {
        val getContact: HashMap<String?, String?> = HashMap()
        val phones: Cursor = activity.contentResolver
            .query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
            )!!
        while (phones.moveToNext()) {
            val name: String =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNumber: String =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            getContact.put(name, phoneNumber)
        }
        phones.close()
        emit(getContact)
    }.flowOn(Dispatchers.IO)

    fun getCurrentSMS(activity: Activity): Flow<List<String>> = flow {
        val getContactList: MutableList<String> = mutableListOf()
        val mSmsQueryUri: Uri = Uri.parse("content://sms/inbox")
        var cursor: Cursor? = null
        try {
            cursor = activity.contentResolver.query(
                mSmsQueryUri,
                null, null, null, null
            )
            if (cursor == null) {
                Log.i("CONTACT_LIST", "cursor is null. uri: \$mSmsQueryUri")
            }
            if (cursor != null) {
                var hasData = cursor.moveToFirst()
                while (hasData) {
                    getContactList.add(cursor.getString(cursor.getColumnIndexOrThrow("body")))
                    hasData = cursor.moveToNext()
                }
            }
        } catch (e: Exception) {
            Log.e("CONTACT_LIST", e.message!!)
        } finally {
            cursor!!.close()
        }
        emit(getContactList)
    }

}