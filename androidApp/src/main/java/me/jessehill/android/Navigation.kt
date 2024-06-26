package me.jessehill.android

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import me.jessehill.tictactoe.UIRoute

@Composable
fun BottomNavigation(
    modifier: Modifier = Modifier,
    onNavigate: (UIRoute) -> Unit = {},
    route: UIRoute
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Home
        Icon(
            imageVector = Icons.Default.Home,
            contentDescription = "Home",
            tint = if (route is UIRoute.Home) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onPrimary
            },
            modifier = Modifier
                .clickable(enabled = (route is UIRoute.Home).not()) { onNavigate(UIRoute.Home) }
        )
        // Leaderboard
        Icon(
            imageVector = Icons.Default.List,
            contentDescription = "Leaderboard",
            tint = if (route is UIRoute.Leaderboard) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onPrimary
            },
            modifier = Modifier
                .clickable(enabled = (route is UIRoute.Leaderboard).not()) { onNavigate(UIRoute.Leaderboard) }
        )
        // FindMatch
        Icon(
            imageVector = Icons.Default.AddCircle,
            contentDescription = "Find Match",
            tint = if (route is UIRoute.FindMatch) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onPrimary
            },
            modifier = Modifier
                .clickable(enabled = (route is UIRoute.FindMatch).not()) { onNavigate(UIRoute.FindMatch) }
        )
        // Profile
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Profile",
            tint = if (route is UIRoute.Profile) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onPrimary
            },
            modifier = Modifier
                .clickable(enabled = (route is UIRoute.Profile).not()) { onNavigate(UIRoute.Profile) }
        )
        // Play
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Play",
            tint = if (route is UIRoute.Play) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onPrimary
            },
            modifier = Modifier
                .clickable(enabled = (route is UIRoute.Play).not()) { onNavigate(UIRoute.Play) }
        )
    }
}

@Preview
@Composable
fun PreviewBottomTabNavigation() {
    TicTacToeTheme {
        BottomAppBar(containerColor = MaterialTheme.colorScheme.primary) {
            BottomNavigation(onNavigate = {}, route = UIRoute.Profile)
        }
    }
}

@Preview
@Composable
fun PreviewBottomTabNavigationWithHomeActive() {
    TicTacToeTheme {
        BottomAppBar(containerColor = MaterialTheme.colorScheme.primary) {
            BottomNavigation(onNavigate = {}, route = UIRoute.Home)
        }
    }
}
