package com.yomlaiolo.f1widget.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val cardBackground = Color(0xFF15151E)
private val textWhite = Color(0xFFFFFFFF)
private val textGray = Color(0xFFAAAAAA)
private val textCyan = Color(0xFF00D9FF)

@Composable
fun YearPickerDialog(
    selectedYear: Int,
    currentYear: Int,
    onYearSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val years = (currentYear downTo 1950).toList()
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        val index = years.indexOf(selectedYear)
        if (index >= 0) {
            listState.scrollToItem(index)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = cardBackground,
        title = {
            Text(
                text = "Choisir une saison",
                color = textWhite,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                items(years) { year ->
                    val isSelected = year == selectedYear
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onYearSelected(year) }
                            .background(
                                if (isSelected) textCyan.copy(alpha = 0.15f)
                                else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "$year",
                            color = if (isSelected) textCyan else textWhite,
                            fontSize = 16.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                        if (year == currentYear) {
                            Text(
                                text = "en cours",
                                color = textGray,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Fermer", color = textCyan)
            }
        }
    )
}

