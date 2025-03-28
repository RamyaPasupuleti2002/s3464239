package uk.ac.tees.mad.decideeasy.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
fun HomeScreen(navController: NavController,
               viewModel: HomeViewModel = hiltViewModel()
) {
    val isShaken by viewModel.isShaken.collectAsState(false)
    var listening by remember { mutableStateOf(false) }
    val choices = listOf("Yes", "No", "Ok", "Do it", "Don't")
    Scaffold(
        containerColor = Color(0xFFFBFBFB),
        topBar = {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(color = Color(0xFFc2e868))
                    .fillMaxWidth()
                    .padding(vertical = 36.dp, horizontal = 18.dp)
                ) {
                AsyncImage(
                    model = "link",
                    contentDescription = "Weather Image",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .border(1.dp, color = Color.Gray, shape = CircleShape)
                        .clickable {
                            navController.navigate(Constants.PROFILE_SCREEN)
                        },
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.placeholder_profile),
                    error = painterResource(id = R.drawable.placeholder_profile)
                )
                Column {
                    Text("Welcome back",
                        fontSize = 20.sp,
                        color = Color(0xFF355F2E),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    Text("My Name",
                        fontSize = 22.sp,
                        color = Color(0xFF355F2E),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {navController.navigate(Constants.CUSTOM_SCREEN)},
                containerColor = Color(0xFFc2e868),
                modifier = Modifier.padding(bottom = 30.dp, end = 12.dp)
                ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "fab",
                    tint = Color.Black
                )
            }
        }
    ) { paddingValues ->
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier.padding(paddingValues).fillMaxSize()){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()) {
                if (listening && !isShaken){
                    Text( "Shake to get",
                        modifier = Modifier.padding(bottom = 18.dp)
                        )
                }
                if (isShaken){
                    listening = false
                    Text( choices.random(),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 18.dp)
                        )
                }
                if(!listening || isShaken){
                    TextButton(onClick = {
                        viewModel.startListening()
                        listening = true
                    },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFc2e868)
                        ),
                        modifier = Modifier
                            .padding(horizontal = 22.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Start",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}