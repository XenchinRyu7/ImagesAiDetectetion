package com.saefulrdevs.imagesconverter.ui.screens.documentscannerscreen

import android.app.Activity
import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.saefulrdevs.imagesconverter.data.model.PDF
import com.saefulrdevs.imagesconverter.viewmodel.PDFViewModel
import com.saefulrdevs.imagesconverter.viewmodel.PDFViewModelFactory
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun DocumentScannerScreen() {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModelFactory = PDFViewModelFactory(application)
    val pdfViewModel: PDFViewModel = viewModel(factory = viewModelFactory)

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

    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var showInputDialog by remember { mutableStateOf(false) }
    var inputFileName by remember { mutableStateOf("") }
    var scannedUri by remember { mutableStateOf<Uri?>(null) }

    val scannerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val scannedResult = GmsDocumentScanningResult.fromActivityResultIntent(data)
                
                scannedResult?.let { result ->
                    // Get individual page images
                    imageUris = result.pages?.map { it.imageUri } ?: emptyList()
                    
                    // Get PDF if available
                    result.pdf?.let { pdf ->
                        scannedUri = pdf.uri
                        showInputDialog = true
                    }
                }
            } else {
                // Handle cancellation or error
                Toast.makeText(context, "Document scanning cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    )
    
    // Auto-start scanner when screen loads
    LaunchedEffect(Unit) {
        scanner.getStartScanIntent(context as Activity)
            .addOnSuccessListener { intentSender ->
                scannerLauncher.launch(
                    IntentSenderRequest.Builder(intentSender).build()
                )
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    context,
                    "Failed to start scanner: ${exception.message}",
                    Toast.LENGTH_LONG
                ).show()
                Log.e("DocumentScanner", "Failed to start scanner", exception)
            }
    }

    if (showInputDialog) {
        AlertDialog(
            onDismissRequest = { showInputDialog = false },
            title = { Text(text = "Enter File Name") },
            text = {
                OutlinedTextField(
                    value = inputFileName,
                    onValueChange = { inputFileName = it },
                    label = { Text("File Name") }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showInputDialog = false
                        val fileName =
                            if (inputFileName.isNotBlank()) inputFileName else "scan" + System.currentTimeMillis()
                        scannedUri?.let { uri ->
                            try {
                                val resolver = context.contentResolver
                                val inputStream = resolver.openInputStream(uri)
                                
                                if (inputStream == null) {
                                    Toast.makeText(context, "Failed to read PDF file", Toast.LENGTH_SHORT).show()
                                    return@let
                                }
                                
                                val directory = File(
                                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                                    "Images Converter"
                                )

                                if (!directory.exists()) {
                                    directory.mkdirs()
                                }

                                val outputFile = File(directory, "$fileName.pdf")

                                inputStream.use { input ->
                                    outputFile.outputStream().use { output ->
                                        input.copyTo(output)
                                    }
                                }

                                if (outputFile.exists()) {
                                    val pdfSize = outputFile.length()
                                    val pdfDate = outputFile.lastModified()

                                    val newPDF = PDF(
                                        name = outputFile.name,
                                        size = pdfSize,
                                        dateCreated = pdfDate
                                    )

                                    Log.d(
                                        "DocumentScannerScreen",
                                        "Saving PDF: ${newPDF.name}, Size: ${newPDF.size}, Date Created: ${newPDF.dateCreated}"
                                    )
                                    pdfViewModel.insert(newPDF)

                                    Toast.makeText(
                                        context,
                                        "PDF Saved Successfully: ${outputFile.absolutePath}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Toast.makeText(context, "Failed to Save PDF", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Log.e("DocumentScannerScreen", "Error saving PDF", e)
                                Toast.makeText(
                                    context,
                                    "Error saving PDF: ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showInputDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Document Scanner") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth(),
                windowInsets = WindowInsets(
                    top = 0.dp,
                    bottom = 0.dp
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        (context as? Activity)?.onBackPressed()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = it)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (imageUris.isNotEmpty()) {
                imageUris.forEach { uri ->
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Starting Document Scanner...",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Please wait while we prepare the scanner",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
