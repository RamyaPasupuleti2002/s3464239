package uk.ac.tees.mad.decideeasy.ui.screen.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.ac.tees.mad.decideeasy.ui.theme.DecideEasyTheme

@Composable
fun CustomScreen() {
    var showDialog by remember { mutableStateOf(false) }
    var savedText by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            Box(Modifier.fillMaxWidth()
                .background(color = Color(0xFFc2e868))
            ) {
                Text("Custom List",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                        .align(Alignment.CenterStart)
                )
                IconButton(onClick = {showDialog = true},
                    modifier = Modifier
                        .padding(16.dp)
                        .size(40.dp)
                        .align(Alignment.CenterEnd)
                ) {
                    Icon(
                        Icons.Default.Add,
                        "add",
                        tint = Color(0xFF355F2E)
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(10){
                ListItem("Hello",{showDialog = true},{})
            }
        }
    }
    InputDialog(
        showDialog = showDialog,
        onDismiss = { showDialog = false },
        onSave = { savedText = it }
    )
}

@Preview(showBackground = true)
@Composable
private fun CustomPrev() {
    DecideEasyTheme {
        CustomScreen()
    }
}