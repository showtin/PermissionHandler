package com.learn.permissiontemplateapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.learn.permissiontemplateapp.ui.theme.PermissionTemplateAppTheme

class MainActivity : ComponentActivity() {

    private val permissionsToRequest = arrayOf(
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.CALL_PHONE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PermissionTemplateAppTheme {
                val viewModel = viewModel<MainViewModel>()
                val dialogQueue = viewModel.visiblePermissionDialogQueue

                val cameraPermissionResultLauncher =
                    rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission(),
                        onResult = { isGranted ->
                            viewModel.onPermissionResult(
                                permission = Manifest.permission.CAMERA,
                                isGranted = false
                            )
                        }
                    )

                val multiplePermissionResultLauncher =
                    rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestMultiplePermissions(),
                        onResult = { perms ->
                            permissionsToRequest.forEach { permission ->
                                viewModel.onPermissionResult(
                                    permission = permission,
                                    isGranted = perms[permission] == true // ?????????????????? 23:00
                                )
                            }
                        }
                    )


                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    Button(onClick = {
                        cameraPermissionResultLauncher.launch(
                            Manifest.permission.CAMERA
                        )
                    }) {
                        Text(text = "Request one permission")
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = {
                        multiplePermissionResultLauncher.launch(permissionsToRequest)
                    }) {
                        Text(text = "Request multiple permission")
                    }
                }

                dialogQueue.reversed().forEach { permission ->
                    PermissionDialog(
                        permissionTextProvider = when (permission) {
                            Manifest.permission.CAMERA -> CameraPermissionTextProvider()
                            Manifest.permission.CALL_PHONE -> CallPhonePermissionTextProvider()
                            Manifest.permission.READ_CONTACTS -> ReadContactsPermissionTextProvider()


                            else -> return@forEach
                        },
                        isPermanentlyDeclined = !shouldShowRequestPermissionRationale(permission),
                        onDismiss = viewModel::dismissDialog,
                        onOkClick = {
                            viewModel.dismissDialog()
                            multiplePermissionResultLauncher.launch(
                                arrayOf(permission)
                            )
                        },
                        onGoToAppSettingsClick = ::openAppSettings
                    )
                }
            }
        }
    }
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}