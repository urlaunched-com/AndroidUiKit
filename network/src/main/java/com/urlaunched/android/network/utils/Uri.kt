package com.urlaunched.android.network.utils

import android.content.ContentResolver
import android.net.Uri
import com.urlaunched.android.common.files.length
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source

fun Uri.asRequestBody(contentResolver: ContentResolver, contentType: okhttp3.MediaType? = null): RequestBody =
    object : RequestBody() {
        override fun contentType() = contentType

        override fun contentLength() = length(contentResolver)

        override fun writeTo(sink: BufferedSink) {
            requireNotNull(contentResolver.openInputStream(this@asRequestBody))
                .source()
                .use { source -> sink.writeAll(source) }
        }
    }