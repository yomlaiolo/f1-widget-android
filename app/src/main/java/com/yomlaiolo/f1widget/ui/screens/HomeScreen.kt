package com.yomlaiolo.f1widget.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomlaiolo.f1widget.ui.MainViewModel
import com.yomlaiolo.f1widget.ui.components.ConstructorStandingItem
import com.yomlaiolo.f1widget.ui.components.DriverStandingItem
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            raceWithContext?.let { context ->
                val race = context.race
                
                // En-tête avec drapeau et round
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = CountryFlags.getFlag(race.circuit.location.country),
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(
                        text = "Round ${race.round}/${context.totalRaces}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Nom du Grand Prix
                Text(
                    text = race.raceName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                // Nom du circuit
                Text(
                    text = race.circuit.circuitName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Dates du weekend
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
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Sessions
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    race.firstPractice?.let {
                        SessionRow("FP1", it.date, it.time)
                    }
                    race.secondPractice?.let {
                        SessionRow("FP2", it.date, it.time)
                    }
                    race.thirdPractice?.let {
                        SessionRow("FP3", it.date, it.time)
                    }
                    race.sprint?.let {
                        SessionRow("Sprint", it.date, it.time)
                    }
                    race.qualifying?.let {
                        SessionRow("Qualifications", it.date, it.time)
                    }
                    SessionRow("Course", race.date, race.time ?: "")
                }
            }
        }
    }
}

@Composable
fun SessionRow(name: String, date: String, time: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "${DateFormatter.formatSessionDate(date)} - ${DateFormatter.formatTime(time)}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
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