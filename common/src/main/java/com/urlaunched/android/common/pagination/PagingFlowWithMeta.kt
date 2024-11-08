package com.urlaunched.android.common.pagination

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

data class PagingFlowWithMeta<T : Any, R : Any?>(
    val pagingFlow: Flow<PagingData<T>>,
    val meta: R?
)