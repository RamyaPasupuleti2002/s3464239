package uk.ac.tees.mad.decideeasy.utils

import android.content.Context
import android.net.Uri

object Utils {
    fun getRealPathFromURI(context: Context, uri: Uri): String? {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            it.moveToFirst()
            val index = it.getColumnIndex("_data")
            if (index != -1) it.getString(index) else null
        }
    }
}