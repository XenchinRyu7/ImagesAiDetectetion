package com.saefulrdevs.imagesconverter.ui.screens.homescreen

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.saefulrdevs.imagesconverter.R
import com.saefulrdevs.imagesconverter.ui.screens.composable.FabButtonItem
import com.saefulrdevs.imagesconverter.ui.screens.composable.FabButtonMain
import com.saefulrdevs.imagesconverter.ui.screens.composable.FabButtonSub
import com.saefulrdevs.imagesconverter.ui.screens.composable.MultiFloatingActionButton

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToDocumentScannerScreen: () -> Unit,
    navigateToCameraRecognitionScreen: () -> Unit
) {
    val context = LocalContext.current
    val documentScreenLabel = stringResource(id = R.string.DocumentScreen)
    val textScreenLabel = stringResource(id = R.string.TextScan)

    val cameraPermission = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    )

    val showRationalDialog = remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        if (!cameraPermission.status.isGranted) {
            if (cameraPermission.status.shouldShowRationale) {
                // Show a rationale if needed (optional)
                showRationalDialog.value = true
            } else {
                // Request the permission
                cameraPermission.launchPermissionRequest()
                Toast.makeText(context, "Permission Given Already", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    if (showRationalDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showRationalDialog.value = false
            },
            title = {
                Text(
                    text = "Permission",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Text(
                    "The notification is important for this app. Please grant the permission.",
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRationalDialog.value = false
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null)
                        )
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        ContextCompat.startActivity(context, intent, null)

                    }) {
                    Text("OK", style = TextStyle(color = Color.Black))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showRationalDialog.value = false
                    }) {
                    Text("Cancel", style = TextStyle(color = Color.Black))
                }
            },
        )
    }

    Scaffold(
        floatingActionButton = {
            MultiFloatingActionButton(
                items = listOf(
                    FabButtonItem(
                        iconRes = ImageVector.vectorResource(id = R.drawable.scan_document),
                        label = stringResource(id = R.string.DocumentScreen)
                    ),
                    FabButtonItem(
                        iconRes = ImageVector.vectorResource(id = R.drawable.text),
                        label = stringResource(id = R.string.TextScan)
                    ),
                ),
                onFabItemClicked = { fabItem ->
                    when (fabItem.label) {
                        documentScreenLabel -> navigateToDocumentScannerScreen()
                        textScreenLabel -> navigateToCameraRecognitionScreen()
                    }
                    Toast.makeText(context, fabItem.label, Toast.LENGTH_SHORT).show()
                },
                fabIcon = FabButtonMain(),
                fabOption = FabButtonSub()
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

        }
    }
}