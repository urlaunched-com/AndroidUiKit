package com.urlaunched.android.cdn.data.remote.source

import com.urlaunched.android.cdn.models.domain.download.DownloadState
import com.urlaunched.android.cdn.models.domain.download.DownloadableCdnDomainModel
import com.urlaunched.android.common.response.Response
import kotlinx.coroutines.flow.Flow

interface DownloadCdnDataSource {
    suspend fun downloadFile(downloadableCdn: DownloadableCdnDomainModel, path: String): Flow<DownloadState>
    suspend fun getPrivateFileLink(downloadableCdn: DownloadableCdnDomainModel.Private): Response<String>
}