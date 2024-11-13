package com.urlaunched.android.snapshottesting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalInspectionMode
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.cash.paparazzi.detectEnvironment
import com.android.ide.common.rendering.api.SessionParams
import com.android.resources.NightMode
import com.android.resources.ScreenOrientation
import com.urlaunched.android.design.ui.paging.LocalPagingMode
import com.urlaunched.android.design.ui.paging.LocalPagingModeEnum
import org.junit.Rule
import java.util.Locale

abstract class BaseSnapshotTest(
    renderingMode: SessionParams.RenderingMode = SessionParams.RenderingMode.NORMAL,
    private val supportsDarkMode: Boolean = false,
    private val deviceConfig: DeviceConfig = DeviceConfig.PIXEL_5,
    private val supportsLandscape: Boolean = false
) {
    init {
        Locale.setDefault(Locale.ENGLISH)
    }

    @get:Rule
    open val paparazzi =
        Paparazzi(
            deviceConfig = deviceConfig,
            renderingMode = renderingMode,
            showSystemUi = false,
            maxPercentDifference = 0.1,
            environment = detectEnvironment().run {
                copy(compileSdkVersion = 33, platformDir = platformDir.replace("34", "33"))
            }
        )

    fun snapshot(pagingMode: LocalPagingModeEnum? = null, composable: @Composable () -> Unit) {
        paparazzi.unsafeUpdateConfig(
            deviceConfig = deviceConfig.copy(
                nightMode = NightMode.NOTNIGHT
            )
        )

        paparazzi.snapshot("light") {
            CompositionLocalProvider(
                LocalInspectionMode provides true,
                LocalPagingMode provides pagingMode
            ) {
                composable()
            }
        }

        if (supportsDarkMode) {
            paparazzi.unsafeUpdateConfig(
                deviceConfig = deviceConfig.copy(
                    nightMode = NightMode.NIGHT
                )
            )

            paparazzi.snapshot("dark") {
                CompositionLocalProvider(
                    LocalInspectionMode provides true,
                    LocalPagingMode provides pagingMode
                ) {
                    composable()
                }
            }
        }

        if (supportsLandscape) {
            paparazzi.unsafeUpdateConfig(
                deviceConfig = deviceConfig.copy(
                    orientation = ScreenOrientation.LANDSCAPE,
                    screenHeight = deviceConfig.screenWidth,
                    screenWidth = deviceConfig.screenHeight
                )
            )

            paparazzi.snapshot("landscape") {
                CompositionLocalProvider(
                    LocalInspectionMode provides true,
                    LocalPagingMode provides pagingMode
                ) {
                    composable()
                }
            }
        }
    }
}