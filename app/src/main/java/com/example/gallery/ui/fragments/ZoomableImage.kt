package com.example.gallery.ui.fragments

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import coil.compose.SubcomposeAsyncImage

@Composable
fun ZoomableImage(imageUri: String?) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    var imageSize by remember { mutableStateOf(IntSize.Zero) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var zoom by remember { mutableStateOf(1f) }

    SubcomposeAsyncImage(
        model = imageUri,
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .onSizeChanged { size = it }
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        offsetX = 0f
                        offsetY = 0f
                        zoom = 1f
                    }
                )
            }
            .pointerInput(Unit) {
                detectTransformGestures(
                    onGesture = { centroid, pan, gestureZoom, _ ->
                        val prevZoom = zoom
                        val newZoom = (zoom * gestureZoom).coerceIn(1f, 8f)
                        val cx = centroid.x - size.width / 2
                        val cy = centroid.y - size.height / 2

                        val newOffsetX = offsetX + cx / prevZoom - (cx / newZoom + pan.x / prevZoom)
                        val newOffsetY = offsetY + cy / prevZoom - (cy / newZoom + pan.y / prevZoom)

                        val maxX = (imageSize.width * newZoom - size.width).coerceAtLeast(0f) / (newZoom * 2)
                        val maxY = (imageSize.height * newZoom - size.height).coerceAtLeast(0f) / (newZoom * 2)

                        offsetX = newOffsetX.coerceIn(-maxX, maxX)
                        offsetY = newOffsetY.coerceIn(-maxY, maxY)
                        zoom = newZoom
                    }
                )
            }
            .graphicsLayer {
                translationX = -offsetX * zoom
                translationY = -offsetY * zoom
                scaleX = zoom
                scaleY = zoom
            },
        onSuccess = {
            val image = it.result.drawable
            imageSize = IntSize(image.minimumWidth, image.minimumHeight)
        }
    )

}
