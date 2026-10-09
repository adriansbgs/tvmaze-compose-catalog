package com.example.tvpulse.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.tvpulse.R
import com.example.tvpulse.ui.favorite.FavoriteRoute
import com.example.tvpulse.ui.home.HomeRoute

private val tabs = listOf(R.string.tab_home, R.string.tab_favorite)

/** Both tab ViewModels are scoped to this nav entry, so switching tabs keeps their state. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(onShowClick: (Int) -> Unit) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(title = { Text(stringResource(R.string.app_name)) })
                PrimaryTabRow(selectedTabIndex = selectedTab) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(stringResource(title)) },
                        )
                    }
                }
            }
        },
    ) { padding ->
        Box(Modifier.padding(padding)) {
            if (selectedTab == 0) HomeRoute(onShowClick) else FavoriteRoute(onShowClick)
        }
    }
}
