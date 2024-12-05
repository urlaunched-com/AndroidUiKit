package com.urlaunched.android.tempattachment.domain.usecases

import android.content.Context
import android.net.Uri
import com.urlaunched.android.common.files.FilePickerHelper
import com.urlaunched.android.common.files.MediaType
import com.urlaunched.android.common.response.Response
import com.urlaunched.android.common.response.flatMap
import com.urlaunched.android.common.response.map
import com.urlaunched.android.tempattachment.domain.repository.TempAttachmentsRepository

class UploadUriAndGetPublicUrlUseCase(
    private val tempAttachmentsRepository: TempAttachmentsRepository,
    private val context: Context
) {
    suspend operator fun invoke(mediaType: MediaType, uri: Uri, isPrivate: Boolean = false): Response<String> =
        tempAttachmentsRepository.getPresignedAndPublicUrl(
            fileName = FilePickerHelper.getFileName(context, uri),
            isPrivate = isPrivate
        ).flatMap { tempAttachment ->
            tempAttachmentsRepository.uploadFileToPresignedUrl(
                mediaType = mediaType,
                uri = uri,
                presignedUrl = tempAttachment.presignedUrl
            ).map {
                tempAttachment.publicUrl
            }
        }
}