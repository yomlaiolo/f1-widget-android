package com.yomlaiolo.f1widget.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yomlaiolo.f1widget.data.models.ConstructorStanding
import com.yomlaiolo.f1widget.data.models.DriverStanding
import com.yomlaiolo.f1widget.utils.TeamColors

// Couleurs identiques au widget / Cards GP
private val cardBackground = Color(0xFF15151E)
private val textWhite = Color(0xFFFFFFFF)
private val textGray = Color(0xFFAAAAAA)
private val textCyan = Color(0xFF00D9FF)


@Composable
fun DriverStandingItem(standing: DriverStanding) {
    val teamColor = standing.constructors?.firstOrNull()?.let {
        TeamColors.getColor(it.constructorId)
    } ?: Color(0xFF999999)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Barre de couleur d'équipe
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(40.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(teamColor)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Position
            Text(
                text = standing.position ?: "-",
                color = textWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(30.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Nom du pilote + Équipe
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "${standing.driver?.givenName ?: ""} ${standing.driver?.familyName ?: ""}".trim(),
                    color = textWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = standing.constructors?.firstOrNull()?.name ?: "",
                    color = textGray,
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }

            // Victoires
            val wins = standing.wins?.toIntOrNull() ?: 0
            if (wins > 0) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(end = 12.dp)
                ) {
                    Text(
                        text = standing.wins ?: "0",
                        color = textCyan,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "wins",
                        color = textGray,
                        fontSize = 10.sp
                    )
                }
            }

            // Points
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = standing.points ?: "0",
                    color = textWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "pts",
                    color = textGray,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
fun ConstructorStandingItem(standing: ConstructorStanding) {
    val teamColor = standing.constructor?.let {
        TeamColors.getColor(it.constructorId)
    } ?: Color(0xFF999999)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Barre de couleur d'équipe
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(40.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(teamColor)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Position
            Text(
                text = standing.position ?: "-",
                color = textWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(30.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Nom du constructeur
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = standing.constructor?.name ?: "",
                    color = textWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = standing.constructor?.nationality ?: "",
                    color = textGray,
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }

            // Victoires
            val wins = standing.wins?.toIntOrNull() ?: 0
            if (wins > 0) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(end = 12.dp)
                ) {
                    Text(
                        text = standing.wins ?: "0",
                        color = textCyan,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "wins",
                        color = textGray,
                        fontSize = 10.sp
                    )
                }
            }

            // Points
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = standing.points ?: "0",
                    color = textWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "pts",
                    color = textGray,
                    fontSize = 10.sp
                )
            }
        }
    }
}
