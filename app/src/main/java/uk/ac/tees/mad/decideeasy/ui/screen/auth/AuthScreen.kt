package uk.ac.tees.mad.decideeasy.ui.screen.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import uk.ac.tees.mad.decideeasy.R
import uk.ac.tees.mad.decideeasy.utils.Constants

@Composable
fun AuthScreen(viewModel: AuthViewModel = hiltViewModel(),
               navController: NavController
) {
    val isLoginSuccess by viewModel.isLoginSuccess.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(0) }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(isLoginSuccess) {
        if(isLoginSuccess){
            navController.navigate(Constants.HOME_SCREEN){
                popUpTo(Constants.AUTH_SCREEN){
                    inclusive = true
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
                .weight(1f)
        ){
            Spacer(Modifier.height(30.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.app_logo),
                    contentDescription = "app_logo",
                    modifier = Modifier.size(64.dp)
                )
                Text("DecideEasy",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    color = Color(0xFF355F2E)
                )
            }
            Spacer(Modifier.height(16.dp))
            Text("Welcome to DecideEasy",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = Color.Black
            )
            Spacer(Modifier.height(12.dp))
            Text("Sign up or login below to manage your data", color = Color.Gray)
            Spacer(Modifier.weight(1f))
            SecondaryTextTabs(
                onTabSelected = {selectedTab =it}
            )
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.LightGray)
            .weight(2f)
        ){
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(Modifier.height(22.dp))
                if (selectedTab==1) {
                    TextField(
                        value = name,
                        onValueChange = {name = it},
                        leadingIcon = { Icon(
                            Icons.Outlined.Person,
                            contentDescription = "",
                            tint = Color.Black
                        ) },
                        placeholder = { Text("Name") },
                        colors = TextFieldDefaults.colors(focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.White,
                            unfocusedIndicatorColor = Color.White,
                            unfocusedTextColor = Color(0xFF355F2E),
                            focusedTextColor = Color(0xFF355F2E)
                        ),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .fillMaxWidth()
                    )
                }
                TextField(
                    value = email,
                    onValueChange = {email = it},
                    leadingIcon = { Icon(
                        Icons.Outlined.Email,
                        contentDescription = "",
                        tint = Color.Black
                    ) },
                    placeholder = { Text("Email", color = Color(0xFF355F2E)) },
                    colors = TextFieldDefaults.colors(focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.White,
                        unfocusedTextColor = Color(0xFF355F2E),
                        focusedTextColor = Color(0xFF355F2E)
                    ),
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .fillMaxWidth()
                )

                TextField(
                    value = password,
                    onValueChange = {password = it},
                    leadingIcon = { Icon(
                        Icons.Outlined.Lock,
                        contentDescription = "",
                        tint = Color.Black
                    ) },
                    placeholder = { Text("Password", color = Color(0xFF355F2E)) },
                    colors = TextFieldDefaults.colors(focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.White,
                        unfocusedTextColor = Color(0xFF355F2E),
                        focusedTextColor = Color(0xFF355F2E)
                        ),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) ImageVector.vectorResource(R.drawable.baseline_visibility_off_24)
                        else ImageVector.vectorResource(R.drawable.baseline_visibility_24)
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                        }
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .fillMaxWidth()
                )
                Spacer(Modifier.weight(1f))
                if (!isLoading) {
                    TextButton(onClick = {
                        if (selectedTab==0){
                            viewModel.loginUser(email,password,context)
                        }
                        else{
                            viewModel.createUser(name,email, password, context)
                        }
                    },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFc2e868)
                        ),
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 30.dp)
                            .fillMaxWidth()
                        ) {
                        Text(
                            text = if (selectedTab==0) "Login" else "Sign Up",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
                else{
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 30.dp)
                        .fillMaxWidth()
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFF355F2E),
                            strokeWidth = 2.dp
                        )
                    }
                }
            }
        }
    }
}