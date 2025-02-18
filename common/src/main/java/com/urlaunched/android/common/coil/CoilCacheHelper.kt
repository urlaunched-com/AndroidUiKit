package com.urlaunched.android.common.coil

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import coil.Coil
import coil.annotation.ExperimentalCoilApi
import coil.decode.DecodeResult
import coil.decode.Decoder
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.request.ImageResult
import java.io.File

object CoilCacheHelper {
    @OptIn(ExperimentalCoilApi::class)
    suspend fun extractImageFromCachesOrDownloadFile(
        context: Context,
        imageUrl: String,
        extension: String? = null
    ): File? {
        val imageLoader = Coil.imageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .build()
        val cacheKey = request.diskCacheKey ?: imageUrl

        val diskCache = imageLoader.diskCache
        val snapshot = diskCache?.openSnapshot(cacheKey)

        return if (snapshot != null && snapshot.data.toFile().exists()) {
            val cachedFile = snapshot.data.toFile()
            val newCachedFile =
                File(cachedFile.parent, "${cacheKey.hashCode()}.${extension ?: imageUrl.substringAfterLast(".", "")}")
            cachedFile.renameTo(newCachedFile)
            newCachedFile
        } else {
            DownloadImageHelper.downloadImage(
                mediaLink = imageUrl,
                context = context
            )
        }
    }

    // https://coil-kt.github.io/coil/getting_started/#preloading
    fun scheduleImagePreload(context: Context, imageUrl: String) {
        val imageLoader = Coil.imageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            // Disable reading from/writing to the memory cache.
            .memoryCachePolicy(CachePolicy.DISABLED)
            // Set a custom `Decoder.Factory` that skips the decoding step.
            .decoderFactory { _, _, _ ->
                Decoder { DecodeResult(ColorDrawable(Color.BLACK), false) }
            }
            .build()
        imageLoader.enqueue(request)
    }

    suspend fun preloadImage(context: Context, imageUrl: String): ImageResult {
        val imageLoader = Coil.imageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .allowHardware(false)
            // Disable reading from/writing to the memory cache.
            .memoryCachePolicy(CachePolicy.DISABLED)
            // Set a custom `Decoder.Factory` that skips the decoding step.
            .decoderFactory { _, _, _ ->
                Decoder { DecodeResult(ColorDrawable(Color.BLACK), false) }
            }
            .build()

        return imageLoader.execute(request)
    }
}