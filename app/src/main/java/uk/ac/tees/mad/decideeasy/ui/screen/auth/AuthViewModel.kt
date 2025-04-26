package uk.ac.tees.mad.decideeasy.ui.screen.auth

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth:FirebaseAuth
):ViewModel() {

    private val _isLoginSuccess = MutableStateFlow(false)
    val isLoginSuccess:StateFlow<Boolean> get() = _isLoginSuccess
    private val _isLoading = MutableStateFlow(false)
    val isLoading:StateFlow<Boolean> get() = _isLoading

    fun loginUser(email:String, password:String, context: Context){
        if (email.isEmpty()){
            Toast.makeText(context, "Email should not be empty.", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.length<6){
            Toast.makeText(context, "Password length must be at least six.", Toast.LENGTH_SHORT).show()
            return
        }
        _isLoading.value = true
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task->
            _isLoading.value = false
            if (task.isSuccessful){
                _isLoginSuccess.value = true
                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun createUser(name:String, email: String, password: String, context: Context){
        if (name.isEmpty()){
            Toast.makeText(context, "Name should not be empty.", Toast.LENGTH_SHORT).show()
            return
        }
        if (email.isEmpty()){
            Toast.makeText(context, "Email should not be empty.", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.length<6){
            Toast.makeText(context, "Password length must be at least six.", Toast.LENGTH_SHORT).show()
            return
        }
        _isLoading.value = true
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {task->
            _isLoading.value = false
            if (task.isSuccessful){
                val profileUpdate = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
                auth.currentUser?.updateProfile(profileUpdate)?.addOnSuccessListener {
                    _isLoginSuccess.value = true
                    Toast.makeText(context, "New user created", Toast.LENGTH_SHORT).show()
                }
                    ?.addOnFailureListener {
                        Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()
                    }
            }
            else{
                Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}