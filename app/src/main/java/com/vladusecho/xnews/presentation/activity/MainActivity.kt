package com.vladusecho.xnews.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vladusecho.xnews.presentation.model.HeroFontFamily
import com.vladusecho.xnews.presentation.navigation.AppNavGraph
import com.vladusecho.xnews.presentation.navigation.MyNavigationItem
import com.vladusecho.xnews.ui.theme.XNewsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            XNewsTheme {

                val navController = rememberNavController()

                Scaffold(
                    topBar = {
                        Column {
                            CenterAlignedTopAppBar(
                                title = {
                                    Text(
                                        text = "Explore",
                                        fontFamily = HeroFontFamily,
                                        fontWeight = FontWeight.Normal,
                                        color = Color.Black,
                                        fontSize = 18.sp
                                    )
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = Color.White
                                ),
                            )
                            HorizontalDivider(
                                modifier = Modifier,
                                thickness = 0.5.dp,
                                color = Color.Gray.copy(alpha = 0.5f)
                            )
                        }
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = Color.White
                        ) {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val items = listOf(
                                MyNavigationItem.Home,
                                MyNavigationItem.Favorite
                            )

                            items.forEach { item ->
                                val selected = navBackStackEntry?.destination?.hierarchy?.any {
                                    it.route == item.screen.route
                                } ?: false

                                NavigationBarItem(
                                    selected = selected,
                                    onClick = {
                                        if (!selected) {
                                            navController.navigate(item.screen.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                // only one same screen at the top
                                                launchSingleTop = true
                                                // save screen state after another screen
                                                restoreState = true
                                            }
                                        }
                                    },
                                    icon = {

                                        Icon(
                                            imageVector = item.icon,
                                            null
                                        )

                                    },
                                    label = {
                                        Text(item.title)
                                    },
                                )
                            }
                        }
                    }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        AppNavGraph(navController)
                    }
                }
            }
        }
    }
}