package com.urlaunched.android.common.files

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import java.io.FileNotFoundException

private const val READ_MODE = "r"

fun Uri.length(contentResolver: ContentResolver): Long {
    val assetFileDescriptor = try {
        contentResolver.openAssetFileDescriptor(this, READ_MODE)
    } catch (e: FileNotFoundException) {
        null
    }
    val length = assetFileDescriptor?.use { it.length } ?: -1L
    if (length != -1L) {
        return length
    }

    if (scheme.equals(ContentResolver.SCHEME_CONTENT)) {
        return contentResolver.query(this, arrayOf(OpenableColumns.SIZE), null, null, null)
            ?.use { cursor ->
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                if (sizeIndex == -1) {
                    return@use -1L
                }
                cursor.moveToFirst()
                return try {
                    cursor.getLong(sizeIndex)
                } catch (_: Throwable) {
                    -1L
                }
            } ?: -1L
    } else {
        return -1L
    }
}