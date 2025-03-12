package uk.ac.tees.mad.decideeasy.ui.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondaryTextTabs(onTabSelected:(Int)->Unit) {
    var state by remember { mutableIntStateOf(0) }
    val titles = listOf("Login", "Sign Up")

    Column {
        SecondaryTabRow(selectedTabIndex = state,
            containerColor = Color.White,
            indicator = {
                Box(
                    Modifier
                        .tabIndicatorOffset(
                            selectedTabIndex = state,
                        )
                        .height(3.dp)
                        .background(Color(0xFF355F2E))
                )
            }
        ) {
            titles.forEachIndexed{ index, title->
                Tab(
                    selected = state==index,
                    onClick = {state = index
                              onTabSelected(state)
                              },
                    text = { Text(title,
                            color = if (state==index) Color(0xFF355F2E) else Color.Gray
                        )
                    }
                )
            }
        }
    }
}