package com.yomlaiolo.f1widget.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yomlaiolo.f1widget.ui.MainViewModel
import com.yomlaiolo.f1widget.ui.components.ConstructorStandingItem
import com.yomlaiolo.f1widget.ui.components.DriverStandingItem
import java.util.Calendar

private val screenBackground = Color(0xFF0D0D15)
private val cardBackground = Color(0xFF15151E)
private val textWhite = Color(0xFFFFFFFF)
private val textGray = Color(0xFFAAAAAA)
private val textCyan = Color(0xFF00D9FF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandingsScreen(
    viewModel: MainViewModel
) {
    val standingsState by viewModel.standingsState.collectAsState()
    var selectedTabIndex by remember { mutableStateOf(0) }
    var showYearPicker by remember { mutableStateOf(false) }
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    // Charger les standings au premier affichage
    LaunchedEffect(Unit) {
        if (standingsState.driverStandings.isEmpty() && !standingsState.isLoading) {
            viewModel.loadStandingsForYear(standingsState.selectedYear)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Classements") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = screenBackground,
                    titleContentColor = textWhite
                )
            )
        },
        containerColor = screenBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Sélecteur de saison
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable { showYearPicker = true },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = cardBackground)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Saison",
                        color = textGray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${standingsState.selectedYear}",
                        color = textCyan,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Changer de saison",
                        tint = textCyan
                    )
                }
            }

            // Onglets Pilotes / Constructeurs
            MaterialTheme(
                colorScheme = MaterialTheme.colorScheme.copy(primary = textCyan)
            ) {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = cardBackground,
                    contentColor = textCyan,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        selectedContentColor = textCyan,
                        unselectedContentColor = textGray,
                        text = {
                            Text(
                                "Pilotes",
                                color = if (selectedTabIndex == 0) textCyan else textGray,
                                fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        selectedContentColor = textCyan,
                        unselectedContentColor = textGray,
                        text = {
                            Text(
                                "Constructeurs",
                                color = if (selectedTabIndex == 1) textCyan else textGray,
                                fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Contenu
            if (standingsState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = textCyan)
                }
            } else if (standingsState.error != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Erreur: ${standingsState.error}",
                            color = textGray,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.loadStandingsForYear(standingsState.selectedYear) },
                            colors = ButtonDefaults.buttonColors(containerColor = textCyan)
                        ) {
                            Text("Réessayer", color = cardBackground)
                        }
                    }
                }
            } else {
                when (selectedTabIndex) {
                    0 -> DriverStandingsTab(standingsState.driverStandings)
                    1 -> ConstructorStandingsTab(standingsState.constructorStandings)
                }
            }
        }
    }

    // Dialog de sélection de saison
    if (showYearPicker) {
        YearPickerDialog(
            selectedYear = standingsState.selectedYear,
            currentYear = currentYear,
            onYearSelected = { year ->
                showYearPicker = false
                viewModel.loadStandingsForYear(year)
            },
            onDismiss = { showYearPicker = false }
        )
    }
}

@Composable
fun YearPickerDialog(
    selectedYear: Int,
    currentYear: Int,
    onYearSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val years = (currentYear downTo 1950).toList()
    val listState = rememberLazyListState()

    // Scroll jusqu'à l'année sélectionnée au lancement
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

@Composable
fun DriverStandingsTab(standings: List<com.yomlaiolo.f1widget.data.models.DriverStanding>) {
    if (standings.isEmpty()) {
        EmptyStandingsMessage()
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(standings) { standing ->
                DriverStandingItem(standing)
            }
        }
    }
}

@Composable
fun ConstructorStandingsTab(standings: List<com.yomlaiolo.f1widget.data.models.ConstructorStanding>) {
    if (standings.isEmpty()) {
        EmptyStandingsMessage()
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(standings) { standing ->
                ConstructorStandingItem(standing)
            }
        }
    }
}

@Composable
fun EmptyStandingsMessage() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Aucun classement disponible pour cette saison.",
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = textGray
        )
    }
}
