package uk.ac.tees.mad.decideeasy.ui.screen.profile

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val application: Application,
    private val cloudinary: Cloudinary,
    private val auth: FirebaseAuth
):ViewModel() {
    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> = _imageUri

    private val _name = MutableStateFlow("Name")
    val name:StateFlow<String> get() = _name

    private val _email = MutableStateFlow("Email")
    val email:StateFlow<String> get() = _email

    private val _hasCameraPermission = MutableStateFlow(false)
    val hasCameraPermission: StateFlow<Boolean> = _hasCameraPermission
    private val _profilePicChange = MutableStateFlow(false)

    private val userId = auth.currentUser?.uid?:""

    init {
        viewModelScope.launch {
            _name.value = auth.currentUser?.displayName.toString()
            _imageUri.value = auth.currentUser?.photoUrl
            _email.value = auth.currentUser?.email.toString()
            Log.e("ImageUri",_imageUri.value?.path.toString())
        }
    }

    fun checkCameraPermission(context: Context) {
        _hasCameraPermission.value =
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    fun generateTempUri(): Uri {
        val context = application.applicationContext
        val file = File(context.cacheDir, "temp_image.jpg")
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }

    fun uploadImageToCloudinary(context: Context) {
        if (_imageUri.value!=null) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val file = File(context.cacheDir, "temp_image.jpg")

                    val request = cloudinary.uploader()
                        .upload(file.absolutePath, ObjectUtils.asMap(
                            "public_id", "profile_pictures/$userId",
                            "overwrite", true
                        ))

                    val imageUrl = request["secure_url"] as? String ?: ""

                    val profileUpdate = UserProfileChangeRequest.Builder()
                        .setPhotoUri(Uri.parse(imageUrl))
                        .build()
                    auth.currentUser?.updateProfile(profileUpdate)

                } catch (e: Exception) {
                    Log.e("Upload error", e.message.toString())
                }
            }
        }
    }

    fun updateProfile(newName:String, context: Context){
        _name.value = newName
        viewModelScope.launch {
            val profileUpdate = UserProfileChangeRequest.Builder()
                .setDisplayName(_name.value)
                .build()
            auth.currentUser?.updateProfile(profileUpdate)
            if (_profilePicChange.value){
                uploadImageToCloudinary(context)
            }
        }
    }

    fun onPicSelected(value:Boolean){
        _profilePicChange.value = value
    }

    fun setImageUri(uri: Uri) {
        _imageUri.value = uri
    }

    fun signOut(){
        auth.signOut()
    }
}