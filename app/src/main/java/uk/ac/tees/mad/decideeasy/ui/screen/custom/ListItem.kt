package uk.ac.tees.mad.decideeasy.ui.screen.custom

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.decideeasy.ui.theme.DecideEasyTheme

@Composable
fun ListItem(
    text:String,
    onEdit:()->Unit,
    onDelete:()->Unit
) {
    Column(Modifier
        .padding(horizontal = 16.dp)
        .fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text,
                modifier = Modifier.weight(1f)
                )
            IconButton(onClick = {onEdit()}) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "edit_icon",
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
            IconButton(onClick = {onDelete()}) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "delete_icon",
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }
        HorizontalDivider(thickness = 2.dp)
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemPrev() {
    DecideEasyTheme {
        ListItem(
            "Hello",
            onEdit = {},
            onDelete = {}
        )
    }
}