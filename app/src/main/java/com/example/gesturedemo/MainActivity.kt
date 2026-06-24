package com.example.gesturedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.gesturedemo.ui.theme.GestureDemoTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GestureDemoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    MultiTouchDemo(modifier)  // ← Выберите любую демонстрацию для тестирования
    // ClickDemo(modifier)
    // TapPressDemo(modifier)
    // PointerInputDrag(modifier)
    // ScrollableModifier(modifier)
    // ScrollModifiers(modifier)
}

// ============================================================
// ШАГ 3: ClickDemo
// ============================================================
@Composable
fun ClickDemo(modifier: Modifier = Modifier) {
    var colorState by remember { mutableStateOf(true) }
    var bgColor by remember { mutableStateOf(Color.Blue) }

    val clickHandler = {
        colorState = !colorState
        bgColor = if (colorState) Color.Blue else Color.DarkGray
    }

    Box(
        modifier = modifier
            .clickable { clickHandler() }
            .background(bgColor)
            .size(100.dp)
    )
}

// ============================================================
// ШАГ 4: TapPressDemo
// ============================================================
@Composable
fun TapPressDemo(modifier: Modifier = Modifier) {
    var textState by remember { mutableStateOf("Ожидание....") }

    val tapHandler = { status: String -> textState = status }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .padding(10.dp)
                .background(Color.Blue)
                .size(100.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = { tapHandler("onPress Detected") },
                        onDoubleTap = { tapHandler("onDoubleTap Detected") },
                        onLongPress = { tapHandler("onLongPress Detected") },
                        onTap = { tapHandler("onTap Detected") }
                    )
                }
        )
        Text(text = textState, modifier = Modifier.padding(16.dp))
    }
}

// ============================================================
// ШАГ 5: PointerInputDrag
// ============================================================
@Composable
fun PointerInputDrag(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        var xOffset by remember { mutableStateOf(0f) }
        var yOffset by remember { mutableStateOf(0f) }

        Box(
            modifier = Modifier
                .offset {
                    IntOffset(xOffset.roundToInt(), yOffset.roundToInt())
                }
                .background(Color.Blue)
                .size(100.dp)
                .pointerInput(Unit) {
                    detectDragGestures { _, distance ->
                        xOffset += distance.x
                        yOffset += distance.y
                    }
                }
        )
    }
}

// ============================================================
// ШАГ 6: ScrollableModifier
// ============================================================
@Composable
fun ScrollableModifier(modifier: Modifier = Modifier) {
    var offset by remember { mutableStateOf(0f) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .scrollable(
                orientation = Orientation.Vertical,
                state = rememberScrollableState { distance ->
                    offset += distance
                    distance
                }
            )
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .offset { IntOffset(0, offset.roundToInt()) }
                .background(Color.Red)
        )
    }
}

// ============================================================
// ШАГ 8: ScrollModifiers
// ============================================================
@Composable
fun ScrollModifiers(modifier: Modifier = Modifier) {
    val image = ImageBitmap.imageResource(id = R.drawable.vacation)

    Box(
        modifier = modifier
            .size(150.dp)
            .verticalScroll(rememberScrollState())
            .horizontalScroll(rememberScrollState())
    ) {
        Canvas(
            modifier = Modifier.size(360.dp, 270.dp)
        ) {
            drawImage(image = image, topLeft = Offset(0f, 0f))
        }
    }
}

// ============================================================
// ШАГ 9-10: MultiTouchDemo (сжатие + вращение)
// ============================================================
@Composable
fun MultiTouchDemo(modifier: Modifier = Modifier) {
    var scale by remember { mutableStateOf(1f) }
    var angle by remember { mutableStateOf(0f) }

    val state = rememberTransformableState { scaleChange, _, rotationChange ->
        scale *= scaleChange
        angle += rotationChange
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = modifier
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    rotationZ = angle
                )
                .transformable(state = state)
                .background(Color.Blue)
                .size(100.dp)
        )
    }
}

// ============================================================
// Предпросмотр
// ============================================================
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    GestureDemoTheme {
        MainScreen()
    }
}