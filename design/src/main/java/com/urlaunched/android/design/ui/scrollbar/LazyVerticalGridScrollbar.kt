package com.urlaunched.android.design.ui.scrollbar

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.urlaunched.android.design.ui.scrollbar.controller.rememberLazyGridStateController
import com.urlaunched.android.design.ui.scrollbar.generic.ElementScrollbar

@Composable
fun LazyVerticalGridScrollbar(
    state: LazyGridState,
    modifier: Modifier = Modifier,
    settings: ScrollbarSettings = ScrollbarSettings.Default,
    indicatorContent: (@Composable (index: Int, isThumbSelected: Boolean) -> Unit)? = null,
    content: @Composable () -> Unit
) {
    if (!settings.enabled) {
        content()
    } else {
        Box(modifier) {
            content()
            InternalLazyVerticalGridScrollbar(
                state = state,
                settings = settings,
                indicatorContent = indicatorContent
            )
        }
    }
}

/**
 * Use this variation if you want to place the scrollbar independently of the list position
 */
@Composable
fun InternalLazyVerticalGridScrollbar(
    state: LazyGridState,
    modifier: Modifier = Modifier,
    settings: ScrollbarSettings = ScrollbarSettings.Default,
    indicatorContent: (@Composable (index: Int, isThumbSelected: Boolean) -> Unit)? = null
) {
    val controller = rememberLazyGridStateController(
        state = state,
        thumbMinLength = settings.thumbMinLength,
        thumbMaxLength = settings.thumbMaxLength,
        alwaysShowScrollBar = settings.alwaysShowScrollbar,
        selectionMode = settings.selectionMode,
        orientation = Orientation.Vertical
    )

    ElementScrollbar(
        orientation = Orientation.Vertical,
        stateController = controller,
        modifier = modifier,
        settings = settings,
        indicatorContent = indicatorContent
    )
}