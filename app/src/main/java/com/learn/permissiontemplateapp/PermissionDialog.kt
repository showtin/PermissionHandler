package com.learn.permissiontemplateapp

import android.Manifest
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun PermissionDialog(
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    AlertDialog(onDismissRequest = onDismiss,
        confirmButton = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Divider()
                Text(
                    text = if (isPermanentlyDeclined) {
                        "Grant permission"
                    } else {
                        "Ok"
                    }, fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (isPermanentlyDeclined) {
                                onGoToAppSettingsClick()
                            } else {
                                onOkClick()
                            }
                        }
                        .padding(16.dp)
                )
            }
        }, title = {
            Text(text = "Permission Required ")
        }, text = {
            Text(
                text = permissionTextProvider.getDescription(isPermanentlyDeclined = isPermanentlyDeclined)
            )
        }, modifier = modifier
    )
}


//TODO Создадим интерфейс который потом будем использовать для каждого нашего разрешения 16:00

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}

class CameraPermissionTextProvider : PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "It seems you permanently declined camera description. " +
                    "You can go to the app settings to grant it."
        } else {
            "This app needs access to your camera so that your friends " +
                    "can see you in a call."
        }
    }

}


class ReadContactsPermissionTextProvider : PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "It seems you permanently declined contacts permission. " +
                    "You can go to the app settings to grant it."
        } else {
            "This app needs access to your contacts so that " +
                    "we could to display your contacts information on screen."
        }
    }

}

class CallPhonePermissionTextProvider : PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "It seems you permanently declined phone calling permission. " +
                    "You can go to the app settings to grant it."
        } else {
            "This app needs phone calling permission so that you can talk " +
                    "to your friends"
        }
    }

}