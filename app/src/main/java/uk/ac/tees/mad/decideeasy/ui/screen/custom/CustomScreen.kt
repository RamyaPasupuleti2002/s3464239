package uk.ac.tees.mad.decideeasy.ui.screen.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import uk.ac.tees.mad.decideeasy.data.AnswerEntity

@Composable
fun CustomScreen(
    viewModel: CustomViewModel = hiltViewModel()
) {
    val answers by viewModel.answers.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var isEdit by rememberSaveable { mutableStateOf(false) }
    var selectedEntity by remember { mutableStateOf<AnswerEntity?>(null) }
    val context = LocalContext.current
    Scaffold(
        topBar = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFFc2e868))
                    .padding(top = 20.dp)
            ) {
                Text(
                    "Custom List",
                    fontSize = 22.sp,
                    color = Color(0xFF355F2E),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterStart)
                )
                IconButton(
                    onClick = {
                        isEdit = false
                        showDialog = true
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .size(32.dp)
                        .border(1.dp, Color(0xFF355F2E), CircleShape)
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
            items(answers) { answer ->
                ListItem(
                    text = answer.answer,
                    onEdit = {
                        selectedEntity = answer
                        isEdit = true
                        showDialog = true
                    },
                    onDelete = {
                        viewModel.delete(answer)
                    }
                )
            }
        }
    }
    InputDialog(
        showDialog = showDialog,
        onDismiss = { showDialog = false },
        onSave = {
            if (isEdit){
                selectedEntity?.let { it1 -> viewModel.updateAnswer(it1.copy(answer = it)) }
            }
            else{
                viewModel.saveAnswer(it, context)
            }
        }
    )
}