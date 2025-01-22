package com.urlaunched.android.design.ui.paging

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.Flow

private const val DEFAULT_PLACEHOLDER_ITEMS_NUM = 10

@Composable
fun <T : Any> PagingColumn(
    modifier: Modifier = Modifier,
    pagingDataFlow: Flow<PagingData<T>>,
    contentPadding: PaddingValues = PaddingValues(),
    state: LazyListState = rememberLazyListState(),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical = if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    placeholderItemsNum: Int = DEFAULT_PLACEHOLDER_ITEMS_NUM,
    defaultIndicatorTrackColor: Color = ProgressIndicatorDefaults.circularTrackColor,
    defaultIndicatorColor: Color = ProgressIndicatorDefaults.circularColor,
    showSnackbar: suspend (message: String) -> Unit,
    placeholderItem: @Composable LazyItemScope.(index: Int) -> Unit,
    item: @Composable LazyItemScope.(item: T) -> Unit,
    itemKey: ((item: T) -> Any)? = null,
    itemContentType: ((item: T) -> Any)? = null,
    onLoadingError: @Composable LazyItemScope.(pagingState: PagingState<T>) -> Unit = {},
    startItems: (LazyListScope.(pagingState: PagingState<T>) -> Unit)? = null,
    endItems: (LazyListScope.(pagingState: PagingState<T>) -> Unit)? = null,
    noItemsPlaceholder: @Composable LazyItemScope.() -> Unit = {},
    appendIndicator: (@Composable LazyItemScope.() -> Unit)? = {
        CircularProgressIndicator(
            color = defaultIndicatorColor,
            trackColor = defaultIndicatorTrackColor
        )
    }
) {
    PagingColumn(
        modifier = modifier,
        pagingDataFlow = pagingDataFlow,
        contentPadding = contentPadding,
        state = state,
        reverseLayout = reverseLayout,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled,
        placeholderItemsNum = placeholderItemsNum,
        defaultIndicatorTrackColor = defaultIndicatorTrackColor,
        defaultIndicatorColor = defaultIndicatorColor,
        showSnackbar = showSnackbar,
        placeholderItem = placeholderItem,
        itemKey = itemKey,
        itemContentType = itemContentType,
        startItems = startItems,
        endItems = endItems,
        noItemsPlaceholder = noItemsPlaceholder,
        onLoadingError = onLoadingError,
        appendIndicator = appendIndicator,
        item = { _, _, itemModel ->
            item(itemModel)
        }
    )
}

@Composable
fun <T : Any> PagingColumn(
    modifier: Modifier = Modifier,
    pagingDataFlow: Flow<PagingData<T>>,
    contentPadding: PaddingValues = PaddingValues(),
    state: LazyListState = rememberLazyListState(),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical = if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    placeholderItemsNum: Int = DEFAULT_PLACEHOLDER_ITEMS_NUM,
    defaultIndicatorTrackColor: Color = ProgressIndicatorDefaults.circularTrackColor,
    defaultIndicatorColor: Color = ProgressIndicatorDefaults.circularColor,
    showSnackbar: suspend (message: String) -> Unit,
    placeholderItem: @Composable LazyItemScope.(index: Int) -> Unit,
    item: @Composable LazyItemScope.(index: Int, itemCount: Int, item: T) -> Unit,
    itemKey: ((item: T) -> Any)? = null,
    itemContentType: ((item: T) -> Any)? = null,
    startItems: (LazyListScope.(pagingState: PagingState<T>) -> Unit)? = null,
    endItems: (LazyListScope.(pagingState: PagingState<T>) -> Unit)? = null,
    noItemsPlaceholder: @Composable LazyItemScope.() -> Unit = {},
    onLoadingError: @Composable LazyItemScope.(pagingState: PagingState<T>) -> Unit = {},
    appendIndicator: (@Composable LazyItemScope.() -> Unit)? = {
        CircularProgressIndicator(
            color = defaultIndicatorColor,
            trackColor = defaultIndicatorTrackColor
        )
    }
) {
    val pagingItems = pagingDataFlow.collectAsLazyPagingItems()

    PagingColumn(
        modifier = modifier,
        pagingItems = pagingItems,
        contentPadding = contentPadding,
        state = state,
        reverseLayout = reverseLayout,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled,
        placeholderItemsNum = placeholderItemsNum,
        defaultIndicatorTrackColor = defaultIndicatorTrackColor,
        defaultIndicatorColor = defaultIndicatorColor,
        showSnackbar = showSnackbar,
        placeholderItem = placeholderItem,
        itemKey = itemKey,
        itemContentType = itemContentType,
        startItems = startItems,
        endItems = endItems,
        noItemsPlaceholder = noItemsPlaceholder,
        onLoadingError = onLoadingError,
        appendIndicator = appendIndicator,
        item = item
    )
}

