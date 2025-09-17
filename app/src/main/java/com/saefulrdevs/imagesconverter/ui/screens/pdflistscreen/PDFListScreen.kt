package com.saefulrdevs.imagesconverter.ui.screens.pdflistscreen

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.saefulrdevs.imagesconverter.data.model.PDF
import com.saefulrdevs.imagesconverter.viewmodel.PDFViewModel
import com.saefulrdevs.imagesconverter.viewmodel.PDFViewModelFactory
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PDFListScreen(pdfViewModel: PDFViewModel, paddingValues: PaddingValues) {
    val pdfList by pdfViewModel.allPDFs.observeAsState(initial = emptyList())

    Box(modifier = Modifier.padding(paddingValues)) {
        LazyColumn(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxSize()
        ) {
            items(pdfList) { pdf ->
                PDFItem(pdf, pdfViewModel)
            }
        }
    }
}

@Composable
fun PDFItem(pdf: PDF, pdfViewModel: PDFViewModel) {
    val context = LocalContext.current
    var showDetailDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var deleteFromExternal by remember { mutableStateOf(false) }

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),

        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                val file = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                    "Images Converter/${pdf.name}"
                )
                val uri =
                    FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, "application/pdf")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(intent, "Buka PDF dengan"))
            }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "File Name: ${pdf.name}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Size: ${readableFileSize(pdf.size)}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Created: ${
                        SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss",
                            Locale.getDefault()
                        ).format(Date(pdf.dateCreated))
                    }",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(onClick = { showDetailDialog = true }) {
                Icon(Icons.Default.Info, contentDescription = "More")
            }
        }
    }

    if (showDetailDialog) {
        AlertDialog(
            onDismissRequest = { showDetailDialog = false },
            title = { Text("Detail PDF") },
            text = {
                Column {
                    Text("File Name: ${pdf.name}")
                    Text(
                        text = "Size: ${readableFileSize(pdf.size)}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Created: ${
                            SimpleDateFormat(
                                "yyyy-MM-dd HH:mm:ss",
                                Locale.getDefault()
                            ).format(Date(pdf.dateCreated))
                        }"
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showDetailDialog = false }) {
                    Text("Close")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDetailDialog = false
                    showDeleteDialog = true
                }) {
                    Text("Delete")
                }
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus PDF") },
            text = {
                Column {
                    Text("Apakah anda yakin ingin menghapus file ini?")
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = deleteFromExternal,
                            onCheckedChange = { deleteFromExternal = it }
                        )
                        Text("Hapus juga dari external storage")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (deleteFromExternal) {
                        val file = File(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                            "Images Converter/${pdf.name}"
                        )
                        if (file.exists()) {
                            file.delete()
                        }
                    }
                    pdfViewModel.delete(pdf)
                    showDeleteDialog = false
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

fun readableFileSize(size: Long): String {
    if (size <= 0) return "0 B"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
    return String.format(
        "%.1f %s",
        size / Math.pow(1024.0, digitGroups.toDouble()),
        units[digitGroups]
    )
}




