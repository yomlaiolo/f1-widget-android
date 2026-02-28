package com.yomlaiolo.f1widget.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yomlaiolo.f1widget.data.models.Race
import com.yomlaiolo.f1widget.ui.MainViewModel
import com.yomlaiolo.f1widget.ui.components.YearPickerDialog
import com.yomlaiolo.f1widget.utils.CircuitImageManager
import com.yomlaiolo.f1widget.utils.CountryFlags
import com.yomlaiolo.f1widget.utils.DateFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val screenBackground = Color(0xFF0D0D15)
private val cardBackground = Color(0xFF15151E)
private val textWhite = Color(0xFFFFFFFF)
private val textGray = Color(0xFFAAAAAA)
private val textCyan = Color(0xFF00D9FF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    viewModel: MainViewModel
) {
    val calendarState by viewModel.calendarState.collectAsState()
    var showYearPicker by remember { mutableStateOf(false) }
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    // Charger le calendrier au premier affichage
    LaunchedEffect(Unit) {
        if (calendarState.races.isEmpty() && !calendarState.isLoading) {
            viewModel.loadCalendarForYear(calendarState.selectedYear)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calendrier") },
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
                        text = "${calendarState.selectedYear}",
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

            // Contenu
            if (calendarState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = textCyan)
                }
            } else if (calendarState.races.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Aucun calendrier disponible pour cette saison.",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = textGray
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val totalRaces = calendarState.races.size
                    items(calendarState.races) { race ->
                        RaceCard(race, totalRaces)
                    }
                }
            }
        }
    }

    // Dialog de sélection de saison
    if (showYearPicker) {
        YearPickerDialog(
            selectedYear = calendarState.selectedYear,
            currentYear = currentYear,
            onYearSelected = { year ->
                showYearPicker = false
                viewModel.loadCalendarForYear(year)
            },
            onDismiss = { showYearPicker = false }
        )
    }
}

@Composable
fun RaceCard(race: Race, totalRaces: Int) {
    val isPast = isRacePast(race.date)
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    // Couleurs identiques au widget
    val widgetBackground = Color(0xFF15151E)
    val pastBackground = Color(0xFF15151E).copy(alpha = 0.5f)
    val textWhite = Color(0xFFFFFFFF)
    val textGray = Color(0xFFAAAAAA)
    val textDarkGray = Color(0xFF888888)
    val textCyan = Color(0xFF00D9FF)
    val textLightGray = Color(0xFFCCCCCC)
    val sprintColor = Color(0xFFFF6600)
    val qualiColor = Color(0xFFFFD700)
    val raceColor = Color(0xFFE10600)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPast) pastBackground else widgetBackground
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(animationSpec = tween(300))
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                // Infos GP (gauche)
                Column(
                    modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    // Drapeau + Nom du GP
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = CountryFlags.getFlag(race.circuit.location.country),
                            fontSize = 22.sp,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = race.raceName,
                            color = if (isPast) textGray else textWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }

                    // Nom du circuit
                    Text(
                        text = race.circuit.circuitName,
                        color = textGray,
                        fontSize = 12.sp,
                        maxLines = 1,
                        modifier = Modifier.padding(start = 30.dp)
                    )

                    // Date de la course
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
                        color = if (isPast) textDarkGray else textCyan,
                        fontSize = 12.sp,
                        fontWeight = if (isPast) FontWeight.Normal else FontWeight.Medium,
                        modifier = Modifier.padding(start = 30.dp)
                    )
                }

                // Round + Image du circuit (droite, centré)
                val circuitResId = CircuitImageManager.getCircuitDrawableRes(
                    context, race.circuit.circuitId
                )
                Column(
                    modifier = Modifier.widthIn(min = 80.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Round ${race.round}/$totalRaces",
                        color = if (isPast) textDarkGray else textCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier.size(60.dp), contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = circuitResId),
                            contentDescription = "Circuit ${race.circuit.circuitName}",
                            modifier = Modifier.fillMaxSize(),
                            alpha = if (isPast) 0.4f else 1f
                        )
                    }
                }
            }

            // Détail des sessions (affiché quand expanded)
            if (expanded) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 0.5.dp,
                    color = textDarkGray.copy(alpha = 0.3f)
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Circuit agrandi (gauche)
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
                            modifier = Modifier.fillMaxSize(),
                            alpha = if (isPast) 0.4f else 1f
                        )
                    }

                    // Sessions (droite)
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        val hasSessions = race.firstPractice != null || race.secondPractice != null ||
                                race.thirdPractice != null || race.sprint != null || race.qualifying != null

                        if (hasSessions) {
                            race.firstPractice?.let {
                                CalendarSessionRow(
                                    "FP1",
                                    it.date,
                                    it.time,
                                    textDarkGray,
                                    textLightGray,
                                    isPast
                                )
                            }
                            race.secondPractice?.let {
                                CalendarSessionRow(
                                    "FP2",
                                    it.date,
                                    it.time,
                                    textDarkGray,
                                    textLightGray,
                                    isPast
                                )
                            }
                            race.thirdPractice?.let {
                                CalendarSessionRow(
                                    "FP3",
                                    it.date,
                                    it.time,
                                    textDarkGray,
                                    textLightGray,
                                    isPast
                                )
                            }
                            race.sprint?.let {
                                CalendarSessionRow(
                                    "SPRINT",
                                    it.date,
                                    it.time,
                                    sprintColor,
                                    textLightGray,
                                    isPast
                                )
                            }
                            race.qualifying?.let {
                                CalendarSessionRow(
                                    "QUALIFS",
                                    it.date,
                                    it.time,
                                    qualiColor,
                                    textLightGray,
                                    isPast
                                )
                            }
                        } else {
                            Text(
                                text = "Horaires non disponibles",
                                color = textDarkGray,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }

                        CalendarSessionRow(
                            "COURSE",
                            race.date,
                            race.time,
                            raceColor,
                            textWhite,
                            isPast,
                            isBold = true
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarSessionRow(
    name: String,
    date: String,
    time: String?,
    labelColor: Color,
    timeColor: Color,
    isPast: Boolean,
    isBold: Boolean = false
) {
    // Essayer date+heure, sinon juste la date formatée
    val formatted = if (!time.isNullOrEmpty()) {
        DateFormatter.formatSessionDateTime(date, time).ifEmpty {
            DateFormatter.formatSessionDate(date)
        }
    } else {
        DateFormatter.formatSessionDate(date)
    }
    if (formatted.isNotEmpty()) {
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                color = if (isPast) labelColor.copy(alpha = 0.5f) else labelColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(70.dp)
            )
            Text(
                text = formatted,
                color = if (isPast) timeColor.copy(alpha = 0.5f) else timeColor,
                fontSize = 11.sp,
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

fun isRacePast(dateString: String): Boolean {
    return try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val raceDate = dateFormat.parse(dateString)
        val now = Date()
        raceDate?.before(now) ?: false
    } catch (_: Exception) {
        false
    }
}