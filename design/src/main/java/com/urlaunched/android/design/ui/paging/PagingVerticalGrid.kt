package com.urlaunched.android.design.ui.paging

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
fun <T : Any> PagingVerticalGrid(
    modifier: Modifier = Modifier,
    pagingDataFlow: Flow<PagingData<T>>,
    columns: GridCells,
    contentPadding: PaddingValues = PaddingValues(),
    state: LazyGridState = rememberLazyGridState(),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical = if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    placeholderItemsNum: Int = DEFAULT_PLACEHOLDER_ITEMS_NUM,
    defaultIndicatorTrackColor: Color = ProgressIndicatorDefaults.circularTrackColor,
    defaultIndicatorColor: Color = ProgressIndicatorDefaults.circularColor,
    placeholderItem: @Composable LazyGridItemScope.(index: Int) -> Unit,
    item: @Composable LazyGridItemScope.(item: T) -> Unit,
    showSnackbar: suspend (message: String) -> Unit,
    itemKey: ((item: T) -> Any)? = null,
    itemContentType: ((item: T) -> Any)? = null,
    itemSpan: ((item: T?) -> GridItemSpan)? = null,
    onLoadingError: @Composable LazyGridItemScope.(pagingState: PagingState<T>) -> Unit = {},
    startItems: (LazyGridScope.(pagingState: PagingState<T>) -> Unit)? = null,
    endItems: (LazyGridScope.(pagingState: PagingState<T>) -> Unit)? = null,
    noItemsPlaceholder: @Composable LazyGridItemScope.() -> Unit = {},
    appendIndicator: (@Composable LazyGridItemScope.() -> Unit)? = {
        CircularProgressIndicator(
            color = defaultIndicatorColor,
            trackColor = defaultIndicatorTrackColor
        )
    }
) {
    PagingVerticalGrid(
        modifier = modifier,
        pagingDataFlow = pagingDataFlow,
        columns = columns,
        contentPadding = contentPadding,
        state = state,
        reverseLayout = reverseLayout,
        verticalArrangement = verticalArrangement,
        horizontalArrangement = horizontalArrangement,
        flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled,
        placeholderItemsNum = placeholderItemsNum,
        defaultIndicatorTrackColor = defaultIndicatorTrackColor,
        defaultIndicatorColor = defaultIndicatorColor,
        showSnackbar = showSnackbar,
        itemKey = itemKey,
        itemContentType = itemContentType,
        itemSpan = itemSpan,
        placeholderItem = placeholderItem,
        startItems = startItems,
        noItemsPlaceholder = noItemsPlaceholder,
        appendIndicator = appendIndicator,
        endItems = endItems,
        onLoadingError = onLoadingError,
        item = { _, _, itemModel ->
            item(itemModel)
        }
    )
}

