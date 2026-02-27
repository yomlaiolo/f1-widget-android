package com.yomlaiolo.f1widget.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yomlaiolo.f1widget.ui.MainViewModel
import com.yomlaiolo.f1widget.ui.components.ConstructorStandingItem
import com.yomlaiolo.f1widget.ui.components.DriverStandingItem
import com.yomlaiolo.f1widget.utils.CircuitImageManager
import com.yomlaiolo.f1widget.utils.CountryFlags
import com.yomlaiolo.f1widget.utils.DateFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("F1 Widget") },
                actions = {
                    IconButton(onClick = { viewModel.loadData() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Rafraîchir")
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Erreur: ${uiState.error}")
                    Button(onClick = { viewModel.loadData() }) {
                        Text("Réessayer")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Prochain Grand Prix
                item {
                    NextRaceCard(uiState.nextRace)
                }
                
                // Classement Pilotes (Top 5)
                item {
                    ClassementSection(
                        title = "Classement Pilotes",
                        items = uiState.driverStandings.take(5)
                    ) { standing ->
                        DriverStandingItem(standing)
                    }
                }
                
                // Classement Constructeurs (Top 5)
                item {
                    ClassementSection(
                        title = "Classement Constructeurs",
                        items = uiState.constructorStandings.take(5)
                    ) { standing ->
                        ConstructorStandingItem(standing)
                    }
                }
            }
        }
    }
}

@Composable
fun NextRaceCard(
    raceWithContext: com.yomlaiolo.f1widget.data.repository.F1Repository.RaceWithContext?
) {
    val context = LocalContext.current

    // Couleurs identiques au widget
    val widgetBackground = Color(0xFF15151E)
    val textWhite = Color(0xFFFFFFFF)
    val textCyan = Color(0xFF00D9FF)
    val textGray = Color(0xFFAAAAAA)
    val textDarkGray = Color(0xFF888888)
    val textLightGray = Color(0xFFCCCCCC)
    val sprintColor = Color(0xFFFF6600)
    val qualiColor = Color(0xFFFFD700)
    val raceColor = Color(0xFFE10600)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = widgetBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            raceWithContext?.let { ctx ->
                val race = ctx.race

                // Ligne 1: Drapeau + Nom GP + Round
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = CountryFlags.getFlag(race.circuit.location.country),
                        fontSize = 24.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = race.raceName,
                        color = textWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Round ${race.round}/${ctx.totalRaces}",
                        color = textCyan,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                // Ligne 2: Nom du circuit
                Text(
                    text = race.circuit.circuitName,
                    color = textGray,
                    fontSize = 13.sp,
                    maxLines = 1,
                    modifier = Modifier.padding(top = 2.dp, start = 32.dp)
                )
                
                // Ligne 3: Dates du weekend
                val firstSessionDate = listOfNotNull(
                    race.firstPractice?.date,
                    race.secondPractice?.date,
                    race.thirdPractice?.date,
                    race.sprint?.date,
                    race.qualifying?.date,
                    race.date
                ).minOrNull() ?: race.date
                
                val weekendDates = DateFormatter.formatWeekendDates(firstSessionDate, race.date)
                
                Text(
                    text = weekendDates,
                    color = textDarkGray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp, start = 32.dp)
                )
                
                Spacer(modifier = Modifier.height(8.dp))

                // Contenu principal: Circuit à gauche, Sessions à droite
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Image du circuit (gauche)
                    val circuitResId = CircuitImageManager.getCircuitDrawableRes(
                        context, race.circuit.circuitId
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(100.dp)
                            .padding(end = 8.dp, top = 4.dp, bottom = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = circuitResId),
                            contentDescription = "Circuit ${race.circuit.circuitName}",
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Sessions (droite)
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        race.firstPractice?.let {
                            WidgetSessionRow("FP1", it.date, it.time, textDarkGray, textLightGray)
                        }
                        race.secondPractice?.let {
                            WidgetSessionRow("FP2", it.date, it.time, textDarkGray, textLightGray)
                        }
                        race.thirdPractice?.let {
                            WidgetSessionRow("FP3", it.date, it.time, textDarkGray, textLightGray)
                        }
                        race.sprint?.let {
                            WidgetSessionRow("SPRINT", it.date, it.time, sprintColor, textLightGray)
                        }
                        race.qualifying?.let {
                            WidgetSessionRow("QUALIFS", it.date, it.time, qualiColor, textLightGray)
                        }
                        WidgetSessionRow("COURSE", race.date, race.time ?: "", raceColor, textWhite, isBold = true)
                    }
                }
            } ?: run {
                Text(
                    text = "Chargement...",
                    color = textLightGray,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun WidgetSessionRow(
    name: String,
    date: String,
    time: String,
    labelColor: Color,
    timeColor: Color,
    isBold: Boolean = false
) {
    val formatted = DateFormatter.formatSessionDateTime(date, time)
    if (formatted.isNotEmpty()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                color = labelColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(70.dp)
            )
            Text(
                text = formatted,
                color = timeColor,
                fontSize = 11.sp,
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
fun <T> ClassementSection(
    title: String,
    items: List<T>,
    itemContent: @Composable (T) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items.forEach { item ->
                    itemContent(item)
                }
            }
        }
    }
}
