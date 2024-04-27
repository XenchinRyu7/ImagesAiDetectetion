package com.saefulrdevs.imagesconverter.ui.screens.textrecognitionscreen

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.saefulrdevs.imagesconverter.R
import java.util.concurrent.Executors

@OptIn(ExperimentalGetImage::class)
@Composable
fun TextRecognitionScreen() {
    val context = LocalContext.current
    val previewView: PreviewView = remember { PreviewView(context) }
    val cameraController = remember { LifecycleCameraController(context) }
    val lifecycleOwner = LocalLifecycleOwner.current

    cameraController.bindToLifecycle(lifecycleOwner)
    cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    previewView.controller = cameraController

    val executor = remember { Executors.newSingleThreadExecutor() }
    val textRecognizer =
        remember { TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS) }

    var text by rememberSaveable {
        mutableStateOf("")
    }
    var isLoading by remember { mutableStateOf(false) }

    var isFlashEnabled by remember { mutableStateOf(false) }

    val cameraManager =
        LocalContext.current.getSystemService(Context.CAMERA_SERVICE) as CameraManager

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.Center)
            )
        } else {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        isFlashEnabled = !isFlashEnabled
                        toggleFlash(cameraManager, isFlashEnabled)
                    },
                    modifier = Modifier.padding(end = 8.dp)
                ) {

                    Icon(
                        painter = painterResource(id = if (isFlashEnabled) R.drawable.flash_off else R.drawable.flash_on),
                        contentDescription = "Flash",
                        modifier = Modifier.size(24.dp)
                    )
                }
                IconButton(
                    modifier = Modifier
                        .padding(16.dp),
                    onClick = {
                        isLoading = true
                        cameraController.setImageAnalysisAnalyzer(executor) { imageProxy ->
                            imageProxy.image?.let { image ->
                                val img = InputImage.fromMediaImage(
                                    image,
                                    imageProxy.imageInfo.rotationDegrees
                                )

                                textRecognizer.process(img).addOnCompleteListener { task ->
                                    isLoading = false
                                    text =
                                        if (!task.isSuccessful) task.exception!!.localizedMessage.toString()
                                        else task.result.text

                                    cameraController.clearImageAnalysisAnalyzer()
                                    imageProxy.close()
                                }
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(54.dp)
                    )
                }
            }
        }
    }

    if (text.isNotEmpty()) {
        Dialog(onDismissRequest = { text = "" }) {
            Card(modifier = Modifier.fillMaxWidth(0.8f)) {
                Text(
                    text = text,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp),
                    style = MaterialTheme.typography.bodySmall
                )
                Button(
                    onClick = { text = "" },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Text(text = "Done")
                }
            }
        }
    }
}

private fun toggleFlash(cameraManager: CameraManager, isFlashEnabled: Boolean) {
    try {
        val cameraIds = cameraManager.cameraIdList

        cameraIds.forEach { cameraId ->
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            val availableModes = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)

            if (availableModes != null && availableModes) {
                cameraManager.setTorchMode(cameraId, isFlashEnabled)
            }
        }
    } catch (e: CameraAccessException) {
        e.printStackTrace()
    }
}
