package com.example.populartvseries.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.populartvseries.domain.model.Series

/**
 * Composables related to series that can be used in different screens
 */

@Composable
fun ReleaseDate(series: Series?, tint: Color) {
    Row(horizontalArrangement = Arrangement.Center) {
        Icon(
            Icons.Filled.DateRange,
            contentDescription = "${series?.releaseDate}",
            tint = tint
        )
        Text(
            "Released: ${series?.releaseDate}",
            Modifier.padding(start = 8.dp),
            color = tint
        )
    }
}