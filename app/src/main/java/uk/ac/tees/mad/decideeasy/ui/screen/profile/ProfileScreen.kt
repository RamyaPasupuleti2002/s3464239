package uk.ac.tees.mad.decideeasy.ui.screen.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import uk.ac.tees.mad.decideeasy.utils.Constants

@Composable
fun ProfileScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    Box(contentAlignment = Alignment.Center,modifier = Modifier.fillMaxSize()){
        Column {
            Text("Profile Screen")
            Button({
                auth.signOut()
                navController.navigate(Constants.AUTH_SCREEN){
                    popUpTo(Constants.PROFILE_SCREEN){
                        inclusive = true
                    }
                }
            }) {
                Text("Log out")
            }
        }
    }
}