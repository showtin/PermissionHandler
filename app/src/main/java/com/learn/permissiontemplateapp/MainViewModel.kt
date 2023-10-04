package com.learn.permissiontemplateapp

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class MainViewModel(

) : ViewModel() {
    val visiblePermissionDialogQueue = mutableStateListOf<String>()


    //TODO Когда мы будем давать каждое разрешение из списка мы будем удалять это разрешение из списка
    fun dismissDialog() {
        visiblePermissionDialogQueue.removeFirst()
    }


    //TODO Если мы не предоставляем разрешение тогда она добавляется в список visiblePermissionDialogQueue
    fun onPermissionResult(
        permission: String,
        isGranted: Boolean,
    ) {
        if (!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
    }
}