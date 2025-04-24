package uk.ac.tees.mad.decideeasy.ui.screen.profile

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import uk.ac.tees.mad.decideeasy.R
import uk.ac.tees.mad.decideeasy.utils.Constants

@Composable
fun ProfileScreen(navController: NavController,viewModel: ProfileViewModel = hiltViewModel()) {
    var expanded by remember { mutableStateOf(false) }
    var uri:Uri? = null
    val context = LocalContext.current
    val name by viewModel.name.collectAsState()
    val email by viewModel.email.collectAsState()
    val imageUri by viewModel.imageUri.collectAsState()
    val hasPermission by viewModel.hasCameraPermission.collectAsState()
    var newName by remember { mutableStateOf(name) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            viewModel.checkCameraPermission(context)
            if (!isGranted) {
                Toast.makeText(context, "Camera permission is required!", Toast.LENGTH_SHORT).show()
            }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                uri?.let { viewModel.setImageUri(it)
                    viewModel.onPicSelected(true)
                }
                Toast.makeText(context, "Picture Captured", Toast.LENGTH_SHORT).show()
            }
        }
    )

    LaunchedEffect(Unit) {
        viewModel.checkCameraPermission(context)
    }

    Scaffold(
        topBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFFc2e868))
                    .padding(top = 20.dp)
            ) {
                Text(
                    "Profile",
                    fontSize = 22.sp,
                    color = Color(0xFF355F2E),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterStart)
                )
            }
        }
    ) {padding->
        Box(modifier = Modifier
            .padding(padding)
            .fillMaxSize()
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier
                    .padding(16.dp)
                    .background(
                        color = Color(0xFFc2e868),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .fillMaxWidth()
                    .padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,

                    ) {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = "Weather Image",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .border(1.dp, color = Color.Gray, shape = CircleShape),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = R.drawable.placeholder_profile),
                            error = painterResource(id = R.drawable.placeholder_profile)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                name,
                                fontSize = 22.sp,
                                color = Color(0xFF355F2E),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                            Text(
                                email,
                                fontSize = 20.sp,
                                color = Color(0xFF355F2E),
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }

                        IconButton(onClick = {expanded = true}) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = Color(0xFF355F2E)
                            )
                        }
                    }
                    AnimatedVisibility(expanded) {
                        Column {
                            OutlinedTextField(
                                value = newName,
                                onValueChange = { newName1 -> newName = newName1 },
                                label = { Text("name") },
                                modifier = Modifier.padding(top = 16.dp)
                                    .fillMaxWidth()
                            )
                            TextButton(onClick = {
                                if (hasPermission) {
                                    uri = viewModel.generateTempUri()
                                    //viewModel.setImageUri(uri)
                                    cameraLauncher.launch(uri!!)
                                } else {
                                    permissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            },
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(2.dp,Color(0xFF355F2E)),
                                modifier = Modifier.padding(top = 8.dp)
                                    .fillMaxWidth()
                                ) {
                                Text("Open Camera")
                            }
                            Row {
                                Spacer(Modifier.weight(1f))
                                TextButton({
                                    viewModel.onPicSelected(false)
                                    expanded = false }) {
                                    Text("Cancel", color = Color.White)
                                }
                                TextButton({
                                    viewModel.updateProfile(newName,context)
                                    expanded = false },modifier = Modifier.padding(start = 12.dp)) {
                                    Text("Save", color = Color(0xFF355F2E), fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                TextButton(onClick = {
                    viewModel.signOut()
                    navController.navigate(Constants.AUTH_SCREEN){
                        popUpTo(0)
                    }
                },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFc2e868),
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp,Color(0xFF355F2E)),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                    ) {
                    Text("Sign Out", color = Color(0xFF355F2E), fontSize = 20.sp)
                }
            }
        }
    }
}