@Composable
fun <T : Any> PagingVerticalGrid(
    modifier: Modifier = Modifier,
    pagingDataFlow: Flow<PagingData<T>>,
    columns: GridCells,
    contentPadding: PaddingValues = PaddingValues(),
    state: LazyGridState = rememberLazyGridState(),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical = if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    placeholderItemsNum: Int = DEFAULT_PLACEHOLDER_ITEMS_NUM,
    defaultIndicatorTrackColor: Color = ProgressIndicatorDefaults.circularTrackColor,
    defaultIndicatorColor: Color = ProgressIndicatorDefaults.circularColor,
    showSnackbar: suspend (message: String) -> Unit,
    placeholderItem: @Composable LazyGridItemScope.(index: Int) -> Unit,
    item: @Composable LazyGridItemScope.(index: Int, itemCount: Int, item: T) -> Unit,
    itemKey: ((item: T) -> Any)? = null,
    itemContentType: ((item: T) -> Any)? = null,
    itemSpan: ((item: T?) -> GridItemSpan)? = null,
    startItems: (LazyGridScope.(pagingState: PagingState<T>) -> Unit)? = null,
    endItems: (LazyGridScope.(pagingState: PagingState<T>) -> Unit)? = null,
    noItemsPlaceholder: @Composable LazyGridItemScope.() -> Unit = {},
    onLoadingError: @Composable LazyGridItemScope.(pagingState: PagingState<T>) -> Unit = {},
    appendIndicator: (@Composable LazyGridItemScope.() -> Unit)? = {
        CircularProgressIndicator(
            color = defaultIndicatorColor,
            trackColor = defaultIndicatorTrackColor
        )
    }
) {
    val pagingItems = pagingDataFlow.collectAsLazyPagingItems()

    PagingVerticalGrid(
        modifier = modifier,
        pagingItems = pagingItems,
        columns = columns,
        contentPadding = contentPadding,
        state = state,
        reverseLayout = reverseLayout,
        verticalArrangement = verticalArrangement,
        horizontalArrangement = horizontalArrangement,
        flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled,
        placeholderItemsNum = placeholderItemsNum,
        defaultIndicatorTrackColor = defaultIndicatorTrackColor,
        defaultIndicatorColor = defaultIndicatorColor,
        showSnackbar = showSnackbar,
        itemKey = itemKey,
        itemContentType = itemContentType,
        itemSpan = itemSpan,
        placeholderItem = placeholderItem,
        startItems = startItems,
        noItemsPlaceholder = noItemsPlaceholder,
        appendIndicator = appendIndicator,
        endItems = endItems,
        onLoadingError = onLoadingError,
        item = item
    )
}

@Composable
fun <T : Any> PagingVerticalGrid(
    modifier: Modifier = Modifier,
    pagingItems: LazyPagingItems<T>,
    columns: GridCells,
    contentPadding: PaddingValues = PaddingValues(),
    state: LazyGridState = rememberLazyGridState(),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical = if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    placeholderItemsNum: Int = DEFAULT_PLACEHOLDER_ITEMS_NUM,
    defaultIndicatorTrackColor: Color = ProgressIndicatorDefaults.circularTrackColor,
    defaultIndicatorColor: Color = ProgressIndicatorDefaults.circularColor,
    showSnackbar: suspend (message: String) -> Unit,
    placeholderItem: @Composable LazyGridItemScope.(index: Int) -> Unit,
    item: @Composable LazyGridItemScope.(index: Int, itemCount: Int, item: T) -> Unit,
    itemKey: ((item: T) -> Any)? = null,
    itemContentType: ((item: T) -> Any)? = null,
    itemSpan: ((item: T?) -> GridItemSpan)? = null,
    startItems: (LazyGridScope.(pagingState: PagingState<T>) -> Unit)? = null,
    endItems: (LazyGridScope.(pagingState: PagingState<T>) -> Unit)? = null,
    noItemsPlaceholder: @Composable LazyGridItemScope.() -> Unit = {},
    onLoadingError: @Composable LazyGridItemScope.(pagingState: PagingState<T>) -> Unit = {},
    appendIndicator: (@Composable LazyGridItemScope.() -> Unit)? = {
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
        LazyVerticalGrid(
            modifier = modifier,
            columns = columns,
            contentPadding = contentPadding,
            state = if (pagingState.isLoading) rememberLazyGridState() else state,
            reverseLayout = reverseLayout,
            verticalArrangement = verticalArrangement,
            horizontalArrangement = horizontalArrangement,
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
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        noItemsPlaceholder()
                    }
                }

                pagingState.isLoadingError -> {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        onLoadingError(pagingState)
                    }
                }

                else -> {
                    items(
                        count = pagingState.pagingItems.itemCount,
                        key = itemKey?.let {
                            pagingState.pagingItems.itemKey(itemKey)
                        },
                        contentType = pagingState.pagingItems.itemContentType(itemContentType),
                        span = itemSpan?.let {
                            { index ->
                                itemSpan(pagingState.pagingItems[index])
                            }
                        }
                    ) { index ->
                        pagingState.pagingItems[index]?.let { item ->
                            item(index, pagingState.pagingItems.itemCount, item)
                        } ?: run {
                            placeholderItem(index)
                        }
                    }

                    if (pagingState.isAppendLoading && appendIndicator != null) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
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