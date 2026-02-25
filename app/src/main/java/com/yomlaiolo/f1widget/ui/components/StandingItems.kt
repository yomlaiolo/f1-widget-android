package com.yomlaiolo.f1widget.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yomlaiolo.f1widget.data.models.ConstructorStanding
import com.yomlaiolo.f1widget.data.models.DriverStanding
import com.yomlaiolo.f1widget.utils.TeamColors

@Composable
fun DriverStandingItem(standing: DriverStanding) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            // Position
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(getPositionColor(standing.position.toInt())),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = standing.position,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            
            // Barre de couleur d'équipe
            val teamColor = standing.constructors.firstOrNull()?.let {
                TeamColors.getColor(it.constructorId)
            } ?: MaterialTheme.colorScheme.primary
            
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(32.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(teamColor)
            )
            
            // Nom du pilote
            Column {
                Text(
                    text = "${standing.driver.givenName} ${standing.driver.familyName}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = standing.constructors.firstOrNull()?.name ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        // Points
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = standing.points,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "pts",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ConstructorStandingItem(standing: ConstructorStanding) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            // Position
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(getPositionColor(standing.position.toInt())),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = standing.position,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            
            // Barre de couleur d'équipe
            val teamColor = TeamColors.getColor(standing.constructor.constructorId)
            
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(32.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(teamColor)
            )
            
            // Nom du constructeur
            Text(
                text = standing.constructor.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
        
        // Points
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = standing.points,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "pts",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun getPositionColor(position: Int) = when (position) {
    1 -> MaterialTheme.colorScheme.primary
    2 -> MaterialTheme.colorScheme.secondary
    3 -> MaterialTheme.colorScheme.tertiary
    else -> MaterialTheme.colorScheme.surfaceVariant
}