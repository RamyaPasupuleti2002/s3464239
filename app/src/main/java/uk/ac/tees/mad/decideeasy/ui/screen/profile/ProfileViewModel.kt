package uk.ac.tees.mad.decideeasy.ui.screen.profile

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val application: Application
):ViewModel() {
    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> = _imageUri

    private val _hasCameraPermission = MutableStateFlow(false)
    val hasCameraPermission: StateFlow<Boolean> = _hasCameraPermission

    fun checkCameraPermission(context: Context) {
        _hasCameraPermission.value =
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    fun generateTempUri(): Uri {
        val context = application.applicationContext
        val file = File(context.cacheDir, "temp_image.jpg")
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }

    fun setImageUri(uri: Uri) {
        _imageUri.value = uri
    }
}