package fireforestsouls.feedwall

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.unit.DpSize

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Feedwall",
        state = WindowState(
            size = DpSize(width = 360.dp, height = 800.dp)
        )

    ) {
        App()
    }
}