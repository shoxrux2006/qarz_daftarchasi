package uz.gita.qarzdaftarchasi.presentation.imagePreview

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import cafe.adriel.voyager.androidx.AndroidScreen
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlin.math.roundToInt

class ImagePreviewScreen(val path: String) : AndroidScreen() {
    @Composable
    override fun Content() {
        ImagePreviewScreenContent()
    }

    @Composable
    fun ImagePreviewScreenContent() {
        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        offsetX.roundToInt(),
                        offsetY.roundToInt()
                    )
                }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            var scale by remember { mutableStateOf(1f) }

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(path)
                    .build(),
                contentDescription = "glass",
                modifier = Modifier
                    .fillMaxSize().background(MaterialTheme.colorScheme.background)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                    )
                    .transformable(rememberTransformableState { zoomChange, _, rotationChange ->
                        scale =
                            if (scale * zoomChange in 1f..3f) scale * zoomChange else scale
                    })
            )
        }

    }
}