@Composable
fun <T : Any> PagingColumn(
    modifier: Modifier = Modifier,
    pagingItems: LazyPagingItems<T>,
    contentPadding: PaddingValues = PaddingValues(),
    state: LazyListState = rememberLazyListState(),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical = if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    placeholderItemsNum: Int = DEFAULT_PLACEHOLDER_ITEMS_NUM,
    defaultIndicatorTrackColor: Color = ProgressIndicatorDefaults.circularTrackColor,
    defaultIndicatorColor: Color = ProgressIndicatorDefaults.circularColor,
    showSnackbar: suspend (message: String) -> Unit,
    placeholderItem: @Composable LazyItemScope.(index: Int) -> Unit,
    item: @Composable LazyItemScope.(index: Int, itemCount: Int, item: T) -> Unit,
    itemKey: ((item: T) -> Any)? = null,
    itemContentType: ((item: T) -> Any)? = null,
    startItems: (LazyListScope.(pagingState: PagingState<T>) -> Unit)? = null,
    endItems: (LazyListScope.(pagingState: PagingState<T>) -> Unit)? = null,
    noItemsPlaceholder: @Composable LazyItemScope.() -> Unit = {},
    onLoadingError: @Composable LazyItemScope.(pagingState: PagingState<T>) -> Unit = {},
    appendIndicator: (@Composable LazyItemScope.() -> Unit)? = {
        CircularProgressIndicator(
            color = defaultIndicatorColor,
            trackColor = defaultIndicatorTrackColor
        )
    }
) {
    PagingContainer(
        pagingItems = pagingItems,
        showSnackbar = showSnackbar
    ) { pagingState ->
        LazyColumn(
            modifier = modifier,
            contentPadding = contentPadding,
            state = if (pagingState.isLoading) rememberLazyListState() else state,
            reverseLayout = reverseLayout,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            flingBehavior = flingBehavior,
            userScrollEnabled = userScrollEnabled && !pagingState.isLoading
        ) {
            startItems?.invoke(this, pagingState)

            when {
                pagingState.isLoading -> {
                    items(placeholderItemsNum) { index ->
                        placeholderItem(index)
                    }
                }

                pagingState.isNoItems -> {
                    item {
                        noItemsPlaceholder()
                    }
                }

                pagingState.isLoadingError -> {
                    item {
                        onLoadingError(pagingState)
                    }
                }

                else -> {
                    items(
                        count = pagingState.pagingItems.itemCount,
                        key = itemKey?.let {
                            pagingState.pagingItems.itemKey(itemKey)
                        },
                        contentType = pagingState.pagingItems.itemContentType(itemContentType)
                    ) { index ->
                        pagingState.pagingItems[index]?.let { item ->
                            item(index, pagingState.pagingItems.itemCount, item)
                        } ?: run {
                            placeholderItem(index)
                        }
                    }

                    if (pagingState.isAppendLoading && appendIndicator != null) {
                        item {
                            appendIndicator()
                        }
                    }
                }
            }

            endItems?.invoke(this, pagingState)
        }

        LaunchedEffect(
            state.canScrollForward,
            pagingState.isAppendError,
            state.isScrollInProgress
        ) {
            if (!state.canScrollForward && pagingState.isAppendError && state.isScrollInProgress) pagingState.pagingItems.retry()
        }
    }
}