package com.saefulrdevs.imagesconverter.ui.screens.documentscannerscreen

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult

@Composable
fun DocumentScannerScreen() {
    val context = LocalContext.current

    val options = GmsDocumentScannerOptions.Builder()
        .setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_FULL)
        .setGalleryImportAllowed(true)
        .setPageLimit(5)
        .setResultFormats(
            GmsDocumentScannerOptions.RESULT_FORMAT_JPEG,
            GmsDocumentScannerOptions.RESULT_FORMAT_PDF
        )
        .build()

    val scanner = GmsDocumentScanning.getClient(options)


    var imageUris by remember {
        mutableStateOf<List<Uri>>(emptyList())
    }

    val scannerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == ComponentActivity.RESULT_OK) {
                val data = result.data
                val scannedResult = GmsDocumentScanningResult.fromActivityResultIntent(data)
                imageUris = scannedResult?.pages?.map { it.imageUri } ?: emptyList()

                scannedResult?.pdf?.let { pdf ->
                    val outputStream = context.openFileOutput("scan.pdf", Context.MODE_PRIVATE)
                    context.contentResolver.openInputStream(pdf.uri)?.use { inputStream ->
                        outputStream.use { output ->
                            inputStream.copyTo(output)
                        }
                    }
                }
            }
        }
    )
    imageUris.forEach { uri ->
        AsyncImage(
            model = uri,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )
    }

    scanner.getStartScanIntent(context as Activity)
        .addOnSuccessListener {
            scannerLauncher.launch(
                IntentSenderRequest.Builder(it).build()
            )
        }
        .addOnFailureListener {
            Toast.makeText(
                context,
                it.message,
                Toast.LENGTH_LONG
            ).show()
        }
}